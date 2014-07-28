package com.mobile.control.jobs;
/**
 *  ²Î¿¼https://github.com/jpush/
 */
import java.util.HashMap;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.mobile.control.Const;
import com.mobile.control.utils.HttpUtils;
import com.mobile.control.utils.HttpUtils.Result;

public class RequestClearPos extends Job{
	private static final String TAG = "RequestTakePhoto";
	String imei = null;
	public RequestClearPos(Context context, String imei) {
		super(context);
		this.imei = imei;
	}

	@Override
	public void startWrok() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("queryType", "requestClearPos");
		params.put("imei", imei);
		Result res =  HttpUtils.post(Const.NET_SERVER_URL + "/ManagerSerlet", params);
		if(null != res){
			showToast(res.toString(), Toast.LENGTH_SHORT);
		}

		if(res.isSuccess()){
			if(TextUtils.isEmpty(res.msg)){
				Log.e(TAG, "in RequestClearPos msg is empty");
				return;
			}
		} else {
			Log.e(TAG, "RequestClearPos failed:" + res.toString());
			return;
		}
	}

}
