package com.mobile.control.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.mobile.control.datatype.LocationInfo;

public class LocationInfoHelper extends SQLiteOpenHelper {
	private static int version = 1;
	public static final String PRE_DB_NAME = "location_";
	public static final String TABLE_NAME = "location";

	public LocationInfoHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public LocationInfoHelper(Context context, String imei){
		super(context, getDBNameByImei(imei) + ".db", null, version);
	}
	public static String getDBNameByImei(String imei){
		return PRE_DB_NAME + imei + ".db";
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		String createTabSql = "create table " + TABLE_NAME + " (" +
				" _id Integer primary key autoincrement," +
				LocationInfo.ITEM_TIME + " text," +
				LocationInfo.ITEM_LOCTYPE + " Integer," +
				LocationInfo.ITEM_LATITUDE + " REAL," +
				LocationInfo.ITEM_LONGITUDE + " REAL," +
				LocationInfo.ITEM_RADIUS + " REAL," +
				LocationInfo.ITEM_ADDR + " TEXT)";

		db.execSQL(createTabSql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
	}

}
