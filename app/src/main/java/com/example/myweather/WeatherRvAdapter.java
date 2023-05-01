package com.example.myweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherRvAdapter extends RecyclerView.Adapter<WeatherRvAdapter.ViewHolder> {

    private Context context;
    private ArrayList<WeatherRvModel> weatherRvModelArrayList;

    public WeatherRvAdapter(Context context, ArrayList<WeatherRvModel> weatherRvModelArrayList) {
        this.context = context;
        this.weatherRvModelArrayList = weatherRvModelArrayList;
    }

    @NonNull
    @Override
    public WeatherRvAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.weather_rv_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRvAdapter.ViewHolder holder, int position) {
        WeatherRvModel model = weatherRvModelArrayList.get(position);
        if(model.isC())
        {
            holder.temp_tv.setText(String.format("%s°C", model.getTemp()));
        }
        else{
            holder.temp_tv.setText(String.format("%s°F", model.getTemp()));
        }
        Picasso.get().load("http:".concat(model.getIcon())).into(holder.condition_iv);
        if(model.isKmh()){
            holder.wind_tv.setText(String.format("%sKm/h", model.getWindSpeed()));
        }
        else{
            holder.wind_tv.setText(String.format("%sMph", model.getWindSpeed()));
        }
        if(model.getDay()==1){
            //DAY
            holder.relativeLayout45.setBackgroundResource(R.drawable.card_back);
        }
        else{
            holder.relativeLayout45.setBackgroundResource(R.drawable.card_back_night);
        }
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output = new SimpleDateFormat(" hh:mm aa");
        try{
            Date t = input.parse(model.getTime());
            holder.time_tv.setText(output.format(t));
        }catch (ParseException e){
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return weatherRvModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView wind_tv,temp_tv,time_tv;
        ImageView condition_iv;
        RelativeLayout relativeLayout45;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            wind_tv = itemView.findViewById(R.id.tv_windspeed);
            temp_tv = itemView.findViewById(R.id.tv_temp);
            time_tv = itemView.findViewById(R.id.tv_time);
            condition_iv = itemView.findViewById(R.id.iv_condition);
            relativeLayout45 = itemView.findViewById(R.id.relativelayout_card2);

        }
    }
}
