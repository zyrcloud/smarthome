package com.mobile.server.netserver;

import java.util.HashMap;
import java.util.Map.Entry;

import android.text.TextUtils;
import android.util.Log;

import com.mobile.server.JobService;
import com.mobile.server.MainPreference;
import com.mobile.server.config.Config;
import com.mobile.server.utils.Command;
import com.mobile.server.utils.HttpUtils;
import com.mobile.server.utils.HttpUtils.Result;

public class NetServerTools {
	private static final String TAG = "NetServerTools";
	public static final String serverHost = "http://zyrcloud.oicp.net:2000/";
//	public static final String serverHost = "http://192.168.0.101:8080/";
	public static final String PUSH_REGIST_URI = "server/PushRegist";
	public static final String LOCATION_REPORT_URI = "server/LocationReport";

	public static final String IMEI = "imei";
	/**
	 * 服务端侧注册该设备
	 * @param context
	 * @return
	 */
	public static boolean registpush(){
		Log.d(TAG, "enter registpush()");
		HashMap<String, Object>params = new HashMap<String, Object>();

		params.put(IMEI, MainPreference.getIMEI());
		String pushId = MainPreference.getPushID();
		params.put("pushId", pushId);
		params.put("action", "registPush");
		if(!TextUtils.isEmpty(Config.getInstance().getNickName())){
			params.put(Config.ITEM_NICKNAME, Config.getInstance().getNickName());
		}
		Result result =  HttpUtils.post(serverHost + PUSH_REGIST_URI, params);
		if(result.isSuccess()){
			MainPreference.setRegistStatus(true);
		}
		return result.isSuccess();

	}
	public static boolean updateBaseInfo(HashMap<String, Object> updateInfo){
		Log.d(TAG, "enter updateBaseInfo()");
		if(null == updateInfo|| updateInfo.size() == 0 ){
			Log.e(TAG, "in updateBaseInfo, size is 0");
			return false;
		}

		for(Entry<String, Object> entry: updateInfo.entrySet()){
			Log.d(TAG, entry.getKey() + " = " + String.valueOf(entry.getValue()));
		}

		HashMap<String, Object>params = new HashMap<String, Object>();

		params.put(IMEI, MainPreference.getIMEI());
		params.put("action", "updateDevice");
		params.putAll(updateInfo);

		Result result =  HttpUtils.post(serverHost + PUSH_REGIST_URI, params);
		if(result.isSuccess()){
			MainPreference.setRegistStatus(true);
		}
		return result.isSuccess();
	}
	public static boolean reportLocation(){
		Log.d(TAG, "enter reportLocation()");
		HashMap<String, Object>params = new HashMap<String, Object>();
		String location = MainPreference.getLocation();
		params.put(IMEI, MainPreference.getIMEI());
		params.put("location", location);

		Result result = HttpUtils.post(serverHost + LOCATION_REPORT_URI, params);
		if(result.isSuccess()){
			MainPreference.setReportSuccessLocationTime(System.currentTimeMillis());
			return true;
		//更新最后位置失败，可能是因为未注册，需再次上报下注册信息
		} else if(200103 == result.code) {
			JobService.StartJobService(Command.CMD_REGIST_PUSH);
			return true;
		} else {
			Log.i(TAG, "in reportLocation return failed, result:" + result);
			return false;
		}
	}
}
