package com.univ.helsinki.app.sensor;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.univ.helsinki.app.R;

/**
 * http://developer.android.com/reference/android/hardware/SensorEvent.html#values
 * @author msingh
 *
 */
public class PressureSensor extends Activity {

	private final String TAG = PressureSensor.class.getSimpleName();

	private SensorManager mSensorManager = null;

	private float sea_level_pressure = SensorManager.PRESSURE_STANDARD_ATMOSPHERE;

	public static final String TAG_PRESSURE = "aws:pressure";
	public static final String TAG_LONGITUDE = "aws:longitude";
	public static final String TAG_LATITUDE = "aws:latitude";
	
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
				mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE),
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
			// when pressure value is changed, this method will be called.
			float pressure_value = 0.0f;
			float height = 0.0f;

			// if you use this listener as listener of only one sensor (ex,
			// Pressure), then you don't need to check sensor type.
			if (Sensor.TYPE_PRESSURE == event.sensor.getType()) {
				// values[0]: Atmospheric pressure in hPa (millibar)
				pressure_value = event.values[0];
				
				// Computes the Altitude in meters from the atmospheric pressure and the pressure at sea level.
				height = SensorManager.getAltitude(
						SensorManager.PRESSURE_STANDARD_ATMOSPHERE,
						pressure_value);
				
				tvResult1.setText("pressure_value :" +  pressure_value + " hPa");
				tvResult2.setText("height :" + height + " m");
			}
		}
	};

	private void updatePressure() {
		Thread task = new Thread(new Runnable() {
			public void run() {
				//sea_level_pressure = GetRefPressure(longitude, latitude);
				Log.d(TAG, "updated! " + sea_level_pressure);
			}
		});
		task.start();
	} // this function is

	public float GetRefPressure(double longitude, double latitude) {
		InputStream is = null;
		float pressure = 0.0f;
		try {
			String strUrl = String
					.format("http://(insert your API Key).api.wxbug.net/getLiveWeatherRSS.aspx?ACode=(insert your API)&lat=%f&long=%f&UnitType=1&OutputType=1",
							latitude, longitude);
			URL text = new URL(strUrl);
			Log.d(TAG, text.toString());

			URLConnection connection = text.openConnection();
			connection.setReadTimeout(30000);
			connection.setConnectTimeout(30000);

			is = connection.getInputStream();

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder domParser = dbf.newDocumentBuilder();
			Document xmldoc = domParser.parse(is);
			Element root = xmldoc.getDocumentElement();

			pressure = Float.parseFloat(getTagValue(TAG_PRESSURE, root));
			float lon = Float.parseFloat(getTagValue(TAG_LONGITUDE, root));
			float lat = Float.parseFloat(getTagValue(TAG_LATITUDE, root));
		} catch (Exception e) {
			Log.e(TAG, "Error in network call", e);
			pressure = SensorManager.PRESSURE_STANDARD_ATMOSPHERE;
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return pressure;
	}

	public String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
				.getChildNodes();
		Node nValue = (Node) nlList.item(0);
		return nValue.getNodeValue();
	}

}
