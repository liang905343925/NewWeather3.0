package com.example.newweather.City;

import org.litepal.crud.DataSupport;

public class HotCity extends DataSupport {
    private String cityName;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
