package com.mobile.server;

import android.app.Application;
import cn.jpush.android.api.JPushInterface;

import com.mobile.server.config.Config;
import com.mobile.server.location.LocationService;
import com.mobile.server.sendmail.SimpleSendMail;
import com.mobile.server.utils.Log;

public class MainApplication extends Application{
	private static MainApplication gServerApplication = null;
	@Override
	public void onCreate() {
		super.onCreate();
		gServerApplication = this;
		init_cfg();
		system_init();
	}
	public void init_cfg(){
		Config cfg = Config.getInstance();
		cfg.loadFromFile();
		Log.init(cfg);
		SimpleSendMail.init(cfg);
	}
	public void system_init(){
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);

		LocationService.startWork(this);
	}

	public static MainApplication getApplication(){
		return gServerApplication;
	}
}
