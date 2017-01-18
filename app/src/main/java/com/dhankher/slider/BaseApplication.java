package com.dhankher.slider;

import android.app.Application;

import com.dhankher.slider.location.CurrentLocation;
import com.dhankher.slider.location.LocationManager;
import com.dhankher.slider.weather.WeatherManager;

/**
 * Created by Dhankher on 1/17/2017.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        new CurrentLocation(getApplicationContext()).init();
        LocationManager.init(getApplicationContext());
        WeatherManager.init(getApplicationContext());
    }


}
