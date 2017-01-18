package com.dhankher.slider.network.requests;

import android.content.Context;

import com.dhankher.slider.location.Location;
import com.dhankher.slider.network.NetworkConstants;
import com.dhankher.slider.network.callbacks.GetWeatherCallback;
import com.dhankher.slider.network.core.HttpRequest;
import com.dhankher.slider.network.core.RequestCallback;
import com.dhankher.slider.weather.WeatherDetector;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by Maninder Taggar on 10/12/16.
 */

public class GetWeatherRequest extends HttpRequest {
    private Context context;
    private Location location;
    private WeatherDetector weatherDetector;

    public GetWeatherRequest(Context context, WeatherDetector weatherDetector, Location location) {
        super(context);
        this.weatherDetector = weatherDetector;
        this.context = context;
        this.location = location;
    }

    public void start() {
        if (location==null)
            return;
        RequestBody formBody = new FormBody.Builder()
                .add("city", location.getCity())
                .add("lat", "" + location.getLat())
                .add("lng", "" + location.getLng())
                .build();

        String url = NetworkConstants.ROUTE_GET_WEATHER;
        GetWeatherCallback getWeatherCallback = new GetWeatherCallback(context, weatherDetector);
        super.send(url, HttpRequest.POST, formBody, getWeatherCallback);

    }
}

