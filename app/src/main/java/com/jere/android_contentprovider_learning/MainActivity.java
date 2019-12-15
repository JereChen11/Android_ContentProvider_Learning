package com.jere.android_contentprovider_learning;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author jere
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MyContentObserver myContentObserver;

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
        Button insertScoreBtn = findViewById(R.id.insert_score_btn);
        Button queryScoreBtn = findViewById(R.id.query_score_btn);
        insertBtn.setOnClickListener(this);
        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        queryBtn.setOnClickListener(this);
        insertScoreBtn.setOnClickListener(this);
        queryScoreBtn.setOnClickListener(this);
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
                ContentValues updateValue = new ContentValues();
                updateValue.put(MyDatabaseOpenHelper.USER_NAME, "Jere");
                updateValue.put(MyDatabaseOpenHelper.USER_AGE, "10000000");
                getContentResolver().update(MyContentProvider.USER_TABLE_CONTENT_URI,
                        updateValue,
                        MyDatabaseOpenHelper.USER_NAME + "=? ",
                        new String[]{"JereChen"});
                break;
            case R.id.delete_btn:
                getContentResolver().delete(MyContentProvider.USER_TABLE_CONTENT_URI,
                        MyDatabaseOpenHelper.USER_NAME + "=? ",
                        new String[]{"Jere"});
                break;
            case R.id.query_btn:
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
                        sb.append("name:").append(name).append(" ")
                                .append("age:").append(age).append(" ")
                                .append("height:").append(height).append(" ")
                                .append("address:").append(address).append(" ")
                                .append("\n");
                    }
                    cursor.close();
                }

                TextView userTableTv = findViewById(R.id.user_table_tv);
                userTableTv.setText(sb.toString());
                break;
            case R.id.insert_score_btn:
                ContentValues scoreValues = new ContentValues();
                scoreValues.put(MyDatabaseOpenHelper.SCORE_STUDENT_NAME, "JereChen");
                scoreValues.put(MyDatabaseOpenHelper.SCORE_MATH, 100);
                scoreValues.put(MyDatabaseOpenHelper.SCORE_ENGLISH, 80);
                scoreValues.put(MyDatabaseOpenHelper.SCORE_CHINESE, 60);
                scoreValues.put(MyDatabaseOpenHelper.SCORE_SCIENCE, 100);
                getContentResolver().insert(MyContentProvider.SCORE_TABLE_CONTENT_URI, scoreValues);
                break;
            case R.id.query_score_btn:
                Cursor queryScoreCursor = getContentResolver().query(MyContentProvider.SCORE_TABLE_CONTENT_URI,
                        MyDatabaseOpenHelper.SCORE_ALL_COLUMNS,
                        MyDatabaseOpenHelper.SCORE_STUDENT_NAME + "=? ",
                        new String[]{"JereChen"},
                        null);
                StringBuilder scoreSb = new StringBuilder();
                if (queryScoreCursor != null) {
                    while (queryScoreCursor.moveToNext()) {
                        String name = queryScoreCursor.getString(queryScoreCursor.getColumnIndex(MyDatabaseOpenHelper.SCORE_STUDENT_NAME));
                        int math = queryScoreCursor.getInt(queryScoreCursor.getColumnIndex(MyDatabaseOpenHelper.SCORE_MATH));
                        int english = queryScoreCursor.getInt(queryScoreCursor.getColumnIndex(MyDatabaseOpenHelper.SCORE_ENGLISH));
                        int chinese = queryScoreCursor.getInt(queryScoreCursor.getColumnIndex(MyDatabaseOpenHelper.SCORE_CHINESE));
                        int science = queryScoreCursor.getInt(queryScoreCursor.getColumnIndex(MyDatabaseOpenHelper.SCORE_SCIENCE));
                        scoreSb.append("name:").append(name).append(" ")
                                .append("math:").append(math).append(" ")
                                .append("english:").append(english).append(" ")
                                .append("chinese:").append(chinese).append(" ")
                                .append("science:").append(science).append(" ")
                                .append("\n");
                    }
                    queryScoreCursor.close();
                }
                TextView scoreTableTv = findViewById(R.id.score_table_tv);
                scoreTableTv.setText(scoreSb.toString());
                break;
            default:
                break;
        }
    }

    private static class MyHandler extends android.os.Handler {
        private final WeakReference<MainActivity> weakReference;

        public MyHandler(MainActivity mainActivity) {
            weakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (weakReference.get() != null) {
                switch (msg.what) {
                    case MyContentObserver.USER_TABLE_WHAT_CODE:
                        Toast.makeText(weakReference.get(), "User table content changed!", Toast.LENGTH_SHORT).show();
                        break;
                    case MyContentObserver.SCORE_TABLE_WHAT_CODE:
                        Toast.makeText(weakReference.get(), "Score table content changed!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (myContentObserver == null) {
            myContentObserver = new MyContentObserver(new MyHandler(this));
        }
        getContentResolver().registerContentObserver(MyContentProvider.USER_TABLE_CONTENT_URI,
                true,
                myContentObserver);
    }

    @Override
    protected void onPause() {
        getContentResolver().unregisterContentObserver(myContentObserver);
        super.onPause();
    }
}
