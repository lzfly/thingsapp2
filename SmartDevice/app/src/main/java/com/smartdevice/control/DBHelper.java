package com.smartdevice.control;

import com.smartdevice.utils.ParamsUtils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	private static final int version = 1; 
	private String sqlString = new String();
	
	public DBHelper(Context context, String name){
		this(context, name, null, version);
	}
	
	public DBHelper(Context context, String name, CursorFactory factory){
		this(context, name, factory, version);
	}

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
	public void setSqlString(String sql){
		this.sqlString = sql;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = sqlString;//"create table user(username varchar(20) not null , password varchar(60) not null );";          
	    db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql = "DROP TABLE IF EXISTS " + ParamsUtils.DB_NAME;
		db.execSQL(sql);
	}
	
	public Cursor select(String sql, String[] selectionArgs){
		SQLiteDatabase db = this.getReadableDatabase();
		return db.rawQuery(sqlString, selectionArgs);
	}
	
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
		try {
			SQLiteDatabase db = this.getReadableDatabase();
			return db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		
	}
	
	public void insert(String sql){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(sql);
	}

	public void delete(String sql){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(sql);
	}
	
	public void update(String sql){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(sql);
	}
}
