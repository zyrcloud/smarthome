package com.mobile.server;

import java.text.ParseException;
import java.util.Date;

import com.mobile.server.utils.Tools;
import com.mobile.server.utils.Log;
import com.mobile.server.utils.MobileUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

public class MainPreference {
	private static final String TAG = "MainPreference";
	public static final String FILE_NAME = "mobileserver";

	public static final String ITEM_PUSH_ID = "pushid";
	public static SharedPreferences getSharedPreference(){
		Context context = MainApplication.getApplication();
		return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
	}
	public static Editor getEditor(){
		return getSharedPreference().edit();
	}

	public static boolean setPushID(String pushId){
		return getEditor().putString(ITEM_PUSH_ID, pushId).commit();
	}
	public static String getPushID(){
		return getSharedPreference().getString(ITEM_PUSH_ID, "");
	}
	public static boolean setRegistStatus(boolean registStatus){
		return getEditor().putBoolean("registStatus", registStatus).commit();
	}
	public static boolean getRegistStatus(){
		return getSharedPreference().getBoolean("registStatus", false);
	}

	public static String getIMEI(){
		//为确保双卡手机，优先使用配置项的。
		String imei = getSharedPreference().getString("imei", "");
		if(TextUtils.isEmpty(imei)){
			imei = MobileUtils.getIMEI();
		}
		if(TextUtils.isEmpty(imei)){
			Log.e(TAG, "get imei failed!!");
			return imei;
		} else {
			Log.e(TAG, "get the imei:" + imei);
			getEditor().putString("imei", imei).commit();
			return imei;
		}
	}

	public static boolean setLocation(String location){
		return getEditor().putString("location", location).commit();
	}
	public static String getLocation(){
		return getSharedPreference().getString("location", "");
	}

	public static boolean setReportSuccessLocationTime(long locationTime){
		String dateStr = Tools.getTimeStr(new Date(locationTime));
		return getEditor().putString("lastReportSuccessLocationTime", dateStr).commit();
	}
	public static long getReportSuccessLocationTime(){
		 String lastLocationFmtTime = getSharedPreference().getString("lastReportSuccessLocationTime", "");
		 if(TextUtils.isEmpty(lastLocationFmtTime)){
			 return 0;
		 }
		 try {
			return Tools.getDateFromStr(lastLocationFmtTime).getTime();
		} catch (ParseException e) {
			Log.e(TAG, e.toString(), e);
			return 0;
		}
	}
}
