package com.mobile.server.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.mobile.server.MainApplication;
import com.mobile.server.utils.Log;

public class Config implements OnSharedPreferenceChangeListener {
//	private static final String CONFIG_FILE_NAME ="com.mobile.server_preferences";
	private static final String TAG = "Config";
	private static Config g_Config = null;
	Context context = null;

	public static String ITEM_NICKNAME = "nickname";
	public static String ITEM_USE_BLUETOOLS = "usebluetooths";
	public static String ITEM_EMAIL_FROM = "emailFrom";
	public static String ITEM_EMAIL_FROM_PWD = "emailFromPwd";
	public static String ITEM_EMAIL_HOST = "emailHost";
	public static String ITEM_EMAIL_PORT = "emailPort";
	public static String ITEM_EMAIL_TO = "emailTo";
	public static String ITEM_EMAIL_SMTP_AUTH = "smtpAuth";

	public static 	String ITEM_PHOTO_DELE_PIC = "photoDelPic";
	public static String ITEM_PHOTO_SAVE_PATH = "photoSavePath";

	public static String ITEM_LOGLEVEL = "logLevel";
	public static String ITEM_CAN_TOAST = "canToast";
	public static String ITEM_AUTO_START = "autoStart";

	HashMap<String, Object> configMap = new HashMap<String, Object>();

	private Config(Context context){
		SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(context);
		sf.registerOnSharedPreferenceChangeListener(this);
		this.context = context;

		loadFromFile();
	}

	public static synchronized Config getInstance(){
		if(null != g_Config){
			return g_Config;
		}
		Context context = MainApplication.getApplication().getApplicationContext();
		g_Config = new Config(context);
		return g_Config;
	}

	public boolean loadFromFile(){
//		SharedPreferences sf = context.getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
		Log.d(TAG, "enter loadFromFile()");
		SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(context);
		configMap.clear();
		Map<String, ?> cfgs = sf.getAll();
		for(Map.Entry<String, ?>entry: cfgs.entrySet()){
			configMap.put(entry.getKey(), entry.getValue());
			Log.d(TAG, entry.getKey() + "=" + String.valueOf(entry.getValue()));
		}
		Log.d(TAG, "load cfg file over!! configMap=" + configMap);
		return true;
	}
	public void syncToFile(){
//		SharedPreferences sf = context.getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(context);
		Editor ed = sf.edit();
		ed.clear();
		for(Map.Entry<String, ?>cfg: configMap.entrySet()){
			Object obj = cfg.getKey();
			if(obj instanceof String){
				ed.putString(cfg.getKey(), String.valueOf(obj));
			} else if(obj instanceof Integer) {
				ed.putInt(cfg.getKey(), (Integer)obj);
			}
		}
	}

	public String getStrConfig(String itemName, String defValue, boolean canEmpty){
		Object value = configMap.get(itemName);
		Log.d(TAG, "cfg in file, itemName:" + itemName + " val:" + value + " configMap:" + configMap);
		if(null == value){
			return defValue;
		} else {
			String res = String.valueOf(value);
			if(!canEmpty && TextUtils.isEmpty(res)){
				return defValue;
			}
			return res;
		}
	}
	public int getIntConfig(String itemName, int defValue){
		Object value = configMap.get(itemName);
		Log.d(TAG, "cfg in file, itemName:" + itemName + " val:" + value + " configMap:" + configMap);
		if(null == value){
			return defValue;
		}

		try{
			return Integer.valueOf(String.valueOf(value));
		} catch(Exception e) {
			Log.e(TAG, "call getIntConfig:" + itemName + ", system err:" + e.toString());
			return defValue;
		}

	}
	public boolean getBoolConfig(String itemName, boolean defValue){
		Object value = configMap.get(itemName);
		Log.d(TAG, "cfg in file, itemName:" + itemName + " val:" + value + " configMap:" + configMap);
		if(null == value){
			return defValue;
		}

		try{
			return (Boolean) value;
		} catch(Exception e) {
			Log.e(TAG, "call getBoolConfig:" + itemName + ", system err:" + e.toString());
			return defValue;
		}
	}
	public boolean getUseBluetools(){
		return getBoolConfig(ITEM_USE_BLUETOOLS, false);
	}
	public String getNickName(){
		return getStrConfig(ITEM_NICKNAME, "", false);
	}
	public String getEmailFrom() {
		return getStrConfig(ITEM_EMAIL_FROM, "zyrcloud@163.com", false);
	}

	public String getEmailFromPwd(){
		return getStrConfig(ITEM_EMAIL_FROM_PWD, "27898228", false);
	}

	public String getEmailHost(){
		return getStrConfig(ITEM_EMAIL_HOST, "smtp.163.com", false);
	}

	public String getEmailPort(){
		return getStrConfig(ITEM_EMAIL_PORT, "25", false);
	}
	public void setEmailPort(String port){
		configMap.put(ITEM_EMAIL_PORT, port);
	}

	public String getEmailTo(){
		return getStrConfig(ITEM_EMAIL_TO, "zyrcloud@163.com", false);
	}
	public boolean getEmailSmtpAuth(){
		return getBoolConfig(ITEM_EMAIL_SMTP_AUTH, true);
	}

	public boolean getDelPic(){
		return getBoolConfig(ITEM_PHOTO_DELE_PIC, false);
	}

	public String getSavePicPath(){
		String defPath = "";
		try {
			defPath =Environment.getExternalStorageDirectory().getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getStrConfig(ITEM_PHOTO_SAVE_PATH, defPath, false);
	}

	public int getLogLevel(){
//		return getIntConfig(ITEM_LOGLEVEL, 10);
		return getIntConfig(ITEM_LOGLEVEL, android.util.Log.DEBUG);
	}
	public boolean getCanToast(){
		return getBoolConfig(ITEM_CAN_TOAST, false);
	}
	public boolean getAutoStart(){
		return getBoolConfig(ITEM_AUTO_START, false);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		MainApplication.getApplication().init_cfg();
	}
}
