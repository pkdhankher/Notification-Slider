package com.dhankher.slider.network.callbacks;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.dhankher.slider.network.core.RequestCallback;
import com.dhankher.slider.weather.Weather;
import com.dhankher.slider.weather.WeatherDetector;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Maninder Taggar on 10/12/16.
 */

public class GetWeatherCallback extends RequestCallback {
    private static final String TAG = GetWeatherCallback.class.getCanonicalName();
    private Context context;
    private WeatherDetector weatherDetector;

    public GetWeatherCallback(Context context, WeatherDetector weatherDetector) {
        super(context);
        this.context = context;
        this.weatherDetector = weatherDetector;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        showError(e);
    }

    private void showError(final Exception e) {
        Log.e(TAG, e.toString());
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                weatherDetector.onWeatherFailedToUpdate(e.toString());
            }
        });
    }

    @Override
    public void onResponse(Call call, Response rawResponse) throws IOException {
        final String response = rawResponse.body().string();
        Log.d(TAG, "onResponse: "+response);
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                Weather weather = new Gson().fromJson(response, Weather.class);
                weatherDetector.onWeatherUpdated(weather);
            }
        });
    }
}

