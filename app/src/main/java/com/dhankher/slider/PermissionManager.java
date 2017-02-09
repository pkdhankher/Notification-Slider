package com.dhankher.slider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Dhankher on 2/9/2017.
 */

public class PermissionManager {

private Context context;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;


    public static void checkForOverlayPermission(Activity activity) {

        if (android.os.Build.VERSION.SDK_INT >= 23) {   //Android M Or Over
            if (!Settings.canDrawOverlays(activity)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName()));
                activity.startActivityForResult(intent, 1);
            }
        }
    }

    public static void checkForNotificationListeningPermission(Activity activity) {
        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        activity.startActivity(intent);
    }

    public static void checkForReadContactsPermission(Activity activity){

        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }

    }

    public static void checkForAccesLocationPermission(Activity activity){

        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);


        }
    }
    public static void checkForReadSMSPermission(Activity activity){

        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED)
        {


            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_SMS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);


        }
    }
    public static void checkForReadCallLogsPermission(Activity activity){

        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED)
        {


            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_CALL_LOG},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);


        }
    }



    public static boolean hasPermissions(Activity activity, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }



    }




