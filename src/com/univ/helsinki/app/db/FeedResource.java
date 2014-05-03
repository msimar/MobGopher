package com.univ.helsinki.app.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;

import com.univ.helsinki.app.adapter.RecentActivityAdapter;
import com.univ.helsinki.app.core.DeviceFeed;
import com.univ.helsinki.app.core.SensorFeed;

public class FeedResource {
	
	@SuppressLint("UseSparseArrays")
	public Map<Integer,String> mAllSensorMap = new HashMap<Integer, String>();
	
	private List<SensorFeed> mSensorFeedList;
	
	private DeviceFeed mDeviceFeed;
	
	private List<Feed> mFeedList;
	
	private RecentActivityDataSource mDatasource;
	
	private RecentActivityAdapter mAdapter;
	
	private static FeedResource INSTANCE;
	
	private FeedResource(){
		
		this.mFeedList = new ArrayList<Feed>();
		
		setmDeviceFeed(new DeviceFeed());
		
		setSensorFeedList(new ArrayList<SensorFeed>());
		
		mAllSensorMap.put(1, "TYPE_ACCELEROMETER");
		mAllSensorMap.put(2, "TYPE_MAGNETIC_FIELD");
		mAllSensorMap.put(3, "TYPE_ORIENTATION");
		mAllSensorMap.put(4, "TYPE_GYROSCOPE");
		mAllSensorMap.put(5, "TYPE_LIGHT");
		mAllSensorMap.put(6, "TYPE_PRESSURE");
		mAllSensorMap.put(7, "TYPE_TEMPERATURE");
		mAllSensorMap.put(8, "TYPE_PROXIMITY");
		mAllSensorMap.put(9, "TYPE_GRAVITY");
		mAllSensorMap.put(10, "TYPE_LINEAR_ACCELERATION");
		mAllSensorMap.put(11, "TYPE_ROTATION_VECTOR");
		mAllSensorMap.put(12, "TYPE_RELATIVE_HUMIDITY");
		mAllSensorMap.put(13, "TYPE_AMBIENT_TEMPERATURE");
		mAllSensorMap.put(14, "TYPE_MAGNETIC_FIELD_UNCALIBRATED");
		mAllSensorMap.put(15, "TYPE_GAME_ROTATION_VECTOR");
		mAllSensorMap.put(16, "TYPE_GYROSCOPE_UNCALIBRATED");
		mAllSensorMap.put(17, "TYPE_SIGNIFICANT_MOTION");
		mAllSensorMap.put(18, "TYPE_STEP_DETECTOR");
		mAllSensorMap.put(19, "TYPE_STEP_COUNTER");
		mAllSensorMap.put(20, "TYPE_GEOMAGNETIC_ROTATION_VECTOR");
	}
	
	public static synchronized FeedResource getInstance(){
		if(INSTANCE == null)
			INSTANCE = new FeedResource();
		return INSTANCE;
	}
	
	public List<SensorFeed> getSensorFeedList() {
		return mSensorFeedList;
	}

	private void setSensorFeedList(List<SensorFeed> sensorFeedList) {
		this.mSensorFeedList = sensorFeedList;
	}
	
	public DeviceFeed getmDeviceFeed() {
		return mDeviceFeed;
	}

	private void setmDeviceFeed(DeviceFeed mDeviceFeed) {
		this.mDeviceFeed = mDeviceFeed;
	}
	
	public void inti(Context context) {
		this.mDatasource = new RecentActivityDataSource(context);
		this.mDatasource.open();
	}
	
	public List<Feed> getAllFeed(){
		this.mFeedList = this.mDatasource.getAllFeeds();;
		return this.mFeedList;
	}
	
	public void addFeed(int location, Feed feed){
		this.mFeedList.add(location, feed);
		this.mAdapter.notifyDataChanged();
	}
	
	public void removeFeed(int location){
		long id = this.mFeedList.get(location).getId();
		
		if(mDatasource != null){
			mDatasource.delete(id);
		}
		mFeedList.remove(location);
	}
	
	public Feed createFeed(String title, String content) {
		return this.mDatasource.createFeed(title, content);
	}
	
	public void setRecentFeedAdapter(RecentActivityAdapter adapter) {
		this.mAdapter = adapter;
	}
	
	/**
	 * Should be called in Activity#onresume()
	 */
	public void openDataSource(){
		if(mDatasource != null)
			mDatasource.open();
	}
	
	/**
	 * Should be called in Activity#onpause()
	 */
	public void closeDataSource(){
		if(mDatasource != null)
			mDatasource.close();
	}
	
	public void destory(){
		if(mDatasource != null)
			mDatasource.close();
		this.mFeedList.clear();
	}
}
