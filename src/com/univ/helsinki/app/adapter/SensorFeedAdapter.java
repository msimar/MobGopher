package com.univ.helsinki.app.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.univ.helsinki.app.R;
import com.univ.helsinki.app.core.SensorFeed;
import com.univ.helsinki.app.db.FeedResource;

public class SensorFeedAdapter extends BaseAdapter {

	private List<SensorFeed> mFeedList;
	private LayoutInflater mInflater;
	
	public SensorFeedAdapter(Context context) {
		this.mFeedList = FeedResource.getInstance().getSensorFeedList();

		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setFeedList(List<SensorFeed> feedList){
		this.mFeedList = feedList;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFeedList.size();
	}

	@Override
	public SensorFeed getItem(int position) {
		// TODO Auto-generated method stub
		return mFeedList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.listview_feed_item,
					parent, false);
			
			holder = new ViewHolder();
			
	        holder.tvTitle = (TextView) convertView.findViewById(R.id.title);
	        holder.tvSubtitle = (TextView) convertView.findViewById(R.id.subtitle);
	        holder.tvContent = (TextView) convertView.findViewById(R.id.content);

	        convertView.setTag(holder);
	    } else {
	        holder = (ViewHolder) convertView.getTag();
	    }

		holder.tvTitle.setText(mFeedList.get(position).getName());
		holder.tvSubtitle.setText(mFeedList.get(position).getVendor());
		holder.tvContent.setText(mFeedList.get(position).getTypeName());
		
		return convertView;
	}

	private static class ViewHolder {
	    public TextView tvTitle;
	    public TextView tvContent;
	    public TextView tvSubtitle;
	}
}
