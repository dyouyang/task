package com.douyang.protaskinate;

import java.util.List;

import com.douyang.contentprovider.MyTaskContentProvider;
import com.douyang.db.TaskTable;


import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends Activity implements LoaderCallbacks<Cursor>{


	private SimpleCursorAdapter adapter;
	private ListView listViewTasks;
	
	private static final int DELETE_ID = Menu.FIRST + 1;
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.e("","onOptions");
		switch (item.getItemId()) {
		case R.id.action_add:

			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle("Title");
			alert.setMessage("Message");

			// Set an EditText view to get user input
			final EditText input = new EditText(this);
			alert.setView(input);

			alert.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							String summary = input.getText().toString();
							

							//start insert to DB
						   // String category = (String) mCategory.getSelectedItem();
						    //String summary = mTitleText.getText().toString();
						    //String description = mBodyText.getText().toString();

						    // Only save if either summary or description
						    // is available

						    

						    ContentValues values = new ContentValues();
						    values.put(TaskTable.COLUMN_CATEGORY, "category");
						    values.put(TaskTable.COLUMN_SUMMARY, summary);
						    values.put(TaskTable.COLUMN_DESCRIPTION, "description");
						    Uri taskUri = null;
						    
						    if (taskUri == null) {
						      // New todo
						      taskUri = getContentResolver().insert(MyTaskContentProvider.CONTENT_URI, values);
						    } else {
						      // Update todo
						      getContentResolver().update(taskUri, values, null, null);
						    }
						    
						    
						    
						}
					});

			alert.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// Canceled.
						}
					});

			alert.show();
			// see
			// http://androidsnippets.com/prompt-user-input-with-an-alertdialog
			

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listViewTasks = (ListView) findViewById(R.id.listView1);
		fillData();
		registerForContextMenu(listViewTasks);
		listViewTasks.setClickable(true);
	}

	private void fillData() {
		String [] from = new String [] {TaskTable.COLUMN_SUMMARY};
		int [] to = new int [] {R.id.textView1};
		getLoaderManager().initLoader(0, null, this);
		adapter = new SimpleCursorAdapter(this, R.layout.task_row, null, from, to, 0);
		
		listViewTasks.setAdapter(adapter);
		
		//short press on list item
		listViewTasks.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.e("TASK", "listitemclicked");
				
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			Uri uri = Uri.parse(MyTaskContentProvider.CONTENT_URI + "/" + info.id);
			getContentResolver().delete(uri, null, null);
			fillData();
			return true;
		}
		return super.onContextItemSelected(item);
		
	}

	//long press on list item
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		Log.e("TASK", "onCreateContextMenu");
		if(v.getId() == R.id.listView1)
		{
			AdapterView.AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle("test");
			menu.add(0, DELETE_ID, 0, "delete");
		}
	}

	public void onClick() {

	}


	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String [] projection = {TaskTable.COLUMN_ID, TaskTable.COLUMN_SUMMARY};
		CursorLoader cursorLoader = new CursorLoader(this, MyTaskContentProvider.CONTENT_URI, projection, null, null, null);
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
