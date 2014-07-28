package com.mobile.control.ui;

import java.util.LinkedList;

import org.json.JSONException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.mobile.control.R;
import com.mobile.control.datatype.DeviceBaseInfo;
import com.mobile.control.db.DBManager;

public class ShowPositonActivity extends Activity{
	private static final String TAG = "ShowPositonActivity";
	private static final String ACTION_UPDATE_MAP_VIEW = "action_updateMapView";
	private static final String DEVICES_JSON_STR = "devicesJsonStr";

	public static final int MSG_ALL_DEVICES_UPDATE = 1;


	UIReceiver uiReceiver = null;
	MapView mapView = null;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_ALL_DEVICES_UPDATE:
				updateDevicePost();
				break;
			default:
				break;
			}
		};
	};

	public void updateDevicePost(){
		Log.d(TAG, "enter ShowPositonActivity::updateDevicePost()");
		mapView.getMap().clear();
		LinkedList<DeviceBaseInfo> deviceBaseInfos = new LinkedList<DeviceBaseInfo>();
		deviceBaseInfos = DBManager.getAllDevices(this);
		if(null == deviceBaseInfos || deviceBaseInfos.size() == 0) {
			Log.e(TAG, "when updateDevicePost, devices list is empty!");
			return;
		}
		int invaidDevices = 0;
		int vaildDevices = 0;
		DeviceBaseInfo lastVailedDevices = null;
		for(DeviceBaseInfo devices : deviceBaseInfos){

			if(0 == devices.latitude && 0 == devices.longitude){
				Log.d(TAG, "devices:" + devices.nickname + " imei:" + devices.imei + " pos is invalid");
				invaidDevices ++;
				continue;
			}
			lastVailedDevices = devices;


			LatLng pt = new LatLng(devices.latitude, devices.longitude);
			BitmapDescriptor bitmapDescriptor =
					BitmapDescriptorFactory.fromResource(R.drawable.icon_marka + vaildDevices);
			Bundle bd = new Bundle();
			bd.putString(DEVICES_JSON_STR, devices.getJSONObject().toString());
			OverlayOptions mark = new MarkerOptions().position(pt).icon(bitmapDescriptor)
								.perspective(false).zIndex(vaildDevices + 2)
								.extraInfo(bd);
			mapView.getMap().addOverlay(mark);
			vaildDevices++;
//			mMarkerD = (Marker) (mBaiduMap.addOverlay(ooD));
		}
		Toast.makeText(this, "there is " + invaidDevices + " maybe LBS failed!", Toast.LENGTH_LONG).show();

		if(null != lastVailedDevices){
			Log.d(TAG, "map at:" + lastVailedDevices.toString());
			MapStatusUpdate u1 = MapStatusUpdateFactory.newLatLng(new LatLng(lastVailedDevices.latitude, lastVailedDevices.longitude));
			mapView.getMap().setMapStatus(u1);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_showposition);

        mapView = (MapView) findViewById(R.id.bmapView);
        mapView.getMap().setOnMarkerClickListener(new MyOnMarkClickListen(this, mapView.getMap()));
		uiReceiver = UIReceiver.regist(this, handler);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
		uiReceiver = UIReceiver.unRegist(this, uiReceiver);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapView.onResume();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mapView.onPause();
	}

	private static class MyOnMarkClickListen implements OnMarkerClickListener{
		private final Activity activity;
		private final BaiduMap map;
		public MyOnMarkClickListen(Activity activity, BaiduMap map) {
			this.activity = activity;
			this.map = map;
		}

		@Override
		public boolean onMarkerClick(Marker marker) {
//			final LatLng ll = marker.getPosition();
//			Point p = map.getProjection().toScreenLocation(ll);
//			p.y -= 47;
//			LatLng llInfo = map.getProjection().fromScreenLocation(p);

			String devicestr = marker.getExtraInfo().getString(DEVICES_JSON_STR);
			DeviceBaseInfo devices;
			try {
				devices = DeviceBaseInfo.fromJSONStr(devicestr);
			} catch (JSONException e) {
				Log.e(TAG, e.toString(), e);
				return true;
			}

			Button posButton = new Button(activity);
			posButton.setBackgroundResource(R.drawable.popup);
			if(!TextUtils.isEmpty(devices.nickname)){
				posButton.setText(devices.nickname);
			} else {
				posButton.setText(devices.imei);
			}

			//创建InfoWindow的点击事件监听者
			MyInfoWindowClickListener listener = new MyInfoWindowClickListener(activity, devices);

			final LatLng ll = marker.getPosition();
			Point p = map.getProjection().toScreenLocation(ll);
			p.y -= 47;
			LatLng pt = map.getProjection().fromScreenLocation(p);

//			LatLng pt = new LatLng(devices.latitude, devices.longitude);
			//创建InfoWindow
			InfoWindow mInfoWindow = new InfoWindow(posButton, pt, listener);
			//显示InfoWindow
			map.showInfoWindow(mInfoWindow);
			return true;
		}

	}
	public static class MyInfoWindowClickListener implements OnInfoWindowClickListener{
		DeviceBaseInfo devices = null;
		Activity activity = null;
		MyInfoWindowClickListener(Activity activity, DeviceBaseInfo devices){
			this.activity = activity;
			this.devices = devices;
		}
		@Override
		public void onInfoWindowClick() {
			Log.d(TAG, "enter onInfoWindowClick(imei:" + devices + ")");
	    	DevicesControlActivity.showByDevices(activity, devices);
		}

	}
	public static class UIReceiver extends BroadcastReceiver {
		Context context = null;
		Handler activityHandler = null;
		public static UIReceiver regist(Context context, Handler activityHandler){
			Log.d(TAG, "enter UIReceiver::regist(context:" + context + ")");
			UIReceiver receiver = new UIReceiver(context, activityHandler);
			IntentFilter intentfilter = new IntentFilter();
			intentfilter.addAction(ACTION_UPDATE_MAP_VIEW);
			context.registerReceiver(receiver, intentfilter);
			return receiver;
		}
		public static UIReceiver unRegist(Context context, UIReceiver receiver){
			Log.d(TAG, "enter UIReceiver::regist(context:" + context + ")");
			context.unregisterReceiver(receiver);
			receiver.destory();
			return null;
		}
		private void destory(){
			activityHandler = null;
		}
		public UIReceiver(Context context, Handler activityHandler){
			this.context = context;
			this.activityHandler = activityHandler;

			//每次生成时，重新加载下数据
			onReceive(context, new Intent(ACTION_UPDATE_MAP_VIEW));
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, "enter UIReceiver::onReceiver(intent:" + intent + ")");
			String action = intent.getAction();
			if(ACTION_UPDATE_MAP_VIEW.equals(action)){
				activityHandler.sendEmptyMessage(MSG_ALL_DEVICES_UPDATE);
			}
		}

	}
}
