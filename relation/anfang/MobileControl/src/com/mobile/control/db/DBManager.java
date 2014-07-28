package com.mobile.control.db;

import java.io.File;
import java.util.LinkedList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Pair;

import com.mobile.control.datatype.DeviceBaseInfo;
import com.mobile.control.datatype.LocationInfo;
import com.mobile.control.db.helper.DeviceBaseInfoHelper;
import com.mobile.control.db.helper.LocationInfoHelper;

public class DBManager {
	private static final String TAG = "DBManager";
	public static boolean insertDevices(Context context, LinkedList<DeviceBaseInfo> lDevices){
		Log.d(TAG,"enter DBManager::insertDevices(context:" + context +  ")");
		if(null == lDevices || lDevices.size() == 0){
			Log.i(TAG, "when insertDevices device is empty" );
			return false;
		}

		DeviceBaseInfoHelper mi = null;
		SQLiteDatabase db = null;
		Cursor queryCursor = null;
		try{
			mi = new DeviceBaseInfoHelper(context);
			db = mi.getWritableDatabase();
			db.execSQL("delete from " + DeviceBaseInfoHelper.TABLE_MOBILEINFO);
			for(DeviceBaseInfo devices:lDevices){
				Log.d(TAG, "begin to insert:" + devices);
				db.insert(DeviceBaseInfoHelper.TABLE_MOBILEINFO, null, devices.getContentValues());
			}
			Log.d(TAG,"insert " + lDevices.size() + " all success!");
			return true;
		} catch(Exception e) {
			Log.d(TAG,"call getAllDevices cause " + e.toString());
			e.printStackTrace();
		} finally {
			if(null != queryCursor){
				try{
					queryCursor.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(null != db && db.isOpen()){
				try{
					db.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(null != mi){
				try{
					mi.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	public static boolean delDevices(Context context, String imei){
		Log.d(TAG, "enter DBManager::delDevices(imei:" + imei + ")");

		DeviceBaseInfoHelper mi = null;
		SQLiteDatabase db = null;
		Cursor queryCursor = null;

		try{
			mi = new DeviceBaseInfoHelper(context);
			db = mi.getWritableDatabase();
			db.delete(DeviceBaseInfoHelper.TABLE_MOBILEINFO, "imei = ?", new String[]{imei});
//			db.delete(LocationInfoHelper.TABLE_NAME, "imei = ?", new String[]{imei});
			File dbFile = context.getDatabasePath(LocationInfoHelper.getDBNameByImei(imei));
			if(null != dbFile){
				dbFile.delete();
			}
			return true;
		} catch(Exception e) {
			Log.d(TAG,"call delDevices cause " + e.toString(), e);
		} finally {
			if(null != queryCursor){
				try{
					queryCursor.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(null != db && db.isOpen()){
				try{
					db.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(null != mi){
				try{
					mi.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	public static LinkedList<DeviceBaseInfo> getAllDevices(Context context){
		Log.d(TAG,"enter DBManager::getAllDevices(context:" + context + ")");

		LinkedList<DeviceBaseInfo> lDevices = new LinkedList<DeviceBaseInfo>();
		DeviceBaseInfoHelper mi = null;
		SQLiteDatabase db = null;
		Cursor queryCursor = null;

		try{
			mi = new DeviceBaseInfoHelper(context);
			db = mi.getReadableDatabase();
			queryCursor = db.query(DeviceBaseInfoHelper.TABLE_MOBILEINFO,
								null, null, null, null, null, null);
			if(null == queryCursor){
				Log.d(TAG,"after db.query(MobileInfoHelper.TABLE_MOBILEINFO, queryCursor is null!!");
				return lDevices;
			} else if(queryCursor.isClosed()){
				Log.d(TAG,"after db.query(MobileInfoHelper.TABLE_MOBILEINFO, queryCursor is isClosed!!");
				return lDevices;
			}
			if(queryCursor.isAfterLast()){
				Log.d(TAG,"after db.query(MobileInfoHelper.TABLE_MOBILEINFO, queryCursor is isAfterLast!!");
				return lDevices;
			}

			while(queryCursor.moveToNext()){
				DeviceBaseInfo devices = DeviceBaseInfo.fromCursor(queryCursor);
				if(null != devices){
					lDevices.add(devices);
				} else {
					Log.d(TAG,"call DeviceBaseInfo.fromCursor errors!");
					break;
				}
			}
			Log.d(TAG,"devices size:" + lDevices.size());
			return lDevices;
		} catch(Exception e) {
			Log.d(TAG,"call getAllDevices cause " + e.toString());
			e.printStackTrace();
		} finally {
			if(null != queryCursor){
				try{
					queryCursor.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(null != db && db.isOpen()){
				try{
					db.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(null != mi){
				try{
					mi.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return lDevices;
	}
	public static DeviceBaseInfo getOneDevices(Context context, LinkedList<Pair<String, String>>params){
		Log.d(TAG,"enter DBManager::getAllDevices(context:" + context + "params" + params + ")");
		DeviceBaseInfoHelper mi = null;
		SQLiteDatabase db = null;
		Cursor queryCursor = null;

		try{
			mi = new DeviceBaseInfoHelper(context);
			db = mi.getReadableDatabase();



			StringBuffer select = null;
			LinkedList<String >values = null;

			if(null != params && params.size() != 0){
				select = new StringBuffer();
				values = new LinkedList<String>();
				String lastParamKey = params.getLast().first;
				for(Pair<String, String> param: params){
					select.append(param.first).append("=? ");
					values.add(param.second);
					if(!lastParamKey.equals(param.first)){
						select.append(" and ");
					}
				}
			}
			queryCursor = db.query(DeviceBaseInfoHelper.TABLE_MOBILEINFO,
								null, select.toString(), values.toArray(new String[]{}), null, null, null);
			if(null == queryCursor){
				Log.d(TAG,"getOneDevices: after db.query(MobileInfoHelper.TABLE_MOBILEINFO, queryCursor is null!!");
				return null;
			} else if(queryCursor.isClosed()){
				Log.d(TAG,"getOneDevices: after db.query(MobileInfoHelper.TABLE_MOBILEINFO, queryCursor is isClosed!!");
				return null;
			}
			if(queryCursor.isAfterLast()){
				Log.d(TAG,"getOneDevices: after db.query(MobileInfoHelper.TABLE_MOBILEINFO, queryCursor is isAfterLast!!");
				return null;
			}
			if(queryCursor.moveToNext()){
				return DeviceBaseInfo.fromCursor(queryCursor);
			} else {
				Log.e(TAG, "when call moveToNext, return false!");
				return null;
			}

		} catch(Exception e) {
			Log.d(TAG,"call getOneDevices cause " + e.toString(), e);
		} finally {
			if(null != queryCursor){
				try{
					queryCursor.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(null != db && db.isOpen()){
				try{
					db.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(null != mi){
				try{
					mi.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static boolean insertLocationByImei(Context context, LinkedList<LocationInfo> locationInfoList, String imei){
		Log.d(TAG,"enter DBManager::insertLocationByImei(context:" + context +  ")");
		if(null == locationInfoList || locationInfoList.size() == 0){
			Log.i(TAG, "when insertDevices device is empty" );
			return false;
		}

		LocationInfoHelper mi = null;
		SQLiteDatabase db = null;
		Cursor queryCursor = null;
		try{
			mi = new LocationInfoHelper(context, imei);
			db = mi.getWritableDatabase();
			db.execSQL("delete from " + LocationInfoHelper.TABLE_NAME);
			for(LocationInfo location:locationInfoList){
				db.insert(LocationInfoHelper.TABLE_NAME, null, location.getContentValues());
			}
			Log.d(TAG,"insert " + locationInfoList.size() + " all success!");
			return true;
		} catch(Exception e) {
			Log.d(TAG,"call getAllDevices cause " + e.toString());
			e.printStackTrace();
		} finally {
			if(null != queryCursor){
				try{
					queryCursor.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(null != db && db.isOpen()){
				try{
					db.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(null != mi){
				try{
					mi.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	public static LinkedList<LocationInfo> getAllLocationByImei(Context context, String imei){
		Log.d(TAG,"enter DBManager::getLastLocationByImei(imei:" + imei +
				" context:" + context + ")");
		LocationInfoHelper lo = null;
		SQLiteDatabase db = null;
		Cursor queryCursor = null;
		try{
			lo = new LocationInfoHelper(context, imei);
			db = lo.getReadableDatabase();
			queryCursor =  db.query(LocationInfoHelper.TABLE_NAME, null, null,
					null, null, null, " _id desc");
			if(null == queryCursor || queryCursor.isClosed() || queryCursor.isAfterLast()){
				Log.d(TAG,"queryCursor is return err in getAllLocationByImei!!");
				return null;
			}

			LinkedList<LocationInfo> locations = new LinkedList<LocationInfo>();
			while(queryCursor.moveToNext()){
				LocationInfo locationInfo = LocationInfo.getFromCursor(queryCursor);
				if(null == locationInfo ){
					Log.d(TAG,"call LocationInfo.getFromCursor return null!, maybe system err");
					break;
				}
				locations.add(LocationInfo.getFromCursor(queryCursor));
				queryCursor.moveToNext();
			}
			return locations;
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			if(null != queryCursor){
				try{
					queryCursor.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(null != db && db.isOpen()){
				try{
					db.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(null != lo){
				try{
					lo.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
