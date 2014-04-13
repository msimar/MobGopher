package com.univ.helsinki.app.sensor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Service;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.univ.helsinki.app.R;

public class SensorListActivity extends Activity {

	private SensorManager sensorManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor);

		// Get the reference to the sensor manager
		sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);

		// Get the list of sensor
		List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

		List<Map<String, String>> sensorDataList = new ArrayList<Map<String, String>>();

		for (Sensor sensor : sensorList) {
			Map<String, String> data = new HashMap<String, String>();
			data.put("name", sensor.getName());
			data.put("vendor", sensor.getVendor());
			sensorDataList.add(data);
		}

		SimpleAdapter adapter = new SimpleAdapter(this, sensorDataList,
				android.R.layout.simple_list_item_2, new String[] { "name", "vendor" },
				new int[] { android.R.id.text1, android.R.id.text2 });
		
		ListView lv = (ListView) findViewById(R.id.listView);
		lv.setAdapter(adapter);


	}
}
