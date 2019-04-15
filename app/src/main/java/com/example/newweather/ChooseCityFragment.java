package com.example.newweather;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.newweather.City.HotCity;
import com.example.newweather.Util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChooseCityFragment extends Fragment {
    public String address;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList=new ArrayList<>();
    private View view;
    private Button btn_Search_city;
    private ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        address="https://search.heweather.com/top?group=cn&" +
                "key=0c96c2de712b4ceeab7c1f6df0bd4315&number=50";
        LitePal.getDatabase();
        getCity(address);
        List<HotCity> hotCityList = DataSupport.findAll(HotCity.class);
        for(int i = 0; i<hotCityList.size();i++){
            dataList.add( hotCityList.get(i).getCityName());
            Log.d("HotCity",hotCityList.get(i).getCityName());
        }
        view=inflater.inflate(R.layout.city_choose,container,false);
        listView=view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        btn_Search_city =view.findViewById(R.id.Search_city);
        btn_Search_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), SearchCityActivity.class);
                startActivity(intent);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String city_name=dataList.get(i);
                Toast.makeText(getContext(),"已选择城市："+city_name,Toast.LENGTH_SHORT).show();
                Log.d("this",city_name);

                if (getActivity() instanceof MainActivity){
                    Intent intent=new Intent(getActivity(), WeatherActivity.class);
                    intent.putExtra("cityNameFromIntent",city_name);
                    startActivity(intent);
                    getActivity().finish();
                }else if (getActivity() instanceof WeatherActivity){
                    SharedPreferences.Editor editor= PreferenceManager
                            .getDefaultSharedPreferences(getContext()).edit();
                    editor.putString("cityName",city_name);
                    editor.apply();
                    WeatherActivity activity=(WeatherActivity)getActivity();
                    activity.drawerLayout.closeDrawers();
                    activity.requestWeatherInfo(city_name);
                    activity.getBingPic();

                }

            }
        });



        return view;
    }




    /*
     * 连接服务器获取数据
     * */
    private void getCity(String address){
        showProgerssDialog();

        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                        closeProgressDialog();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responText=response.body().string();
                if(!TextUtils.isEmpty(responText)){
                    try{

                        JSONObject jsonObject=new JSONObject(responText);
                        JSONArray jsonArray_HeWeather=jsonObject.getJSONArray("HeWeather6");
                        JSONObject jsonObject1=jsonArray_HeWeather.getJSONObject(0);
                        JSONArray jsonbasic= jsonObject1.getJSONArray("basic");
                        int length = jsonbasic.length();
                        DataSupport.deleteAll(HotCity.class);
                        for(int i=0;i<length;i++){
                            JSONObject cityLocation =  jsonbasic.getJSONObject(i);
                            HotCity hotCity = new HotCity();
                            hotCity.setCityName(cityLocation.getString("location"));
                            hotCity.save();



                        }




                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }



            }
        });

        closeProgressDialog();

    }

    public void showProgerssDialog(){
        if (progressDialog==null){
            progressDialog=new ProgressDialog(getActivity());
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
