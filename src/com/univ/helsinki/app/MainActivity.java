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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.univ.helsinki.app.activities.EndPointActivity;
import com.univ.helsinki.app.activities.SettingPreferenceActivity;
import com.univ.helsinki.app.activities.ViewActivity;
import com.univ.helsinki.app.adapter.RecentActivityAdapter;
import com.univ.helsinki.app.adapter.SensorFeedAdapter;
import com.univ.helsinki.app.core.SensorFeed;
import com.univ.helsinki.app.db.FeedResource;
import com.univ.helsinki.app.util.Constant;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	 // Splash screen timer
    private static int SPLASH_TIME_OUT = 1;
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
    
    private ViewGroup splashLayout;

    private SensorFeedAdapter mSensorFeedAdapter;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        
        {
        	hideTitle();
        }
        
        setContentView(R.layout.activity_main);
        
        SharedPreferences activityPrefs = getSharedPreferences(
        		Constant.SHARED_PREFS_FILENAME, 
        		Context. MODE_PRIVATE	);

        /*if (activityPrefs.getBoolean("is_first_time", true)) {
            // record the fact that the app has been started at least once
        	activityPrefs.edit().putBoolean("is_first_time", false).commit(); 
        	
        	final ViewGroup splashLayout =  (ViewGroup) findViewById(R.id.splashLayout);
        	
        	new Handler().postDelayed(new Runnable() {
   			 
                @Override
                public void run() {
                	activateSplashScreen(splashLayout);
                }
            }, SPLASH_TIME_OUT);
        	
        } */
        
        splashLayout =  (ViewGroup) findViewById(R.id.splashLayout);
        
        if(AppController.isFirstLoad){
        	new Handler().postDelayed(new Runnable() {
   			 
                @Override
                public void run() {
                	activateSplashScreen(splashLayout);
                }
            }, SPLASH_TIME_OUT);
        }else{
        	splashLayout.setVisibility(View.GONE);
        }
    	
    	
        
        FeedResource.getInstance().inti(this);
        
        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        
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
                		//Toast.makeText(getApplicationContext(), "onPageSelected :" + position, Toast.LENGTH_SHORT).show();
                		
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
                
            	default:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new LaunchpadSectionFragment();

            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
        	CharSequence title = "" ;
        	if( position == 0 ){
        		title = "Recent Activities";
        	}else if( position == 1 ){
        		title = "All Sensor";
        	} 
            return title;
        }
    }
    
    public class RecentSectionFragment extends Fragment {

    	private ListView listview;
    	private TextView emptyView;
    	@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_recent, container, false);

            listview = (ListView) rootView.findViewById(R.id.listview);
            emptyView = (TextView) rootView.findViewById(R.id.emptystub);
    		
    		// create adapter instance
    		RecentActivityAdapter adapter = new RecentActivityAdapter(getActivity());
    		// set list adapter
    		listview.setAdapter(adapter);
    		// set adapter in feed resource 
    		FeedResource.getInstance().setRecentFeedAdapter(adapter);
    		
    		if(FeedResource.getInstance().getAllFeed().size() > 0){
    			listview.setVisibility(View.VISIBLE);
    			emptyView.setVisibility(View.GONE);
			}
    		
    		listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(MainActivity.this, ViewActivity.class);
					intent.putExtra(ViewActivity.EXTRAS_ROW_ID, position);
					startActivity(intent);
					// FeedResource.getInstance().removeFeed(position);
				}
			});
    		
    		getActivity().registerForContextMenu(listview);
    		
            return rootView;
        }
    	
    	@Override
    	public void onResume() {
        	FeedResource.getInstance().openDataSource();
        	if(FeedResource.getInstance().getAllFeed().size() > 0){
        		listview.setVisibility(View.VISIBLE);
    			emptyView.setVisibility(View.GONE);
			}
    		super.onResume();
    	}

    	@Override
    	public void onPause() {
    		FeedResource.getInstance().closeDataSource();
    		super.onPause();
    	}
    	
    	@Override
    	public void onDestroy() {
    		FeedResource.getInstance().destory();
    		super.onDestroy();
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
            mListview.setEmptyView(rootView.findViewById(R.id.emptystub));
             
    		registerForContextMenu(mListview);
    		
    		// Show contextview on item click
    		mListview.setOnItemClickListener(new OnItemClickListener() {

    			@Override
    			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
    				// TODO Auto-generated method stub
    			}
    		});

//    		List<SensorFeed> mtempAllowedSensorFeedList = new ArrayList<SensorFeed>();
//    		// update the list over here.. to avoid empty shell
//    		for (SensorFeed sensor : FeedResource.getInstance().getSensorFeedList()) {
//    			
//    			boolean isChecked = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
//    				.getBoolean(sensor.getSensorKey(), false);
//    			
//    			if(isChecked)
//    				mtempAllowedSensorFeedList.add(sensor);
//			}
    		// update the list with new created list
    		mSensorFeedAdapter.setFeedList(FeedResource.getInstance().getSensorFeedList());
    		
    		mListview.setAdapter(mSensorFeedAdapter);
    		
            return rootView;
        }
        
        @Override
        public void onResume() {
        	// TODO Auto-generated method stub
        	
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
    		
        	super.onResume();
        }

//		@Override
//		public void onFocusChange(View v, boolean hasFocus) {
//			// TODO Auto-generated method stub
//			Toast.makeText(getActivity(), "focused :" + hasFocus, Toast.LENGTH_SHORT).show();
//		}
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
			
			FeedResource.getInstance().removeRecentFeed(info.position);
			
		}else if (menuItemName.contains("View")) {
			Intent intent = new Intent(MainActivity.this, ViewActivity.class);
			intent.putExtra(ViewActivity.EXTRAS_ROW_ID, info.position);
			startActivity(intent);
		} 
		return true;
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
            	startActivity(new Intent(this, SettingPreferenceActivity.class));
            return true;
            case R.id.action_manage:
            	startActivity(new Intent(this, EndPointActivity.class));
            	return true;
            case R.id.action_sync:
            	startActivity(new Intent(this, SensorEvaluator.class));
            	return true;
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
		
		SharedPreferences activityPrefs = getSharedPreferences(
        		Constant.SHARED_PREFS_FILENAME, 
        		Context. MODE_PRIVATE	);

        activityPrefs.edit().putBoolean("is_first_time", true).commit(); 
        AppController.isFirstLoad = true;
        	
		super.onDestroy();
	}
	
	/**
	 * Handling of Splash screen
	 */
	private void activateSplashScreen(final View view){
		// UP to DOWN Animation
		final float direction =  -1 ;
		final float yDelta =  (getScreenHeight() - (2 * view.getHeight()));
		
		final Animation animation = new TranslateAnimation(0,0,0, yDelta * direction);

		animation.setDuration(1500);

		animation.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
				getActionBar().show();
				
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				view.setVisibility(View.GONE);
				
				showTitle();
				 
				View titleView = getWindow().findViewById(android.R.id.title);
			    if (titleView != null) {
			        ViewParent parent = titleView.getParent();
			        if (parent != null && (parent instanceof View)) {
			         View parentView = (View)parent;
			         parentView.setVisibility(View.VISIBLE);
			        }
			    }
			    
			    AppController.isFirstLoad = false;
			}
		});

		view.startAnimation(animation);
	}
	
	private float getScreenHeight() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		return (float) displaymetrics.heightPixels;
	}

	public void hideTitle() {
        try {
            ((View) findViewById(android.R.id.title).getParent())
                    .setVisibility(View.GONE);
        } catch (Exception e) {
        }
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }

    public void showTitle() {
        try {
            ((View) findViewById(android.R.id.title).getParent())
                    .setVisibility(View.VISIBLE);
        } catch (Exception e) {
        }
        
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
