package com.mobile.server.arm.bluetooth;

import android.bluetooth.BluetoothSocket;

public interface ISocketEvent {
	void onConnecting(BluetoothSocket socket);
	void onConnected(BluetoothSocket socket);  //通知调用方已经连接上
	void onReceived(BluetoothSocket socket, byte[] buf, int start, int end); //通知已经收到了数据，其中start为buf起点，end为终点idx
	void onDisConnect(BluetoothSocket socket); //通知已经断连
	
	enum NotifyType{Recevied, DisConnect, Connect};
	
}
