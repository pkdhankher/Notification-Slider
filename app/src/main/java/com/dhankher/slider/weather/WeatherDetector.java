package com.dhankher.slider.weather;

/**
 * Created by Dhankher on 1/17/2017.
 */

public interface WeatherDetector {

    void onWeatherUpdated(Weather weather);

    void onWeatherFailedToUpdate(String error);

}
