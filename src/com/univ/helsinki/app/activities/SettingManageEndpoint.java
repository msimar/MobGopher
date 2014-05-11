package com.univ.helsinki.app.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.univ.helsinki.app.R;
import com.univ.helsinki.app.core.SensorFeed;
import com.univ.helsinki.app.db.FeedEndPoint;
import com.univ.helsinki.app.db.FeedResource;
import com.univ.helsinki.app.util.Constant;

public class SettingManageEndpoint extends PreferenceActivity {
	
	private final String TAG = SettingManageEndpoint.class.getSimpleName();
	
	private static FeedEndPoint mFeedEndPoint;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mFeedEndPoint = new FeedEndPoint();
    }
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
		if( mFeedEndPoint.getFeedTitle() == null){
			final AlertDialog dialog =	new AlertDialog.Builder(this)
		    .setTitle("Delete entry")
		    .setMessage("Are you sure you want to delete this entry?")
		    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // continue with delete
		        	dialog.dismiss();
		        }
		     })
		    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // do nothing
		        }
		     })
		    .setIcon(android.R.drawable.ic_dialog_alert)
		     .show();
		}
	}
	
	@Override
	protected void onDestroy() {
		Log.i(TAG,"onDestroy()");
		Log.i(TAG,"Before Saving Feed End Point \n" + mFeedEndPoint);
		FeedResource.getInstance().addFeedEndPoint(mFeedEndPoint);
		
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		
//		SharedPreferences myPreference=PreferenceManager.getDefaultSharedPreferences(this);
//		
//		Toast.makeText(this, 
//				"Cbox: " + myPreference.getBoolean("checkbox_preference", false),
//				Toast.LENGTH_SHORT).show();
	}

    /**
     * Populate the activity with the top-level headers.
     */
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }
    
    public static class FeedFragment extends PreferenceFragment 
    implements OnSharedPreferenceChangeListener {

    	private final String TAG = FeedFragment.class.getSimpleName();
    	
    	@Override
    	public void onCreate(Bundle savedInstanceState) {
    		super.onCreate(savedInstanceState);

    		// Load the preferences from an XML resource
    		addPreferencesFromResource(R.xml.fragmented_preferences);
    	}

		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			Log.i(TAG,"onSharedPreferenceChanged: " + key);
			
			if (key.equals(getActivity().getResources().getString(R.string.pref_key_feed_title))) {
				String val = sharedPreferences.getString(key, "");
				mFeedEndPoint.setFeedTitle(val);
    		}
			else if (key.equals(getActivity().getResources().getString(R.string.pref_key_feed_server_name))) {
				String val = sharedPreferences.getString(key, "");
				mFeedEndPoint.setFeedServerName(val);
    		}
			else if (key.equals(getActivity().getResources().getString(R.string.pref_key_feed_server_url))) {
				String val = sharedPreferences.getString(key, "");
				mFeedEndPoint.setFeedServerUrl(val);
				
    		}else if (key.equals(getActivity().getResources().getString(R.string.pref_key_feed_connection_mode))) {
				String val = sharedPreferences.getString(key, "");
				Log.i(TAG,"onSharedPreferenceChanged: " + key + "::" + val);
				mFeedEndPoint.setConnectionPreference(Integer.parseInt(val));
				
    		}else if (key.equals(getActivity().getResources().getString(R.string.pref_key_feed_sync_frequency))) {
				String val = sharedPreferences.getString(key, "");
				Log.i(TAG,"onSharedPreferenceChanged: " + key + "::" + val);
				mFeedEndPoint.setSyncFrequency(Integer.parseInt(val));
				
    		}else if (key.equals(getActivity().getResources().getString(R.string.pref_key_feed_sync_repeat))) {
    			boolean val = sharedPreferences.getBoolean(key, false);
    			mFeedEndPoint.setRepeatMode(val);
				
    		}else if (key.equals(getActivity().getResources().getString(R.string.pref_key_feed_manage_feed_logging))) {
    			boolean val = sharedPreferences.getBoolean(key, false);
    			mFeedEndPoint.setFeedLogging(val);
				
    		}else if (key.equals(getActivity().getResources().getString(R.string.pref_key_feed_manage_feed))) {
    			boolean val = sharedPreferences.getBoolean(key, false);
    			mFeedEndPoint.setFeedEnable(val);
    		}
		}
		
		@Override
    	public void onResume() {
    	    super.onResume();
    	    getPreferenceScreen().getSharedPreferences()
    	            .registerOnSharedPreferenceChangeListener(this);
    	}

    	@Override
    	public void onPause() {
    	    super.onPause();
    	    getPreferenceScreen().getSharedPreferences()
    	            .unregisterOnSharedPreferenceChangeListener(this);
    	}
    	
    	@Override
    	public void onDestroy() {
    		resetPreferences();
    		super.onDestroy();
    	}
    	
    	private void resetPreferences(){
    		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    		
    		Editor editor = sharedPreferences.edit();
    		
    		Resources res = getActivity().getResources();
    		
    		editor.putString(res.getString(R.string.pref_key_feed_title), "");
    		editor.putString(res.getString(R.string.pref_key_feed_server_name), "");
    		editor.putString(res.getString(R.string.pref_key_feed_server_url), "");
    		
    		editor.putString(res.getString(R.string.pref_key_feed_connection_mode), "");
    		editor.putString(res.getString(R.string.pref_key_feed_sync_frequency), "");
    		
    		editor.putBoolean(res.getString(R.string.pref_key_feed_manage_feed_logging), true);
    		editor.putBoolean(res.getString(R.string.pref_key_feed_manage_feed), true);
    		editor.putBoolean(res.getString(R.string.pref_key_feed_sync_repeat), true);
    		
    		editor.commit();
    	}
    }
    
    public static class ChooseSensorFragment extends PreferenceFragment  
    implements OnSharedPreferenceChangeListener {
    	
    	private final String TAG = ChooseSensorFragment.class.getSimpleName();
    	
    	private PreferenceCategory mSensorCategory;
    	
    	private List<String> mAllSensorKey;
    	
    	private Map<String,Boolean> mSelectedSensorMap = new HashMap<String,Boolean>();
    	
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.settings_sensor);

            mSensorCategory = (PreferenceCategory) findPreference("pref_configure_sensor_setting_key");
            
            /*CheckBoxPreference cboxPrefAllSensor = new CheckBoxPreference(getActivity());
            cboxPrefAllSensor.setDefaultValue(true);
            cboxPrefAllSensor.setKey(Constant.PREF_TYPE_ALL_SENSOR_KEY);
            cboxPrefAllSensor.setTitle(this.getResources().getText(R.string.pref_type_all_sensor));
            cboxPrefAllSensor.setSummary(this.getResources().getText(R.string.pref_type_all_sensor_summary));
            this.mSelectedSensorMap.put(Constant.PREF_TYPE_ALL_SENSOR_KEY, true);
            
            this.mSensorCategory.addPreference(cboxPrefAllSensor);*/

//            bindPreferenceListener(findPreference(Constant.PREF_TYPE_ALL_SENSOR_KEY));
            
            this.mAllSensorKey = new ArrayList<String>();
            
            for(SensorFeed sensorFeed : FeedResource.getInstance().getSensorFeedList()){
            	
            	CheckBoxPreference cboxPref = new CheckBoxPreference(getActivity());
                cboxPref.setDefaultValue(true);
                cboxPref.setKey(sensorFeed.getSensorKey());
                cboxPref.setTitle(sensorFeed.getTypeName());
                cboxPref.setSummary("Choose Android Sensor for feed");
                
                this.mSelectedSensorMap.put(sensorFeed.getSensorKey(), true);
                
                this.mSensorCategory.addPreference(cboxPref);
                
                this.mAllSensorKey.add(sensorFeed.getSensorKey());
            }
            
            ActionBar actionBar = getActivity().getActionBar();
    		actionBar.setDisplayHomeAsUpEnabled(true);
        }
        
//    	private void bindPreferenceListener(Preference preference) {
//    		// TODO Auto-generated method stub
//    		preference.setOnPreferenceChangeListener(onPrefChangeListener);
//    	}

    	@Override
    	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    		Log.i(TAG,"onSharedPreferenceChanged: " + key);
    		
    		boolean isChecked = sharedPreferences.getBoolean(key, false);
    		
    		if (key.equals(Constant.PREF_TYPE_ALL_SENSOR_KEY)) {
    			
    			Log.i(TAG,"PREF_TYPE_ALL_SENSOR_KEY :" + isChecked);
    		} 
    		
    		this.mSelectedSensorMap.put(key, isChecked);
    	}
    	
//    	Preference.OnPreferenceChangeListener onPrefChangeListener = new Preference.OnPreferenceChangeListener() {
//    		
//    		@Override
//    	    public boolean onPreferenceChange(Preference preference, Object newValue) {
//    			Log.i(TAG,"onPreferenceChange: " + preference.getKey());
//    	    	Boolean prefValue = (Boolean) newValue;
//    			
//    			if (preference instanceof CheckBoxPreference) {
//    				
//    				CheckBoxPreference cboxpreference = (CheckBoxPreference) preference;
//    				 
//    				if( preference.getKey().equals(Constant.PREF_TYPE_ALL_SENSOR_KEY)){
//    					
//    					
//    					
//    					if( prefValue ){
//    						// iterate over all checkbox or sensors to make them checked.
//    						
//    						for(String keyValue : mAllSensorKey){
//    							// checked the instance value
////    							((CheckBoxPreference)findPreference(keyValue)).setChecked(true);
////    							
////    							// update the preference to recent value
////    							PreferenceManager.getDefaultSharedPreferences(
////    									preference.getContext()).getBoolean(keyValue, false);
//    						}
//    					}
//    				} 
//    				cboxpreference.setChecked(prefValue);
//    			}
//    			
//    			return prefValue;
//    		}
//    	};

    	
    	@Override
    	public void onResume() {
    	    super.onResume();
    	    getPreferenceScreen().getSharedPreferences()
    	            .registerOnSharedPreferenceChangeListener(this);
    	}

    	@Override
    	public void onPause() {
    	    super.onPause();
    	    getPreferenceScreen().getSharedPreferences()
    	            .unregisterOnSharedPreferenceChangeListener(this);
    	}
    	
    	@Override
    	public void onDestroy() {
    		mFeedEndPoint.setFeedSensorMap(this.mSelectedSensorMap);
    		resetPreferences();
    		super.onDestroy();
    	}
    	
    	private void resetPreferences(){
    		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    		
    		Editor editor = sharedPreferences.edit();
    		for(SensorFeed sensorFeed : FeedResource.getInstance().getSensorFeedList()){
    			editor.putBoolean(sensorFeed.getSensorKey(), true);
    		}
    		editor.commit();
    	}
    }
}