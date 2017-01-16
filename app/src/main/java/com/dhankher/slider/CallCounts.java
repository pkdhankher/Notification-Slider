package com.dhankher.slider;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Dhankher on 1/11/2017.
 */

public class CallCounts {

    private int missedCallCount;
    private Context context;
    private String where;


    public CallCounts(Context context) {
        this.context = context;
    }

    public String getMissedCalls() {
        String[] selection = {CallLog.Calls.CACHED_NAME, CallLog.Calls.CACHED_NUMBER_LABEL, CallLog.Calls.TYPE};
        where = CallLog.Calls.CACHED_NAME + "=" + CallLog.Calls.MISSED_TYPE;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return where;
        }
        Cursor c = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, selection, where, null, null);
        c.moveToFirst();
        Log.d("CALL", "" + c.getCount()); //do some other operation
        missedCallCount = c.getCount();
        return where;
        //  if(c.getCount() == SOME_VALUE_TO_START_APP_ONE)//...etc etc
    }
}
