package com.jere.android_contentprovider_learning;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author jere
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Uri uri = MyContentProvider.CONTENT_URI;

        ContentValues values = new ContentValues();
        values.put(MyDatabaseOpenHelper.USER_NAME, "JereChen");
        values.put(MyDatabaseOpenHelper.USER_AGE, "24");
        values.put(MyDatabaseOpenHelper.USER_HEIGHT, "150");
        values.put(MyDatabaseOpenHelper.USER_ADDRESS, "China HZ");

        ContentResolver contentResolver = getContentResolver();
        contentResolver.insert(uri, values);

        Cursor cursor = contentResolver.query(uri, MyDatabaseOpenHelper.USER_ALL_COLUMNS,
                null, null, null);
        StringBuilder sb = new StringBuilder();
        if (cursor != null) {
            while(cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(MyDatabaseOpenHelper.USER_NAME));
                int age = cursor.getInt(cursor.getColumnIndex(MyDatabaseOpenHelper.USER_AGE));
                int height = cursor.getInt(cursor.getColumnIndex(MyDatabaseOpenHelper.USER_HEIGHT));
                String address = cursor.getString(cursor.getColumnIndex(MyDatabaseOpenHelper.USER_ADDRESS));
                sb.append("name: ").append(name)
                        .append("age: ").append(age)
                        .append("height: ").append(height)
                        .append("address: ").append(address)
                        .append("\n");
            }
            cursor.close();
        }

        TextView tv = findViewById(R.id.tv);
        tv.setText(sb.toString());

    }
}
