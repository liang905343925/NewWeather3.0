package com.example.newweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.newweather.Service.RefreshService;
import com.example.newweather.Util.HttpUtil;
import com.example.newweather.Util.Utility;
import com.example.newweather.WeatherInfo.AQI;
import com.example.newweather.WeatherInfo.Forecast;
import com.example.newweather.WeatherInfo.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private TextView city_lat_Text;
    private TextView city_lon_Text;
    private TextView Location_tip_Text;

    private TextView wind_deg_tips;
    private TextView wind_deg_show;
    private TextView wind_dir_tips;
    private TextView wind_dir_show;
    private TextView wind_sc_tips;
    private TextView wind_sc_show;
    private TextView wind_spd_tips;
    private TextView wind_spd_show;

    private TextView hum_tips;
    private TextView hum_show;
    private TextView pcpn_tips;
    private TextView pcpn_show;

    private TextView forecast_tips;

    private TextView pres_tips;
    private TextView pres_show;
    private TextView vis_tips;
    private TextView vis_show;

    private LinearLayout forecastLayout;
    private TextView daily_forecast_tips;
    private LinearLayout forecast_sun_Layout;
    private LinearLayout forecast_mood_layout;
    private LinearLayout forecast_tmp_layout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private TextView uvText;

    private ImageView bingPicImg;

    public String Img_URL;

    private ProgressDialog progressDialog;

    public SwipeRefreshLayout swipeRefreshLayout;

    public DrawerLayout drawerLayout;
    private Button navButton;
    private AQI aqi = new AQI();

    private String cityNameInWeatherPre;
    private String cityNameInAQIPre;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT>=21){
            View decorView=getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        Intent intent = getIntent();
        String cityName = intent.getStringExtra("cityNameFromIntent");
        SharedPreferences.Editor editor = PreferenceManager.
                getDefaultSharedPreferences(WeatherActivity.this).edit();
        editor.putString("cityName",cityName);
        editor.apply();

        final SharedPreferences sharedPreferences =PreferenceManager.getDefaultSharedPreferences(this);
       // String cityName = sharedPreferences.getString("cityName",null);
        initComponent();
        getWeatherInfo(cityName);
        getAQIInfo(cityName);
        getBingPic();
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showProgerssDialog();
                String cityName = sharedPreferences.getString("cityName",null);
                getWeatherInfo(cityName);
                getAQIInfo(cityName);
                getBingPic();
                closeProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
            }

        });

    }

    public void initComponent(){
        weatherLayout = findViewById(R.id.weather_layout);

        titleCity = findViewById(R.id.title_city);
        titleUpdateTime = findViewById(R.id.title_update_time);

        degreeText = findViewById(R.id.degree_text);
        weatherInfoText = findViewById(R.id.weather_info);
        city_lat_Text = findViewById(R.id.city_lat);
        city_lon_Text = findViewById(R.id.city_loc);
        Location_tip_Text = findViewById(R.id.Location_tip);

        wind_deg_tips = findViewById(R.id.win_deg_tips);
        wind_deg_show = findViewById(R.id.win_deg_show);
        wind_dir_tips = findViewById(R.id.win_dir_tips);
        wind_dir_show = findViewById(R.id.win_dir_show);
        wind_sc_tips = findViewById(R.id.win_sc_tips);
        wind_sc_show = findViewById(R.id.win_sc_show);
        wind_spd_tips = findViewById(R.id.win_spd_tips);
        wind_spd_show = findViewById(R.id.win_spd_show);

        hum_tips = findViewById(R.id.hum_tips);
        hum_show = findViewById(R.id.hum_show);
        pcpn_tips = findViewById(R.id.pcpn_tips);
        pcpn_show = findViewById(R.id.pcpn_show);

        forecast_tips = findViewById(R.id.forecast_tips);

        pres_tips = findViewById(R.id.pres_tips);
        pres_show = findViewById(R.id.pres_show);
        vis_tips = findViewById(R.id.vis_tips);
        vis_show = findViewById(R.id.vis_show);

        daily_forecast_tips = findViewById(R.id.daily_forecast_tips);
        forecastLayout = findViewById(R.id.forecast_layout);

        forecast_sun_Layout = findViewById(R.id.forecast_sun_time_layout);
        forecast_mood_layout = findViewById(R.id.forecast_mood_time_layout);
        forecast_tmp_layout = findViewById(R.id.forecast_tmp_layout);

        aqiText = findViewById(R.id.aqi_text);
        pm25Text = findViewById(R.id.pm25_text);
        comfortText = findViewById(R.id.comfort_text);
        carWashText = findViewById(R.id.car_wash_text);
        sportText = findViewById(R.id.sport_text);
        uvText = findViewById(R.id.uv_text);
        aqiText=findViewById(R.id.aqi_text);
        pm25Text=findViewById(R.id.pm25_text);
        bingPicImg=findViewById(R.id.bing_pic_img);
        swipeRefreshLayout=findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        drawerLayout=findViewById(R.id.drawer_layout);
        navButton=findViewById(R.id.nav_button);


    }

    public void getWeatherInfo(String cityName){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = sharedPreferences.getString("weather",null);
        if (weatherString != null){
            try {
                JSONObject jsonObject = new JSONObject(weatherString);
                JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                JSONObject jsonObject2 = jsonObject1.getJSONObject("basic");
                cityNameInWeatherPre = jsonObject2.getString("location");
                Log.d("cityNameInWeatherPre",cityNameInWeatherPre);
            }catch (Exception e){
                e.printStackTrace();
            }

            if (cityNameInWeatherPre.equals(cityName)) {
                Weather weather = Utility.handleWeatherResponse(weatherString);
                showWeatherInfo(weather);
            }else{
                weatherLayout.setVisibility(View.INVISIBLE);
                requestWeatherInfo(cityName);
            }

        }else{
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeatherInfo(cityName);


        }


    }

    public void getAQIInfo(String cityName){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String aqiString = sharedPreferences.getString("AQI",null);

        if (aqiString != null){
            try{
            JSONObject jsonObject = new JSONObject(aqiString);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
            JSONObject jsonObject2 = jsonObject1.getJSONObject("basic");
            cityNameInAQIPre = jsonObject2.getString("location");
            JSONObject jsonObject3 = jsonObject1.getJSONObject("air_now_city");
            aqi.setAqi(jsonObject3.getString("aqi"));
            aqi.setPm25(jsonObject3.getString("pm25"));
            Log.d("cityAqiPreInfo",cityNameInAQIPre);
            }catch (Exception e){
                e.printStackTrace();
            }

            if (cityNameInAQIPre.equals(cityName)) {

                showAQIInfo();
            }else {
                requestAQIInfo(cityName);
            }

        }else{
            requestAQIInfo(cityName);
        }


    }

    public void getBingPic(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Img_URL = sharedPreferences.getString("bingPic",null);
        if (Img_URL != null){
            showBingpic();
        }else {
            requestBingImg();
        }
    }

    public void requestWeatherInfo(String cityName){
        showProgerssDialog();
        String weatherUrl = "https://free-api.heweather.com/s6/weather?location=" + cityName +
                "&key=0c96c2de712b4ceeab7c1f6df0bd4315";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(WeatherActivity.this, "获取天气失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            closeProgressDialog();
                            SharedPreferences.Editor editor = PreferenceManager.
                                    getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weather);

                        } else {
                            Toast.makeText(WeatherActivity.this, "天气信息加载失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


    }

    public void requestAQIInfo(String cityName){

        String AQIUrl = "https://free-api.heweather.com/s6/air/now?location=" + cityName +
                "&key=0c96c2de712b4ceeab7c1f6df0bd4315";

        HttpUtil.sendOkHttpRequest(AQIUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String responseText = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.
                        getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("AQI",responseText);
                editor.apply();
                try {
                    JSONObject jsonObject = new JSONObject(responseText);
                    JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("air_now_city");
                    aqi.setAqi(jsonObject2.getString("aqi"));
                    aqi.setPm25(jsonObject2.getString("pm25"));
                    Log.d("WeatherActivity",jsonObject2.getString("aqi"));

                }catch (Exception e){
                    e.printStackTrace();

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (aqi != null ) {
                            showAQIInfo();



                        } else {
                            Toast.makeText(WeatherActivity.this, "AQI信息加载失败", Toast.LENGTH_SHORT).show();
                        }


                    }
                });


            }
        });
    }

    public void requestBingImg(){


        String pic_address="http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";
        HttpUtil.sendOkHttpRequest(pic_address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bingPic_response=response.body().string();
                if (!TextUtils.isEmpty(bingPic_response)){
                    try{
                        JSONObject jsonObject=new JSONObject(bingPic_response);
                        JSONArray bing_images=jsonObject.getJSONArray("images");
                        JSONObject bing_imges_object=bing_images.getJSONObject(0);
                        String bing_Pic_Url=bing_imges_object.getString("url");
                        Log.d("pic",bing_Pic_Url);
                        Img_URL="http://s.cn.bing.net"+bing_Pic_Url;
                        SharedPreferences.Editor editor = PreferenceManager.
                                getDefaultSharedPreferences(WeatherActivity.this).edit();
                        editor.putString("bingPic",Img_URL);
                        editor.apply();




                    }catch (JSONException e){
                        e.printStackTrace();

                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showBingpic();

                        }
                    });

                }

            }
        });

    }

    public void showProgerssDialog(){
        if (progressDialog==null){
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("正在加载中....");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();

    }

    public void closeProgressDialog(){
        if (progressDialog!=null){
            progressDialog.dismiss();
        }

    }


    public void showWeatherInfo( Weather weather){
        DecimalFormat format = new DecimalFormat("0.00");
        titleCity.setText(weather.basic.getCityLocation());
        titleUpdateTime.setText(weather.update.getLocation_time());
        degreeText.setText(weather.now.getWeather_tmp_now());
        weatherInfoText.setText(weather.now.getWeather_info_now());
        city_lat_Text.setText(format.format(new BigDecimal(weather.basic.getCityLatitude())));
        city_lon_Text.setText(format.format(new BigDecimal(weather.basic.getCityLongitude())));
        Location_tip_Text.setText("经纬度：");
        wind_deg_tips.setText("风向角度：");
        wind_deg_show.setText(weather.now.getWind_deg());
        wind_dir_tips.setText("方向：");
        wind_dir_show.setText(weather.now.getWind_dir());
        wind_sc_tips.setText("风力：");
        wind_sc_show.setText(weather.now.getWind_sc());
        wind_spd_tips.setText("风速：");
        wind_spd_show.setText(weather.now.getWind_spd() + "km/h");
        hum_tips.setText("相对湿度：");
        hum_show.setText(weather.now.getHum());
        pcpn_tips.setText("降水量：");
        pcpn_show.setText(weather.now.getPcpn());
        forecast_tips.setText("实时天气：");
        pres_tips.setText("大气压强：");
        pres_show.setText(weather.now.getPres());
        vis_tips.setText("能见度：");
        vis_show.setText(weather.now.getVis() + "km");


        //三天预报
        daily_forecast_tips.setText("三天预报：");
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {

            View view = LayoutInflater.from(this).inflate
                    (R.layout.forecast_item, forecastLayout, false);
            TextView dateText = view.findViewById(R.id.date_text);
            TextView infoText = view.findViewById(R.id.info_text);
            dateText.setText(forecast.getDate());
            infoText.setText(forecast.getCond_txt_d());

            View tmpview = LayoutInflater.from(this).
                    inflate(R.layout.forecast_tmp_layout, forecastLayout, false);
            TextView max_tips = tmpview.findViewById(R.id.max_tips);
            TextView maxText = tmpview.findViewById(R.id.max_text);
            TextView min_tips = tmpview.findViewById(R.id.min_tips);
            TextView minText = tmpview.findViewById(R.id.min_text);
            max_tips.setText("最高温度：");
            maxText.setText(forecast.getTmp_max());
            min_tips.setText("最低温度：");
            minText.setText(forecast.getTmp_min());

            View sunview = LayoutInflater.from(this).
                    inflate(R.layout.forecast_sun_time_layout, forecastLayout, false);
            TextView sun_up_tips = sunview.findViewById(R.id.sun_up_tips);
            TextView sun_up_text = sunview.findViewById(R.id.sun_up_text);
            TextView sun_down_tips = sunview.findViewById(R.id.sun_down_tips);
            TextView sun_down_text = sunview.findViewById(R.id.sun_down_text);
            sun_up_tips.setText("日出时间：");
            sun_up_text.setText(forecast.getSr());
            sun_down_tips.setText("日落时间：");
            sun_down_text.setText(forecast.getSs());

            View moodview = LayoutInflater.from(this).
                    inflate(R.layout.forecast_mood_time_layout, forecastLayout, false);
            TextView mood_up_tips = moodview.findViewById(R.id.mood_up_tips);
            TextView mood_up_text = moodview.findViewById(R.id.mood_up_text);
            TextView mood_down_tips = moodview.findViewById(R.id.mood_down_tips);
            TextView mood_down_text = moodview.findViewById(R.id.mood_down_text);
            mood_up_tips.setText("月出时间：");
            mood_up_text.setText(forecast.getMr());
            mood_down_tips.setText("月落时间：");
            mood_down_text.setText(forecast.getMs());

            forecastLayout.addView(view);
            forecastLayout.addView(tmpview);
            forecastLayout.addView(sunview);
            forecastLayout.addView(moodview);
        }

        String comfort = "舒适度：" + weather.suggestionList.get(0).getTxt();
        String carWash = "洗车指数：" + weather.suggestionList.get(6).getTxt();
        String sport = "运动指数：" + weather.suggestionList.get(3).getTxt();
        String uv_info = "紫外线指数：" + weather.suggestionList.get(5).getTxt();
        sportText.setText(sport);
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        uvText.setText(uv_info);

        weatherLayout.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, RefreshService.class);
        startService(intent);

    }

    public void showAQIInfo(){
        aqiText.setText(aqi.getAqi());
        pm25Text.setText(aqi.getPm25());
    }

    public void showBingpic(){
        Glide.with(WeatherActivity.this).load(Img_URL).into(bingPicImg);
    }


}
