package com.douyang.protaskinate;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;

public class MyCursorAdapter extends SimpleCursorAdapter {

	public MyCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox1);
		checkBox.setTag(Long.valueOf(cursor.getLong(cursor.getColumnIndex(BaseColumns._ID))));
		super.bindView(view, context, cursor);
	}

}
