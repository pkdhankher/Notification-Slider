package com.dhankher.slider.weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Dhankher on 1/17/2017.
 */

public class WeatherUpdateReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        WeatherManager.getInstance().requestForWeatherUpdate();
    }
}
