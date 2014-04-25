package com.univ.helsinki.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.univ.helsinki.app.activities.AccelerometerSensorActivity;
import com.univ.helsinki.app.activities.AmbientTemperatureSensor;
import com.univ.helsinki.app.activities.BarometerSensorActivity;
import com.univ.helsinki.app.activities.LocationActivity;
import com.univ.helsinki.app.activities.PressureSensor;
import com.univ.helsinki.app.activities.SensorListActivity;

public class MainSensorActivity extends Activity {

	private List<Map<String, String>> mSensorActyList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor);

		mSensorActyList = new ArrayList<Map<String, String>>();

		createActivityList();

		SimpleAdapter adapter = new SimpleAdapter(this, mSensorActyList,
				android.R.layout.simple_list_item_2, new String[] { "name",
						"status" }, new int[] { android.R.id.text1,
						android.R.id.text2 });

		ListView lv = (ListView) findViewById(R.id.listView);
		lv.setAdapter(adapter);

		// React to user clicks on item
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parentAdapter, View view,
					int position, long id) {
				Intent intent = null;;
					
				switch(position){
				case 0:{
					intent = new Intent(MainSensorActivity.this, SensorListActivity.class);
				}break;
				case 1:{
					intent = new Intent(MainSensorActivity.this, PressureSensor.class);
				}break;
				case 2:{
					intent = new Intent(MainSensorActivity.this, BarometerSensorActivity.class);
				}break;
				case 3:{
					intent = new Intent(MainSensorActivity.this, AmbientTemperatureSensor.class);
				}break;
				case 4:{
					intent = new Intent(MainSensorActivity.this, AccelerometerSensorActivity.class);
				}break;
				case 5:{
					intent = new Intent(MainSensorActivity.this, LocationActivity.class);
				}break;
				
				}
				startActivity(intent);
			}
		});

	}

	private String[] mActivityList = new String[] {
			SensorListActivity.class.getSimpleName(),
			PressureSensor.class.getSimpleName(),
			BarometerSensorActivity.class.getSimpleName(),
			AmbientTemperatureSensor.class.getSimpleName(),
			AccelerometerSensorActivity.class.getSimpleName(),
			LocationActivity.class.getSimpleName() };

	private void createActivityList() {
		Map<String, String> data1 = new HashMap<String, String>();
		data1.put("name", mActivityList[0]);
		data1.put("status", "All sensor list");
		mSensorActyList.add(data1);

		Map<String, String> data2 = new HashMap<String, String>();
		data2.put("name", mActivityList[1]);
		data2.put("status", "Presure and Height AndroidSensor");
		mSensorActyList.add(data2);

		Map<String, String> data3 = new HashMap<String, String>();
		data3.put("name", mActivityList[2]);
		data3.put("status", "Barometer AndroidSensor");
		mSensorActyList.add(data3);

		Map<String, String> data4 = new HashMap<String, String>();
		data4.put("name", mActivityList[3]);
		data4.put("status", "Temperature AndroidSensor");
		mSensorActyList.add(data4);

		Map<String, String> data5 = new HashMap<String, String>();
		data5.put("name", mActivityList[4]);
		data5.put("status", "Accelerometer AndroidSensor");
		mSensorActyList.add(data5);

		Map<String, String> data6 = new HashMap<String, String>();
		data6.put("name", mActivityList[5]);
		data6.put("status", "Location AndroidSensor");
		mSensorActyList.add(data6);
	}
}
