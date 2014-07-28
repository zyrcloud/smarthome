package com.android.comfun;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.TextUtils;
import android.util.Base64;

public class Tools {
	public static SimpleDateFormat getDateFormate(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	public static String getTimeStr(Date date){
		return getDateFormate().format(date);
	}
	public static Date getDateFromStr(String dateTimeStr) throws ParseException{
		return getDateFormate().parse(dateTimeStr);
	}
	public static String Base64_decode(String base64_EnCodeStr){
		if(TextUtils.isEmpty(base64_EnCodeStr)){
			return "";
		}
		return new String(Base64.decode(base64_EnCodeStr, Base64.URL_SAFE|Base64.NO_WRAP));
	}
	public static String Base64_encode(String srcStr){
		if(TextUtils.isEmpty(srcStr)){
			return "";
		}
		return Base64.encodeToString(srcStr.getBytes(), Base64.URL_SAFE|Base64.NO_WRAP);
	}
}
