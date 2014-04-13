package com.univ.helsinki.app.sensor;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.univ.helsinki.app.R;

public class AmbientTemperatureSensor  extends Activity {

	private final String TAG = AmbientTemperatureSensor.class.getSimpleName();

	private SensorManager mSensorManager = null;

	private TextView tvResult1, tvResult2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pressure_sensor);

		// get SensorManager instance.
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		
		tvResult1 = (TextView) findViewById(R.id.tvResult1);
		tvResult2 = (TextView) findViewById(R.id.tvResult2);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Register listener
		boolean isSupported = mSensorManager.registerListener(mSensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
				SensorManager.SENSOR_DELAY_NORMAL);
		
		Toast.makeText(this, "" + isSupported, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Unregister listener
		mSensorManager.unregisterListener(mSensorListener);
	}

	private SensorEventListener mSensorListener = new SensorEventListener() {

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// when accuracy changed, this method will be called.
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			float temperature_value = 0.0f;

			// ambient (room) temperature in degree Celsius.
			
			// if you use this listener as listener of only one sensor (ex,
			// Pressure), then you don't need to check sensor type.
			if (Sensor.TYPE_AMBIENT_TEMPERATURE == event.sensor.getType()) {
				// values[0]: Atmospheric pressure in hPa (millibar)
				temperature_value = event.values[0];
				
				tvResult1.setText("temperature_value :" +  temperature_value + " hPa");
				tvResult2.setText("");
			}
		}
	};


}
