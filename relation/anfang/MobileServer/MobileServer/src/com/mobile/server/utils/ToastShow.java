package com.mobile.server.utils;

import android.content.Context;
import android.widget.Toast;

import com.mobile.server.config.Config;

public class ToastShow {
	public static int LENGTH_LONG = Toast.LENGTH_LONG;
	public static int LENGTH_SHORT = Toast.LENGTH_SHORT;
	public static void show(Context context, CharSequence  text, int duration) {
		if(Config.getInstance().getCanToast()){
			Toast.makeText(context, text, duration).show();
		}		
	}

}
