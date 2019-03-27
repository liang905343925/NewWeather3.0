package com.example.newweather.WeatherInfo;

import com.google.gson.annotations.SerializedName;

public class Now {
    @SerializedName("cond_txt")
    private String weather_info_now;

    @SerializedName("tmp")
    private String weather_tmp_now;

    private String wind_deg;
    private String wind_dir;
    private String wind_sc;
    private String wind_spd;

    private String hum;
    private String pcpn;

    private String pres;
    private String vis;

    public String getPres() {
        return pres;
    }

    public String getVis() {
        return vis;
    }

    public String getWeather_info_now() {
        return weather_info_now;
    }

    public String getWeather_tmp_now() {
        return weather_tmp_now;
    }

    public String getWind_deg() {
        return wind_deg;
    }

    public String getWind_dir() {
        return wind_dir;
    }

    public String getWind_sc() {
        return wind_sc;
    }

    public String getWind_spd() {
        return wind_spd;
    }

    public String getHum() {
        return hum;
    }

    public String getPcpn() {
        return pcpn;
    }
}
