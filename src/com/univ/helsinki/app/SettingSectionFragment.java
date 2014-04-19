package com.univ.helsinki.app;

import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;

import com.univ.helsinki.app.util.Constant;
import com.univ.helsinki.app.util.PreferenceFragment;

public class SettingSectionFragment extends PreferenceFragment {
	
	public Context mContext ;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);
        
        this.mContext = getActivity();
        
        bindPreferenceSummaryToValue(findPreference(Constant.PREF_TYPE_ALL_SENSOR_KEY));
        
        for(String keyValue : Constant.sALL_SENSOR_KEY){
        	bindPreferenceSummaryToValue(findPreference(keyValue));
        }
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
					
					if( prefValue ){
						// iterate over all checkbox or sensors to make them checked.
						
						for(String keyValue : Constant.sALL_SENSOR_KEY){
							// checked the instance value
							((CheckBoxPreference)findPreference(keyValue)).setChecked(true);
							
							// update the preference to recent value
							PreferenceManager.getDefaultSharedPreferences(
									preference.getContext()).getBoolean(keyValue, false);
						}
					}
				} 
				
				// update the state of current instance of preference
				cboxpreference.setChecked(prefValue);
			}
			
			return prefValue;
		}
	};
}