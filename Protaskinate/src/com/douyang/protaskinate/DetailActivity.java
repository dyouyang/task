package com.douyang.protaskinate;

import com.douyang.contentprovider.MyTaskContentProvider;
import com.douyang.db.TaskTable;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author yinglong The task detail activity, which handles new tasks or editing
 *         existing tasks
 */
public class DetailActivity extends Activity {

	private Uri taskUri;
	EditText task_summary;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		task_summary = (EditText) findViewById(R.id.editText1);

		Bundle extras = getIntent().getExtras();

		// Getting data from previous instance
		if (savedInstanceState == null)
			taskUri = null;
		else
			taskUri = savedInstanceState
					.getParcelable(MyTaskContentProvider.CONTENT_ITEM_TYPE);

		// Getting data from other activity
		if (extras != null) {
			taskUri = extras
					.getParcelable(MyTaskContentProvider.CONTENT_ITEM_TYPE);
			fillData(taskUri);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		// Save action checks to see if the required fields are present, and if
		// so finishes the activity
		case R.id.action_save:
			if (TextUtils.isEmpty(task_summary.getText().toString())) {
				showEmptySummaryToast();
				return true;
			} else {
				setResult(RESULT_OK);
				finish();
				return true;
			}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * fills the task fields on this activity with the data in task clicked on
	 * in list activity
	 * 
	 * @param uri
	 */
	private void fillData(Uri uri) {
		String[] projection = { TaskTable.COLUMN_SUMMARY,
				TaskTable.COLUMN_DESCRIPTION, TaskTable.COLUMN_CATEGORY };
		Cursor cursor = getContentResolver().query(uri, projection, null, null,
				null);

		if (cursor != null) {
			cursor.moveToFirst();
			task_summary.setText(cursor.getString(cursor
					.getColumnIndexOrThrow(TaskTable.COLUMN_SUMMARY)));
		}

		cursor.close();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putParcelable(MyTaskContentProvider.CONTENT_ITEM_TYPE, taskUri);
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}

	/**
	 * Saves the updated data to the DB, but only if the required fields are
	 * present. If they aren't make no changes. Should be called whenever the
	 * detail activity finishes.
	 */
	private void saveState() {
		String summary = task_summary.getText().toString();

		// if summary is empty don't make any changes
		if (TextUtils.isEmpty(summary)) {
			return;
		}

		ContentValues values = new ContentValues();
		values.put(TaskTable.COLUMN_SUMMARY, summary);
		values.put(TaskTable.COLUMN_CATEGORY, "category");
		values.put(TaskTable.COLUMN_DESCRIPTION, "description");

		if (taskUri == null)
			taskUri = getContentResolver().insert(
					MyTaskContentProvider.CONTENT_URI, values);
		else
			getContentResolver().update(taskUri, values, null, null);

	}

	/**
	 * Alert the user if they try to Save a task where the summary is empty
	 */
	private void showEmptySummaryToast() {
		Toast.makeText(DetailActivity.this, R.string.summary_cannot_be_empty,
				Toast.LENGTH_LONG).show();
	}

}
