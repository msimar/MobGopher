package com.univ.helsinki.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.univ.helsinki.app.db.FeedReaderContract.FeedEndPointEntry;
import com.univ.helsinki.app.db.FeedReaderContract.FeedEntry;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final String TAG = DatabaseHelper.class.getSimpleName();
	
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";
    
    private static final String TIMESTAMP 		= " TIMESTAMP";
    private static final String TEXT_TYPE 		= " TEXT";
    private static final String INTEGER_TYPE 	= " INTEGER";
    
    private static final String COMMA_SEP = ",";
    
    private static final String SQL_CREATE_FEED_ENTRIES =
        "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
        FeedEntry._ID 								+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
        FeedEntry.COLUMN_NAME_TITLE 				+ TEXT_TYPE + COMMA_SEP +
        FeedEntry.COLUMN_NAME_CONTENT 				+ TEXT_TYPE + COMMA_SEP +
        FeedEntry.COLUMN_NAME_UPDATED 				+ TIMESTAMP +
        " )";
    
    private static final String SQL_CREATE_FEED_ENDPOINT_ENTRIES =
            "CREATE TABLE " + FeedEndPointEntry.TABLE_NAME + " (" +
            FeedEndPointEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            		
            FeedEndPointEntry.COLUMN_NAME_TITLE 			+ TEXT_TYPE + COMMA_SEP +
            FeedEndPointEntry.COLUMN_NAME_SERVER_NAME 		+ TEXT_TYPE + COMMA_SEP +
            FeedEndPointEntry.COLUMN_NAME_SERVER_URL 		+ TEXT_TYPE + COMMA_SEP +
            
            FeedEndPointEntry.COLUMN_NAME_CONN 				+ INTEGER_TYPE + COMMA_SEP +
            FeedEndPointEntry.COLUMN_NAME_SYNC_FREQUENCY 	+ INTEGER_TYPE + COMMA_SEP +
            
            FeedEndPointEntry.COLUMN_NAME_REPEAT 			+ TEXT_TYPE + COMMA_SEP +
            FeedEndPointEntry.COLUMN_NAME_LOGGING 			+ TEXT_TYPE + COMMA_SEP +
            FeedEndPointEntry.COLUMN_NAME_ISACTIVE 			+ TEXT_TYPE + COMMA_SEP +
            
            FeedEndPointEntry.COLUMN_NAME_SENSOR_BUNDLE 	+ TEXT_TYPE + COMMA_SEP +
            
            FeedEndPointEntry.COLUMN_NAME_UPDATED 			+ TIMESTAMP +
            " )";

    private static final String SQL_DELETE_FEED_ENTRIES = "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
    
    private static final String SQL_DELETE_FEED_ENDPOINT_ENTRIES = "DROP TABLE IF EXISTS " + FeedEndPointEntry.TABLE_NAME;
    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
    	Log.i(TAG, SQL_CREATE_FEED_ENTRIES);
        db.execSQL(SQL_CREATE_FEED_ENTRIES);
        
        Log.i(TAG, SQL_CREATE_FEED_ENDPOINT_ENTRIES);
        db.execSQL(SQL_CREATE_FEED_ENDPOINT_ENTRIES);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_FEED_ENTRIES);
        db.execSQL(SQL_DELETE_FEED_ENDPOINT_ENTRIES);        
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}