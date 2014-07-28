package com.mobile.control.utils;

import java.io.File;

import android.content.Context;
import android.util.Log;

import com.mobile.control.db.DBManager;

public class MobileUtils {
	private static final String TAG = "MobileUtils";
	public static boolean clearAllDB(Context context){

		File dir = context.getDatabasePath("tempDB").getParentFile();
		if(null == dir){
			Log.d(TAG, "no database exists!");
			return true;
		}
		Log.d(TAG, "dir:" + dir.getAbsolutePath());
		if(null == dir.list() || 0 == dir.list().length){
			Log.d(TAG, "no database exists, list empty!");
			return true;
		}
		for(String fName: dir.list()){
			String fPathName = dir.getPath() + "/" + fName;
			Log.d(TAG, "ready to del " + fPathName);
			File fdel = new File(fPathName);
			fdel.delete();
		}
		return true;
	}
	public static boolean createDB(Context context){
		DBManager.getAllDevices(context);
		return true;
	}
}
