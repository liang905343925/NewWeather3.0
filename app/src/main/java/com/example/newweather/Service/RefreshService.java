package com.example.newweather.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;


import com.example.newweather.Util.HttpUtil;
import com.example.newweather.Util.Utility;
import com.example.newweather.WeatherInfo.Weather;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RefreshService extends Service {
    public RefreshService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateAQI();
        updateBingPic();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 60*60*1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this,RefreshService.class);
        PendingIntent pi = PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }


    public void updateWeather(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String cityName = preferences.getString("cityName",null);
        if (cityName !=null){
            String weatherUrl = "https://free-api.heweather.com/s6/weather?location=" + cityName +
                    "&key=0c96c2de712b4ceeab7c1f6df0bd4315";
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    Weather weather = Utility.handleWeatherResponse(responseText);
                    if (weather != null && "ok".equals(weather.status)){
                        SharedPreferences.Editor editor = PreferenceManager.
                        getDefaultSharedPreferences(RefreshService.this).edit();
                        editor.putString("weather",responseText);
                        editor.apply();

                    }

                }
            });
        }

    }


    public void updateAQI(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String cityName = preferences.getString("cityName",null);
        if (cityName != null){
            String AQIUrl = "https://free-api.heweather.com/s6/air/now?location=" + cityName +
                    "&key=0c96c2de712b4ceeab7c1f6df0bd4315";
            HttpUtil.sendOkHttpRequest(AQIUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    Weather weather = Utility.handleWeatherResponse(responseText);
                    if (weather !=null && "ok".equals(weather.status)){
                        SharedPreferences.Editor editor = PreferenceManager.
                                getDefaultSharedPreferences(RefreshService.this).edit();
                        editor.putString("AQI",responseText);
                        editor.apply();
                    }

                }
            });

        }
    }


    public  void updateBingPic(){
        String pic_address="http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";
        HttpUtil.sendOkHttpRequest(pic_address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic = response.body().string();
                SharedPreferences.Editor editor =PreferenceManager.
                        getDefaultSharedPreferences(RefreshService.this).edit();
                editor.putString("bingPic",bingPic);
                editor.apply();

            }
        });
    }
}
