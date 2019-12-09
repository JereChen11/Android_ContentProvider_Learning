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
    private static final String BASE_PATH = "jereTest";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    public static final int USER_CODE = 1;
    private static final UriMatcher uriMatch = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatch.addURI(AUTHORITY, BASE_PATH, USER_CODE);
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
            case USER_CODE:
                cursor = sqLiteDatabase.query(MyDatabaseOpenHelper.TABLE_NAME_USER,
                        MyDatabaseOpenHelper.USER_ALL_COLUMNS,
                        null, null, null, null, null);
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
            Uri uri1 = ContentUris.withAppendedId(CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(uri1, null);
            return uri1;
        }
        throw new SQLiteException("failed to add a record into: " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        switch (uriMatch.match(uri)) {
            case USER_CODE:
                count = sqLiteDatabase.delete(BASE_PATH, selection, selectionArgs);
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
            case USER_CODE:
                count = sqLiteDatabase.update(BASE_PATH, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("unknow uri: " + uri);
        }

        return count;
    }

    private String getTableNameByUri(Uri uri) {
        String tableName;
        switch (uriMatch.match(uri)) {
            case USER_CODE:
                tableName = MyDatabaseOpenHelper.TABLE_NAME_USER;
                break;
            default:
                throw new IllegalArgumentException("unknow uri: " + uri);
        }
        return tableName;
    }
}
