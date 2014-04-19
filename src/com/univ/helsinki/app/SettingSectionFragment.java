package com.univ.helsinki.app;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;

import com.univ.helsinki.app.core.SensorFeed;
import com.univ.helsinki.app.db.FeedResource;
import com.univ.helsinki.app.util.Constant;
import com.univ.helsinki.app.util.PreferenceFragment;

public class SettingSectionFragment extends PreferenceFragment {
	
	public Context mContext ;
	
	private PreferenceCategory mSensorCategory;
	
	private boolean isSensorListLoaded = false;
	
	private List<String> mAllSensorKey;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
        
        this.mContext = getActivity();
        
        mSensorCategory = (PreferenceCategory) findPreference("pref_configure_sensor_setting_key");
        
        CheckBoxPreference cboxPrefAllSensor = new CheckBoxPreference(mContext);
        cboxPrefAllSensor.setDefaultValue(true);
        cboxPrefAllSensor.setKey(Constant.PREF_TYPE_ALL_SENSOR_KEY);
        cboxPrefAllSensor.setTitle(this.mContext.getResources().getText(R.string.pref_type_all_sensor));
        cboxPrefAllSensor.setSummary(this.mContext.getResources().getText(R.string.pref_type_all_sensor_summary));
        
        this.mSensorCategory.addPreference(cboxPrefAllSensor);
        
        bindPreferenceSummaryToValue(findPreference(Constant.PREF_TYPE_ALL_SENSOR_KEY));
        
        this.mAllSensorKey = new ArrayList<String>();
        
        for(SensorFeed sensorFeed : FeedResource.getInstance().getSensorFeedList()){
        	
        	CheckBoxPreference cboxPref = new CheckBoxPreference(mContext);
            cboxPref.setDefaultValue(true);
            cboxPref.setKey(sensorFeed.getSensorKey());
            cboxPref.setTitle(sensorFeed.getTypeName());
            cboxPref.setSummary("Enable/Disable Sensor");
            
            this.mSensorCategory.addPreference(cboxPref);
            
            bindPreferenceSummaryToValue(findPreference(sensorFeed.getSensorKey()));
            
            this.mAllSensorKey.add(sensorFeed.getSensorKey());
        }
        
        isSensorListLoaded = true;
        /*for(String keyValue : Constant.sALL_SENSOR_KEY){
        	bindPreferenceSummaryToValue(findPreference(keyValue));
        }*/
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

			Boolean prefValue = (Boolean) newValue;
			
			if (preference instanceof CheckBoxPreference) {
				
				CheckBoxPreference cboxpreference = (CheckBoxPreference) preference;
				 
				if( preference.getKey().equals(Constant.PREF_TYPE_ALL_SENSOR_KEY)){
					
					if( prefValue  && isSensorListLoaded){
						// iterate over all checkbox or sensors to make them checked.
						
						for(String keyValue : mAllSensorKey){
							// checked the instance value
							((CheckBoxPreference)findPreference(keyValue)).setChecked(true);
							
							// update the preference to recent value
							PreferenceManager.getDefaultSharedPreferences(
									preference.getContext()).getBoolean(keyValue, false);
						}
					}
				} 
				
				cboxpreference.setChecked(prefValue);
			}
			
			return prefValue;
		}
	};
}