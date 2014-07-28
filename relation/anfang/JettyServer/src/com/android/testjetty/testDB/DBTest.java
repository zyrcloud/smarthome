package com.android.testjetty.testDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBTest {
	Context mContext;
	DBHelpler dbHelper;
	
	public DBTest(Context context) {
		dbHelper = new DBHelpler(context);
		mContext = context;
	}
	
	public int addOne(){

		SQLiteDatabase  db = null;
		Cursor cursor = null;
		try{
			db = dbHelper.getWritableDatabase();
			cursor = 
					db.query(DBHelpler.STUDENT_TABLE, new String[]{DBHelpler.AGE}, 
							" name = ?", new String[]{"zhangsan"}, 
							null, null, null);
			if(null == cursor || cursor.isAfterLast()){
				return 0;
			}
			cursor.moveToNext();
			int age = cursor.getInt(cursor.getColumnIndex(DBHelpler.AGE));
			ContentValues values = new ContentValues();
			values.put(DBHelpler.AGE, age + 1);
			db.update(DBHelpler.STUDENT_TABLE, values, "name = ?", new String[]{"zhangsan"});

			return age + 1;
		} finally{
			if(null != cursor && !cursor.isClosed()){
				cursor.close();
			}
		}
	}
}
