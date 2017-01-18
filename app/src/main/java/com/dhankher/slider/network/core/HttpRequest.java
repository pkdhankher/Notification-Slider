package com.dhankher.slider.network.core;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Maninder Taggar on 12/4/16.
 */
public abstract class HttpRequest {
    private static final String TAG = HttpRequest.class.getCanonicalName();

    private static MediaType JSON = null;

    public static final String POST = "post";
    public static final String PUT = "put";
    public static final String GET = "get";
    public static final String DELETE = "delete";
    public static final String PATCH = "patch";

    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_TEXT_JSON = "text/json";

    final Context mContext;


    public HttpRequest(Context context) {
        mContext = context;
    }


    public void send(String url, String requestType, RequestBody requestBody, RequestCallback callback) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build();

        Request.Builder requestBuilder = new Request.Builder()
                .addHeader(HEADER_CONTENT_TYPE, HEADER_TEXT_JSON)
                .url(url);

        Request httpRequest = getHttpRequest(requestBuilder, requestType, requestBody);

        if (callback != null)
            client.newCall(httpRequest).enqueue(callback);
        else
            client.newCall(httpRequest);

    }

    private Request getHttpRequest(Request.Builder requestBuilder, String requestType, RequestBody body) {

        Request request = null;

        switch (requestType) {
            case POST:
                request = requestBuilder.post(body).build();
                break;
            case PUT:
                request = requestBuilder.put(body).build();
                break;
            case PATCH:
                request = requestBuilder.patch(body).build();
                break;
            case GET:
                request = requestBuilder.get().build();
                break;
            case DELETE:
                request = requestBuilder.delete().build();
                break;
        }

        return request;
    }
}
