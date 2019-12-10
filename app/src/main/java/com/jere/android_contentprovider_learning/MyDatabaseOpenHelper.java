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
    public static final String TABLE_USER = "user";
    public static final String TABLE_SCORE = "score";

    //user table columns
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

    //score table columns
    public static final String SCORE_ID = "_id";
    public static final String SCORE_STUDENT_NAME = "name";
    public static final String SCORE_MATH = "math";
    public static final String SCORE_CHINESE = "chinese";
    public static final String SCORE_ENGLISH = "english";
    public static final String SCORE_SCIENCE = "science";

    public static final String[] SCORE_ALL_COLUMNS = new String[] {
            SCORE_ID,
            SCORE_STUDENT_NAME,
            SCORE_MATH,
            SCORE_CHINESE,
            SCORE_ENGLISH,
            SCORE_SCIENCE
    };

    //create user table sql
    private static final String CREATE_USER_TABLE = "create table " + TABLE_USER + "(" + USER_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USER_NAME + " TEXT, " + USER_AGE + " INT, "
            + USER_HEIGHT + " INT, " + USER_ADDRESS + " TEXT);";

    //create work table sq;
    private static final String CREATE_SCORE_TABLE = "create table " + TABLE_SCORE + "("
            + SCORE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SCORE_STUDENT_NAME + " TEXT, " + SCORE_MATH + " INT, " + SCORE_CHINESE
            + " INT, " + SCORE_ENGLISH + " INT, " + SCORE_SCIENCE + " INT);";

    public MyDatabaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_SCORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORE);
        onCreate(db);
    }
}
