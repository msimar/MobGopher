/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.univ.helsinki.app;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.univ.helsinki.app.activities.ViewActivity;
import com.univ.helsinki.app.adapter.RecentActivityAdapter;
import com.univ.helsinki.app.adapter.SensorFeedAdapter;
import com.univ.helsinki.app.core.SensorFeed;
import com.univ.helsinki.app.db.FeedResource;
import com.univ.helsinki.app.util.Constant;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * three primary sections of the app. We use a {@link android.support.v4.app.FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    ViewPager mViewPager;

    private SensorFeedAdapter mSensorFeedAdapter;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        FeedResource.getInstance().inti(this);
        
        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        
        Constant.sAllSensorPreferenceKeyMap.put(1, Constant.PREF_TYPE_ACCELEROMETER_KEY);
        Constant.sAllSensorPreferenceKeyMap.put(2, Constant.PREF_TYPE_MAGNETIC_FIELD_KEY);
        
        Constant.sAllSensorPreferenceKeyMap.put(3, Constant.PREF_TYPE_ORIENTATION_KEY);
        Constant.sAllSensorPreferenceKeyMap.put(4, Constant.PREF_TYPE_GYROSCOPE_KEY);
        Constant.sAllSensorPreferenceKeyMap.put(5, Constant.PREF_TYPE_LIGHT_KEY);
        Constant.sAllSensorPreferenceKeyMap.put(6, Constant.PREF_TYPE_PRESSURE_KEY);
        
        Constant.sAllSensorPreferenceKeyMap.put(7, Constant.PREF_TYPE_TEMPERATURE_KEY);
        Constant.sAllSensorPreferenceKeyMap.put(8, Constant.PREF_TYPE_PROXIMITY_KEY);
        Constant.sAllSensorPreferenceKeyMap.put(9, Constant.PREF_TYPE_GRAVITY_KEY);
        Constant.sAllSensorPreferenceKeyMap.put(10, Constant.PREF_TYPE_LINEAR_ACCELERATION_KEY);
        
        Constant.sAllSensorPreferenceKeyMap.put(11, Constant.PREF_TYPE_ROTATION_VECTOR_KEY);
        Constant.sAllSensorPreferenceKeyMap.put(12, Constant.PREF_TYPE_RELATIVE_HUMIDITY_KEY);
        Constant.sAllSensorPreferenceKeyMap.put(13, Constant.PREF_TYPE_AMBIENT_TEMPERATURE_KEY);
        Constant.sAllSensorPreferenceKeyMap.put(14, Constant.PREF_TYPE_MAGNETIC_FIELD_UNCALIBRATED_KEY);
        
        Constant.sAllSensorPreferenceKeyMap.put(15, Constant.PREF_TYPE_GAME_ROTATION_VECTOR_KEY);
        Constant.sAllSensorPreferenceKeyMap.put(16, Constant.PREF_TYPE_GYROSCOPE_UNCALIBRATED_KEY);
        Constant.sAllSensorPreferenceKeyMap.put(17, Constant.PREF_TYPE_SIGNIFICANT_MOTION_KEY);
        Constant.sAllSensorPreferenceKeyMap.put(18, Constant.PREF_TYPE_STEP_DETECTOR_KEY);
        
        Constant.sAllSensorPreferenceKeyMap.put(19, Constant.PREF_TYPE_STEP_COUNTER_KEY);
        Constant.sAllSensorPreferenceKeyMap.put(20, Constant.PREF_TYPE_GEOMAGNETIC_ROTATION_VECTOR_KEY);
        
        getAllDeviceSensorList();
        
        mSensorFeedAdapter = new SensorFeedAdapter(this);

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
                switch (position) {
                	case 0:{
                		// on focus when first fragment
                		Toast.makeText(getApplicationContext(), "onPageSelected :" + position, Toast.LENGTH_SHORT).show();
                		
                		List<SensorFeed> mtempAllowedSensorFeedList = new ArrayList<SensorFeed>();
                		// update the list over here.. to avoid empty shell
                		for (SensorFeed sensor : FeedResource.getInstance().getSensorFeedList()) {
                			
                			boolean isChecked = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                				.getBoolean(sensor.getSensorKey(), false);
                			
                			if(isChecked)
                				mtempAllowedSensorFeedList.add(sensor);
						}
                		// update the list with new created list
                		mSensorFeedAdapter.setFeedList(mtempAllowedSensorFeedList);
                		
                	}break;
                }// switch ends
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
            
            	case 0:
                // The first section of the app is the most interesting -- it offers
                // a launchpad into the other demonstrations in this example application.
                return new RecentSectionFragment();
                
                case 1:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new LaunchpadSectionFragment();

                default:
                    return new SettingSectionFragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
        	CharSequence title = "" ;
        	if( position == 0 ){
        		title = "Recent Activities";
        	}else if( position == 1 ){
        		title = "All Sensor";
        	}else if( position == 2 ){
        		title = "Configure";
        	}
            return title;
        }
    }
    
    public class RecentSectionFragment extends Fragment {

    	@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_recent, container, false);

    		final ListView listview = (ListView) rootView.findViewById(R.id.listview);
    		
    		// create adapter instance
    		RecentActivityAdapter adapter = new RecentActivityAdapter(getActivity());
    		// set list adapter
    		listview.setAdapter(adapter);
    		// set adapter in feed resource 
    		FeedResource.getInstance().setRecentFeedAdapter(adapter);
    		
    		if(FeedResource.getInstance().getAllFeed().size() > 0){
    			listview.setVisibility(View.VISIBLE);
    			rootView.findViewById(R.id.emptystub).setVisibility(View.GONE);
			}
    		
    		listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(MainActivity.this, ViewActivity.class);
					intent.putExtra(ViewActivity.EXTRAS_ROW_ID, position);
					startActivity(intent);
				}
			});
    		
            return rootView;
        }
    }

    /**
     * A fragment that launches other parts of the demo application.
     */
    public class LaunchpadSectionFragment extends Fragment
    //implements OnFocusChangeListener 
    {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_launchpad, container, false);

            ListView mListview = (ListView) rootView.findViewById(R.id.listview);
             
    		registerForContextMenu(mListview);
    		
    		// Show contextview on item click
    		mListview.setOnItemClickListener(new OnItemClickListener() {

    			@Override
    			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
    				// TODO Auto-generated method stub
    			}
    		});

    		mListview.setAdapter(mSensorFeedAdapter);
    		
            return rootView;
        }

//		@Override
//		public void onFocusChange(View v, boolean hasFocus) {
//			// TODO Auto-generated method stub
//			Toast.makeText(getActivity(), "focused :" + hasFocus, Toast.LENGTH_SHORT).show();
//		}
    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            /*View rootView = inflater.inflate(R.layout.fragment_section_dummy, container, false);
            Bundle args = getArguments();
            ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                    getString(R.string.dummy_section_text, args.getInt(ARG_SECTION_NUMBER)));
            return rootView;*/
        	return null;
        }
    }
    
    public void getAllDeviceSensorList(){
    	SensorManager sensorManager;

    		// Get the reference to the sensor manager
    		sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);

    		// Get the list of sensor
    		List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

    		for (Sensor sensor : sensorList) {
    			
    			SensorFeed sFeed = new SensorFeed();
    			
    			sFeed.setName(sensor.getName());
    			sFeed.setVendor(sensor.getVendor());
    			sFeed.setTypeValue(sensor.getType());
    			
    			FeedResource.getInstance().getSensorFeedList().add(sFeed);
    		}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_send:
            	startActivity(new Intent(this, SensorEvaluator.class));
            return true;
//            case R.id.action_settings:
//                openSettings();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
	protected void onResume() {
    	FeedResource.getInstance().openDataSource();
		super.onResume();
	}

	@Override
	protected void onPause() {
		FeedResource.getInstance().closeDataSource();
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		FeedResource.getInstance().destory();
		super.onDestroy();
	}
}
