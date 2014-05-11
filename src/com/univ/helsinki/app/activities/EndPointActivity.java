package com.univ.helsinki.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.univ.helsinki.app.R;
import com.univ.helsinki.app.adapter.FeedEndPointAdapter;
import com.univ.helsinki.app.db.FeedResource;

public class EndPointActivity extends Activity {
	
	private FeedEndPointAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recent);

		final TextView tvEmptyStub = (TextView) findViewById(R.id.emptystub);
		tvEmptyStub.setText("Create some feeds. \nManage and Control feeds.");

		final ListView listview = (ListView) findViewById(R.id.listview);

		// create adapter instance
		mAdapter = new FeedEndPointAdapter(EndPointActivity.this);
		// set list adapter
		listview.setAdapter(mAdapter);
		// set adapter in feed resource
		FeedResource.getInstance().setFeedEndPointAdapter(mAdapter);

		if (FeedResource.getInstance().getAllFeedEndPoint().size() > 0) {
			listview.setVisibility(View.VISIBLE);
			findViewById(R.id.emptystub).setVisibility(View.GONE);
		}

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Intent intent = new Intent(EndPointActivity.this,
				// ViewActivity.class);
				// intent.putExtra(ViewActivity.EXTRAS_ROW_ID, position);
				// startActivity(intent);
			}
		});

		registerForContextMenu(listview);
	}

	@Override
	protected void onResume() {
		FeedResource.getInstance().openDataSource();
		mAdapter.notifyDataChanged();
		super.onResume();
	}

	@Override
	protected void onPause() {
		FeedResource.getInstance().closeDataSource();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		FeedResource.getInstance().destory();
		super.onDestroy();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.listview) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

			String[] menuItems = getResources().getStringArray(R.array.listitem_menu_array);
			for (int i = 0; i < menuItems.length; i++) {
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}
		}
	}
    
    @Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) 
				item.getMenuInfo();

		int menuItemIndex = item.getItemId();

		String[] menuItems = getResources().getStringArray(R.array.listitem_menu_array);

		String menuItemName = menuItems[menuItemIndex];

		if (menuItemName.equalsIgnoreCase("Delete")) {
			
			FeedResource.getInstance().removeFeedEndPoint(info.position);
			
		}else if (menuItemName.contains("View")) {
			/*Intent intent = new Intent(EndPointActivity.this, ViewActivity.class);
			intent.putExtra(ViewActivity.EXTRAS_ROW_ID, info.position);
			startActivity(intent);*/
		} 
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_endpoint_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		/*case R.id.action_settings:
			startActivity(new Intent(this, SettingPreferenceActivity.class));
			return true;*/
		case R.id.action_sync:
			// startActivity(new Intent(this, SensorEvaluator.class));
			startActivity(new Intent(this, ManageEndPointActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
