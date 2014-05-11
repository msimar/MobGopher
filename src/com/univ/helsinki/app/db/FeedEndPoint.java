package com.univ.helsinki.app.db;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FeedEndPoint {
	
	private long id;
	
	private String feedTitle;
	private String feedServerName;
	private String feedServerUrl;
	
	private int connectionPreference;
	private int syncFrequency;
	
	private boolean repeatMode;
	private boolean feedLogging;
	private boolean feedEnable;
	
	private String sensorBundle;
	
	private long updated;
	
	private Map<String,Boolean> feedSensorMap = new HashMap<String, Boolean>();
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return 	"Feed End Point Detail :" + "\n"
				+ 	"Feed title : " 	+ feedTitle + "\n"
				+ 	"Server name : " 	+ feedServerName + "\n"
				+ 	"Server url : " 	+ feedServerUrl + "\n"
				
				+ 	"Feed Conn : " 		+ connectionPreference + "\n"
				+ 	"Sync Freq : " 		+ syncFrequency + "\n"
				
				+ 	"Repeat Mode : " 	+ repeatMode + "\n"
				+ 	"Logging : " 		+ feedLogging + "\n"
				+ 	"Is Enabled : " 	+ feedEnable + "\n"
				
				+ 	"Sensors : " 	+ sensorBundle + "\n"
			 	+ 	"Sensors Map: " 	+ feedSensorMap + "\n"
			 	+ 	"Updated: " 	+ updated + "\n";
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFeedTitle() {
		return feedTitle;
	}

	public void setFeedTitle(String feedTitle) {
		this.feedTitle = feedTitle;
	}

	public String getFeedServerName() {
		return feedServerName;
	}

	public void setFeedServerName(String feedServerName) {
		this.feedServerName = feedServerName;
	}

	public String getFeedServerUrl() {
		return feedServerUrl;
	}

	public void setFeedServerUrl(String feedServerUrl) {
		this.feedServerUrl = feedServerUrl;
	}

	public int getConnectionPreference() {
		return connectionPreference;
	}

	public void setConnectionPreference(int connectionPreference) {
		this.connectionPreference = connectionPreference;
	}

	public int getSyncFrequency() {
		return syncFrequency;
	}

	public void setSyncFrequency(int syncFrequency) {
		this.syncFrequency = syncFrequency;
	}

	public boolean isRepeatMode() {
		return repeatMode;
	}

	public void setRepeatMode(boolean repeatMode) {
		this.repeatMode = repeatMode;
	}

	public boolean isFeedLogging() {
		return feedLogging;
	}

	public void setFeedLogging(boolean feedLogging) {
		this.feedLogging = feedLogging;
	}

	public boolean isFeedEnable() {
		return feedEnable;
	}

	public void setFeedEnable(boolean feedEnable) {
		this.feedEnable = feedEnable;
	}

	public String getSensorBundle() {
		return sensorBundle;
	}

	public void setSensorBundle(String sensorBundle) {
		this.sensorBundle = sensorBundle;
	}

	public Map<String,Boolean> getFeedSensorMap() {
		return feedSensorMap;
	}

	public void setFeedSensorMap(Map<String,Boolean> feedSensorMap) {
		this.feedSensorMap = feedSensorMap;
	}
	
	public long getUpdated() {
		return updated;
	}
	
	public String getUpdatedTimestamp() {
		return getDate(updated, "dd MMM, yyyy HH:mm:ss");
	}

	public void setUpdated(long updated) {
		this.updated = updated;
	}
	
	/**
	 * Return date in specified format.
	 * @param milliSeconds Date in milliseconds
	 * @param dateFormat Date format 
	 * @return String representing date in specified format
	 */
	public static String getDate(long milliSeconds, String dateFormat)
	{
	    // Create a DateFormatter object for displaying date in specified format.
	    DateFormat formatter = new SimpleDateFormat(dateFormat);

	    // Create a calendar object that will convert the date and time value in milliseconds to date. 
	     Calendar calendar = Calendar.getInstance();
	     calendar.setTimeInMillis(milliSeconds);
	     return formatter.format(calendar.getTime());
	}
}
