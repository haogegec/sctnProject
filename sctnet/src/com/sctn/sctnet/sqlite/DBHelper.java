package com.sctn.sctnet.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "mydb";
	private static final int VERSION = 1;
	
	private String whichTable;//判断是创建哪个表

	public DBHelper(Context context,String whichTable) {
		super(context, DB_NAME, null, VERSION);
		this.whichTable = whichTable;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String sql = "";
		if("jobSearchLog".equals(whichTable)){
			sql = "create table IF NOT EXISTS jobSearchLog (_id integer primary key autoincrement, workAreaName text," +
					"jobClassName text,needProfessionName text,workAreaId text,jobClassId text,needProfessionId text,total text)";
		}else{
			sql = "create table IF NOT EXISTS searchLog (_id integer primary key autoincrement, key text,name text)";
		}
		
		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
