package com.mobile.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mobile.server.config.Config;
import com.mobile.server.location.LocationService;
import com.mobile.server.utils.Command;
import com.mobile.server.utils.Log;

public class MainReceiver extends BroadcastReceiver{
	private static final String TAG = "MainReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "enter onReceive(intent:" + intent + ")");
		String action = intent.getAction();
		if("android.provider.Telephony.SECRET_CODE".equals(action)){
			Intent startActIntent = new Intent(context, MainActivity.class);
			startActIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(startActIntent);
			return;
		}
		if(Config.getInstance().getAutoStart()){
			MainService.startServiceByCmd(context, Command.CMD_SYSTEM_START_SERVICE);
		}

	}

	public void trigeJobsInNetWork(Context context, Intent intent){
		String action = intent.getAction();
		if(!ConnectivityManager.CONNECTIVITY_ACTION.equals(action)){
			return;
		}

//        State wifiState = null;
//        State mobileState = null;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
//        mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(!networkInfo.isAvailable()){
        	return;
        }

        //push未注册ok时，有网络变化时，赶紧补注册下
        if(!MainPreference.getRegistStatus()){
        	JobService.StartJobService(Command.CMD_REGIST_PUSH);
        }

        //超过一天，上报下地理位置
        long lastReportPosInterval = Math.abs(MainPreference.getReportSuccessLocationTime() - System.currentTimeMillis());
        if(lastReportPosInterval >= 24 * 60 * 60 * 1000){
        	LocationService.startWork(context);
        }


	}

}
