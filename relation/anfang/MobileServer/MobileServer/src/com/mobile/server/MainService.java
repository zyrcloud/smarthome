package com.mobile.server;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;

import com.mobile.server.arm.ArmManager;
import com.mobile.server.config.Config;
import com.mobile.server.location.LocationService;
import com.mobile.server.takephoto.PhotoMainActivity;
import com.mobile.server.utils.Command;
import com.mobile.server.utils.Const;
import com.mobile.server.utils.Log;

public class MainService extends Service {

	private static final String TAG = "MainService";
	boolean isFirstAction = true;

	public static boolean startServiceByCmd(Context context, int cmd){
		Log.d(TAG, "enter notifySocketState");
		Intent service = new Intent(context, MainService.class);
		service.putExtra(Command.CMD_TYPE, cmd);
		context.startService(service);
		return true;
	}
	public static boolean startServiceByMessage(Context context, String msg){
		if(!TextUtils.isEmpty(msg)){
			int cmd = Command.getCommandFromStr(msg);

			if(Command.CMD_NONE == cmd){
				Log.e(TAG, "invalid cmd for " + msg);
				return false;
			}
			Intent startServiceIntent = new Intent(context, MainService.class);
			startServiceIntent.putExtra(Command.CMD_TYPE, cmd);
			context.startService(startServiceIntent);
			return true;
		} else {
			Log.e(TAG, "in startServiceByMessage msg is null");
			return false;
		}
	}



	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int command = 0;
		if(null != intent){
			command = intent.getIntExtra(Command.CMD_TYPE, Command.CMD_NONE);
			if(Command.CMD_NONE != command){
				handler.sendEmptyMessage(command);
			}
		}

		envCheck();
		return super.onStartCommand(intent, flags, startId);
	}
	private void envCheck(){
		if(Config.getInstance().getUseBluetools()){
			armManager.startConnect();
//			handler.postDelayed(new Runnable() {
//				@Override
//				public void run() {
//					if(null == armManager){
//						Log.d(TAG, "arm Manager is null, stop Service");
//						stopSelf();
//						return;
//					}
//					if(armManager.getState()== Bluetoothclient.State.WaitStart.ordinal()){
//						Log.d(TAG, "armManager state is WaitStart, stop Service");
//						stopSelf();
//						return;
//					}
//					handler.post(this);
//				}
//			}, 15000); //10s一次检查，如果处于无等待状态则退出Service;
		} else {
			armManager.disConnect();
		}
	}
	private boolean startConnect(){
		Log.d(TAG, "enter startConnect()");
		armManager.startConnect();
		return true;
	}
	private boolean stopConnect(){
		Log.d(TAG, "enter stopConnect()");
		if(null != armManager){
			armManager.disConnect();
		}

		return true;
	}
	private boolean rspState(){
		Intent rspStateIntent = new Intent(Const.ACTION_SOCKET_STATE);
		rspStateIntent.putExtra(Const.EXTRA_SOCKET_STATE, armManager.getState());
		sendBroadcast(rspStateIntent);
		return true;
	}


	@Override
	public void onDestroy() {
		Log.d(TAG, "enter onDestroy()");
		stopConnect();
		super.onDestroy();
	}


	Handler handler = null;
	ArmManager armManager = null;
	@Override
	public void onCreate() {
		handler =  new Handler(){
			@Override
			public void handleMessage(Message msg) {
				Log.d(TAG, "enter handleMessage(msg:" + msg.what + ")");
				switch (msg.what) {
				case Command.CMD_51_CALL_SEND_PIC:
					PhotoMainActivity.startTakePic(getApplicationContext());
					break;
				case Command.CMD_CONNECT_BLUETH:
					startConnect();
					break;
				case Command.CMD_DISCONNECT_BLUETH:
					stopConnect();
					break;
				case Command.CMD_SYSTEM_STOP_SERVICE:
					stopSelf();
					break;
				case Command.CMD_SYSTEM_GET_STATE:
					rspState();
					break;
				case Command.CMD_REQUEST_UPDATE_DEVICES:
					//先发送重新注册请求
					JobService.StartJobService(Command.CMD_REGIST_PUSH);

					//再发起定位请求
					LocationService.startWork(getBaseContext());
					break;
				default:
					break;
				}
			};
		};
		armManager = new ArmManager(this, handler);


	};
}
