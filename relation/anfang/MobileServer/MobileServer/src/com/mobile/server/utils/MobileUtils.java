package com.mobile.server.utils;

import java.util.List;

import com.mobile.server.MainApplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.telephony.TelephonyManager;

public class MobileUtils {
	public static String getIMEI(){
		Context context = MainApplication.getApplication();
		TelephonyManager telephonyManager= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei=telephonyManager.getDeviceId();
		return imei;
	}

	public static boolean isInstalled(Context context, String action) {
		return isInstalled(context, new Intent(action));
    }
	public static boolean isInstalled(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        //检索所有可用于给定的意图进行的活动。如果没有匹配的活动，则返回一个空列表。
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
}
