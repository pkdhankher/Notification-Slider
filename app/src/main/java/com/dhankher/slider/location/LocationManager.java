package com.dhankher.slider.location;

import android.content.Context;
import android.util.Log;

/**
 * Created by Dhankher on 1/17/2017.
 */

public class LocationManager {
    private static final String TAG = "pawan";
    private static LocationManager runningInstance;
    private Context context;
    private Location lastKnownLocation;

    public LocationManager(Context context) {
        if (runningInstance == null) {
            runningInstance = this;
        } else {
            return;
        }

        this.context = context;

        lastKnownLocation = new Location();
        CurrentLocation currentLocation = CurrentLocation.getRunningInstance();
//        lastKnownLocation.setCity(currentLocation.currentCity);
        lastKnownLocation.setCity("gurugram");
        Log.d(TAG, "current: "+currentLocation.currentCity);
        lastKnownLocation.setLat(30.193951);
        lastKnownLocation.setLng(74.497063);

    }

    public Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    public static void init(Context context) {
        new LocationManager(context);
    }

    public static LocationManager getInstance() {
        return runningInstance;
    }
}
