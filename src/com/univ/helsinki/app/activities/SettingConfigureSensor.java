package com.univ.helsinki.app.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;

import com.univ.helsinki.app.R;
import com.univ.helsinki.app.R.string;
import com.univ.helsinki.app.R.xml;
import com.univ.helsinki.app.core.SensorFeed;
import com.univ.helsinki.app.db.FeedResource;
import com.univ.helsinki.app.util.Constant;
import com.univ.helsinki.app.util.PreferenceFragment;

public class SettingConfigureSensor extends PreferenceActivity {
	
	private PreferenceCategory mSensorCategory;
	
	private boolean isSensorListLoaded = false;
	
	private List<String> mAllSensorKey;
	
	private Map<String,Boolean> mSelectedSensorMap = new HashMap<String,Boolean>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings_sensor);
        
        mSensorCategory = (PreferenceCategory) findPreference("pref_configure_sensor_setting_key");
        
        CheckBoxPreference cboxPrefAllSensor = new CheckBoxPreference(this);
        cboxPrefAllSensor.setDefaultValue(true);
        cboxPrefAllSensor.setKey(Constant.PREF_TYPE_ALL_SENSOR_KEY);
        cboxPrefAllSensor.setTitle(this.getResources().getText(R.string.pref_type_all_sensor));
        cboxPrefAllSensor.setSummary(this.getResources().getText(R.string.pref_type_all_sensor_summary));
        
        this.mSensorCategory.addPreference(cboxPrefAllSensor);
        
        bindPreferenceSummaryToValue(findPreference(Constant.PREF_TYPE_ALL_SENSOR_KEY));
        
        this.mAllSensorKey = new ArrayList<String>();
        
        for(SensorFeed sensorFeed : FeedResource.getInstance().getSensorFeedList()){
        	
        	CheckBoxPreference cboxPref = new CheckBoxPreference(this);
            cboxPref.setDefaultValue(true);
            cboxPref.setKey(sensorFeed.getSensorKey());
            cboxPref.setTitle(sensorFeed.getTypeName());
            cboxPref.setSummary("Enable/Disable AndroidSensor");
            
            this.mSensorCategory.addPreference(cboxPref);
            
            bindPreferenceSummaryToValue(findPreference(sensorFeed.getSensorKey()));
            
            this.mAllSensorKey.add(sensorFeed.getSensorKey());
            
            mSelectedSensorMap.put(sensorFeed.getSensorKey(), true);
            
            FeedResource.getInstance().setSelectedSensorMap(mSelectedSensorMap);
        }
        
        isSensorListLoaded = true;
        /*for(String keyValue : Constant.sALL_SENSOR_KEY){
        	bindPreferenceSummaryToValue(findPreference(keyValue));
        }*/
        
        ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
    }
    
    private void bindPreferenceSummaryToValue(Preference preference) {
		// Set the listener to watch for value changes.
		preference
				.setOnPreferenceChangeListener(mBindPreferenceSummaryToValueListener);

		// Trigger the listener immediately with the preference's
		// current value.
		mBindPreferenceSummaryToValueListener.onPreferenceChange(
				preference,
				PreferenceManager.getDefaultSharedPreferences(
						preference.getContext()).getBoolean(preference.getKey(),
						false)
						);
	}
    
    /**
	 * A preference value change listener that updates the preference's summary
	 * to reflect its new value.
	 */
	private Preference.OnPreferenceChangeListener mBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {

			if( !preference.getKey().equals(Constant.PREF_TYPE_ALL_SENSOR_KEY)){
				mSelectedSensorMap.put(preference.getKey(), true);
			}
			
			Boolean prefValue = (Boolean) newValue;
			
			if (preference instanceof CheckBoxPreference) {
				
				CheckBoxPreference cboxpreference = (CheckBoxPreference) preference;
				 
				if( preference.getKey().equals(Constant.PREF_TYPE_ALL_SENSOR_KEY)){
					
					if( prefValue  && isSensorListLoaded){
						// iterate over all checkbox or sensors to make them checked.
						
						for(String keyValue : mAllSensorKey){
							// checked the instance value
							((CheckBoxPreference)findPreference(keyValue)).setChecked(true);
							
							mSelectedSensorMap.put(preference.getKey(), true);
						}
					}
				} 
				
				cboxpreference.setChecked(prefValue);
			}
			
			return prefValue;
		}
	};
}