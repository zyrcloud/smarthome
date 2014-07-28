package com.android.datatype;

import java.util.Date;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.android.comfun.Tools;

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
	public DeviceBaseInfo(String imei, String pushId, String nickName){
		this.imei = imei;
		this.pushId = pushId;
		this.nickname = nickName;
		this.regDate = Tools.getTimeStr(new Date());
	}
	public ContentValues getContentValues(){
		ContentValues values = new ContentValues();
		values.put(ITEM_IMEI, imei);
		values.put(ITEM_PUSHID, pushId);
		values.put(ITEM_REGDATE, regDate);
		values.put(ITEM_NICKNAME, Tools.Base64_encode(nickname));
		values.put(ITEM_EXTINFO, Tools.Base64_encode(extinfo));

		values.put(ITEM_TIME, time);
		values.put(ITEM_LOCTYPE, locType);
		values.put(ITEM_LATITUDE, latitude);
		values.put(ITEM_LONGITUDE, longitude);
		values.put(ITEM_RADIUS, radius);
		values.put(ITEM_ADDR, Tools.Base64_encode(addr));
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
	public static ContentValues fromHttpServletRequest(HttpServletRequest request){
		ContentValues values = new ContentValues();
//		DeviceBaseInfo devices = new DeviceBaseInfo();
		String tempStr = request.getParameter(ITEM_IMEI);
		if(!TextUtils.isEmpty(tempStr)){
//			devices.imei = tempStr;
			values.put(ITEM_IMEI, tempStr);
		}
		tempStr = request.getParameter(ITEM_PUSHID);
		if(!TextUtils.isEmpty(tempStr)){
//			devices.pushId = tempStr;
			values.put(ITEM_PUSHID, tempStr);
		}
		tempStr = request.getParameter(ITEM_REGDATE);
		if(!TextUtils.isEmpty(tempStr)){
//			devices.regDate = tempStr;
			values.put(ITEM_REGDATE, tempStr);
		}
		tempStr = request.getParameter(ITEM_NICKNAME);
		if(!TextUtils.isEmpty(tempStr)){
//			devices.nickname = Tools.Base64_decode(tempStr);
			values.put(ITEM_NICKNAME, tempStr);
		}
		tempStr = request.getParameter(ITEM_EXTINFO);
		if(!TextUtils.isEmpty(tempStr)){
//			devices.extinfo = Tools.Base64_decode(tempStr);
			values.put(ITEM_EXTINFO, tempStr);
		}
		tempStr = request.getParameter(ITEM_TIME);
		if(!TextUtils.isEmpty(tempStr)){
//			devices.time = tempStr;
			values.put(ITEM_TIME, tempStr);
		}
		tempStr = request.getParameter(ITEM_LOCTYPE);
		if(!TextUtils.isEmpty(tempStr)){
			try{
//				devices.locType = Integer.parseInt(tempStr);
				values.put(ITEM_LOCTYPE, Integer.parseInt(tempStr));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		tempStr = request.getParameter(ITEM_LATITUDE);
		if(!TextUtils.isEmpty(tempStr)){
			try{
//				devices.latitude = Double.parseDouble(tempStr);
				values.put(ITEM_LATITUDE, Double.parseDouble(tempStr));
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		tempStr = request.getParameter(ITEM_LONGITUDE);
		if(!TextUtils.isEmpty(tempStr)){
			try{
//				devices.longitude = Double.parseDouble(tempStr);
				values.put(ITEM_LONGITUDE, Double.parseDouble(tempStr));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		tempStr = request.getParameter(ITEM_RADIUS);
		if(!TextUtils.isEmpty(tempStr)){
			try{
//				devices.radius = Double.parseDouble(tempStr);
				values.put(ITEM_RADIUS, Double.parseDouble(tempStr));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		tempStr = request.getParameter(ITEM_ADDR);
		if(!TextUtils.isEmpty(tempStr)){
//			devices.addr = Tools.Base64_decode(ITEM_ADDR);
			values.put(ITEM_ADDR, ITEM_ADDR);
		}
//		return devices;
		return values;
	}
//	public void setIfNotEmpty(DeviceBaseInfo devices, ){
//
//	}
	@Override
	public String toString() {
		return getJSONObject().toString();
	}

}
