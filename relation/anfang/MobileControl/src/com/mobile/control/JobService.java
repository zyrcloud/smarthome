package com.mobile.control;

import com.mobile.control.jobs.GetAllDevices;
import com.mobile.control.jobs.RequestClearPos;
import com.mobile.control.jobs.RequestNotification;
import com.mobile.control.jobs.RequestTakePhoto;
import com.mobile.control.jobs.RequestUpdateDevices;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class JobService extends IntentService{
	private static final String TAG = "JobService";
	public static final String ACT_GET_ALL_DEVICES = "action_getAlldevices";
	public static final String ACT_REQ_TAKE_PIC = "action_req_takePics";
	public static final String ACT_REQ_SHOW_NOTIFICATION = "action_req_notifycation";
	public static final String ACT_REQ_CLEAR_LOCATION = "action_clear_regist";
	public static final String ACT_REQ_UPDATE_DEVICES = "action_update_devices";

	public static final String Extra_END_PushIDS = ".pushIds";
	public static final String Extra_END_IMEI = ".imei";
	public static final String Extra_END_MSG = ".msg";

	public static void startJob(Context context, String action){
		Log.d(TAG, "enter JobService::startJob(action:" + action + ")");
		Intent intent = new Intent(context, JobService.class);
		intent.setAction(action);
		context.startService(intent);
	}
	public static void startJob(Context context, String action, Bundle bd){
		Log.d(TAG, "enter JobService::startJob(action:" + action + ")");
		Intent intent = new Intent(context, JobService.class);
		intent.putExtras(bd);
		intent.setAction(action);
		context.startService(intent);
	}
	public JobService(){
		super("JobService");
	}
	public JobService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(TAG, "enter JobService::onHandleIntent(intent:" + intent + ")");
		String action = intent.getAction();
		if(ACT_GET_ALL_DEVICES.equals(action)){
			new GetAllDevices(this).startWrok();
		} else if(ACT_REQ_TAKE_PIC.equals(action)){
			String pushIds = intent.getStringExtra(getPackageName() + Extra_END_PushIDS);
			new RequestTakePhoto(this, pushIds).startWrok();
		} else if(ACT_REQ_SHOW_NOTIFICATION.equals(action)){
			String pushIds = intent.getStringExtra(getPackageName() + Extra_END_PushIDS);
			String msg = intent.getStringExtra(getPackageName() + Extra_END_MSG);
			new RequestNotification(this, pushIds, msg).startWrok();
		} else if(ACT_REQ_CLEAR_LOCATION.equals(action)) {
			String imei = intent.getStringExtra(getPackageName() + Extra_END_IMEI);
			new RequestClearPos(this, imei).startWrok();
		} else if(ACT_REQ_UPDATE_DEVICES.equals(action)){
			String pushId = intent.getStringExtra(getPackageName() + Extra_END_PushIDS);
			new RequestUpdateDevices(this, pushId).startWrok();
		}
	}

}
