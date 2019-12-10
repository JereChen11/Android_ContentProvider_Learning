package com.jere.android_contentprovider_learning;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author jere
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayout();

    }

    private void initLayout() {
        Button insertBtn = findViewById(R.id.insert_btn);
        Button updateBtn = findViewById(R.id.update_btn);
        Button deleteBtn = findViewById(R.id.delete_btn);
        Button queryBtn = findViewById(R.id.query_btn);
        insertBtn.setOnClickListener(this);
        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        queryBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.insert_btn:
                ContentValues values = new ContentValues();
                values.put(MyDatabaseOpenHelper.USER_NAME, "JereChen");
                values.put(MyDatabaseOpenHelper.USER_AGE, "24");
                values.put(MyDatabaseOpenHelper.USER_HEIGHT, "150");
                values.put(MyDatabaseOpenHelper.USER_ADDRESS, "China HZ");
                getContentResolver().insert(MyContentProvider.USER_TABLE_CONTENT_URI, values);
                break;
            case R.id.update_btn:
                //todo update db
                break;
            case R.id.delete_btn:
                //todo delete


                break;
            case R.id.query_btn:
                //todo query

                Cursor cursor = getContentResolver().query(MyContentProvider.USER_TABLE_CONTENT_URI,
                        MyDatabaseOpenHelper.USER_ALL_COLUMNS,
                        null, null, null);
                StringBuilder sb = new StringBuilder();
                if (cursor != null) {
                    while (cursor.moveToNext()) {
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
                break;
            default:
                break;
        }
    }
}
