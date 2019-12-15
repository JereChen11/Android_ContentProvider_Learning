package com.jere.android_contentprovider_learning;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

/**
 * @author jere
 */
public class MyContentObserver extends ContentObserver {
    public static final int USER_TABLE_WHAT_CODE = 1111;
    public static final int SCORE_TABLE_WHAT_CODE = 2222;
    private Handler mHandler;

    public MyContentObserver(Handler handler) {
        super(handler);
        this.mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        switch (MyContentProvider.uriMatch.match(uri)) {
            case MyContentProvider.USER_TABLE_CODE:
                mHandler.obtainMessage(USER_TABLE_WHAT_CODE).sendToTarget();
                break;
            case MyContentProvider.SCORE_TABLE_CODE:
                mHandler.obtainMessage(SCORE_TABLE_WHAT_CODE).sendToTarget();
                break;
            default:
                mHandler.obtainMessage(USER_TABLE_WHAT_CODE).sendToTarget();
                break;
        }
    }
}
