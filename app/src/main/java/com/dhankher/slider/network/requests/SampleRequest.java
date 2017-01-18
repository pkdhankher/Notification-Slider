package com.dhankher.slider.network.requests;

import android.content.Context;

import com.dhankher.slider.network.NetworkConstants;
import com.dhankher.slider.network.callbacks.SampleCallback;
import com.dhankher.slider.network.core.HttpRequest;
import com.dhankher.slider.network.core.RequestCallback;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by Maninder Taggar on 10/12/16.
 */

public class SampleRequest extends HttpRequest {
    private Context context;

    public SampleRequest(Context context) {
        super(context);
        this.context = context;

    }

    public void start() {
        RequestBody formBody = new FormBody.Builder()
                .add("", "")
                .build();

//            RequestCallback callback = new SampleCallback();
//         String url = NetworkConstants.;
//          super.send(url, HttpRequest.POST, formBody, callback);

    }
}

