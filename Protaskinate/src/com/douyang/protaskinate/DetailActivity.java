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

public class DetailActivity extends Activity {

	private Uri taskUri;
	EditText edit_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		edit_text = (EditText) findViewById(R.id.editText1);

		Bundle extras = getIntent().getExtras();

		// Get from saved state
		if (savedInstanceState == null)
			taskUri = null;
		else
			taskUri = savedInstanceState
					.getParcelable(MyTaskContentProvider.CONTENT_ITEM_TYPE);

		// get from other activity
		if (extras != null) {
			taskUri = extras
					.getParcelable(MyTaskContentProvider.CONTENT_ITEM_TYPE);
			fillData(taskUri);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_save:
			if (TextUtils.isEmpty(edit_text.getText().toString())) {
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
	 * fills the task fields on this activity with the task clicked on list
	 * activity
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
			edit_text.setText(cursor.getString(cursor
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

	private void saveState() {
		String summary = edit_text.getText().toString();

		//if summary is empty don't make any changes
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

	private void showEmptySummaryToast() {
		Toast.makeText(DetailActivity.this, R.string.summary_cannot_be_empty,
				Toast.LENGTH_LONG).show();
	}

}
