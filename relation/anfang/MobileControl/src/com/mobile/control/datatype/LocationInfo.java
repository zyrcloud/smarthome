package com.mobile.control.datatype;

import java.util.Date;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

import com.mobile.control.utils.Tools;

public class LocationInfo {
	public static final String ITEM_TIME = "time";
	public static final String ITEM_LOCTYPE = "locType";
	public static final String ITEM_LATITUDE = "latitude";
	public static final String ITEM_LONGITUDE = "longitude";
	public static final String ITEM_RADIUS = "radius";
	public static final String ITEM_ADDR = "addr";

	String time;
	int locType;
	double latitude;
	double longitude;
	double radius;
	String addr;

	public ContentValues getContentValues(){
		ContentValues values = new ContentValues();
		values.put(ITEM_TIME, time);
		values.put(ITEM_LOCTYPE, locType);
		values.put(ITEM_LATITUDE, latitude);
		values.put(ITEM_LONGITUDE, longitude);
		values.put(ITEM_RADIUS, radius);
		values.put(ITEM_ADDR, addr);
		return values;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getJSONObject().toString();
	}
	public static LocationInfo getFromCursor(Cursor cursor){
		if(null == cursor || cursor.isClosed() || cursor.isAfterLast()){
			System.out.println("LocationInfo:while get data from cursor, cursor is close!");
			return null;
		}
		LocationInfo location = new LocationInfo();
//		String[] names = cursor.getColumnNames();
//		System.out.println("cursorName:" + Arrays.asList(names));
//		System.out.println(ITEM_TIME + " in cursor.getColumnIndex:" + cursor.getColumnIndex(ITEM_TIME));
//		System.out.println(ITEM_LOCTYPE + " in cursor.getColumnIndex:" + cursor.getColumnIndex(ITEM_LOCTYPE));
		location.time = cursor.getString(cursor.getColumnIndex(ITEM_TIME));
		location.locType = cursor.getInt(cursor.getColumnIndex(ITEM_LOCTYPE));
		location.latitude = cursor.getDouble(cursor.getColumnIndex(ITEM_LATITUDE));
		location.longitude = cursor.getDouble(cursor.getColumnIndex(ITEM_LONGITUDE));
		location.radius = cursor.getDouble(cursor.getColumnIndex(ITEM_RADIUS));
		location.addr = cursor.getString(cursor.getColumnIndex(ITEM_ADDR));
		return location;
	}
	public static LocationInfo getFromJSONStr(String jsonstr) throws JSONException{
		System.out.println("in LocationInfo jsonStr:" + jsonstr);
		JSONObject json = new JSONObject(jsonstr);
		LocationInfo location = new LocationInfo();
		location.time = Tools.getTimeStr(new Date());
		location.locType = json.getInt(ITEM_LOCTYPE);
		location.latitude = json.getDouble(ITEM_LATITUDE);
		location.longitude = json.getDouble(ITEM_LONGITUDE);
		location.radius = json.getDouble(ITEM_RADIUS);
		location.addr = json.getString(ITEM_ADDR);
		return location;
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getLocType() {
		return locType;
	}

	public void setLocType(int locType) {
		this.locType = locType;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

}
