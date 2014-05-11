package com.univ.helsinki.app;

import java.util.List;

import android.app.Application;
import android.app.Service;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.univ.helsinki.app.core.SensorFeed;
import com.univ.helsinki.app.db.DatabaseHelper;
import com.univ.helsinki.app.db.FeedResource;
import com.univ.helsinki.app.util.Constant;

public class AppController extends Application {
	
	public static boolean isFirstLoad = false;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		isFirstLoad = true;
		
		setSensorMap();
		
		getAllDeviceSensorList();
		
		FeedResource.getInstance().inti(getApplicationContext());
		
	}
	
	private void setSensorMap(){
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
	}
	
	private void getAllDeviceSensorList(){
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
}
