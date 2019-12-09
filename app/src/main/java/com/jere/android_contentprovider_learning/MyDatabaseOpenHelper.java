package com.jere.android_contentprovider_learning;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author jere
 */
public class MyDatabaseOpenHelper extends SQLiteOpenHelper {
    //database name
    private static final String DB_NAME =  "jereTest.db";
    //database version
    private static final int DB_VERSION = 1;

    //table name
    public static final String TABLE_NAME_USER = "user";

    //table columns
    public static final String USER_ID = "_id";
    public static final String USER_NAME = "name";
    public static final String USER_AGE = "age";
    public static final String USER_HEIGHT = "height";
    public static final String USER_ADDRESS = "address";

    public static final String[] USER_ALL_COLUMNS = new String[] {
            USER_ID,
            USER_NAME,
            USER_AGE,
            USER_HEIGHT,
            USER_ADDRESS
    };

    //create table sql
    private static final String CREATE_USER_TABLE = "create table " + TABLE_NAME_USER + "(" + USER_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USER_NAME + " TEXT, " + USER_AGE + " INT, "
            + USER_HEIGHT + " INT, " + USER_ADDRESS + " TEXT);";

    public MyDatabaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USER);
        onCreate(db);
    }
}
