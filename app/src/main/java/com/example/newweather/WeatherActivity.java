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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.newweather.Service.RefreshService;
import com.example.newweather.Util.HttpUtil;
import com.example.newweather.Util.Utility;
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

    private TextView txt_titleCity;
    private TextView txt_titleUpdateTime;
    private TextView txt_now_degree;
    private TextView txt_now_weatherInfo;
    private TextView txt_city_lat;
    private TextView txt_city_lon_;
    private TextView txt_now_hum;
    private TextView txt_now_pcpn;
    private TextView txt_now_pres;
    private TextView txt_now_win_sc;

    private TextView txt_car_wash;
    private TextView txt_sport;
    private TextView txt_comfo;

    private RecyclerView recyclerView;
    private ImageView bingPicImg;


    private ProgressDialog progressDialog;

    public SwipeRefreshLayout swipeRefreshLayout;

    public DrawerLayout drawerLayout;
    private Button navButton;
    private String cityNameInWeatherPre;
    private String Img_URL;




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
        initCompent();
        getWeatherInfo(cityName);
        getBingPic();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String cityName = sharedPreferences.getString("cityName", null);
                getWeatherInfo(cityName);
                getBingPic();
                closeProgressDialog();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });



    }

   public void getBingPic() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String bingPic = sharedPreferences.getString("bingPic",null);
        if (bingPic !=null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else{
            loadBingPic();
        }
    }

    private void loadBingPic() {

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
                            Glide.with(WeatherActivity.this).load(Img_URL).into(bingPicImg);

                        }
                    });

                }

            }
        });
    }

    public void initCompent(){
        weatherLayout = findViewById(R.id.weather_layout);
        txt_titleCity =  findViewById(R.id.title_city);
        txt_titleUpdateTime = findViewById(R.id.title_update_time);
        txt_city_lat = findViewById(R.id.city_lat);
        txt_city_lon_ = findViewById(R.id.city_loc);
        txt_now_degree = findViewById(R.id.degree_text);
        txt_now_weatherInfo = findViewById(R.id.weather_info);
        txt_now_hum = findViewById(R.id.txt_now_hum);
        txt_now_pcpn = findViewById(R.id.txt_now_pcpn);
        txt_now_pres = findViewById(R.id.txt_now_pres);
        txt_now_win_sc = findViewById(R.id.txt_now_wind_sc);
        recyclerView = findViewById(R.id.forecast_recyclerView);
        txt_car_wash = findViewById(R.id.car_wash_text);
        txt_comfo = findViewById(R.id.comfort_text);
        txt_sport = findViewById(R.id.sport_text);
        bingPicImg = findViewById(R.id.bing_pic_img);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_weather);
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

    public void requestWeatherInfo(String cityName) {
        showProgerssDialog();
        String weatherUrl = "https://free-api.heweather.net/s6/weather?location=" + cityName +
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

    public void showWeatherInfo(Weather weather) {
        DecimalFormat format = new DecimalFormat("0.00");
        txt_titleCity.setText(weather.basic.getCityLocation());
        txt_titleUpdateTime.setText(weather.update.getLocation_time());
        txt_now_degree.setText(weather.now.getWeather_tmp_now());
        txt_now_weatherInfo.setText(weather.now.weather_info_now);
        txt_city_lat.setText(format.format(new BigDecimal(weather.basic.getCityLatitude())));
        txt_city_lon_.setText(format.format(new BigDecimal(weather.basic.getCityLongitude())));
        txt_now_hum.setText(weather.now.getHum());
        txt_now_pcpn.setText(weather.now.getPcpn());
        txt_now_pres.setText(weather.now.getPres());
        txt_now_win_sc.setText(weather.now.getWind_sc());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter myAdapter = new MyAdapter(weather.forecastList,this);
        recyclerView.setAdapter(myAdapter);
        recyclerView.addItemDecoration(new MyDividerItemDecoration
                (this, MyDividerItemDecoration.VERTICAL_LIST));

        txt_sport.setText("运动指数:"+weather.suggestionList.get(3).getTxt());
        txt_comfo.setText("舒适度指数："+weather.suggestionList.get(0).getTxt());
        txt_car_wash.setText("洗车指数:"+weather.suggestionList.get(6).getTxt());
        weatherLayout.setVisibility(View.VISIBLE);

        Intent intent = new Intent(this, RefreshService.class);
        startService(intent);

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

}
