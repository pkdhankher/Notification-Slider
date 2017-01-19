package com.dhankher.slider.location;

/**
 * Created by Dhankher on 1/17/2017.
 */

public class Loc {
    Double lat, lng;
    String city;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        String data= "lat: "+lat+"\nlng: "+lng+"\ncity:"+city;
        return data;
    }
}
