package com.example.newweather.WeatherInfo;

import com.google.gson.annotations.SerializedName;

public class Basic {
    @SerializedName("location")
    private String cityLocation;

    @SerializedName("lon")
    private String cityLongitude;

    @SerializedName("lat")
    private String cityLatitude;

    public String getCityLocation() {
        return cityLocation;
    }

    public void setCityLocation(String cityLocation) {
        this.cityLocation = cityLocation;
    }

    public String getCityLongitude() {
        return cityLongitude;
    }

    public void setCityLongitude(String cityLongitude) {
        this.cityLongitude = cityLongitude;
    }

    public String getCityLatitude() {
        return cityLatitude;
    }

    public void setCityLatitude(String cityLatitude) {
        this.cityLatitude = cityLatitude;
    }
}
