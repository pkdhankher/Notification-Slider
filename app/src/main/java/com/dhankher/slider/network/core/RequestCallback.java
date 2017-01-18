package com.dhankher.slider.network.core;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import okhttp3.Callback;

/**
 * Created by Maninder Taggar on 06/06/16.
 */
public abstract class RequestCallback implements Callback {

    final Context mContext;
    private Handler handler = new Handler(Looper.getMainLooper());

    public RequestCallback(Context context) {
        mContext = context;
    }

    protected Handler getHandler() {
         return handler;
    }
}

