package com.dhankher.slider.network.callbacks;

import android.app.Activity;
import android.util.Log;

import com.dhankher.slider.network.core.RequestCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Maninder Taggar on 10/12/16.
 */

public class SampleCallback extends RequestCallback {
    private static final String TAG = SampleCallback.class.getCanonicalName();
    private final static String KEY_ = "";

    public SampleCallback(Activity activity) {
        super(activity);
    }


    @Override
    public void onFailure(Call call, IOException e) {
        showError(e);

    }

    private void showError(Exception e) {
        Log.e(TAG, e.toString());
        getHandler().post(new Runnable() {
            @Override
            public void run() {
//                loginActivity.checkAccountCallfailed();
            }
        });
    }

    @Override
    public void onResponse(Call call, Response rawResponse) throws IOException {
        final String response = rawResponse.body().string();
        if (response.contains("error")) {
            showError(new Exception(response));
        } else {
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject dataObject = new JSONObject(response).getJSONObject("data");
                    } catch (JSONException e) {
                        showError(e);
                    }
                }
            });
        }
    }
}


