package com.univ.helsinki.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.gson.Gson;
import com.univ.helsinki.app.db.FeedResource;
import com.univ.helsinki.app.sensor.SensorStream;
import com.univ.helsinki.app.sensor.SensorUnit;
import com.univ.helsinki.app.util.Constant;

public 	class 		SensorEvaluator 
		extends 	Activity 
		implements 	GooglePlayServicesClient.ConnectionCallbacks,
					GooglePlayServicesClient.OnConnectionFailedListener,
					LocationListener {

	private final String TAG = SensorEvaluator.class.getSimpleName();

	private SensorManager mSensorManager = null;
	
	private TextView mTextView;
	
	private SensorStream mSensorStream;
	
	private ProgressDialog mSyncDialog;
	
	private boolean isAccelerometerAdded = false;
	
	/**
	 * Location Handling
	 */
	private LocationClient locationclient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mTextView = new TextView(this);
		mTextView.setTextSize((float) 21.0);
		
//		setContentView(mTextView);
		
		mSyncDialog = ProgressDialog.show(this, "", 
				 getResources().getString(R.string.sync_dialog_message), true); 
		
		this.mSensorStream = new SensorStream();
		
		// get SensorManager instance.
		this.mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		
		// do initialization of services
		this.doInitialize(this);
	}
	public void doInitialize(Context context){
		
		// Location Handling
		doLocationInit(context);
	}
	
	public void doLocationInit(Context context){
		int resp = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
		
		if(resp == ConnectionResult.SUCCESS){
			locationclient = new LocationClient(context, this, this);
			locationclient.connect();
		}
		else{
			Toast.makeText(context, "Google Play Service Error " + resp, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Register listener
		boolean isSupported = false;
		
		if( PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
				Constant.PREF_TYPE_PRESSURE_KEY, false)){
			
			isSupported = mSensorManager.registerListener(mSensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE),
				SensorManager.SENSOR_DELAY_NORMAL);
			
			Log.i(TAG,"AndroidSensor.TYPE_PRESSURE supported :" + isSupported);
		}
		
		if( PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
				Constant.PREF_TYPE_AMBIENT_TEMPERATURE_KEY, false)){
			
			isSupported = mSensorManager.registerListener(mSensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
				SensorManager.SENSOR_DELAY_NORMAL);
			
			Log.i(TAG,"AndroidSensor.TYPE_AMBIENT_TEMPERATURE supported :" + isSupported);
		}
		
		if( PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
				Constant.PREF_TYPE_ACCELEROMETER_KEY, false)){
		
			isSupported = mSensorManager.registerListener(mSensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		
			Log.i(TAG,"AndroidSensor.TYPE_ACCELEROMETER supported :" + isSupported);
			
		}
		
		if( PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
				Constant.PREF_TYPE_LIGHT_KEY, false)){
		
			isSupported = mSensorManager.registerListener(mSensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
				SensorManager.SENSOR_DELAY_NORMAL);
			
			Log.i(TAG,"AndroidSensor.TYPE_LIGHT supported :" + isSupported);
		}
		
		if( PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
				Constant.PREF_TYPE_GYROSCOPE_KEY, false)){
		
			isSupported = mSensorManager.registerListener(mSensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
				SensorManager.SENSOR_DELAY_NORMAL);
			
			Log.i(TAG,"AndroidSensor.TYPE_GYROSCOPE supported :" + isSupported);
		}
		
		if( PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
				Constant.PREF_TYPE_MAGNETIC_FIELD_UNCALIBRATED_KEY, false)){
			
			isSupported = mSensorManager.registerListener(mSensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED),
				SensorManager.SENSOR_DELAY_NORMAL);
			
			Log.i(TAG,"AndroidSensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED supported :" + isSupported);
		}
		
		if( PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
				Constant.PREF_TYPE_MAGNETIC_FIELD_KEY, false)){
		
			isSupported = mSensorManager.registerListener(mSensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_NORMAL);
			
			Log.i(TAG,"AndroidSensor.TYPE_MAGNETIC_FIELD supported :" + isSupported);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Unregister listener
		mSensorManager.unregisterListener(mSensorListener);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(locationclient!=null)
			locationclient.disconnect();
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
				
				Log.i(TAG,"pressure_value :" +  pressure_value + " hPa");
				Log.i(TAG,"height :" + height + " m");
				
				//setPressure(pressure_value + " hPa");
				//setHeight(height + " m");
				
				SensorUnit object = new SensorUnit();
				object.setName("Sensor.TYPE_PRESSURE");
				object.setStatus(true);
				object.setTypeValue(Integer.toString(Sensor.TYPE_PRESSURE));
				object.setVendor(event.sensor.getVendor());
				
				Map<String, Float> valueMap = new HashMap<String, Float>();
				valueMap.put("pressure", pressure_value);
				valueMap.put("height", height);
				
				object.getValues().add(valueMap);
				
				mSensorStream.getSensors().add(object);
				
				
			}else if (Sensor.TYPE_AMBIENT_TEMPERATURE == event.sensor.getType()) {
				// values[0]: Atmospheric pressure in hPa (millibar)
				float temperature_value = event.values[0];
				
				Log.i(TAG,"temperature_value :" +  temperature_value + " hPa");
		 
				//setTemperature(temperature_value + " hPa");
				
				SensorUnit object = new SensorUnit();
				object.setName("Sensor.TYPE_AMBIENT_TEMPERATURE");
				object.setStatus(true);
				object.setTypeValue(Integer.toString(Sensor.TYPE_AMBIENT_TEMPERATURE));
				object.setVendor(event.sensor.getVendor());
				
				Map<String, Float> valueMap = new HashMap<String, Float>();
				valueMap.put("ambient_temperature", temperature_value);
				
				object.getValues().add(valueMap);
				
				mSensorStream.getSensors().add(object);
				
			}else if ( !isAccelerometerAdded && event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				float[] values = event.values;
//				Log.i(TAG,"Accel X: " + values[0]);
//				Log.i(TAG,"Accel Y: " + values[1]);
//				Log.i(TAG,"Accel Z: " + values[2]);
				
				//setAccelerometer(values[0], values[1], values[2]);
				
				SensorUnit object = new SensorUnit();
				object.setName("Sensor.TYPE_ACCELEROMETER");
				object.setStatus(true);
				object.setTypeValue(Integer.toString(Sensor.TYPE_ACCELEROMETER));
				object.setVendor(event.sensor.getVendor());
				
				Map<String, Float> valueMap = new HashMap<String, Float>();
				valueMap.put("accelerometer_x", values[0]);
				valueMap.put("accelerometer_y", values[1]);
				valueMap.put("accelerometer_z", values[2]);
				
				object.getValues().add(valueMap);
				
				mSensorStream.getSensors().add(object);
				
				isAccelerometerAdded = !isAccelerometerAdded;
			}else if ( event.sensor.getType() == Sensor.TYPE_LIGHT) {
				float[] values = event.values;
				
				SensorUnit object = new SensorUnit();
				object.setName("Sensor.TYPE_LIGHT");
				object.setStatus(true);
				object.setTypeValue(Integer.toString(Sensor.TYPE_LIGHT));
				object.setVendor(event.sensor.getVendor());
				
				Map<String, Float> valueMap = new HashMap<String, Float>();
				valueMap.put("light", values[0]);
				
				object.getValues().add(valueMap);
				
				mSensorStream.getSensors().add(object);
			} else if (  event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
				Map<String, Float> valueMap = new HashMap<String, Float>();
				// This timestep's delta rotation to be multiplied by the current rotation
		          // after computing it from the gyro sample data.
		          if (timestamp != 0) {
		              final float dT = (event.timestamp - timestamp) * NS2S;
		              // Axis of the rotation sample, not normalized yet.
		              float axisX = event.values[0];
		              float axisY = event.values[1];
		              float axisZ = event.values[2];

		              // Calculate the angular speed of the sample
		              float omegaMagnitude = (float) Math.sqrt(axisX*axisX + axisY*axisY + axisZ*axisZ);

		              // Normalize the rotation vector if it's big enough to get the axis
//		              if (omegaMagnitude > EPSILON) {
//		                  axisX /= omegaMagnitude;
//		                  axisY /= omegaMagnitude;
//		                  axisZ /= omegaMagnitude;
//		              }

		              // Integrate around this axis with the angular speed by the timestep
		              // in order to get a delta rotation from this sample over the timestep
		              // We will convert this axis-angle representation of the delta rotation
		              // into a quaternion before turning it into the rotation matrix.
//		              float thetaOverTwo = omegaMagnitude * dT / 2.0f;
//		              float sinThetaOverTwo = sin(thetaOverTwo);
//		              float cosThetaOverTwo = cos(thetaOverTwo);
//		              deltaRotationVector[0] = sinThetaOverTwo * axisX;
//		              deltaRotationVector[1] = sinThetaOverTwo * axisY;
//		              deltaRotationVector[2] = sinThetaOverTwo * axisZ;
//		              deltaRotationVector[3] = cosThetaOverTwo;
		              
		              valueMap.put("axisX", axisX);
						valueMap.put("axisY", axisY);
						valueMap.put("axisZ", axisZ);
		          }
		          timestamp = event.timestamp;
//		          float[] deltaRotationMatrix = new float[9];
//		          SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
		          // User code should concatenate the delta rotation we computed with the current rotation
		          // in order to get the updated rotation.
		          // rotationCurrent = rotationCurrent * deltaRotationMatrix;
				
				SensorUnit object = new SensorUnit();
				object.setName("Sensor.TYPE_GYROSCOPE");
				object.setStatus(true);
				object.setTypeValue(Integer.toString(Sensor.TYPE_GYROSCOPE));
				object.setVendor(event.sensor.getVendor());
				
				
				valueMap.put("timestamp", timestamp);
				
				object.getValues().add(valueMap);
				
				mSensorStream.getSensors().add(object);
				
				
			} else if ( event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED) {
				float[] values = event.values;
				
				SensorUnit object = new SensorUnit();
				object.setName("Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED");
				object.setStatus(true);
				object.setTypeValue(Integer.toString(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED));
				object.setVendor(event.sensor.getVendor());
				
				Map<String, Float> valueMap = new HashMap<String, Float>();
				valueMap.put("x_uncalib", values[0]);
				valueMap.put("y_uncalib", values[1]);
				valueMap.put("z_uncalib", values[2]);
				
				valueMap.put("x_bias", values[3]);
				valueMap.put("y_bias", values[4]);
				valueMap.put("z_bias", values[5]);
				
				object.getValues().add(valueMap);
				
				mSensorStream.getSensors().add(object);
			}else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
				
				SensorUnit object = new SensorUnit();
				object.setName("Sensor.TYPE_MAGNETIC_FIELD");
				object.setStatus(true);
				object.setTypeValue(Integer.toString(Sensor.TYPE_MAGNETIC_FIELD));
				object.setVendor(event.sensor.getVendor());
				
				Map<String, Float> valueMap = new HashMap<String, Float>();
				
				for(float value : event.values ){
					valueMap.put("value", value);
				}
				object.getValues().add(valueMap);
				
				mSensorStream.getSensors().add(object);
			}
			      
//		    if (mGravity != null && mGeomagnetic != null) {
//		      float R[] = new float[9];
//		      float I[] = new float[9];
//		      boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
//		      if (success) {
//		        float orientation[] = new float[3];
//		        SensorManager.getOrientation(R, orientation);
//		        azimut = orientation[0]; // orientation contains: azimut, pitch and roll
//		      }
//		    }
		}
	};
	
	private float[] mGravity;
	private float[] mGeomagnetic;
	private static final float NS2S = 1.0f / 1000000000.0f;
    private float timestamp;

	@Override
	public void onConnected(Bundle connectionHint) {
		Log.i(TAG, "onConnected");
		Log.i(TAG, "Location : Connection Status : Connected");
		
		// Request for location
					final Location loc =locationclient.getLastLocation();
					Log.i(TAG, "Last Known Location :" + loc.getLatitude() + "," + loc.getLongitude());
					
					//setLastKnowLocation(loc.getLatitude(),  loc.getLongitude());
					
					SensorUnit object = new SensorUnit();
					object.setName("Sensor.TYPE_LOCATION");
					object.setStatus(true);
					object.setTypeValue(Integer.toString(-111));
					object.setVendor("");
					
					Map<String, Float> valueMap = new HashMap<String, Float>();
					valueMap.put("latitude", (float) loc.getLatitude());
					valueMap.put("longitude", (float) loc.getLongitude());
					
					object.getValues().add(valueMap);
					
					mSensorStream.getSensors().add(object);
					
					
					Log.i(TAG, "Requesting new location :");
					new LongOperation().execute(loc);
		
	}

	@Override
	public void onDisconnected() {
		Log.i(TAG, "onDisconnected");
		Log.i(TAG, "Location : Connection Status : Disconnected");
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.i(TAG, "onConnectionFailed");
		Log.i(TAG, "Location : Connection Status : Fail");

	}

	@Override
	public void onLocationChanged(Location location) {
		if(location!=null){
			Log.i(TAG, "Location (Lat,Long) :" + location.getLatitude() + "," + location.getLongitude());
		}
		
	}
	
	 private class LongOperation extends AsyncTask<Location, Void, List<Address>> {

	        @Override
	        protected List<Address> doInBackground(Location... params) {
	        	
	        	Log.i(TAG, "doInBackground");
	        	
	        	List<Address> addr = getFromLocation(params[0].getLatitude(), params[0].getLongitude());
				
	            return addr;
	        }

	        @Override
	        protected void onPostExecute(List<Address> addr) {
	            // might want to change "executed" for the returned string passed
	            // into onPostExecute() but that is upto you
	        	
	        	if(addr != null){
	        		Log.d(TAG, addr.get(0).toString())	;
		        	/*Intent intent = new Intent(LocationActivity.this, DisplayActivity.class);
		        	intent.putExtra(DisplayActivity.KEY, addr.get(0).toString());
		        	startActivity(intent);*/
		        	
		        	String address = addr.get(0).toString();
					int startIndx = address.indexOf(":\"")+2;
					int endIndx = address.indexOf("],")-1;
					 
					Log.i(TAG, "Location Address: " + address.subSequence(startIndx, endIndx));	
	        	}
				
				//setAddres(address.subSequence(startIndx, endIndx).toString());
				
				//mTextView.setText("" + mSensorFeedDataJson);
				
//				SensorUnit object = new SensorUnit();
//				object.setName("Sensor.TYPE_LOCATION");
//				object.setStatus(true);
//				object.setTypeValue(Integer.toString(-111));
//				object.setVendor("");
//				
//				Map<String, Float> valueMap = new HashMap<String, Float>();
//				valueMap.put("latitude", (float) loc.getLatitude());
//				valueMap.put("longitude", (float) loc.getLongitude());
//				
//				object.getValues().add(valueMap);
//				
//				mSensorStream.getSensors().add(object);
				
				
				Gson gson = new Gson();
				String jsonString = gson.toJson(mSensorStream);
				
				Log.i(TAG, "json: " + jsonString);
				
				mTextView.setText(jsonString);
				
				FeedResource.getInstance().openDataSource();
				FeedResource.getInstance().addRecentFeed(0, 
						FeedResource.getInstance().createRecentFeed("Synced", jsonString));
				
				closeActivity();
	        }

	        @Override
	        protected void onPreExecute() {}

	        @Override
	        protected void onProgressUpdate(Void... values) {}
	 }
	 
	 private void closeActivity(){
		 Toast.makeText(this, getResources().getString(R.string.sync_complete_message), 
				 Toast.LENGTH_SHORT).show();
		 this.mSyncDialog.cancel();
		 this.finish();
	 }
	 
	 public List<Address> getFromLocation(double lat, double lng){
			
			String address = String.format(Locale.ENGLISH,"http://maps.googleapis.com/maps/api/geocode/json?latlng=%1$f,%2$f&sensor=true&language="+Locale.ENGLISH, lat, lng);
			HttpGet httpGet = new HttpGet(address);
			HttpClient client = new DefaultHttpClient();
			HttpResponse response;
			StringBuilder stringBuilder = new StringBuilder();

			List<Address> retList = null;
			
			try {
				response = client.execute(httpGet);
				HttpEntity entity = response.getEntity();
				InputStream stream = entity.getContent();
				int b;
				while ((b = stream.read()) != -1) {
					stringBuilder.append((char) b);
				}
				
				JSONObject jsonObject = new JSONObject();
				jsonObject = new JSONObject(stringBuilder.toString());
				
				
				retList = new ArrayList<Address>();
				
				
				if("OK".equalsIgnoreCase(jsonObject.getString("status"))){
					JSONArray results = jsonObject.getJSONArray("results");
					for (int i=0;i<results.length();i++ ) {
						JSONObject result = results.getJSONObject(i);
						String indiStr = result.getString("formatted_address");
						Address addr = new Address(Locale.ENGLISH);
						addr.setAddressLine(0, indiStr);
						retList.add(addr);
					}
				}
				
				
			} catch (ClientProtocolException e) {
				Log.e(TAG, "Error calling Google geocode webservice.", e);
			} catch (IOException e) {
				Log.e(TAG, "Error calling Google geocode webservice.", e);
			} catch (JSONException e) {
				Log.e(TAG, "Error parsing Google geocode webservice response.", e);
			}
			
			return retList;
		}
}
