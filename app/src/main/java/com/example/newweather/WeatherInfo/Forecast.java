package com.example.newweather.WeatherInfo;



public class Forecast {


   private String date;
   private String cond_txt_d;
   private String tmp_max;
   private String tmp_min;

   private String sr;
   private String ss;

   private String mr;
   private String ms;


   private String uv_index;

   public String getDate() {
      return date;
   }

   public void setDate(String date) {
      this.date = date;
   }

   public String getCond_txt_d() {
      return cond_txt_d;
   }

   public void setCond_txt_d(String cond_txt_d) {
      this.cond_txt_d = cond_txt_d;
   }

   public String getTmp_max() {
      return tmp_max;
   }

   public void setTmp_max(String tmp_max) {
      this.tmp_max = tmp_max;
   }

   public String getTmp_min() {
      return tmp_min;
   }

   public void setTmp_min(String tmp_min) {
      this.tmp_min = tmp_min;
   }

   public String getSr() {
      return sr;
   }

   public void setSr(String sr) {
      this.sr = sr;
   }

   public String getSs() {
      return ss;
   }

   public void setSs(String ss) {
      this.ss = ss;
   }

   public String getMr() {
      return mr;
   }

   public void setMr(String mr) {
      this.mr = mr;
   }

   public String getMs() {
      return ms;
   }

   public void setMs(String ms) {
      this.ms = ms;
   }

   public String getUv_index() {
      return uv_index;
   }

   public void setUv_index(String uv_index) {
      this.uv_index = uv_index;
   }
}
