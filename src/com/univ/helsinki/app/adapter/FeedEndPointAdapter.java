package com.univ.helsinki.app.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.univ.helsinki.app.R;
import com.univ.helsinki.app.db.FeedEndPoint;
import com.univ.helsinki.app.db.FeedResource;

public class FeedEndPointAdapter extends BaseAdapter {
	
	private final String TAG = FeedEndPointAdapter.class.getSimpleName();

	private List<FeedEndPoint> mFeedEndPointList;
	private LayoutInflater mInflater;
	
	private String[] mConnectionModeArray;
	private String[] mSyncFrequencyArray;

	public FeedEndPointAdapter(Context context) {
		
		this.mFeedEndPointList = FeedResource.getInstance().getAllFeedEndPoint();

		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		this.mConnectionModeArray = context.getResources().getStringArray(R.array.syncConnectionMode);
		this.mSyncFrequencyArray = context.getResources().getStringArray(R.array.syncFrequency);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.mFeedEndPointList.size();
	}

	@Override
	public FeedEndPoint getItem(int position) {
		// TODO Auto-generated method stub
		return this.mFeedEndPointList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return this.mFeedEndPointList.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.listview_feed_endpoint_item,
					parent, false);
			
			holder = new ViewHolder();
			
	        holder.tvTitle = (TextView) convertView.findViewById(R.id.title);
	        holder.tvContent = (TextView) convertView.findViewById(R.id.content);
	        holder.tvTimestamp = (TextView) convertView.findViewById(R.id.timestamp);

	        convertView.setTag(holder);
	    } else {
	        holder = (ViewHolder) convertView.getTag();
	    }

		
		int connPref = this.mFeedEndPointList.get(position).getConnectionPreference();
		String connPrefValue = mConnectionModeArray[connPref];
		
		int syncFreqPref = this.mFeedEndPointList.get(position).getSyncFrequency();
		String syncFreqPrefValue = mSyncFrequencyArray[syncFreqPref];
		
		String[] tokens = this.mFeedEndPointList.get(position).getSensorBundle().split(";");
		StringBuffer sensorBuffer = new StringBuffer();
		for(int indx = 1 ; indx < tokens.length ; indx++ ){
			if( FeedResource.sAllSensorKeyNameMap.get(tokens[indx]) != null);
				sensorBuffer.append(FeedResource.sAllSensorKeyNameMap.get(tokens[indx])).append(", ");
		}
		
		holder.tvTitle.setText(this.mFeedEndPointList.get(position).getFeedTitle());
		
		StringBuffer contentBuffer = new StringBuffer();
		
		contentBuffer.append("Server Name: ").append(this.mFeedEndPointList.get(position).getFeedServerName());
		contentBuffer.append(" \n");
		contentBuffer.append("URL: ").append(this.mFeedEndPointList.get(position).getFeedServerUrl());
		contentBuffer.append(" \n");
		contentBuffer.append("Connection: ").append(connPrefValue);
		contentBuffer.append(" \n");
		contentBuffer.append("Sync Frequency: ").append(syncFreqPrefValue);
		contentBuffer.append(" \n");
		if( !sensorBuffer.toString().isEmpty()){
			contentBuffer.append("Selected Sensors: ").append(sensorBuffer);
			contentBuffer.append(" \n");
		}
		
		holder.tvContent.setText(contentBuffer.toString());
		holder.tvTimestamp.setText(this.mFeedEndPointList.get(position).getUpdatedTimestamp());
		
		
		
		Log.i(TAG, "feed : " + this.mFeedEndPointList.get(position) );
		
		return convertView;
	}

	private static class ViewHolder {
	    public TextView tvTitle;
	    public TextView tvContent;
	    public TextView tvTimestamp;
	}
	
	public void notifyDataChanged(){
		this.mFeedEndPointList = FeedResource.getInstance().getAllFeedEndPoint();
		this.notifyDataSetChanged();
	}
}