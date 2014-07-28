package com.mobile.server.location;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.mobile.server.JobService;
import com.mobile.server.MainPreference;
import com.mobile.server.utils.Command;
import com.mobile.server.utils.Log;

public class MyLocationListener implements BDLocationListener {
	private static final String TAG = "MyLocationListener";

	@Override
	public void onReceiveLocation(BDLocation location) {
		Log.d(TAG, "enter MyLocationListener::onReceiveLocation(" + location + ")");
		if (location == null){
			return;
		}
		PositionInfo pi = new PositionInfo(location);
		String locationStr = pi.toJsonStr();
		Log.d(TAG, "loc:" + locationStr);
		MainPreference.setLocation(pi.toJsonStr());
		JobService.StartJobService(Command.CMD_LOCATION);
		LocationService.stopWork();
	}

	@Override
	public void onReceivePoi(BDLocation poiLocation) {
		Log.d(TAG, "enter MyLocationListener::onReceivePoi(poiLocation:" + poiLocation + "), will not report");
	}
}
