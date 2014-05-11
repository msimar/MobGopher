package com.univ.helsinki.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.univ.helsinki.app.R;
import com.univ.helsinki.app.adapter.RecentActivityAdapter;
import com.univ.helsinki.app.db.DataSourceRecentFeed;

public class RecentActivity extends Activity {

	private DataSourceRecentFeed mDatasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recent);

		//mDatasource = new DataSourceRecentFeed(this);

		final ListView listview = (ListView) findViewById(R.id.listview);

		listview.setAdapter(
				new RecentActivityAdapter(RecentActivity.this));
		
		registerForContextMenu(listview);
	}
	
	@Override
	protected void onResume() {
		if(mDatasource != null)
			mDatasource.open();
		super.onResume();
	}

	@Override
	protected void onPause() {
		if(mDatasource != null)
			mDatasource.close();
		super.onPause();
	}
}
