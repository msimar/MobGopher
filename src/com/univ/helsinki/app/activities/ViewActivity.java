package com.univ.helsinki.app.activities;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.univ.helsinki.app.R;
import com.univ.helsinki.app.db.Feed;
import com.univ.helsinki.app.db.FeedResource;

public class ViewActivity extends Activity {
	
	public static final String EXTRAS_ROW_ID = "row_id";

	private TextView tvTitle;
	private TextView tvContent;
	private TextView tvTimestamp;
	
	private int mListItemIndex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_view);
		
		tvTitle = (TextView) findViewById(R.id.title);
		tvContent = (TextView) findViewById(R.id.content);
		tvTimestamp = (TextView) findViewById(R.id.timestamp);
		
		Bundle extras = getIntent().getExtras();

		if (extras == null) {
			finish();
		} else {
			mListItemIndex = extras.getInt(EXTRAS_ROW_ID);
			
//			Toast.makeText(this, "index: " + mListItemIndex, Toast.LENGTH_SHORT).show();

			FeedResource.getInstance().inti(this);
			List<Feed> mFeedList = FeedResource.getInstance().getAllFeed();
			{
				tvTitle.setText(mFeedList.get(mListItemIndex).getTitle());
				
				String formattedContent = formatter(mFeedList.get(mListItemIndex).getContent());
				
				tvContent.setText(formattedContent);
				
				tvTimestamp.setText( "Synced on " + mFeedList.get(mListItemIndex).getUpdatedTimestamp());
			}
		}
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	private String formatter(String input){
		StringBuffer buffer = new StringBuffer(input);
		
		for(int index= 0; index< buffer.length(); index++){
			
			if( buffer.charAt(index) == '}' && (index+1 < buffer.length() ) && buffer.charAt(index+1) == ','){
				buffer.insert(index + 2, " \n ");
				index = index + 2;
			}else if( buffer.charAt(index) == '[' && (index+1 < buffer.length() ) &&   buffer.charAt(index+1) == '{'){
				buffer.insert(index + 2, " \n ");
				index = index + 2;
			}else if( buffer.charAt(index) == ']' && (index+1 < buffer.length() ) &&   buffer.charAt(index+1) == ','){
				buffer.insert(index + 2, " \n ");
				index = index + 2;
			}else if( buffer.charAt(index) == '{' 
					||  buffer.charAt(index) == '}'
					||  buffer.charAt(index) == '['
					||  buffer.charAt(index) == ']'
					||  buffer.charAt(index) == ','){
				buffer.insert(index + 1, " \n ");
				index = index + 1;
			}
		}
		
		return buffer.toString();
	}
	
	@Override
	protected void onResume() {
		FeedResource.getInstance().openDataSource();
		super.onResume();
	}

	@Override
	protected void onPause() {
		FeedResource.getInstance().closeDataSource();
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		FeedResource.getInstance().destory();
		super.onDestroy();
	}
	 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
}
