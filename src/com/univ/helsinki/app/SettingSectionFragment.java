package com.univ.helsinki.app;

import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;

import com.univ.helsinki.app.util.PreferenceFragment;

public class SettingSectionFragment extends PreferenceFragment {
	
	public static final String PREF_TYPE_ALL_SENSOR_KEY = "pref_type_all_sensor_key";
	
	public static final String PREF_TYPE_ACCELEROMETER_KEY = "pref_type_accelerometer_key";
	public static final String PREF_TYPE_MAGNETIC_FIELD_KEY = "pref_type_magnetic_field_key";
	
	public static final String PREF_TYPE_ORIENTATION_KEY = "pref_type_orientation_key";
	public static final String PREF_TYPE_GYROSCOPE_KEY = "pref_type_gyroscope_key";
	public static final String PREF_TYPE_LIGHT_KEY = "pref_type_light_key";
	public static final String PREF_TYPE_PRESSURE_KEY = "pref_type_pressure_key";
	
	public static final String PREF_TYPE_TEMPERATURE_KEY = "pref_type_temperature_key";
	public static final String PREF_TYPE_PROXIMITY_KEY = "pref_type_proximity_key";
	public static final String PREF_TYPE_GRAVITY_KEY = "pref_type_gravity_key";
	public static final String PREF_TYPE_LINEAR_ACCELERATION_KEY = "pref_type_linear_acceleration_key";
	
	public static final String PREF_TYPE_ROTATION_VECTOR_KEY = "pref_type_rotation_vector_key";
	public static final String PREF_TYPE_RELATIVE_HUMIDITY_KEY = "pref_type_relative_humidity_key";
	public static final String PREF_TYPE_AMBIENT_TEMPERATURE_KEY = "pref_type_ambient_temperature_key";
	public static final String PREF_TYPE_MAGNETIC_FIELD_UNCALIBRATED_KEY = "pref_type_magnetic_field_uncalibrated_key";
	
	public static final String PREF_TYPE_GAME_ROTATION_VECTOR_KEY = "pref_type_game_rotation_vector_key";
	public static final String PREF_TYPE_GYROSCOPE_UNCALIBRATED_KEY = "pref_type_gyroscope_uncalibrated_key";
	public static final String PREF_TYPE_SIGNIFICANT_MOTION_KEY = "pref_type_significant_motion_key";
	public static final String PREF_TYPE_STEP_DETECTOR_KEY = "pref_type_step_detector_key";
	
	public static final String PREF_TYPE_STEP_COUNTER_KEY = "pref_type_step_counter_key";
	public static final String PREF_TYPE_GEOMAGNETIC_ROTATION_VECTOR_KEY = "pref_type_geomagnetic_rotation_vector_key";
	
	private final String[] ALL_SENSOR_KEY = {
			PREF_TYPE_ACCELEROMETER_KEY, // value : 1
			PREF_TYPE_MAGNETIC_FIELD_KEY, // value : 2
			
			PREF_TYPE_ORIENTATION_KEY, // value : 3
			PREF_TYPE_GYROSCOPE_KEY, // value : 4
			PREF_TYPE_LIGHT_KEY, // value : 5
			PREF_TYPE_PRESSURE_KEY, // value : 6
			
			PREF_TYPE_TEMPERATURE_KEY, // value : 7
			PREF_TYPE_PROXIMITY_KEY, // value : 8
			PREF_TYPE_GRAVITY_KEY, // value : 9
			PREF_TYPE_LINEAR_ACCELERATION_KEY,  // value : 10
			
			PREF_TYPE_ROTATION_VECTOR_KEY,  // value : 11
			PREF_TYPE_RELATIVE_HUMIDITY_KEY,  // value : 12
			PREF_TYPE_AMBIENT_TEMPERATURE_KEY,  // value : 13
			PREF_TYPE_MAGNETIC_FIELD_UNCALIBRATED_KEY,  // value : 14
			
			PREF_TYPE_GAME_ROTATION_VECTOR_KEY,  // value : 15
			PREF_TYPE_GYROSCOPE_UNCALIBRATED_KEY,  // value : 16
			PREF_TYPE_SIGNIFICANT_MOTION_KEY,  // value : 17
			PREF_TYPE_STEP_DETECTOR_KEY,  // value : 18
			
			PREF_TYPE_STEP_COUNTER_KEY,  // value : 19
			PREF_TYPE_GEOMAGNETIC_ROTATION_VECTOR_KEY  // value : 20
	};
	

	public Context mContext ;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);
        
        this.mContext = getActivity();
        
        bindPreferenceSummaryToValue(findPreference(PREF_TYPE_ALL_SENSOR_KEY));
        
        for(String keyValue : ALL_SENSOR_KEY){
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
				 
				if( preference.getKey().equals(PREF_TYPE_ALL_SENSOR_KEY)){
					
					if( prefValue ){
						// iterate over all checkbox or sensors to make them checked.
						
						for(String keyValue : ALL_SENSOR_KEY){
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