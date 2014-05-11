package com.univ.helsinki.app.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.univ.helsinki.app.db.FeedReaderContract.FeedEntry;

public class DataSourceRecentFeed {

	private static final String TAG = DataSourceRecentFeed.class
			.getSimpleName();

	private SQLiteDatabase database;

	private DatabaseHelper mDbHelper;

	private String[] allColumns = { FeedEntry._ID, FeedEntry.COLUMN_NAME_TITLE,
			FeedEntry.COLUMN_NAME_CONTENT, FeedEntry.COLUMN_NAME_UPDATED };

	public DataSourceRecentFeed(DatabaseHelper dbHelper) {
		this.mDbHelper = dbHelper;
	}

	public void open() throws SQLException {
		database = mDbHelper.getWritableDatabase();
	}

	public void close() {
		mDbHelper.close();
	}

	public Feed createRecentFeed(String title, String content) {

		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_NAME_TITLE, title);
		values.put(FeedEntry.COLUMN_NAME_CONTENT, content);
		values.put(FeedEntry.COLUMN_NAME_UPDATED, System.currentTimeMillis());

		// Insert the new row, returning the primary key value of the new row
		long insertRowId = database.insert(FeedEntry.TABLE_NAME,
				FeedEntry.COLUMN_NAME_NULLABLE, values);

		if (insertRowId == -1 ? false : true) {
			Cursor cursor = database
					.query(FeedEntry.TABLE_NAME, allColumns, FeedEntry._ID
							+ " = " + insertRowId, null, null, null, null);

			if (cursor != null) {

				cursor.moveToFirst();
				Feed feed = cursorToFeed(cursor);
				cursor.close();

				return feed;
			}
		}
		return null;
	}

	public List<Feed> getAllRecentFeed() {
		List<Feed> feeds = new ArrayList<Feed>();

		Cursor cursor = database.query(FeedEntry.TABLE_NAME, allColumns, null,
				null, null, null, FeedEntry.COLUMN_NAME_UPDATED + " DESC");
		
		if(cursor == null)return null;

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Feed feed = cursorToFeed(cursor);
			feeds.add(feed);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return feeds;
	}

	private Feed cursorToFeed(Cursor cursor) {
		Feed feed = new Feed();

		feed.setId(cursor.getLong(cursor
				.getColumnIndexOrThrow(FeedEntry._ID)));
		feed.setTitle(cursor.getString(cursor
				.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_TITLE)));
		feed.setContent(cursor.getString(cursor
				.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_CONTENT)));
		feed.setUpdated(cursor.getLong(cursor
				.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_UPDATED)));

		return feed;
	}
	
	public void delete(long id){
        String string =String.valueOf(id);
        database.execSQL("DELETE FROM " + FeedEntry.TABLE_NAME + " WHERE " + FeedEntry._ID + "= '" + string + "'");
    }
}
