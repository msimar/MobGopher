package com.univ.helsinki.app.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.univ.helsinki.app.core.DeviceFeed;
import com.univ.helsinki.app.core.SensorFeed;

import android.hardware.Sensor;
import android.hardware.SensorManager;

public class FeedResource {
	
	public Map<Integer,String> mAllSensorMap = new HashMap<Integer, String>();
	
	private List<SensorFeed> mSensorFeedList;
	
	private DeviceFeed mDeviceFeed;
	
	private static FeedResource INSTANCE;
	
	private FeedResource(){
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
	
	
	
}
