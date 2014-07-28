package com.android.testjetty.testDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelpler extends SQLiteOpenHelper{
	private static final int VERSION = 1;//版本  
    private static final String DB_NAME = "people.db";//数据库名  
    public static final String STUDENT_TABLE = "student";//表名  
    public static final String _ID = "_id";//表中的列名  
    public static final String NAME = "name";//表中的列名  
    public static final String AGE = "age";//表中的列名  
    //创建数据库语句，STUDENT_TABLE，_ID ，NAME的前后都要加空格  
	private static final String CREATE_TABLE = 
			"create table " + STUDENT_TABLE + " ( " + 
				_ID + " Integer primary key autoincrement," + 
				NAME + " text," +
				AGE + " Integer)";
//	private static final String INSERT_DATA =
//			"insert into " + STUDENT_TABLE + "(name, age) values(" +
//			 "'zhangsan', 0);";
	public DBHelpler(Context context) {
		super(context, DB_NAME, null, VERSION);
	}
	public DBHelpler(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

    //数据库第一次被创建时调用   
    @Override  
    public void onCreate(SQLiteDatabase db) {  
        db.execSQL(CREATE_TABLE);
        
        ContentValues values = new ContentValues();  
        values.put(NAME, "zhangsan");  
        values.put(AGE, 0);
        db.insert(STUDENT_TABLE, null, values);
    }  
  
    //版本升级时被调用   
    @Override  
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
  
    } 

}
