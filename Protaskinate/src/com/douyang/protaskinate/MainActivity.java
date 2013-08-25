package com.douyang.protaskinate;

import com.douyang.contentprovider.MyTaskContentProvider;
import com.douyang.db.TaskTable;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * @author yinglong The main listView activity that displays all current tasks.
 * 
 */
public class MainActivity extends Activity implements LoaderCallbacks<Cursor> {

	private SimpleCursorAdapter adapter;
	private ListView listViewTasks;

	private static final int DELETE_ID = Menu.FIRST + 1;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		// the add action will launch a blank task detail activity, where a new
		// task can be created
		case R.id.action_add:
			Intent i = new Intent(this, DetailActivity.class);
			startActivity(i);

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listViewTasks = (ListView) findViewById(R.id.listView1);

		// populate the list
		fillData();
		registerForContextMenu(listViewTasks);
		listViewTasks.setClickable(true);

		// short press on list item opens up detail activity, passing along the
		// content provider uri
		listViewTasks.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				Intent intent = new Intent(getApplicationContext(),
						DetailActivity.class);
				Uri taskUri = Uri.parse(MyTaskContentProvider.CONTENT_URI + "/"
						+ id);

				intent.putExtra(MyTaskContentProvider.CONTENT_ITEM_TYPE,
						taskUri);
				startActivity(intent);

				Log.d("TASK", "listitemclicked");

			}

		});
	}

	/**
	 * Helper function used to map the task summaries to the listview
	 */
	private void fillData() {
		String[] from = new String[] { TaskTable.COLUMN_SUMMARY };
		int[] to = new int[] { R.id.textView1 };
		getLoaderManager().initLoader(0, null, this);
		adapter = new MyCursorAdapter(this, R.layout.task_row, null, from, to,
				0);

		listViewTasks.setAdapter(adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		//handle a delete action from the context menu by deleting through content provider
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			Uri uri = Uri.parse(MyTaskContentProvider.CONTENT_URI + "/"
					+ info.id);
			getContentResolver().delete(uri, null, null);
			fillData();
			return true;
		}
		return super.onContextItemSelected(item);

	}
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		Log.e("TASK", "onCreateContextMenu");
		if (v.getId() == R.id.listView1) {

			menu.setHeaderTitle("test");
			menu.add(0, DELETE_ID, 0, "delete");
		}
	}

	/**
	 * TODO: mark the checked task as completed, delete later explicitly When a
	 * checkBox is checked.
	 * 
	 * The callback for a task in listview becoming checked.  After a short delay,
	 * the checked item is deleted through the content provider
	 * @param v 
	 */
	public void onCheck(final View v) {
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				
				/*
				 * get the tag from the checkBox, which is set in the cursor to
				 * be the row ID Then delete based on the row ID after a short
				 * delay
				 */
				long id = (Long) v.getTag();
				Uri uri = Uri.parse(MyTaskContentProvider.CONTENT_URI + "/"
						+ id);
				getContentResolver().delete(uri, null, null);
				fillData();
			}
		}, 500);

		Log.e("TASK", "onClick : ");
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { TaskTable.COLUMN_ID, TaskTable.COLUMN_SUMMARY };
		CursorLoader cursorLoader = new CursorLoader(this,
				MyTaskContentProvider.CONTENT_URI, projection, null, null, null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor data) {
		adapter.swapCursor(data);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);

	}
}
