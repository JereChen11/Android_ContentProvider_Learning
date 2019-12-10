package com.jere.android_contentprovider_learning;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author jere
 */
public class MyContentProvider extends ContentProvider {
    private static final String AUTHORITY = "com.jere.android_contentprovider_learning";
    public static final Uri USER_TABLE_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + MyDatabaseOpenHelper.TABLE_USER);
    public static final Uri SCORE_TABLE_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + MyDatabaseOpenHelper.TABLE_SCORE);

    public static final int USER_TABLE_CODE = 1;
    public static final int SCORE_TABLE_CODE = 2;

    private static final UriMatcher uriMatch = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatch.addURI(AUTHORITY, MyDatabaseOpenHelper.TABLE_USER, USER_TABLE_CODE);
        uriMatch.addURI(AUTHORITY, MyDatabaseOpenHelper.TABLE_SCORE, SCORE_TABLE_CODE);
    }

    private SQLiteDatabase sqLiteDatabase;

    /**
     * 创建 ContentProvider
     *
     * @return 如果成功加载provider会返回true, 否则返回false
     */
    @Override
    public boolean onCreate() {
        MyDatabaseOpenHelper myDatabaseOpenHelper = new MyDatabaseOpenHelper(getContext());
        sqLiteDatabase = myDatabaseOpenHelper.getWritableDatabase();
        return sqLiteDatabase != null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        switch (uriMatch.match(uri)) {
            case USER_TABLE_CODE:
                cursor = sqLiteDatabase.query(MyDatabaseOpenHelper.TABLE_USER,
                        MyDatabaseOpenHelper.USER_ALL_COLUMNS,
                        selection,
                        selectionArgs,
                        sortOrder,
                        null, null);
                break;
            case SCORE_TABLE_CODE:
                cursor = sqLiteDatabase.query(MyDatabaseOpenHelper.TABLE_SCORE,
                        MyDatabaseOpenHelper.SCORE_ALL_COLUMNS,
                        selection,
                        selectionArgs,
                        sortOrder,
                        null, null);
                break;
            default:
                throw new IllegalArgumentException("unKnow uri : " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String tableName = getTableNameByUri(uri);
        long rowId = sqLiteDatabase.insert(tableName, "" , values);
        if (rowId > 0) {
            Uri uri1 = ContentUris.withAppendedId(USER_TABLE_CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(uri1, null);
            return uri1;
        }
        throw new SQLiteException("failed to add a record into: " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        switch (uriMatch.match(uri)) {
            case USER_TABLE_CODE:
                count = sqLiteDatabase.delete(MyDatabaseOpenHelper.TABLE_USER, selection, selectionArgs);
                break;
            case SCORE_TABLE_CODE:
                count = sqLiteDatabase.delete(MyDatabaseOpenHelper.TABLE_SCORE, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("unknow uri: " + uri);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        switch (uriMatch.match(uri)) {
            case USER_TABLE_CODE:
                count = sqLiteDatabase.update(MyDatabaseOpenHelper.TABLE_USER, values, selection, selectionArgs);
                break;
            case SCORE_TABLE_CODE:
                count = sqLiteDatabase.update(MyDatabaseOpenHelper.TABLE_SCORE, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("unknow uri: " + uri);
        }

        return count;
    }

    private String getTableNameByUri(Uri uri) {
        String tableName;
        switch (uriMatch.match(uri)) {
            case USER_TABLE_CODE:
                tableName = MyDatabaseOpenHelper.TABLE_USER;
                break;
            case SCORE_TABLE_CODE:
                tableName = MyDatabaseOpenHelper.TABLE_SCORE;
                break;
            default:
                throw new IllegalArgumentException("unknow uri: " + uri);
        }
        return tableName;
    }
}
