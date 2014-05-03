package com.univ.helsinki.app.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.univ.helsinki.app.R;

public class SettingPreferenceActivity extends PreferenceActivity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
        
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

			Boolean prefValue = (Boolean) newValue;
			
			if (preference instanceof CheckBoxPreference) {
				
			}
			
			return prefValue;
		}
	};
}