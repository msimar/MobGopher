package com.univ.helsinki.app.db;

import android.provider.BaseColumns;

public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "SyncSensorFeed";
        
        public static final String COLUMN_NAME_TITLE = "title";
		public static final String COLUMN_NAME_CONTENT = "content";
		public static final String COLUMN_NAME_UPDATED = "updated";
		public static final String COLUMN_NAME_NULLABLE = null;
    }
    
    public static abstract class FeedEndPointEntry implements BaseColumns {
        public static final String TABLE_NAME = "FeedEndPoint";
        
        public static final String COLUMN_NAME_TITLE = "title";
		public static final String COLUMN_NAME_SERVER_NAME = "server_name";
		public static final String COLUMN_NAME_SERVER_URL = "server_url";
		
		public static final String COLUMN_NAME_CONN = "connection";
		public static final String COLUMN_NAME_SYNC_FREQUENCY = "sync_frequency";
		public static final String COLUMN_NAME_REPEAT = "repeat";
		public static final String COLUMN_NAME_LOGGING = "logging";
		public static final String COLUMN_NAME_ISACTIVE = "isActive";
		
		public static final String COLUMN_NAME_SENSOR_BUNDLE = "sensor_bundle";
		
		public static final String COLUMN_NAME_UPDATED = "updated";
		public static final String COLUMN_NAME_NULLABLE = null;
    }
}