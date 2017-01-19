package com.dhankher.slider.weather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.dhankher.slider.SliderService;
import com.dhankher.slider.location.Loc;
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
    WeatherUpdateDetector weatherUpdateDetector;

    public WeatherManager(Context context, WeatherUpdateDetector weatherUpdateDetector) {
        if (runningInstance == null) {
            runningInstance = this;
        } else {
            return;
        }
        Log.d(TAG, "WeatherManagerConstructor: ");
        this.context = context;
        this.weatherUpdateDetector = weatherUpdateDetector;
        startUpdatingEveryHour();
    }

    public Weather getWeather() {
        return curruntWeather;
    }

    public void requestForWeatherUpdate() {
        Log.d(TAG, "requestForWeatherUpdate: ");
        Loc loc = LocationManager.getRunningInstance().getLastKnownLocation();
        new GetWeatherRequest(context, this, loc).start();
    }

    public static WeatherManager getInstance() {
        return runningInstance;
    }

    public static void init(Context context, WeatherUpdateDetector weatherUpdateDetector) {
        Log.d(TAG, "init: ");
        new WeatherManager(context, weatherUpdateDetector);
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
        weatherUpdateDetector.onWeatherUpdated(weather);
    }

    @Override
    public void onWeatherFailedToUpdate(String error) {
        Log.e(TAG, "onWeatherFailedToUpdate: " + error);
    }
}
