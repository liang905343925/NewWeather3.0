package com.example.newweather.WeatherInfo;

import com.google.gson.annotations.SerializedName;

public class Now {
    @SerializedName("cond_txt")
   public  String weather_info_now;

    @SerializedName("tmp")
    private String weather_tmp_now;

    private String wind_deg;
    private String wind_dir;
    private String wind_sc;
    private String wind_spd;

    private String hum;
    private String pcpn;

    private String pres;

 public String getWeather_info_now() {
  return weather_info_now;
 }

 public void setWeather_info_now(String weather_info_now) {
  this.weather_info_now = weather_info_now;
 }

 public String getWeather_tmp_now() {
  return weather_tmp_now;
 }

 public void setWeather_tmp_now(String weather_tmp_now) {
  this.weather_tmp_now = weather_tmp_now;
 }

 public String getWind_deg() {
  return wind_deg;
 }

 public void setWind_deg(String wind_deg) {
  this.wind_deg = wind_deg;
 }

 public String getWind_dir() {
  return wind_dir;
 }

 public void setWind_dir(String wind_dir) {
  this.wind_dir = wind_dir;
 }

 public String getWind_sc() {
  return wind_sc;
 }

 public void setWind_sc(String wind_sc) {
  this.wind_sc = wind_sc;
 }

 public String getWind_spd() {
  return wind_spd;
 }

 public void setWind_spd(String wind_spd) {
  this.wind_spd = wind_spd;
 }

 public String getHum() {
  return hum;
 }

 public void setHum(String hum) {
  this.hum = hum;
 }

 public String getPcpn() {
  return pcpn;
 }

 public void setPcpn(String pcpn) {
  this.pcpn = pcpn;
 }

 public String getPres() {
  return pres;
 }

 public void setPres(String pres) {
  this.pres = pres;
 }

 public String getVis() {
  return vis;
 }

 public void setVis(String vis) {
  this.vis = vis;
 }

 private String vis;


}
