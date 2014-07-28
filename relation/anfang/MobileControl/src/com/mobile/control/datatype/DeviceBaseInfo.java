package com.mobile.control.datatype;

import java.util.Date;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

import com.mobile.control.utils.Tools;

public class DeviceBaseInfo {
	//基础信息
    public static final String ITEM_IMEI = "imei";//手机唯一键
    public static final String ITEM_PUSHID = "pushId";//push唯一识别符
    public static final String ITEM_REGDATE = "regdate"; //注册、更新设备的时间
    public static final String ITEM_NICKNAME = "nickname"; //用户昵称
    public static final String ITEM_EXTINFO = "extinfo";   //扩展信息

    //位置信息
	public static final String ITEM_TIME = "time";
	public static final String ITEM_LOCTYPE = "locType";
	public static final String ITEM_LATITUDE = "latitude";
	public static final String ITEM_LONGITUDE = "longitude";
	public static final String ITEM_RADIUS = "radius";
	public static final String ITEM_ADDR = "addr";



	public String imei;
	public String pushId;
	public String regDate = "";
	public String nickname = "";
	public String extinfo = "";

	public String time;
	public int locType;
	public double latitude;
	public double longitude;
	public double radius;
	public String addr;

	public DeviceBaseInfo(){}
	public DeviceBaseInfo(String imei, String pushId){
		this.imei = imei;
		this.pushId = pushId;
		this.regDate = Tools.getTimeStr(new Date());
	}
	public ContentValues getContentValues(){
		return getContentValues(true);
	}
	public ContentValues getContentValues(boolean encode){

		ContentValues values = new ContentValues();
		values.put(ITEM_IMEI, imei);
		values.put(ITEM_PUSHID, pushId);
		values.put(ITEM_REGDATE, regDate);
		if(encode){
			values.put(ITEM_NICKNAME, Tools.Base64_encode(nickname));
			values.put(ITEM_EXTINFO, Tools.Base64_encode(extinfo));
			values.put(ITEM_ADDR, Tools.Base64_encode(addr));
		} else {
			values.put(ITEM_NICKNAME, nickname);
			values.put(ITEM_EXTINFO, extinfo);
			values.put(ITEM_ADDR, addr);
		}

		values.put(ITEM_TIME, time);
		values.put(ITEM_LOCTYPE, locType);
		values.put(ITEM_LATITUDE, latitude);
		values.put(ITEM_LONGITUDE, longitude);
		values.put(ITEM_RADIUS, radius);
		return values;
	}
	public JSONObject getJSONObject(){
		JSONObject obj = new JSONObject();
		ContentValues values = getContentValues();
		for(Entry<String, Object> entry: values.valueSet()){
			try {
				obj.put(entry.getKey(), entry.getValue());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return obj;
	}
	public static DeviceBaseInfo fromCursor(Cursor cursor){
		if(null == cursor || cursor.isClosed() || cursor.isAfterLast()){
			System.out.println("when call DeviceBaseInfo.fromCursor, cur error!!");
			return null;
		}
		try{
			DeviceBaseInfo devices = new DeviceBaseInfo();
			devices.imei = cursor.getString(cursor.getColumnIndex(ITEM_IMEI));
			devices.pushId = cursor.getString(cursor.getColumnIndex(ITEM_PUSHID));
			devices.regDate = cursor.getString(cursor.getColumnIndex(ITEM_REGDATE));
			devices.nickname = Tools.Base64_decode(cursor.getString(cursor.getColumnIndex(ITEM_NICKNAME)));
			devices.extinfo = Tools.Base64_decode(cursor.getString(cursor.getColumnIndex(ITEM_EXTINFO)));

			devices.time = cursor.getString(cursor.getColumnIndex(ITEM_TIME));
			devices.locType = cursor.getInt(cursor.getColumnIndex(ITEM_LOCTYPE));
			devices.latitude = cursor.getDouble(cursor.getColumnIndex(ITEM_LATITUDE));
			devices.longitude = cursor.getDouble(cursor.getColumnIndex(ITEM_LONGITUDE));
			devices.radius = cursor.getDouble(cursor.getColumnIndex(ITEM_RADIUS));
			devices.addr = Tools.Base64_decode(cursor.getString(cursor.getColumnIndex(ITEM_ADDR)));
			return devices;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static DeviceBaseInfo fromJSONStr(String jsonstr) throws JSONException{
		System.out.println("in DeviceBaseInfo jsonStr:" + jsonstr);

		JSONObject json = new JSONObject(jsonstr);
		DeviceBaseInfo devices = new DeviceBaseInfo();

		devices.imei = json.getString(ITEM_IMEI);
		if(json.has(ITEM_PUSHID)){devices.pushId = json.getString(ITEM_PUSHID);}
		if(json.has(ITEM_REGDATE)){devices.regDate = json.getString(ITEM_REGDATE);}
		if(json.has(ITEM_NICKNAME)){devices.nickname =  Tools.Base64_decode(json.getString(ITEM_NICKNAME));}
		if(json.has(ITEM_EXTINFO)){devices.extinfo = Tools.Base64_decode(json.getString(ITEM_EXTINFO));}

		if(json.has(ITEM_TIME)) { devices.time = json.getString(ITEM_TIME);}
		if(json.has(ITEM_LOCTYPE)){ devices.locType = json.getInt(ITEM_LOCTYPE);}
		if(json.has(ITEM_LATITUDE)){ devices.latitude = json.getDouble(ITEM_LATITUDE);}
		if(json.has(ITEM_LONGITUDE)){devices.longitude = json.getDouble(ITEM_LONGITUDE);}
		if(json.has(ITEM_RADIUS)){devices.radius = json.getDouble(ITEM_RADIUS);}
		if(json.has(ITEM_ADDR)){devices.addr = Tools.Base64_decode(json.getString(ITEM_ADDR));}
		return devices;
	}
	@Override
	public String toString() {
		return getContentValues(false).toString();
	}

}
