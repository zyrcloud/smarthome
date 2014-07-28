package com.mobile.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;

import com.mobile.server.arm.bluetooth.Bluetoothclient;
import com.mobile.server.arm.bluetooth.Bluetoothclient.State;
import com.mobile.server.config.ConfigActivity;
import com.mobile.server.utils.Command;
import com.mobile.server.utils.Const;

public class MainActivity extends InstrumentedActivity {
	public static int MSG_REFRESH_STATE = 1000;

	MainInnerRecevier innerRecevier = new MainInnerRecevier();
	Button connectBlueth = null;
	TextView tInfo = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		setContentView(R.layout.activity_main);
//		bTakePhoto = (Button) findViewById(R.id.bTakePic);
//		connectBlueth = (Button) findViewById(R.id.bConnectBlueth);
//		bOpenConfig = (Button) findViewById(R.id.bOpenConfig);
	    final LinearLayout layout = new LinearLayout(this);
	    layout.setOrientation(LinearLayout.VERTICAL);

	    tInfo = new TextView(this);
	    layout.addView(tInfo);
	    tInfo.setText("系统初始中。");
		MainService.startServiceByCmd(getApplicationContext(), Command.CMD_SYSTEM_START_SERVICE);
//	    sendEmptyMessageDelayed(MSG_REFRESH_STATE, 1000);

	    Button bTakePhoto = new Button(this);
	    layout.addView(bTakePhoto);
	    bTakePhoto.setText("拍照并上传");
		bTakePhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainService.startServiceByCmd(getApplicationContext(), Command.CMD_51_CALL_SEND_PIC);
			}
		});

		connectBlueth = new Button(this);
		layout.addView(connectBlueth);
		connectBlueth.setText("连接蓝牙从机");
		connectBlueth.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if("连接蓝牙从机".equals(connectBlueth.getText())){
					Intent service = new Intent(getApplicationContext(), MainService.class);
					service.putExtra(Command.CMD_TYPE, Command.CMD_CONNECT_BLUETH);
					startService(service);
					connectBlueth.setText("断开蓝牙从机");
				} else {
					Intent service = new Intent(getApplicationContext(), MainService.class);
					service.putExtra(Command.CMD_TYPE, Command.CMD_DISCONNECT_BLUETH);
					startService(service);
					connectBlueth.setText("连接蓝牙从机");
				}
			}
		});

		Button bOpenConfig = new Button(this);
		layout.addView(bOpenConfig);
		bOpenConfig.setText("环境配置");
		bOpenConfig.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), ConfigActivity.class));
			}
		});

		Button bStopService = new Button(this);
		layout.addView(bStopService);
		bStopService.setText("停止后台服务");
		bStopService.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent stopIntent = new Intent(getBaseContext(), MainService.class);
//				stopIntent.putExtra(Command.CMD_TYPE, Command.CMD_SYSTEM_STOP_SERVICE);
////				stopService(stopIntent);//友好的退出。。。
//				startService(stopIntent);
				MainService.startServiceByCmd(getApplicationContext(), Command.CMD_SYSTEM_STOP_SERVICE);
			}
		});
		setContentView(layout);
		super.onCreate(savedInstanceState);


		innerRecevier.RegistReceiver();
	}

    @Override
    protected void onResume() {
    	super.onResume();
		JPushInterface.onResume(this);
    }
    @Override
    protected void onPause() {
    	super.onPause();
    	JPushInterface.onPause(this);
    }
	@Override
	protected void onDestroy() {
		super.onDestroy();
		innerRecevier.unRegisterReceiver();
	}

	class MainInnerRecevier extends BroadcastReceiver {
		IntentFilter intentFile = new IntentFilter();
		public MainInnerRecevier() {
			intentFile.addAction(Const.ACTION_SOCKET_STATE);
			intentFile.addAction(Const.ACTION_SOCKET_RECEVIED_DATAS);
		}
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(Const.ACTION_SOCKET_STATE.equals(action)){
				int socketState = intent.getIntExtra(Const.EXTRA_SOCKET_STATE, Bluetoothclient.State.WaitStart.ordinal());
				State state = Bluetoothclient.State.values()[socketState];
				tInfo.setText("state:" + state);
			} else if(Const.ACTION_SOCKET_RECEVIED_DATAS.equals(action)) {
				String readDatas = intent.getStringExtra(Const.EXTRA_ITEM);
				tInfo.setText("rec:" + readDatas);
			}
		}
		public boolean RegistReceiver(){
			getBaseContext().registerReceiver(this, intentFile);
			return true;
		}
		public boolean unRegisterReceiver(){
			getBaseContext().unregisterReceiver(this);
			return true;
		}
	}
}
