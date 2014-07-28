package com.mobile.server.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.mobile.server.JobService;
import com.mobile.server.MainPreference;
import com.mobile.server.MainService;
import com.mobile.server.utils.Command;

public class JPushReceiver extends BroadcastReceiver {
	private static final String TAG = "JPushReceiver";


	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "enter JPushReceiver::onReceive(intent:" + intent + ")");
		String action = intent.getAction();
		Bundle bundle = intent.getExtras();
		if(JPushInterface.ACTION_MESSAGE_RECEIVED.equals(action)){
			if(null != bundle){
				String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
				MainService.startServiceByMessage(context, message);
			}
		}else if(JPushInterface.ACTION_REGISTRATION_ID.equals(action)) {
			String pushId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			MainPreference.setPushID(pushId);
			JobService.StartJobService(Command.CMD_REGIST_PUSH);
		}
	}

}
