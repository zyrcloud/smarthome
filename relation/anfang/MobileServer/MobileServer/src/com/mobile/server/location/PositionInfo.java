package com.mobile.server.location;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.mobile.server.utils.Tools;

public class PositionInfo {
	private static final String TAG = "PositionInfo";
	String time; //定位时间
//    61 ： GPS定位结果
//    62 ： 扫描整合定位依据失败。此时定位结果无效。
//    63 ： 网络异常，没有成功向服务器发起请求。此时定位结果无效。
//    65 ： 定位缓存的结果。
//    66 ： 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果
//    67 ： 离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果
//    68 ： 网络连接失败时，查找本地离线定位时对应的返回结果
//    161： 表示网络定位结果
//    162~167： 服务端定位失败
//    502：key参数错误
//    505：key不存在或者非法
//    601：key服务被开发者自己禁用
//    602：key mcode不匹配
//    501～700：key验证失败
	int locType;

	double latitude; //纬度
	double longitude; //经度

	float radius; //定位的精确度，半径范围
	String addr; //位置
	public PositionInfo(){}
	public PositionInfo(BDLocation dbLocation){
		this.time = dbLocation.getTime();
		this.locType = dbLocation.getLocType();
		this.latitude = dbLocation.getLatitude();
		this.longitude = dbLocation.getLongitude();
		this.radius = dbLocation.getRadius();
		this.addr = dbLocation.getAddrStr();
	}
	String toJsonStr(){
		try{
			JSONObject jsonObj =  toJson();
			return jsonObj.toString();
		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
		}
		return "";
	}
	JSONObject toJson() throws JSONException{
		JSONObject json = new JSONObject();
		json.put("time", time);
		json.put("locType", locType);
		json.put("latitude", latitude);
		json.put("longitude", longitude);
		json.put("radius", radius);
		json.put("addr", Base64.encodeToString(addr.getBytes(), Base64.URL_SAFE));
		return json;
	}
	public static PositionInfo getFromJSONStr(String jsonstr) throws JSONException{
		System.out.println("in LocationInfo jsonStr:" + jsonstr);
		JSONObject json = new JSONObject(jsonstr);
		PositionInfo location = new PositionInfo();
		location.time = json.getString("time");
		location.locType = json.getInt("locType");
		location.latitude = json.getDouble("latitude");
		location.longitude = json.getDouble("longitude");
		location.radius = (float) json.getDouble("radius");

		byte[] addrDatas = Base64.decode(json.getString("addr").getBytes(), Base64.URL_SAFE);
		location.addr = new String(addrDatas);
		return location;
	}

}
