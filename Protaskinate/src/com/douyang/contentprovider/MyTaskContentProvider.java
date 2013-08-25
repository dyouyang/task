package com.douyang.contentprovider;

import com.douyang.db.TaskDBHelper;
import com.douyang.db.TaskTable;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class MyTaskContentProvider extends ContentProvider {

	private TaskDBHelper dbHelper;

	// strings for URI matching
	private static final int TASKS = 1;
	private static final int TASK_ID = 2;

	private static final String AUTHORITY = "com.douyang.contentprovider";
	private static final String BASE_PATH = "tasks";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/tasks";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/task";

	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, TASKS);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TASK_ID);

	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int rowsDeleted = 0;
		
		switch(uriType){
		case TASKS:
			rowsDeleted = db.delete(TaskTable.TABLE_TASK, selection, selectionArgs);
			break;
		case TASK_ID:
			String id = uri.getLastPathSegment();
			if(TextUtils.isEmpty(selection))
			{
				rowsDeleted = db.delete(TaskTable.TABLE_TASK, TaskTable.COLUMN_ID + "=" + id, null);
			}
			else
			{
				rowsDeleted = db.delete(TaskTable.TABLE_TASK, TaskTable.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
			}
			break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
				
		
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long id = 0;
		switch(uriType){
		case TASKS:
			id = db.insert(TaskTable.TABLE_TASK, null, values);
			break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public boolean onCreate() {
		dbHelper = new TaskDBHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		// check for illegal columns
		checkColumns(projection);
		
		queryBuilder.setTables(TaskTable.TABLE_TASK);

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case TASKS:
			break;
		case TASK_ID:
			queryBuilder.appendWhere(TaskTable.COLUMN_ID + " = "
					+ uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
		
		//execute query
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		
		
		return cursor;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		
		return 0;
	}
	
	private void checkColumns(String [] projection){
		//TODO

	}

}
