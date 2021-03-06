package com.dhankher.slider;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.method.Touch;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dhankher.slider.location.Loc;
import com.dhankher.slider.location.LocationManager;
import com.dhankher.slider.location.LocationUpdateDetector;
import com.dhankher.slider.weather.Weather;
import com.dhankher.slider.weather.WeatherManager;
import com.dhankher.slider.weather.WeatherUpdateDetector;

import java.util.Calendar;

import static android.content.ContentValues.TAG;

/**
 * Created by Dhankher on 1/11/2017.
 */

public class SliderService extends Service implements LocationUpdateDetector, WeatherUpdateDetector, GestureDetector.OnGestureListener {
    WindowManager windowManager;
    LayoutInflater layoutInflater;
    FrameLayout sliderTouchLayout, sliderLayout;
    WindowManager.LayoutParams sliderTouchParams, sliderParams;
    RelativeLayout.LayoutParams missed_content_relativeLayoutParams, emails_content_relativeLayoutParams, meetings_content_relativeLayoutParams;
    LinearLayout linearLayout_2nd;
    RelativeLayout clock_relativeLayout, calls_relativeLayout, email_relativeLayout, metting_relativeLayout;
    RelativeLayout missed_content_relativeLayout, emails_content_relativeLayout, meetings_content_relativeLayout;
    LinearLayout linearLayout_1st;
    ImageButton missedCalls_imageButton, emails_imageButton, meetings_imageButton;
    CardView clock_cardView, missedCalls_cardView, emails_cardView, meetings_cardView;
    int linearLayout_1st_height, linearLayout_1st_width;
    int statusBarHeight, screenWidth, screenHeight;
    int x, y;
    int initialx, finalx, diffx;
    int initialPosition, finalPositiopn, diff;
    float currentx, current = 1;
    float downx, upx;
    boolean isSliderVisible = false;
    boolean isContentOpen = false;
    boolean startOnScroll = false;
    TextView messeges, misscall, alarm;
    TextView temp, lowestTemp, highestTemp, cityName;
    TextView textClock, timePeriod, date;
    ImageView weatherThumbnil;
    MessageCounts messageCounts;
    CallCounts callCounts;
    ObjectAnimator OpeningobjectAnimator;
    float  gestureTouchX,gestureTouchUpX,gestureTouchDiff;
    GestureDetector gestureDetector;
    float lastValueOfGestureTouchX;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new LocationManager(getApplicationContext(), this).init();
        WeatherManager.init(getApplicationContext(), this);

        LocationManager.getRunningInstance().onStart();
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
        linearLayout_1st = (LinearLayout) sliderLayout.findViewById(R.id.linearLayout_1stView);
        messeges = (TextView) sliderLayout.findViewById(R.id.messeges);
        misscall = (TextView) sliderLayout.findViewById(R.id.missalls);
        alarm = (TextView) sliderLayout.findViewById(R.id.alarm);

        linearLayout_2nd = (LinearLayout) sliderLayout.findViewById(R.id.relativeLayout_2ndview);
        clock_relativeLayout = (RelativeLayout) linearLayout_2nd.findViewById(R.id.clock_relativeLayout);
        clock_cardView = (CardView) clock_relativeLayout.findViewById(R.id.clock_cardView);
        temp = (TextView) clock_cardView.findViewById(R.id.temp);
        lowestTemp = (TextView) clock_cardView.findViewById(R.id.lowestTemp);
        highestTemp = (TextView) clock_cardView.findViewById(R.id.highestTemp);
        cityName = (TextView) clock_cardView.findViewById(R.id.cityName);
        textClock = (TextView) clock_cardView.findViewById(R.id.textClock);
        timePeriod = (TextView) clock_cardView.findViewById(R.id.timePeriod);
        date = (TextView) clock_cardView.findViewById(R.id.date);

        calls_relativeLayout = (RelativeLayout) linearLayout_2nd.findViewById(R.id.calls_relativeLayout);
        missedCalls_cardView = (CardView) calls_relativeLayout.findViewById(R.id.missedCalls_cardView);
        missedCalls_imageButton = (ImageButton) missedCalls_cardView.findViewById(R.id.missedCalls_imageButton);
        missed_content_relativeLayout = (RelativeLayout) calls_relativeLayout.findViewById(R.id.missed_content_relativeLayout);

        email_relativeLayout = (RelativeLayout) linearLayout_2nd.findViewById(R.id.email_relativeLayout);
        emails_cardView = (CardView) email_relativeLayout.findViewById(R.id.emails_cardView);
        emails_imageButton = (ImageButton) emails_cardView.findViewById(R.id.emails_imageButton);
        emails_content_relativeLayout = (RelativeLayout) email_relativeLayout.findViewById(R.id.emails_content_relativeLayout);

        metting_relativeLayout = (RelativeLayout) linearLayout_2nd.findViewById(R.id.meeting_relativeLayout);
        meetings_cardView = (CardView) metting_relativeLayout.findViewById(R.id.meetings_cardView);
        meetings_imageButton = (ImageButton) meetings_cardView.findViewById(R.id.meetings_imageButton);
        meetings_content_relativeLayout = (RelativeLayout) metting_relativeLayout.findViewById(R.id.meetings_content_relativeLayout);


        sliderParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);
        sliderParams.gravity = Gravity.TOP | Gravity.LEFT;
        linearLayout_1st.getLayoutParams().width = screenHeight;

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

                        misscall.setAlpha(linearLayout_1st_Alpha_openingTime());
                        messeges.setAlpha(linearLayout_1st_Alpha_openingTime());
                        alarm.setAlpha(linearLayout_1st_Alpha_openingTime());
                        linearLayout_2nd.setAlpha(relativeLayout_2nd_Alpha_openingTime());


                        Calendar c = Calendar.getInstance();
                        int hours = c.get(Calendar.HOUR);
                        int min = c.get(Calendar.MINUTE);
                        int zone = c.get(Calendar.AM_PM);
                        String timeZone;
                        if (zone == 0) {
                            timeZone = "AM";
                        } else {
                            timeZone = "PM";
                        }
                        textClock.setText(hours + ":" + min);
                        timePeriod.setText(timeZone);


                        break;
                    case MotionEvent.ACTION_UP:

                        if (x < (2 * oneThirdOfScreen)) {

                            OpeningobjectAnimator = new ObjectAnimator();
                            OpeningobjectAnimator.setDuration(200);
                            OpeningobjectAnimator.setFloatValues(x, 0f);
                            OpeningobjectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    float value = (float) animation.getAnimatedValue();
                                    float current = (int) value;
                                    sliderParams.x = (int) current;
                                    sliderParams.y = (0);
                                    windowManager.updateViewLayout(sliderLayout, sliderParams);
                                }
                            });
                            OpeningobjectAnimator.start();
                            linearLayout_2nd.setAlpha(0);

                        } else {

                            OpeningobjectAnimator = new ObjectAnimator();
                            OpeningobjectAnimator.setDuration(500);
                            OpeningobjectAnimator.setFloatValues(x, screenWidth);
                            OpeningobjectAnimator.setInterpolator(new OvershootInterpolator());
                            OpeningobjectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    float value = (float) animation.getAnimatedValue();
                                    float current = value;
                                    sliderParams.x = (int) current;
                                    sliderParams.y = 0;
                                    windowManager.updateViewLayout(sliderLayout, sliderParams);
                                }
                            });
                            OpeningobjectAnimator.start();
                        }
                        linearLayout_2nd.setAlpha(1);
                        //   endTime = System.currentTimeMillis();


                        break;
                }
                return false;
            }
        });

        gestureDetector = new GestureDetector(this, this);
        sliderLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:


                        break;
                    case MotionEvent.ACTION_UP:
                        if ((x - gestureTouchX) >= 0) {
                            int oneThirdOfScreen = screenWidth / 3;
                            if ((x - gestureTouchX) > oneThirdOfScreen) {

                                ObjectAnimator objectAnimator = new ObjectAnimator();
                                objectAnimator.setDuration(500);
                                objectAnimator.setFloatValues((x - gestureTouchX), screenWidth);
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


                            } else {

                                ObjectAnimator objectAnimator = new ObjectAnimator();
                                objectAnimator.setDuration(500);
                                objectAnimator.setFloatValues((x - gestureTouchX), 0);
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
                        } else {
                            sliderParams.x = (0);
                            sliderParams.y = 0;
                            windowManager.updateViewLayout(sliderLayout, sliderParams);
                        }


                        break;

                        case MotionEvent.ACTION_MOVE:

                            if (startOnScroll == true) {

                            x = (int) event.getRawX();
                            y = (int) event.getRawY();
                            if ((x - gestureTouchX) > 0) {
                                sliderParams.x = (int) (x - gestureTouchX);
                                sliderParams.y = 0;
                                windowManager.updateViewLayout(sliderLayout, sliderParams);
                            } else {
                                sliderParams.x = (0);
                                sliderParams.y = 0;
                                windowManager.updateViewLayout(sliderLayout, sliderParams);
                            }


//                        misscall.setAlpha(linearLayout_1st_Alpha_closingTime());
//                        messeges.setAlpha(linearLayout_1st_Alpha_closingTime());
//                        alarm.setAlpha(linearLayout_1st_Alpha_closingTime());
//                        linearLayout_2nd.setAlpha(relativeLayout_2nd_Alpha_closingTime());
                                startOnScroll = false;
                            break;

                    }

            }
                return false;
            }

        });

        misscall.setText("" + callCounts.getMissedCalls());
        messeges.setText("Messeges = " + messageCounts.getMessages());
        Log.d(TAG, "Count: " + callCounts.getMissedCalls());
        Log.d(TAG, "Count: " + messageCounts.getMessages());
        windowManager.updateViewLayout(sliderLayout, sliderParams);


        ViewTreeObserver observer = linearLayout_1st.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // TODO Auto-generated method stub
                init();
                linearLayout_1st.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
            }
        });


        missedCalls_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isContentOpen) {
                    missed_content_relativeLayout.setVisibility(View.GONE);
                    missedCalls_imageButton.animate().rotation(-5).setDuration(100);
                    int initialPosition = (int) v.getY();
                    int finalPosition = initialPosition + 40;

                    ObjectAnimator objectAnimator = new ObjectAnimator();
                    objectAnimator.setDuration(500);
                    objectAnimator.setFloatValues(initialPosition, finalPosition);
                    objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float value = (float) animation.getAnimatedValue();
                            float current = value;


                        }
                    });
                    objectAnimator.start();

                    isContentOpen = false;
                } else {
                    missedCalls_imageButton.animate().rotation(90).setDuration(100);
                    missed_content_relativeLayout.setVisibility(View.VISIBLE);

                    ObjectAnimator objectAnimator = new ObjectAnimator();
                    objectAnimator.setDuration(500);
                    objectAnimator.setFloatValues(x, screenWidth);
                    objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float value = (float) animation.getAnimatedValue();

                        }
                    });
                    objectAnimator.start();

                    isContentOpen = true;
                }
            }
        });

        emails_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isContentOpen) {
                    emails_imageButton.animate().rotation(-5).setDuration(100);
                    emails_content_relativeLayout.setVisibility(View.GONE);

                    ObjectAnimator objectAnimator = new ObjectAnimator();
                    objectAnimator.setDuration(500);
                    objectAnimator.setFloatValues(x, screenWidth);
                    objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float value = (float) animation.getAnimatedValue();

                        }
                    });
                    objectAnimator.start();

                    isContentOpen = false;
                } else {
                    emails_imageButton.animate().rotation(90).setDuration(100);
                    emails_content_relativeLayout.setVisibility(View.VISIBLE);

                    ObjectAnimator objectAnimator = new ObjectAnimator();
                    objectAnimator.setDuration(500);
                    objectAnimator.setFloatValues(x, screenWidth);
                    objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float value = (float) animation.getAnimatedValue();

                        }
                    });
                    objectAnimator.start();

                    isContentOpen = true;
                }
            }
        });

        meetings_imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                windowManager.removeView(sliderTouchLayout);
                windowManager.removeView(sliderLayout);
                stopSelf();

                if (isContentOpen) {
                    meetings_imageButton.animate().rotation(-5).setDuration(100);
                    meetings_content_relativeLayout.setVisibility(View.GONE);

                    ObjectAnimator objectAnimator = new ObjectAnimator();
                    objectAnimator.setDuration(500);
                    objectAnimator.setFloatValues(x, screenWidth);
                    objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float value = (float) animation.getAnimatedValue();

                        }
                    });
                    objectAnimator.start();

                    isContentOpen = false;
                } else {
                    meetings_imageButton.animate().rotation(90).setDuration(100);
                    meetings_content_relativeLayout.setVisibility(View.VISIBLE);
                    isContentOpen = true;

                    ObjectAnimator objectAnimator = new ObjectAnimator();
                    objectAnimator.setDuration(500);
                    objectAnimator.setFloatValues(x, screenWidth);
                    objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float value = (float) animation.getAnimatedValue();

                        }
                    });
                    objectAnimator.start();

                }
            }
        });
    }


    protected void init() {
        linearLayout_1st_height = linearLayout_1st.getHeight();
        linearLayout_1st_width = linearLayout_1st.getWidth();
        Log.d("height" + linearLayout_1st_height, "width" + linearLayout_1st_width);
    }

    private float relativeLayout_2nd_Alpha_openingTime() {

        initialPosition = screenWidth - (2 * linearLayout_1st_height);
        finalPositiopn = 0;
        diffx = initialPosition - x;
        currentx = (diffx * 100 / initialPosition);
        if (x > initialPosition) {
            linearLayout_2nd.setVisibility(View.GONE);
        } else {
            linearLayout_2nd.setVisibility(View.VISIBLE);
        }

        return currentx / 100;
    }
    private float relativeLayout_2nd_Alpha_closingTime() {

        initialPosition =0;
        finalPositiopn = screenWidth - (2 * linearLayout_1st_height);;
        diffx = (int) (initialPosition - (x-gestureTouchX));
        currentx = (diffx * 100 / finalPositiopn);
        return currentx / 100;

    }

    private float linearLayout_1st_Alpha_openingTime() {


        initialx = screenWidth - linearLayout_1st_height;
        finalx = 2 * linearLayout_1st_height;
        diffx = initialx - x;
        currentx = (diffx * 100 / linearLayout_1st_height);

        Log.d("diffX" + diffx, "   currentX" + currentx);
        return (100 - currentx) / 100;

    }
    private float linearLayout_1st_Alpha_closingTime() {


        initialx = screenWidth - (2 * linearLayout_1st_height);

        diffx = (int) (initialx - (x-gestureTouchX));
        currentx = (diffx * 100 / (2 * linearLayout_1st_height));

        Log.d("diffX" + diffx, "   currentX" + currentx);
        return (100 - currentx) / 100;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationManager.getRunningInstance().onStop();
    }

    @Override
    public void onLocationUpdated(Loc location) {
        Log.d(TAG, "onLocationUpdated: " + location);
        WeatherManager.getInstance().requestForWeatherUpdate();
    }

    @Override
    public void onWeatherUpdated(Weather weather) {
        Log.d("WeatherManager", "onWeatherUpdated: " + weather);
        int wTemp = Integer.parseInt(weather.getQuery().getResults().getChannel().getItem().getCondition().getTemp());
        int minTemp = Integer.parseInt(weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getLow().toString());
        int maxTemp = Integer.parseInt(weather.getQuery().getResults().getChannel().getItem().getForecast().get(0).getHigh().toString());
        String city = weather.getQuery().getResults().getChannel().getLocation().getCity();
        Log.d(TAG, "currentTemp: " + wTemp + minTemp + city);
        String WeatherImage = weather.getQuery().getResults().getChannel().getItem().getDescription();
        //          Log.i("lowTemp", "onWeatherUpdated: " + WeatherImage);
        final String DEGREE = "\u00b0";
        temp.setText("" + fahrenToCelsius(wTemp) + DEGREE);
        lowestTemp.setText("L " + fahrenToCelsius(minTemp) + DEGREE);
        highestTemp.setText("H " + fahrenToCelsius(maxTemp) + DEGREE);
        cityName.setText(city);

    }

    public int fahrenToCelsius(int f) {
        final String DEGREE = "\u00b0";
        int celsious = ((f - 32) * 5 / 9);

        return celsious;
    }



    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
         gestureTouchX = e1.getX();
         gestureTouchUpX = e2.getX();
        Log.d("initialX: "+gestureTouchX,"finalx"+gestureTouchUpX);
         gestureTouchDiff = gestureTouchUpX - gestureTouchX;
startOnScroll =true;
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}