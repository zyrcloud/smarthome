package com.mobile.server.location;

import android.content.Context;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.mobile.server.utils.Log;

public class LocationService {
	private static final String TAG = "LocationService";
	public static LocationClient mLocationClient = null;
	public static BDLocationListener myListener = null;
	public static void init(Context context){
		//每次启动的时候，上报下地理位置吧
		startWork(context);
	}
	public static void startWork(Context context){
		Log.d(TAG, "enter LocationService::init()");
		if(null != mLocationClient && mLocationClient.isStarted()){
			stopWork();
		}
		myListener = new MyLocationListener();
		mLocationClient = new LocationClient(context.getApplicationContext()); //声明LocationClient类
		mLocationClient.registerLocationListener( myListener ); //注册监听函数

	    LocationClientOption option = new LocationClientOption();
	    option.setLocationMode(LocationMode.Battery_Saving);//设置定位模式
	    option.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认值gcj02
	    option.setScanSpan(10000);
//	    option.setScanType(5000);//设置发起定位请求的间隔时间为5000ms
	    option.setIsNeedAddress(true);//返回的定位结果包含地址信息
	    option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
	    mLocationClient.setLocOption(option);

	    mLocationClient.start();
	    startLocation();
	}

	public static void stopWork(){
		Log.d(TAG, "enter LocationService::stopWork");
		if(null != mLocationClient){
			mLocationClient.unRegisterLocationListener(myListener);
			myListener = null;
			mLocationClient = null;
		}
	}

	private static void startLocation(){
		Log.d(TAG, "enter LocationService::startLocation()");
	    if (mLocationClient != null && mLocationClient.isStarted()){
	    	Log.d(TAG, "begin to request location");
	    	mLocationClient.requestLocation();
	    	mLocationClient.requestOfflineLocation();
	    }else {
	        Log.d(TAG, "locClient is null or not started");
	    }
	}
}
