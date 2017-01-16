package com.dhankher.slider;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.content.ContentValues.TAG;

/**
 * Created by Dhankher on 1/11/2017.
 */

public class SliderService extends Service {
    WindowManager windowManager;
    LayoutInflater layoutInflater;
    FrameLayout sliderTouchLayout, sliderLayout;
    WindowManager.LayoutParams sliderTouchParams, sliderParams;
    RelativeLayout relativeLayout_2nd;
    int statusBarHeight, screenWidth, screenHeight;
    int x, y;
    int initialx, finalx, diffx;
    int initialPosition, finalPositiopn, diff;
    float currentx, current = 1;
    float downx, upx;
    boolean isSliderVisible = false;
    TextView messeges, misscall, alarm;
    MessageCounts messageCounts;
    CallCounts callCounts;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        callCounts = new CallCounts(getBaseContext());
        messageCounts = new MessageCounts(getBaseContext());
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        final Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        Log.d("screenWidth: " + screenWidth, "screenHeight" + screenHeight);


        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        sliderTouchLayout = (FrameLayout) layoutInflater.inflate(R.layout.slider_touch_layout, null);
//        sliderTouchView = sliderTouchLayout.findViewById(R.id.slider_touch_view);

        sliderTouchParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        sliderTouchParams.gravity = Gravity.TOP | Gravity.RIGHT;
//        sliderTouchParams.x = 0;
//        sliderTouchParams.y = 10;
        windowManager.addView(sliderTouchLayout, sliderTouchParams);


        sliderLayout = (FrameLayout) layoutInflater.inflate(R.layout.slider_layout, null);
        relativeLayout_2nd = (RelativeLayout) sliderLayout.findViewById(R.id.relativeLayout_2ndview);
        messeges = (TextView) sliderLayout.findViewById(R.id.messeges);
        misscall = (TextView) sliderLayout.findViewById(R.id.missalls);
        alarm = (TextView) sliderLayout.findViewById(R.id.alarm);
        sliderParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        sliderParams.gravity = Gravity.TOP | Gravity.LEFT;
//        sliderParams.x = 0;
//        sliderParams.y = 10;
        windowManager.addView(sliderLayout, sliderParams);


        sliderTouchLayout.setOnTouchListener(new View.OnTouchListener() {
            int oneThirdOfScreen = screenWidth / 3;
            long startTime, endTime;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startTime = System.currentTimeMillis();


                        break;
                    case MotionEvent.ACTION_MOVE:
                        x = (int) event.getRawX();
                        y = (int) event.getRawY();
                        sliderLayout.setVisibility(View.VISIBLE);
                        sliderParams.x = x;
                        sliderParams.y = 0;
                        windowManager.updateViewLayout(sliderLayout, sliderParams);


                        misscall.setAlpha(CurrentAlpha());
                        messeges.setAlpha(CurrentAlpha());
                        alarm.setAlpha(CurrentAlpha());
                        relativeLayout_2nd.setAlpha(relativeLayoutAlpha());


                        break;
                    case MotionEvent.ACTION_UP:

                        if (x < (2 * oneThirdOfScreen)) {

                            ObjectAnimator objectAnimator = new ObjectAnimator();
                            objectAnimator.setDuration(200);
                            objectAnimator.setFloatValues(x, 0f);
                            objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    float value = (float) animation.getAnimatedValue();
                                    float current = (int) value;
                                    sliderParams.x = (int) current;
                                    sliderParams.y = (0);
                                    windowManager.updateViewLayout(sliderLayout, sliderParams);
                                }
                            });
                            objectAnimator.start();
                            relativeLayout_2nd.setAlpha(0);

                        } else {

                            ObjectAnimator objectAnimator = new ObjectAnimator();
                            objectAnimator.setDuration(1000);
                            objectAnimator.setFloatValues(x, screenWidth);
                            objectAnimator.setInterpolator(new OvershootInterpolator());
                            objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    float value = (float) animation.getAnimatedValue();
                                    float current = value;
                                    sliderParams.x = (int) current;
                                    sliderParams.y = 0;
                                    windowManager.updateViewLayout(sliderLayout, sliderParams);
                                }
                            });
                            objectAnimator.start();
                        }
                        relativeLayout_2nd.setAlpha(1);
                        //   endTime = System.currentTimeMillis();


                        break;
                }
                return false;
            }
        });


        sliderLayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:



                        break;
                    case MotionEvent.ACTION_UP:
int oneThirdOfScreen = screenWidth/3;
                        if (x >  oneThirdOfScreen) {

                            ObjectAnimator objectAnimator = new ObjectAnimator();
                            objectAnimator.setDuration(500);
                            objectAnimator.setFloatValues(x, screenWidth);
                            objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    float value = (float) animation.getAnimatedValue();
                                    float current = (int) value;
                                    sliderParams.x = (int) current;
                                    sliderParams.y = (0);
                                    windowManager.updateViewLayout(sliderLayout, sliderParams);
                                }
                            });
                            objectAnimator.start();
                     //       relativeLayout_2nd.setAlpha(0);

                        } else {

                            ObjectAnimator objectAnimator = new ObjectAnimator();
                            objectAnimator.setDuration(500);
                            objectAnimator.setFloatValues(x, 0);
                            objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    float value = (float) animation.getAnimatedValue();
                                    float current = value;
                                    sliderParams.x = (int) current;
                                    sliderParams.y = 0;
                                    windowManager.updateViewLayout(sliderLayout, sliderParams);
                                }
                            });
                            objectAnimator.start();
                        }
                    //    relativeLayout_2nd.setAlpha(1);
                        //   endTime = System.currentTimeMillis();



                        break;
                    case MotionEvent.ACTION_MOVE:

                        x = (int) event.getRawX();
                        y = (int) event.getRawY();
                        sliderParams.x = x;
                        sliderParams.y = 0;
                        windowManager.updateViewLayout(sliderLayout, sliderParams);

                        misscall.setAlpha(CurrentAlpha());
                        messeges.setAlpha(CurrentAlpha());
                        alarm.setAlpha(CurrentAlpha());
                        relativeLayout_2nd.setAlpha(relativeLayoutAlpha());

                        break;
                }
                return false;
            }

        });

        misscall.setText("" + callCounts.getMissedCalls());
        messeges.setText("" + messageCounts.getMessages());
        Log.d(TAG, "Count: " + callCounts.getMissedCalls());
        Log.d(TAG, "Count: " + messageCounts.getMessages());
        windowManager.updateViewLayout(sliderLayout, sliderParams);


    }


    private void sliderTouchLayout_LongClick() {
        ObjectAnimator objectAnimator = new ObjectAnimator();
        objectAnimator.setDuration(500);
        objectAnimator.setFloatValues(screenWidth, 623);
        objectAnimator.setInterpolator(new OvershootInterpolator());

        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                float current = value;
                sliderParams.x = (int) current;
                sliderParams.y = 0;
                windowManager.updateViewLayout(sliderLayout, sliderParams);
            }
        });
        objectAnimator.start();

    }

    private float relativeLayoutAlpha() {
        int oneThird = screenWidth / 3;
        initialPosition = 2 * oneThird;
        finalPositiopn = 0;
        diffx = initialPosition - x;
        currentx = (diffx * 100 / (2 * oneThird));

        return currentx / 100;
    }

    private float CurrentAlpha() {


        int oneThird = screenWidth / 3;
        initialx = screenWidth;
        finalx = 2 * oneThird;
        diffx = initialx - x;
        currentx = (diffx * 100 / oneThird);


        Log.d("diffX" + diffx, "   currentX" + currentx);
        return (100 - currentx) / 100;

    }

}