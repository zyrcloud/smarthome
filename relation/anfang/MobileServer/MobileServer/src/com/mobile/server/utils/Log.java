package com.mobile.server.utils;

import com.mobile.server.config.Config;
 
public class Log {
	private static final String TAG = "Log";
	private static int curLogLevel = 0;
	
	public static void init(Config config){
		curLogLevel = config.getLogLevel();
		android.util.Log.d(TAG, "in init curLogLevel:" + curLogLevel);
	}
	public static void d(String tag, String msg){
		d(tag, msg, null);
	}
	public static void i(String tag, String msg){
		i(tag, msg, null);
	}
	public static void e(String tag, String msg){
		e(tag, msg, null);
	}
	
	public static void d(String tag, String msg, Throwable t){
		if( android.util.Log.DEBUG >= curLogLevel ) {
			android.util.Log.d(tag, msg, t);
		}
	}
	public static void i(String tag, String msg, Throwable t){
		if( android.util.Log.INFO >= curLogLevel ) {
			android.util.Log.d(tag, msg, t);
		}
	}
	public static void e(String tag, String msg, Throwable t){
		if( android.util.Log.ERROR >= curLogLevel ) {
			android.util.Log.d(tag, msg, t);
		}
	}
}
