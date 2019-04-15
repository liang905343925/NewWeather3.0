package com.example.newweather;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.newweather.WeatherInfo.Forecast;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Forecast> forecastList;
    private Context mContext;

    public MyAdapter(List<Forecast> forecastList,Context mContext){
        this.mContext = mContext;
        this.forecastList = forecastList;
        for (int i = 0;i<forecastList.size();i++){
            Log.d("forecastList:",this.forecastList.get(i).getWind_sc());
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_date;
        private TextView txt_info_day;
        private TextView txt_info_night;
        private TextView txt_temp_max;
        private TextView txt_temp_min;
        private TextView txt_info_hum;
        private TextView txt_info_pcpn;
        private TextView txt_info_pop;
        private TextView txt_info_pres;
        private TextView txt_info_uv_index;
        private TextView txt_info_vis;
        private TextView txt_info_wind_dir;
        private TextView txt_info_wind_sc;

       public  MyViewHolder(View view){
           super(view);
           this.txt_date = view.findViewById(R.id.text_date);
           this.txt_info_day = view.findViewById(R.id.text_info_day);
           this.txt_info_night = view.findViewById(R.id.text_info_night);
           this.txt_temp_max = view.findViewById(R.id.text_info_temp_max);
           this.txt_temp_min = view.findViewById(R.id.text_info_temp_min);
           this.txt_info_hum = view.findViewById(R.id.text_info_hum);
           this.txt_info_pcpn = view.findViewById(R.id.text_info_pcpn);
           this.txt_info_pop = view.findViewById(R.id.text_info_pop);
           this.txt_info_pres = view.findViewById(R.id.text_info_pres);
           this.txt_info_uv_index = view.findViewById(R.id.text_info_uv_index);
           this.txt_info_vis = view.findViewById(R.id.text_info_vis);
           this.txt_info_wind_dir = view.findViewById(R.id.text_info_wind_dir);
           this.txt_info_wind_sc = view.findViewById(R.id.text_info_wind_sc);
       }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.txt_date.setText("日期："+forecastList.get(i).getDate());
        myViewHolder.txt_info_day.setText("白天天气:"+forecastList.get(i).getCond_txt_d());
        myViewHolder.txt_info_night.setText("夜间天气:"+forecastList.get(i).getCond_txt_n());
        myViewHolder.txt_temp_max.setText("最高气温："+forecastList.get(i).getTmp_max());
        myViewHolder.txt_temp_min.setText("最低气温："+forecastList.get(i).getTmp_min());
        myViewHolder.txt_info_hum.setText("相对湿度:"+forecastList.get(i).getHum());
        myViewHolder.txt_info_pcpn.setText("降水量："+forecastList.get(i).getPcpn());
        myViewHolder.txt_info_pop.setText("降水概率："+forecastList.get(i).getPop());
        myViewHolder.txt_info_pres.setText("大气压强："+forecastList.get(i).getPres());
        myViewHolder.txt_info_uv_index.setText("紫外线强度："+forecastList.get(i).getUv_index());
        myViewHolder.txt_info_vis.setText("能见度："+forecastList.get(i).getVis());
        myViewHolder.txt_info_wind_dir.setText("风向："+forecastList.get(i).getWind_dir());
        myViewHolder.txt_info_wind_sc.setText("风力："+forecastList.get(i).getWind_sc());


    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerciew_item,viewGroup,false);
        return new MyViewHolder(view);
    }
}
