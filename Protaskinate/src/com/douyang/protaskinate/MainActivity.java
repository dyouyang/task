package com.douyang.protaskinate;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * @author yinglong The main listView activity that displays all current tasks.
 * 
 */
public class MainActivity extends Activity {

	FragmentManager fragmentManager;
	
	MainFragment mainFragment;
	
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

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
		
		//get the fragment for the task list. We need to do this for passing the onCheck callback
		fragmentManager = getFragmentManager();
		mainFragment = (MainFragment) fragmentManager.findFragmentById(R.id.main_fragment);
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
     String [] mPlanetTitles = {"All Tasks", "Personal", "Work", "Shopping"};
     
		// Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
		mainFragment.onCheck(v);
		Log.e("TASK", "onClick : ");
	}


}
