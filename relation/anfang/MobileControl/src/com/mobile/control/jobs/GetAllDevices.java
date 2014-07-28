package com.mobile.control.jobs;

import java.util.HashMap;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.mobile.control.Const;
import com.mobile.control.datatype.DeviceBaseInfo;
import com.mobile.control.db.DBManager;
import com.mobile.control.utils.HttpUtils;
import com.mobile.control.utils.HttpUtils.Result;
import com.mobile.control.utils.MobileUtils;

public class GetAllDevices extends Job{
	private static final String TAG = "GetAllDevices";
	public GetAllDevices(Context context) {
		super(context);
	}

	@Override
	public void startWrok() {
		MobileUtils.clearAllDB(context);
		getAllDevices();
	}
	public void getAllDevices(){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("queryType", "getAllDevices");
		Result res =  HttpUtils.post(Const.NET_SERVER_URL + "/ManagerSerlet", params);
		if(res.isSuccess()){
			if(TextUtils.isEmpty(res.msg)){
				Log.e(TAG, "in getAllDevices msg is empty");
				return;
			}
			try {
				JSONArray jsonDevices  =  new JSONArray(res.msg);
				LinkedList<DeviceBaseInfo> lDevices = new LinkedList<DeviceBaseInfo>();
				for(int i = 0; i < jsonDevices.length(); i++){
					DeviceBaseInfo devices = DeviceBaseInfo.fromJSONStr(String.valueOf(jsonDevices.get(i)));
					lDevices.add(devices);
				}
				DBManager.insertDevices(context, lDevices);
				showToast("sync " + lDevices.size() + " devices", Toast.LENGTH_SHORT);
				return;
			} catch (JSONException e) {
				Log.e(TAG, e.getMessage(), e);
				showToast("call GetAllDevices cause:" + e.toString(), Toast.LENGTH_SHORT);
			}
		} else if(null != res){
			showToast(res.toString(), Toast.LENGTH_SHORT);
		}
	}

}
