package com.univ.helsinki.app.activities;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.univ.helsinki.app.R;
import com.univ.helsinki.app.util.LocationService;

public class LocationActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,GooglePlayServicesClient.OnConnectionFailedListener,LocationListener {

	private final static String TAG = LocationActivity.class.getSimpleName();
	
	private TextView txtConnectionStatus;
	private TextView txtLastKnownLoc;
	private EditText etLocationInterval;
	private TextView txtLocationRequest;
	
	private LocationClient locationclient;
	private LocationRequest locationrequest;
	private Intent mIntentService;
	private PendingIntent mPendingIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor_location);
		txtConnectionStatus = (TextView) findViewById(R.id.txtConnectionStatus);
		txtLastKnownLoc = (TextView) findViewById(R.id.txtLastKnownLoc);
		etLocationInterval = (EditText) findViewById(R.id.etLocationInterval);
		txtLocationRequest = (TextView) findViewById(R.id.txtLocationRequest);
		
		mIntentService = new Intent(this,LocationService.class);
		mPendingIntent = PendingIntent.getService(this, 1, mIntentService, 0);
		
		int resp =GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if(resp == ConnectionResult.SUCCESS){
			locationclient = new LocationClient(this,this,this);
			locationclient.connect();
		}
		else{
			Toast.makeText(this, "Google Play Service Error " + resp, Toast.LENGTH_LONG).show();
			
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
        	Log.d(TAG, addr.get(0).toString())	;
        	/*Intent intent = new Intent(LocationActivity.this, DisplayActivity.class);
        	intent.putExtra(DisplayActivity.KEY, addr.get(0).toString());
        	startActivity(intent);*/
        	
        	String address = addr.get(0).toString();
			int startIndx = address.indexOf(":\"")+2;
			int endIndx = address.indexOf("],")-1;
			 
			txtLocationRequest.setText(address.subSequence(startIndx, endIndx));
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
	
	 
	
	public void buttonClicked(View v){
		if(v.getId() == R.id.btnLastLoc){
			if(locationclient!=null && locationclient.isConnected()){
				final Location loc =locationclient.getLastLocation();
				Log.i(TAG, "Last Known Location :" + loc.getLatitude() + "," + loc.getLongitude());
				txtLastKnownLoc.setText(loc.getLatitude() + "," + loc.getLongitude());	
				
				Log.i(TAG, "doInBackground");
				new LongOperation().execute(loc);
			}
		}
		if(v.getId() == R.id.btnStartRequest){
			if(locationclient!=null && locationclient.isConnected()){
				
				if(((Button)v).getText().equals("Start")){
					locationrequest = LocationRequest.create();
					locationrequest.setInterval(Long.parseLong(etLocationInterval.getText().toString()));
					locationclient.requestLocationUpdates(locationrequest, this);
					((Button) v).setText("Stop");	
				}
				else{
					locationclient.removeLocationUpdates(this);
					((Button) v).setText("Start");
				}
				
			}
		}
		if(v.getId() == R.id.btnRequestLocationIntent){
			if(((Button)v).getText().equals("Start")){
				
				locationrequest = LocationRequest.create();
				locationrequest.setInterval(100);
				locationclient.requestLocationUpdates(locationrequest, mPendingIntent);
				
				((Button) v).setText("Stop");
			}
			else{
				locationclient.removeLocationUpdates(mPendingIntent);
				((Button) v).setText("Start");
			}
			
			
			
			
			
		}
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(locationclient!=null)
			locationclient.disconnect();
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		Log.i(TAG, "onConnected");
		txtConnectionStatus.setText("Connection Status : Connected");
		
	}

	@Override
	public void onDisconnected() {
		Log.i(TAG, "onDisconnected");
		txtConnectionStatus.setText("Connection Status : Disconnected");
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.i(TAG, "onConnectionFailed");
		txtConnectionStatus.setText("Connection Status : Fail");

	}

	@Override
	public void onLocationChanged(Location location) {
		if(location!=null){
			Log.i(TAG, "Location Request :" + location.getLatitude() + "," + location.getLongitude());
			txtLocationRequest.setText(location.getLatitude() + "," + location.getLongitude());
		}
		
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
