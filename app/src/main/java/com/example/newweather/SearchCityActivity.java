package com.example.newweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.newweather.Util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static java.security.AccessController.getContext;

public class SearchCityActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText input_city;
    private ListView Search_listView;
    private List<String> dataList = new ArrayList<>();
    private ArrayAdapter<String> adapter ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);

        input_city=findViewById(R.id.txt_SearchCity);
        Button search_city_button=findViewById(R.id.btn_SearchCity);
        Search_listView=findViewById(R.id.search_city_List);
        search_city_button.setOnClickListener(this);
        Search_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String search_List_City = dataList.get(position);
                Toast.makeText(SearchCityActivity.this,"已选择城市："+
                        search_List_City,Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(SearchCityActivity.this, WeatherActivity.class);
                intent.putExtra("cityNameFromIntent",search_List_City);
                startActivity(intent);
                SearchCityActivity.this.finish();
            }

        });





    }

    @Override
    public void onClick(View v) {
       switch(v.getId()){
           case R.id.btn_SearchCity :
               String EditText_city=input_city.getText().toString();
               String Edit_addreess="https://search.heweather.com/find?location="+EditText_city
                       +"&key=0c96c2de712b4ceeab7c1f6df0bd4315";
               HttpUtil.sendOkHttpRequest(Edit_addreess, new Callback() {
                   @Override
                   public void onFailure(Call call, IOException e) {
                       e.printStackTrace();
                   }

                   @Override
                   public void onResponse(Call call, Response response) throws IOException {
                       String searchCityResponse = response.body().string();
                       if (!TextUtils.isEmpty(searchCityResponse)){
                           try{
                               JSONObject jsonObject = new JSONObject(searchCityResponse);
                               JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
                               JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                               JSONArray jsonArray1 = jsonObject1.getJSONArray("basic");
                               int length = jsonArray1.length();
                               for(int i=0;i<length;i++){
                                   JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                                   Log.d("SearchActivity",jsonObject2.getString("location"));
                                   dataList.add(jsonObject2.getString("location"));
                               }


                           }catch (Exception e){
                               e.printStackTrace();
                           }
                       }


                   }
               });
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       adapter = new ArrayAdapter<>(SearchCityActivity.this,android.R.layout.simple_list_item_1,dataList);
                       Search_listView.setAdapter(adapter);
                   }
               });
               break;

               default:
                   break;


       }
    }
}
