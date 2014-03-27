package com.mob.location;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayActivity extends Activity {
	
	public static final String KEY = "location";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TextView view = new TextView(this);
		
		view.setTextSize((float) 28.0);
		
		Bundle extras = getIntent().getExtras();
		if( extras != null){
			
			String address = extras.getString(KEY);
			int startIndx = address.indexOf(":\"")+2;
			int endIndx = address.indexOf("],")-1;
			 
			view.setText(address.subSequence(startIndx, endIndx));
			//view.setText(address);
		}
		
		
		
		setContentView(view);
	}
}
