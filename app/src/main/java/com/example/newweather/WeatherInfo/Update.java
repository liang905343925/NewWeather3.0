package com.example.newweather.WeatherInfo;

import com.google.gson.annotations.SerializedName;

public class Update {

    @SerializedName("loc")
    private String location_time;

    public String getLocation_time() {
        return location_time;
    }

    public void setLocation_time(String location_time) {
        this.location_time = location_time;
    }
}
