package com.android.db;

import java.io.File;
import java.util.LinkedList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.datatype.DeviceBaseInfo;
import com.android.datatype.GlobalValues;
import com.android.datatype.LocationInfo;
import com.android.db.helper.DeviceBaseInfoHelper;
import com.android.db.helper.LocationInfoHelper;

public class DBManager {
	public static boolean registDevice(DeviceBaseInfo devices){
		System.out.println("enter DBManager::registDevice(devices:" + devices + " context:" + GlobalValues.context + ")");
		Context context = GlobalValues.context;
		DeviceBaseInfoHelper mi = null;
		SQLiteDatabase db = null;
		Cursor queryCursor = null;

		try{
			mi = new DeviceBaseInfoHelper(context);
			db = mi.getWritableDatabase();
			if(null == db || !db.isOpen()){
				System.out.println("db is null or not opened!");
				return false;
			}
			queryCursor = db.query(
					DeviceBaseInfoHelper.TABLE_MOBILEINFO,
					null,
					" imei=?",
					new String[]{devices.imei},
					null, null, null);
			if(queryCursor.getCount() > 0){
				queryCursor.moveToFirst();
				DeviceBaseInfo dbDevices = DeviceBaseInfo.fromCursor(queryCursor);
				System.out.println(dbDevices);
				return upDateDevice(context, devices, db);
			} else {
				return inserDevice(context, devices, db);
			}
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
			if(null != mi){
				try{
					mi.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	public static boolean delDevices(Context context, String imei){
		System.out.println("enter DBManager::delDevices(imei:" + imei + ")");

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
			System.out.println("call delDevices cause " + e.toString());
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
	public static boolean updateDevice(ContentValues values, String imei){
		System.out.println("enter DBManager::updateDevice(values:" + values + " imei:" + imei + ")");
		if(null == values || values.size() == 0){
			System.out.println("in DBManager::updateDevice(values is empty!!");
			return false;
		}
		Context context = GlobalValues.context;
		DeviceBaseInfoHelper mi = null;
		SQLiteDatabase db = null;
		Cursor queryCursor = null;

		try{
			mi = new DeviceBaseInfoHelper(context);
			db = mi.getWritableDatabase();
			if(null == db || !db.isOpen()){
				System.out.println("db is null or not opened!");
				return false;
			}
			return db.update(DeviceBaseInfoHelper.TABLE_MOBILEINFO, values, " imei = ?", new String[]{imei}) > 0;
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
	private static boolean inserDevice(Context context, DeviceBaseInfo devices, SQLiteDatabase db){
		System.out.println("enter DBManager::inserDevice(devices:" + devices + " context:" + GlobalValues.context + ")");
		return db.insert(DeviceBaseInfoHelper.TABLE_MOBILEINFO, null, devices.getContentValues()) > 0;
	}
	private static boolean upDateDevice(Context context, DeviceBaseInfo devices, SQLiteDatabase db){
		System.out.println("enter DBManager::upDateDevice(devices:" + devices + " context:" + GlobalValues.context + ")");
		return db.update(DeviceBaseInfoHelper.TABLE_MOBILEINFO, devices.getContentValues(),
				"imei =?", new String[]{ devices.imei}) > 0;
	}
	public static LinkedList<DeviceBaseInfo> getAllDevices(){
		System.out.println("enter DBManager::getAllDevices(context:" + GlobalValues.context + ")");

		LinkedList<DeviceBaseInfo> lDevices = new LinkedList<DeviceBaseInfo>();
		Context context = GlobalValues.context;
		DeviceBaseInfoHelper mi = null;
		SQLiteDatabase db = null;
		Cursor queryCursor = null;

		try{
			mi = new DeviceBaseInfoHelper(context);
			db = mi.getReadableDatabase();
			queryCursor = db.query(DeviceBaseInfoHelper.TABLE_MOBILEINFO,
								null, null, null, null, null, null);
			if(null == queryCursor){
				System.out.println("after db.query(MobileInfoHelper.TABLE_MOBILEINFO, queryCursor is null!!");
				return lDevices;
			} else if(queryCursor.isClosed()){
				System.out.println("after db.query(MobileInfoHelper.TABLE_MOBILEINFO, queryCursor is isClosed!!");
				return lDevices;
			}
			if(queryCursor.isAfterLast()){
				System.out.println("after db.query(MobileInfoHelper.TABLE_MOBILEINFO, queryCursor is isAfterLast!!");
				return lDevices;
			}

			while(queryCursor.moveToNext()){
				DeviceBaseInfo devices = DeviceBaseInfo.fromCursor(queryCursor);
				if(null != devices){
					lDevices.add(devices);
				} else {
					System.out.println("call DeviceBaseInfo.fromCursor errors!");
					break;
				}
			}
			System.out.println("devices size:" + lDevices.size());
			return lDevices;
		} catch(Exception e) {
			System.out.println("call getAllDevices cause " + e.toString());
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


	public static boolean insertLocation(LocationInfo locationInfo, String imei){
		System.out.println("enter DBManager::insertLocation(locationInfo:" + locationInfo +
				" imei:" + imei +
				" context:" + GlobalValues.context + ")");
		Context context = GlobalValues.context;
		LocationInfoHelper lo = null;
		SQLiteDatabase db = null;
		Cursor queryCursor = null;
		try{
			lo = new LocationInfoHelper(context, imei);
			db = lo.getWritableDatabase();
			return db.insert(LocationInfoHelper.TABLE_NAME, null, locationInfo.getContentValues()) > 0;
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
		return false;
	}
	public static boolean updateLastLocation(LocationInfo locationInfo, String imei){

		System.out.println("enter DBManager::updateLastLocation(locationInfo:" + locationInfo +
				" imei:" + imei +
				" context:" + GlobalValues.context + ")");
		Context context = GlobalValues.context;
		DeviceBaseInfoHelper lo = null;
		SQLiteDatabase db = null;
		Cursor queryCursor = null;

		try{
			lo = new DeviceBaseInfoHelper(context);
			db = lo.getWritableDatabase();
			return db.update(DeviceBaseInfoHelper.TABLE_MOBILEINFO, locationInfo.getContentValues(),
						" imei=?", new String[]{imei}) > 0;
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
		return false;
	}


//	public static LocationInfo getLastLocationByImei(String imei){
//		System.out.println("enter DBManager::getLastLocationByImei(imei:" + imei +
//				" context:" + GlobalValues.context + ")");
//		Context context = GlobalValues.context;
//		LocationInfoHelper lo = null;
//		SQLiteDatabase db = null;
//		Cursor queryCursor = null;
//		try{
//			lo = new LocationInfoHelper(context, imei);
//			db = lo.getReadableDatabase();
//			queryCursor =  db.query(LocationInfoHelper.TABLE_NAME, null, null,
//					null, null, null, " _id desc", " 1");
//			if(null == queryCursor || queryCursor.isClosed() || queryCursor.isAfterLast()){
//				return null;
//			}
//
//			queryCursor.moveToFirst();
//			return LocationInfo.getFromCursor(queryCursor);
//		} catch (Exception e){
//			e.printStackTrace();
//		} finally {
//			if(null != queryCursor){
//				try{
//					queryCursor.close();
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//			}
//			if(null != db && db.isOpen()){
//				try{
//					db.close();
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//			}
//			if(null != lo){
//				try{
//					lo.close();
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//			}
//		}
//		return null;
//	}

	public static LinkedList<LocationInfo> getAllLocationByImei(String imei){
		System.out.println("enter DBManager::getLastLocationByImei(imei:" + imei +
				" context:" + GlobalValues.context + ")");
		Context context = GlobalValues.context;
		LocationInfoHelper lo = null;
		SQLiteDatabase db = null;
		Cursor queryCursor = null;
		try{
			lo = new LocationInfoHelper(context, imei);
			db = lo.getReadableDatabase();
			queryCursor =  db.query(LocationInfoHelper.TABLE_NAME, null, null,
					null, null, null, " _id desc");
			if(null == queryCursor || queryCursor.isClosed() || queryCursor.isAfterLast()){
				System.out.println("queryCursor is return err in getAllLocationByImei!!");
				return null;
			}

			LinkedList<LocationInfo> locations = new LinkedList<LocationInfo>();
			while(queryCursor.moveToNext()){
				LocationInfo locationInfo = LocationInfo.getFromCursor(queryCursor);
				if(null == locationInfo ){
					System.out.println("call LocationInfo.getFromCursor return null!, maybe system err");
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
