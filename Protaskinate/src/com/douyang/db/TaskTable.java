package com.douyang.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TaskTable {

	public static final String TABLE_TASK = "tasks";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_CATEGORY = "category";
	public static final String COLUMN_SUMMARY = "summary";
	public static final String COLUMN_DESCRIPTION = "description";
	
	public static final String DB_CREATE = "create table  " +
	TABLE_TASK + "(" +
	COLUMN_ID + "integer primary key autoincrement, " +
	COLUMN_CATEGORY + " text not null, " +
	COLUMN_SUMMARY + " text not null, " +
	COLUMN_DESCRIPTION + ");";
	
	public static final String DB_CREATE_2 = "create table " 
		      + TABLE_TASK
		      + "(" 
		      + COLUMN_ID + " integer primary key autoincrement, " 
		      + COLUMN_CATEGORY + " text not null, " 
		      + COLUMN_SUMMARY + " text not null," 
		      + COLUMN_DESCRIPTION
		      + " text not null" 
		      + ");";
	
	public static void onCreate(SQLiteDatabase db){
		db.execSQL(DB_CREATE_2);
	}
	
	public static void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion){
	    Log.w(TaskTable.class.getName(), "Upgrading database from version "
	            + oldVersion + " to " + newVersion
	            + ", which will destroy all old data");
	        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
	        onCreate(db);
	}
}
