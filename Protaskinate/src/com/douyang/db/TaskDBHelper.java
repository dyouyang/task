package com.douyang.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDBHelper extends SQLiteOpenHelper{

	private static final String DB_NAME = "task_table.db";
	private static final int DB_VERSION = 1;
	

	public TaskDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		TaskTable.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		TaskTable.onUpgrade(db, oldVersion, newVersion);
		
	}

}
