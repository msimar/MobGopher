package com.univ.helsinki.app.activities;

import android.app.Activity;
import android.app.Service;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.univ.helsinki.app.R;

public class BarometerSensorActivity extends Activity implements
		SensorEventListener {

	private TextView mResult;

	private SensorManager mSensorManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_sensor_barometer);

		// Look for barometer sensor
		mSensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);

		mResult = (TextView) findViewById(R.id.tvResult);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// register this class as a listener for the Pressure AndroidSensor
		boolean isSupported = mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE),
				SensorManager.SENSOR_DELAY_NORMAL);
		
		Toast.makeText(this, "" + isSupported, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onPause() {
		// unregister listener
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
			float[] values = event.values;
			mResult.setText("" + values[0]);
		}

	}
}
