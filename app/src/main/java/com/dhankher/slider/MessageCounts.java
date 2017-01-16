package com.dhankher.slider;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Dhankher on 1/11/2017.
 */

public class MessageCounts {
    private Context context;

    public MessageCounts(Context context) {
        this.context = context;
    }

    public int getMessages() {


        Uri sms_content = Uri.parse("content://sms/inbox");
        Cursor c = context.getContentResolver().query(sms_content, null, null, null, null);
        c.moveToFirst();
        Log.d("SMS COUNT", "" + c.getCount()); //do some other operation
//Here proceed with the what you wanted
        //    if(c.getCount() == SOME_VALUE_TO_START_APP_ONE)//...etc etc
        return c.getCount();
    }
}


//}
