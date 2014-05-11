package com.univ.helsinki.app.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;

import com.univ.helsinki.app.adapter.FeedEndPointAdapter;
import com.univ.helsinki.app.adapter.RecentActivityAdapter;
import com.univ.helsinki.app.core.DeviceFeed;
import com.univ.helsinki.app.core.SensorFeed;
import com.univ.helsinki.app.util.Constant;

public class FeedResource {
	
	@SuppressLint("UseSparseArrays")
	public Map<Integer,String> mAllSensorMap = new HashMap<Integer, String>();
	public static Map<String,String> sAllSensorKeyNameMap = new HashMap<String, String>();
	
	private List<SensorFeed> mSensorFeedList;
	
	private DeviceFeed mDeviceFeed;
	
	private List<Feed> mRecentFeedList;
	private List<FeedEndPoint> mFeedEndPointList;
	
	private DataSourceRecentFeed mDatasourceRecentFeed;
	private DataSourceFeedEndPoint mDatasourceFeedEndPoint;
	
	private RecentActivityAdapter mRecentAdapter;
	private FeedEndPointAdapter mFeedEndPointAdapter;
	
	private static FeedResource INSTANCE;
	
	private Map<String,Boolean> mSelectedSensorMap = new HashMap<String,Boolean>();
	
	private FeedResource(){
		
		this.mRecentFeedList = new ArrayList<Feed>();
		this.setFeedEndPointList(new ArrayList<FeedEndPoint>());
		
		setDeviceFeed(new DeviceFeed());
		
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
		
		sAllSensorKeyNameMap.put(Constant.PREF_TYPE_ACCELEROMETER_KEY, "ACCELEROMETER");
		sAllSensorKeyNameMap.put(Constant.PREF_TYPE_MAGNETIC_FIELD_KEY, "MAGNETIC FIELD");
		
		sAllSensorKeyNameMap.put(Constant.PREF_TYPE_ORIENTATION_KEY, "ORIENTATION");
		sAllSensorKeyNameMap.put(Constant.PREF_TYPE_GYROSCOPE_KEY, "GYROSCOPE");
		sAllSensorKeyNameMap.put(Constant.PREF_TYPE_LIGHT_KEY, "LIGHT");
		sAllSensorKeyNameMap.put(Constant.PREF_TYPE_PRESSURE_KEY, "PRESSURE");
		
		sAllSensorKeyNameMap.put(Constant.PREF_TYPE_TEMPERATURE_KEY, "TEMPERATURE");
		sAllSensorKeyNameMap.put(Constant.PREF_TYPE_PROXIMITY_KEY, "PROXIMITY");
		sAllSensorKeyNameMap.put(Constant.PREF_TYPE_GRAVITY_KEY, "GRAVITY");
		sAllSensorKeyNameMap.put(Constant.PREF_TYPE_LINEAR_ACCELERATION_KEY, "LINEAR ACCELERATION");
		
		sAllSensorKeyNameMap.put(Constant.PREF_TYPE_ROTATION_VECTOR_KEY, "ROTATION VECTOR");
		sAllSensorKeyNameMap.put(Constant.PREF_TYPE_RELATIVE_HUMIDITY_KEY, "RELATIVE HUMIDITY");
		sAllSensorKeyNameMap.put(Constant.PREF_TYPE_AMBIENT_TEMPERATURE_KEY, "AMBIENT TEMPERATURE");
		sAllSensorKeyNameMap.put(Constant.PREF_TYPE_MAGNETIC_FIELD_UNCALIBRATED_KEY, "MAGNETIC FIELD UNCALIBRATED");
		
		sAllSensorKeyNameMap.put(Constant.PREF_TYPE_GAME_ROTATION_VECTOR_KEY, "GAME ROTATION VECTOR");
		sAllSensorKeyNameMap.put(Constant.PREF_TYPE_GYROSCOPE_UNCALIBRATED_KEY, "GYROSCOPE UNCALIBRATED");
		sAllSensorKeyNameMap.put(Constant.PREF_TYPE_SIGNIFICANT_MOTION_KEY, "SIGNIFICANT MOTION");
		sAllSensorKeyNameMap.put(Constant.PREF_TYPE_STEP_DETECTOR_KEY, "STEP DETECTOR");
		
		sAllSensorKeyNameMap.put(Constant.PREF_TYPE_STEP_COUNTER_KEY, "STEP COUNTER");
		sAllSensorKeyNameMap.put(Constant.PREF_TYPE_GEOMAGNETIC_ROTATION_VECTOR_KEY, "GEOMAGNETIC ROTATION VECTOR");
	}
	
	public static synchronized FeedResource getInstance(){
		if(INSTANCE == null)
			INSTANCE = new FeedResource();
		return INSTANCE;
	}
	
	/////////////////////////////////////////////////////
	
	private DatabaseHelper mDbHelper;
	
	public DatabaseHelper getDbHelper(){
		return this.mDbHelper;
	}
	
	/////////////////////////////////////////////////////
	
	public List<SensorFeed> getSensorFeedList() {
		return mSensorFeedList;
	}

	private void setSensorFeedList(List<SensorFeed> sensorFeedList) {
		this.mSensorFeedList = sensorFeedList;
	}
	
	public DeviceFeed getDeviceFeed() {
		return mDeviceFeed;
	}

	private void setDeviceFeed(DeviceFeed mDeviceFeed) {
		this.mDeviceFeed = mDeviceFeed;
	}
	
	/*
	 * Manage Call for Recent Feeds
	 */
	
	public List<Feed> getAllFeed(){
		this.mRecentFeedList = this.mDatasourceRecentFeed.getAllRecentFeed();;
		return this.mRecentFeedList;
	}
	
	public void addRecentFeed(int location, Feed feed){
		this.mRecentFeedList.add(location, feed);
		this.mRecentAdapter.notifyDataChanged();
	}
	
	public Feed createRecentFeed(String title, String content) {
		return this.mDatasourceRecentFeed.createRecentFeed(title, content);
	}
	
	public void removeRecentFeed(int location){
		long id = this.mRecentFeedList.get(location).getId();
		
		if(mDatasourceRecentFeed != null){
			mDatasourceRecentFeed.delete(id);
		}
		this.mRecentFeedList.remove(location);
		this.mRecentAdapter.notifyDataChanged();
	}
	
	/////////////////////////////////////////////////////
	
	/*
	 * Manage Call for Feed End Points
	 */
	public void createFeedEndPoint(FeedEndPoint feedEndPoint) {
		this.mDatasourceFeedEndPoint.createFeedEndPoint(feedEndPoint);
	}
	
	public void removeFeedEndPoint(int location){
		long id = this.mFeedEndPointList.get(location).getId();
		
		if(mDatasourceFeedEndPoint != null){
			mDatasourceFeedEndPoint.delete(id);
		}
		mFeedEndPointList.remove(location);
		this.mFeedEndPointAdapter.notifyDataChanged();
	}
	
	public void addFeedEndPoint(FeedEndPoint endPoint){
		// save to the list
		this.mFeedEndPointList.add(endPoint);
		// save to the db
		this.createFeedEndPoint(endPoint);
		// notify adapter
		this.mFeedEndPointAdapter.notifyDataChanged();
	}
	
	public List<FeedEndPoint> getAllFeedEndPoint(){
		this.mFeedEndPointList = this.mDatasourceFeedEndPoint.getAllFeedEndPoint();;
		return this.mFeedEndPointList;
	}

	public List<FeedEndPoint> getFeedEndPointList() {
		return mFeedEndPointList;
	}
	
	private void setFeedEndPointList(List<FeedEndPoint> mFeedEndPointList) {
		this.mFeedEndPointList = mFeedEndPointList;
	}
	
	/////////////////////////////////////////////////////
	
	public void destory(){
		if(mDatasourceRecentFeed != null)
			mDatasourceRecentFeed.close();
		if(mDatasourceFeedEndPoint != null)
			mDatasourceFeedEndPoint.close();
		this.mRecentFeedList.clear();
		this.mFeedEndPointList.clear();
	}
	
	/**
	 * Manage Datasource
	 */
	
	public void inti(Context context) {
		this.mDbHelper = new DatabaseHelper(context);
		
		this.mDatasourceRecentFeed = new DataSourceRecentFeed(this.mDbHelper);
		this.mDatasourceRecentFeed.open();
		
		this.mDatasourceFeedEndPoint = new DataSourceFeedEndPoint(this.mDbHelper);
		this.mDatasourceFeedEndPoint.open();
		
	}
	
	/**
	 * Should be called in Activity#onresume()
	 */
	public void openDataSource(){
		if(mDatasourceRecentFeed != null)
			mDatasourceRecentFeed.open();
		if(mDatasourceFeedEndPoint != null)
			mDatasourceFeedEndPoint.open();
	}
	
	/**
	 * Should be called in Activity#onpause()
	 */
	public void closeDataSource(){
		if(mDatasourceRecentFeed != null)
			mDatasourceRecentFeed.close();
		if(mDatasourceFeedEndPoint != null)
			mDatasourceFeedEndPoint.close();
	}
	
	/**
	 * Set Adapters for Recent and FeedEnd Point
	 */
	public void setRecentFeedAdapter(RecentActivityAdapter adapter) {
		this.mRecentAdapter = adapter;
	}
	
	public void setFeedEndPointAdapter(FeedEndPointAdapter adapter) {
		this.mFeedEndPointAdapter = adapter;
	}

	public Map<String,Boolean> getSelectedSensorMap() {
		return mSelectedSensorMap;
	}

	public void setSelectedSensorMap(Map<String,Boolean> mSelectedSensorMap) {
		this.mSelectedSensorMap = mSelectedSensorMap;
	}
}
