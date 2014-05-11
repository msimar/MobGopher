package com.univ.helsinki.app.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.univ.helsinki.app.db.FeedReaderContract.FeedEndPointEntry;

public class DataSourceFeedEndPoint {

	private final String TAG = DataSourceFeedEndPoint.class.getSimpleName();

	private SQLiteDatabase database;

	private DatabaseHelper mDbHelper;
	
	private final String SENSOR_DELIMITER = ";";
	
	private String[] allColumns = { 
			FeedEndPointEntry._ID, 
			
			FeedEndPointEntry.COLUMN_NAME_TITLE,
			FeedEndPointEntry.COLUMN_NAME_SERVER_NAME, 
			FeedEndPointEntry.COLUMN_NAME_SERVER_URL,
			
			FeedEndPointEntry.COLUMN_NAME_CONN,
			FeedEndPointEntry.COLUMN_NAME_SYNC_FREQUENCY, 
			
			FeedEndPointEntry.COLUMN_NAME_REPEAT,
			FeedEndPointEntry.COLUMN_NAME_LOGGING, 
			FeedEndPointEntry.COLUMN_NAME_ISACTIVE,
			
			FeedEndPointEntry.COLUMN_NAME_SENSOR_BUNDLE,
			
			FeedEndPointEntry.COLUMN_NAME_UPDATED };

	public DataSourceFeedEndPoint(DatabaseHelper dbHelper) {
		this.mDbHelper = dbHelper;
	}

	public void open() throws SQLException {
		database = mDbHelper.getWritableDatabase();
	}

	public void close() {
		mDbHelper.close();
	}

	public FeedEndPoint createFeedEndPoint(FeedEndPoint feedEndPoint){
		Log.i(TAG, "createFeedEndPoint()");
		
		open();

		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		
		values.put(FeedEndPointEntry.COLUMN_NAME_TITLE, feedEndPoint.getFeedTitle());
		values.put(FeedEndPointEntry.COLUMN_NAME_SERVER_NAME, feedEndPoint.getFeedServerName());
		values.put(FeedEndPointEntry.COLUMN_NAME_SERVER_URL, feedEndPoint.getFeedServerUrl());
		
		values.put(FeedEndPointEntry.COLUMN_NAME_CONN, feedEndPoint.getConnectionPreference());
		values.put(FeedEndPointEntry.COLUMN_NAME_SYNC_FREQUENCY, feedEndPoint.getSyncFrequency());
		
		values.put(FeedEndPointEntry.COLUMN_NAME_REPEAT, feedEndPoint.isRepeatMode());
		values.put(FeedEndPointEntry.COLUMN_NAME_LOGGING, feedEndPoint.isFeedLogging());
		values.put(FeedEndPointEntry.COLUMN_NAME_ISACTIVE, feedEndPoint.isFeedEnable());
		
		Map<String,Boolean> sensorMap = feedEndPoint.getFeedSensorMap();
		Iterator<Entry<String, Boolean>> mapIter = sensorMap.entrySet().iterator();
		StringBuilder sensorBuffer = new StringBuilder();
		while(mapIter.hasNext()){
			Entry<String, Boolean> eSet = mapIter.next();
			if(eSet.getValue()){
				sensorBuffer.append(SENSOR_DELIMITER).append(eSet.getKey());
			}
		}
		values.put(FeedEndPointEntry.COLUMN_NAME_SENSOR_BUNDLE, sensorBuffer.toString());
		
		values.put(FeedEndPointEntry.COLUMN_NAME_UPDATED, System.currentTimeMillis());

		// Insert the new row, returning the primary key value of the new row
		long insertRowId = database.insert(FeedEndPointEntry.TABLE_NAME,
				FeedEndPointEntry.COLUMN_NAME_NULLABLE, values);
		
		Log.i(TAG,"FeedEndPoint inserted row id : DB : " + insertRowId);

		if (insertRowId == -1 ? false : true) {
			Cursor cursor = database
					.query(FeedEndPointEntry.TABLE_NAME, 
							allColumns, 
							FeedEndPointEntry._ID + " = " + insertRowId, 
							null, null, null, null);

			if (cursor != null) {
				Log.i(TAG,"FeedEndPoint inserted to DB.");
			}
		}
		return null;
	}

	public List<FeedEndPoint> getAllFeedEndPoint() {
		Log.i(TAG, "getAllFeedEndPoint()");
		open();
		
		List<FeedEndPoint> feedEndPointList = new ArrayList<FeedEndPoint>();

		Cursor cursor = database.query(
				FeedEndPointEntry.TABLE_NAME,
				allColumns, 
				null, null, null, null, 
				FeedEndPointEntry.COLUMN_NAME_UPDATED + " DESC");
		
		if(cursor == null) return null;

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			FeedEndPoint feedEndPoint = cursorToFeed(cursor);
			feedEndPointList.add(feedEndPoint);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return feedEndPointList;
	}
	
	private FeedEndPoint cursorToFeed(Cursor cursor) {
		FeedEndPoint feedEndPoint = new FeedEndPoint();
		
		feedEndPoint.setId(cursor.getLong(cursor.getColumnIndexOrThrow(FeedEndPointEntry._ID)));
		
		feedEndPoint.setFeedTitle(cursor.getString(cursor.getColumnIndexOrThrow(FeedEndPointEntry.COLUMN_NAME_TITLE)));
		feedEndPoint.setFeedServerName(cursor.getString(cursor.getColumnIndexOrThrow(FeedEndPointEntry.COLUMN_NAME_SERVER_NAME)));
		feedEndPoint.setFeedServerUrl(cursor.getString(cursor.getColumnIndexOrThrow(FeedEndPointEntry.COLUMN_NAME_SERVER_URL)));
		
		feedEndPoint.setConnectionPreference(cursor.getInt(cursor.getColumnIndexOrThrow(FeedEndPointEntry.COLUMN_NAME_CONN)));
		feedEndPoint.setSyncFrequency(cursor.getInt(cursor.getColumnIndexOrThrow(FeedEndPointEntry.COLUMN_NAME_SYNC_FREQUENCY)));
		
		feedEndPoint.setRepeatMode(Boolean.parseBoolean(
				cursor.getString(cursor.getColumnIndexOrThrow(FeedEndPointEntry.COLUMN_NAME_REPEAT))
				));
		feedEndPoint.setFeedLogging(Boolean.parseBoolean(
				cursor.getString(cursor.getColumnIndexOrThrow(FeedEndPointEntry.COLUMN_NAME_LOGGING))
				));
		feedEndPoint.setFeedEnable(Boolean.parseBoolean(
				cursor.getString(cursor.getColumnIndexOrThrow(FeedEndPointEntry.COLUMN_NAME_ISACTIVE))
				));
		
		feedEndPoint.setSensorBundle(cursor.getString(cursor.getColumnIndexOrThrow(FeedEndPointEntry.COLUMN_NAME_SENSOR_BUNDLE)));
		
		if( feedEndPoint.getSensorBundle().isEmpty()){
			String[] sensorToken = feedEndPoint.getSensorBundle().split(SENSOR_DELIMITER);
			for(int indx = 1; indx < sensorToken.length ; indx++ ){
				feedEndPoint.getFeedSensorMap().put(sensorToken[1],true);
			}
		}
		
		feedEndPoint.setUpdated(cursor.getLong(cursor.getColumnIndexOrThrow(FeedEndPointEntry.COLUMN_NAME_UPDATED)));

		return feedEndPoint;
	}
	
	public void delete(long id){
		Log.i(TAG, "delete()");
		
        String string =String.valueOf(id);
        database.execSQL("DELETE FROM " + 
        				FeedEndPointEntry.TABLE_NAME + " WHERE " + 
        				FeedEndPointEntry._ID + "= '" + string + "'");
    }
}
