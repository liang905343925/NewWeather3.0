package com.example.newweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences pre= PreferenceManager.getDefaultSharedPreferences(this);
        String cityName = pre.getString("cityName",null);
        if (cityName!=null){
            Intent intent=new Intent(this,WeatherActivity.class);
            intent.putExtra("cityNameFromIntent",cityName);
            startActivity(intent);
            finish();
        }

    }
}
