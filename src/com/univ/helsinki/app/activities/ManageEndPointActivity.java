package com.univ.helsinki.app.activities;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;

import com.univ.helsinki.app.R;
import com.univ.helsinki.app.db.FeedEndPoint;
import com.univ.helsinki.app.db.FeedResource;

public class ManageEndPointActivity extends Activity implements OnClickListener {
	
	private final String TAG = ManageEndPointActivity.class.getSimpleName();
	
	private FeedEndPoint mFeedEndPoint;
	
	private EditText mEditTextFeedTitle;
	private EditText mEditTextServerName;
	private EditText mEditTextServerURL;
	
	private CheckBox mCboxRepeatMode;
	private CheckBox mCboxManageFeed;
	private CheckBox mCboxManageLogging;
	
	private final int MODE_SYNC_FREQUENCY = 112;
	private final int MODE_CONNECTION_PREF = 114;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_endpoint);
		
		initView();
		
		mFeedEndPoint = new FeedEndPoint();
	}
	
	private void initView(){
		mEditTextFeedTitle = (EditText) findViewById(R.id.edtxt_title_feed_title);
		mEditTextServerName = (EditText) findViewById(R.id.edtxt_feed_server_name);
		mEditTextServerURL = (EditText) findViewById(R.id.edtxt_feed_server_url);
		
		mCboxRepeatMode = (CheckBox) findViewById(R.id.cbox_repeat_mode);
		mCboxManageLogging = (CheckBox) findViewById(R.id.cbox_manage_feed);
		mCboxManageFeed = (CheckBox) findViewById(R.id.cbox_manage_logging);

		findViewById(R.id.btn_connection_mode).setOnClickListener(this);
		findViewById(R.id.btn_sync_frequency).setOnClickListener(this);
		
		findViewById(R.id.btn_choose_sensor).setOnClickListener(this);
		findViewById(R.id.btn_cancel).setOnClickListener(this);
		findViewById(R.id.btn_save).setOnClickListener(this);
	}
	
	public void showPreferenceDialog(final int mode, final String[] stringArray){
    
    	AlertDialog.Builder builder=new AlertDialog.Builder(this);
    	builder.setTitle("Choose");
    	builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
				if( MODE_CONNECTION_PREF == mode){
					mFeedEndPoint.setConnectionPreference(selectedPosition);
				}else if( MODE_SYNC_FREQUENCY == mode){
					mFeedEndPoint.setSyncFrequency(selectedPosition);
				}
			}
		});
    	
    	int selectedPreference = 0 ;
    	
    	if(MODE_CONNECTION_PREF == mode){
    		selectedPreference = mFeedEndPoint.getConnectionPreference();
    	}else if(MODE_SYNC_FREQUENCY == mode){
    		selectedPreference = mFeedEndPoint.getSyncFrequency();
    	}
    	
     	builder.setSingleChoiceItems(stringArray,selectedPreference, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG,"choice:" + stringArray[which]);
				int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
				if( MODE_CONNECTION_PREF == mode){
					mFeedEndPoint.setConnectionPreference(selectedPosition);
				}else if( MODE_SYNC_FREQUENCY == mode){
					mFeedEndPoint.setSyncFrequency(selectedPosition);
				}
				
			}
		});
    	builder.show();
    
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.btn_connection_mode:{
				String[] stringArray = getResources().getStringArray(R.array.syncConnectionMode);
				showPreferenceDialog(MODE_CONNECTION_PREF, stringArray);
			}break;
			case R.id.btn_sync_frequency:{
				String[] stringArray = getResources().getStringArray(R.array.syncFrequency);
				showPreferenceDialog(MODE_SYNC_FREQUENCY, stringArray);
			}break;
			case R.id.btn_choose_sensor:{
				startActivity(new Intent(this, SettingConfigureSensor.class));
			}break;
			case R.id.btn_save:{
				
				mFeedEndPoint.setFeedTitle(mEditTextFeedTitle.getText().toString());
				mFeedEndPoint.setFeedServerName(mEditTextServerName.getText().toString());
				mFeedEndPoint.setFeedServerUrl(mEditTextServerURL.getText().toString());
				
				mFeedEndPoint.setRepeatMode(mCboxRepeatMode.isChecked());
				mFeedEndPoint.setFeedLogging(mCboxManageLogging.isChecked());
				mFeedEndPoint.setFeedEnable(mCboxManageFeed.isChecked());
				
				mFeedEndPoint.setFeedSensorMap(FeedResource.getInstance().getSelectedSensorMap());
				
				Log.i(TAG,"Before Saving Feed End Point \n" + mFeedEndPoint);
				FeedResource.getInstance().addFeedEndPoint(mFeedEndPoint);
				
				finish();
				
			}break;
			case R.id.btn_cancel:{
				finish();
			}break;
		}
	}
}
