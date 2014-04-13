package com.univ.helsinki.app.sensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.univ.helsinki.app.R;

/**
 * http://developer.android.com/reference/android/hardware/SensorManager.html
 * @author msingh
 *
 */
public class AccelerometerSensorActivity extends Activity implements SensorEventListener {

	private final String TAG = AccelerometerSensorActivity.class.getSimpleName();

	private SensorManager mSensorManager;
	private Sensor mAccelerometer;

	private TextView xViewA = null;
	private TextView yViewA = null;
	private TextView zViewA = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_sensor_accelerometer);
		
		 mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
	        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		xViewA = (TextView) findViewById(R.id.xbox);
		yViewA = (TextView) findViewById(R.id.ybox);
		zViewA = (TextView) findViewById(R.id.zbox);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// register this class as a listener for the Pressure Sensor
		boolean isSupported = mSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			float[] values = event.values;
			xViewA.setText("Accel X: " + values[0]);
			yViewA.setText("Accel Y: " + values[1]);
			zViewA.setText("Accel Z: " + values[2]);
		}
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		Log.d(TAG, "onAccuracyChanged: " + sensor + ", accuracy: " + accuracy);
	}

}