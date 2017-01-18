package com.dhankher.slider.weather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.dhankher.slider.location.Location;
import com.dhankher.slider.location.LocationManager;
import com.dhankher.slider.network.requests.GetWeatherRequest;

/**
 * Created by Dhankher on 1/17/2017.
 */

public class WeatherManager implements WeatherDetector {
    private static final String TAG = WeatherManager.class.getCanonicalName();
    private static WeatherManager runningInstance;
    private Context context;
    private Weather curruntWeather;
    private AlarmManager updateEveryHourAlarmManager;
    private PendingIntent updateEveryHourPendingIntent;

    public WeatherManager(Context context) {
        if (runningInstance == null) {
            runningInstance = this;
        } else {
            return;
        }
        Log.d(TAG, "WeatherManagerConstructor: ");
        this.context = context;

        requestForWeatherUpdate();
        startUpdatingEveryHour();
    }

    public Weather getWeather() {
        return curruntWeather;
    }

    public void requestForWeatherUpdate() {
        Log.d(TAG, "requestForWeatherUpdate: ");
        Location location = LocationManager.getInstance().getLastKnownLocation();
        new GetWeatherRequest(context, this, location).start();
    }

    public static WeatherManager getInstance() {
        return runningInstance;
    }

    public static void init(Context context) {

        Log.d(TAG, "init: ");
        new WeatherManager(context);
    }

    private void startUpdatingEveryHour() {
        Log.d(TAG, "startUpdatingEveryHour: ");
        updateEveryHourAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, WeatherUpdateReciever.class);
        updateEveryHourPendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        updateEveryHourAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HOUR,
                AlarmManager.INTERVAL_HOUR, updateEveryHourPendingIntent);
    }

    @Override
    public void onWeatherUpdated(Weather weather) {
        Log.d(TAG, "onWeatherUpdated: ");
        curruntWeather = weather;
    }

    @Override
    public void onWeatherFailedToUpdate(String error) {
    }
}
