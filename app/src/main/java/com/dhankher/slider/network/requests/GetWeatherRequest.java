package com.dhankher.slider.network.requests;

import android.content.Context;
import android.util.Log;

import com.dhankher.slider.location.Loc;
import com.dhankher.slider.network.NetworkConstants;
import com.dhankher.slider.network.callbacks.GetWeatherCallback;
import com.dhankher.slider.network.core.HttpRequest;
import com.dhankher.slider.weather.WeatherDetector;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by Maninder Taggar on 10/12/16.
 */

public class GetWeatherRequest extends HttpRequest {
    private Context context;
    private Loc loc;
    private WeatherDetector weatherDetector;

    public GetWeatherRequest(Context context, WeatherDetector weatherDetector, Loc loc) {
        super(context);
        this.weatherDetector = weatherDetector;
        this.context = context;
        this.loc = loc;
    }

    public void start() {
        if (loc ==null) {
            Log.e("WeatherManager", "start: location is null, not sending request");
            return;
        }
        RequestBody formBody = new FormBody.Builder()
                .add("city", loc.getCity())
                .add("lat", "" + loc.getLat())
                .add("lng", "" + loc.getLng())
                .build();

        String url = NetworkConstants.ROUTE_GET_WEATHER;
        GetWeatherCallback getWeatherCallback = new GetWeatherCallback(context, weatherDetector);
        super.send(url, HttpRequest.POST, formBody, getWeatherCallback);

    }
}

