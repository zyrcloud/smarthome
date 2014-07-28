package com.mobile.server.arm;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.mobile.server.MainService;
import com.mobile.server.arm.bluetooth.Bluetoothclient;
import com.mobile.server.arm.bluetooth.Bluetoothclient.State;
import com.mobile.server.arm.bluetooth.ISocketEvent;
import com.mobile.server.utils.Command;
import com.mobile.server.utils.Const;
import com.mobile.server.utils.Log;
import com.mobile.server.utils.ToastShow;

public class ArmManager {
	private static final String TAG = "ArmManager";
	private static final int MSG_DECODE_STR = 10;
	private static final int MSG_SHOW_STR = 11;
	StringBuffer recDatas = new StringBuffer();
	Handler outHandler = null;  //用于外部通讯
	Context context = null;

	Bluetoothclient bluetoothClient = null;

	Handler innerHandler = new Handler(){  //用于ArmManager内部延迟处理
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_DECODE_STR:
				String dataStr = recDatas.toString();
				recDatas = new StringBuffer();
				int action = Command.getCommandFromStr(dataStr);
				outHandler.sendEmptyMessage(action);
				notifyReceviedData(dataStr);
				break;
			case MSG_SHOW_STR:
				String text = (String) msg.obj;
				int duration = msg.arg1;
				ToastShow.show(context, text, duration);
			default:
				break;
			}
		};
	};

	private void showToast(String text, boolean isLong){
		Message msg = innerHandler.obtainMessage(MSG_SHOW_STR);
		msg.arg1 = isLong? ToastShow.LENGTH_LONG: ToastShow.LENGTH_SHORT;
		msg.obj = text;
		msg.sendToTarget();
	}
	public ArmManager(Context context, Handler handler){
		this.outHandler = handler;
		this.context = context;
	}
	public synchronized void startConnect(){
		Log.d(TAG, "enter ArmManager::startConnect()");
		if(null != bluetoothClient){
			if(bluetoothClient.getState().ordinal() == State.Connected.ordinal() ){
				Log.d(TAG, "aread connected, do nothing!");
				return;
			} else {
				//必须要让之前的线程退出，否则会存在野线程在创建连接
				bluetoothClient.exit();
			}
		}
		bluetoothClient = new  Bluetoothclient(new ISocketEvent(){
				long lastNotifyTime = 0;
				NotifyType notifyType = null;
				boolean canNotify(NotifyType type){
					//3s之内同一个消息只弹出一个提示，避免狂弹提示
					if(null != notifyType &&
						notifyType.ordinal() == type.ordinal() &&
						System.currentTimeMillis() - lastNotifyTime <= 3000){
						return false;
					}
					notifyType = type;
					lastNotifyTime = System.currentTimeMillis();
					return true;
				}

				@Override
				public void onReceived(BluetoothSocket socket, byte[] buf, int start,
						int end) {
					recDatas.append(new String(buf, start, end));

					innerHandler.removeMessages(MSG_DECODE_STR);
					Message msg = innerHandler.obtainMessage(MSG_DECODE_STR);
					innerHandler.sendMessageDelayed(msg, 1000); // 2s处理一次
				}

				@Override
				public void onDisConnect(BluetoothSocket socket) {
//					Toast.makeText(context, "socket disconnect" + socket, Toast.LENGTH_SHORT).show();
					if(canNotify(NotifyType.DisConnect)){
						showToast("socket disconnect, socket:" + socket, false);
					}
					MainService.startServiceByCmd(context, Command.CMD_SYSTEM_GET_STATE);
				}

				@Override
				public void onConnected(BluetoothSocket socket) {
//					Toast.makeText(context, "socket connected" + socket.getRemoteDevice().getName(), Toast.LENGTH_LONG).show();
					if(canNotify(NotifyType.Connect)){
						showToast("socket connected" + socket.getRemoteDevice().getName(), true);
					}
					MainService.startServiceByCmd(context, Command.CMD_SYSTEM_GET_STATE);
				}

				@Override
				public void onConnecting(BluetoothSocket socket) {
					MainService.startServiceByCmd(context, Command.CMD_SYSTEM_GET_STATE);
				}
			});
	}
	private void notifyReceviedData(String data){
		Intent receviedIntent = new Intent(Const.ACTION_SOCKET_RECEVIED_DATAS);
		receviedIntent.putExtra(Const.EXTRA_ITEM, data);
		context.sendBroadcast(receviedIntent);
	}
	public synchronized void disConnect(){
		Log.d(TAG, "enter ArmManager::disConnect()");
		if(null != bluetoothClient){
			bluetoothClient.exit();
			bluetoothClient = null;
		}
	}
	public int getState(){
		if(null == bluetoothClient){
			return Bluetoothclient.State.WaitStart.ordinal();
		}
		return bluetoothClient.getState().ordinal();
	}
}
