package com.mobile.control.ui;

import java.util.LinkedList;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.control.JobService;
import com.mobile.control.datatype.DeviceBaseInfo;
import com.mobile.control.db.DBManager;

public class DevicesControlActivity extends Activity {
	private static final String TAG = "DevicesControlActivity";

	private static final String ITEM_DEVICES = "devices";

	public static boolean showByImei(Activity activty, String imei){
		DeviceBaseInfo devices = getDevices(activty, imei, null);
		if(null == devices) {
			Log.e(TAG, "call getDevices return null");
			return false;
		}
		return showByDevices(activty, devices);
	}
	public static boolean showByPushId(Activity activty, String pushId){
		DeviceBaseInfo devices = getDevices(activty, null, pushId);
		if(null == devices) {
			Log.e(TAG, "call getDevices return null");
			return false;
		}
		return showByDevices(activty, devices);
	}
	public static boolean showByDevices(Activity activity, DeviceBaseInfo devices){
		if(null == devices){
			Log.e(TAG, "in showByDevices(devices is null!");
			return false;
		}
		Intent intent = new Intent(activity, DevicesControlActivity.class);
		intent.putExtra(ITEM_DEVICES, devices.getJSONObject().toString());
		activity.startActivity(intent);
		return true;
	}

	public static DeviceBaseInfo getDevices(Context context, String imei, String pushId){
		Log.d(TAG, "enter getDevices(imei:" + imei + " pushId:" + pushId + ")");
		LinkedList<Pair<String, String>> lParams = new LinkedList<Pair<String,String>>();
		if(!TextUtils.isEmpty(imei)){
			lParams.add(new Pair<String, String>(DeviceBaseInfo.ITEM_IMEI, imei));
		} else if(!TextUtils.isEmpty(pushId)) {
			lParams.add(new Pair<String, String>(DeviceBaseInfo.ITEM_PUSHID, pushId));
		} else {
			Toast.makeText(context, "in DevicesControlActivity imei and pushid all null!!", Toast.LENGTH_SHORT).show();
			return null;
		}
		DeviceBaseInfo devices = DBManager.getOneDevices(context, lParams);
		return devices;
	}


	EditText giveMsg = null;
	DeviceBaseInfo devices = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent callIntent =getIntent();
		String devicesJsonStr = callIntent.getStringExtra(ITEM_DEVICES);
		try {
			devices = DeviceBaseInfo.fromJSONStr(devicesJsonStr);

		} catch (JSONException e) {
			Log.e(TAG, e.toString(), e);
		} finally {
			if(null == devices){
				Log.e(TAG, "call DeviceBaseInfo.fromJSONStr(" + devicesJsonStr + " return null");
				finish();
				return;
			}
		}

		init_Layout();
	}
	void init_Layout(){
        final LinearLayout linearLayout=new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);



        TextView info = new TextView(this);
        info.setText(devices.toString());
        info.setMinLines(4);
        linearLayout.addView(info);

		Button bTakePhoto = new Button(this);
		bTakePhoto.setText("来拍一张");
		bTakePhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bd = new Bundle();
				bd.putString(getPackageName() + JobService.Extra_END_PushIDS, devices.pushId);
				JobService.startJob(getBaseContext(), JobService.ACT_REQ_TAKE_PIC, bd);
			}
		});
		linearLayout.addView(bTakePhoto);



		giveMsg = new EditText(this);
		giveMsg.setText("你好，我的朋友！");
		linearLayout.addView(giveMsg);

		Button bShowMsgs = new Button(this);
		bShowMsgs.setText("给个通知");
		bShowMsgs.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String sendMsg = giveMsg.getText().toString();
				if(TextUtils.isEmpty(sendMsg)){
					Toast.makeText(DevicesControlActivity.this, "empty msg cannot send!!", Toast.LENGTH_SHORT).show();
					return;
				}
				Bundle bd = new Bundle();
				bd.putString(getPackageName() + JobService.Extra_END_PushIDS, devices.pushId);
				bd.putString(getPackageName() + JobService.Extra_END_MSG, giveMsg.getText().toString());
				JobService.startJob(getBaseContext(), JobService.ACT_REQ_SHOW_NOTIFICATION, bd);
			}
		});
		linearLayout.addView(bShowMsgs);


		Button bClearDevices = new Button(this);
		bClearDevices.setText("清除注册信息");
		bClearDevices.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bd = new Bundle();
				bd.putString(getPackageName() + JobService.Extra_END_IMEI, devices.imei);
				JobService.startJob(getBaseContext(), JobService.ACT_REQ_CLEAR_LOCATION, bd);
			}
		});
		linearLayout.addView(bClearDevices);


		Button bUpdateDevices = new Button(this);
		bUpdateDevices.setText("更新该设备");
		bUpdateDevices.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bd = new Bundle();
				bd.putString(getPackageName() + JobService.Extra_END_PushIDS, devices.pushId);
				JobService.startJob(getBaseContext(), JobService.ACT_REQ_UPDATE_DEVICES, bd);
			}
		});
		linearLayout.addView(bUpdateDevices);




		setContentView(linearLayout);
	}
}
