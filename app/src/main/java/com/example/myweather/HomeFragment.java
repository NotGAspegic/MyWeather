package com.example.myweather;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.matteobattilana.weather.PrecipType;
import com.github.matteobattilana.weather.WeatherView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment {


    View view;
    RelativeLayout relativeLayout, relativeLayout1;
    TextView cityName_tv, temp_tv, condition_tv, feels_tv, date_tv, wind_tv, hum_tv, rain_tv;
    ImageView backIv, iconIv;
    RecyclerView weatherRV;
    ArrayList<WeatherRvModel> weatherRvModelArrayList;
    WeatherRvAdapter weatherRvAdapter;
    public String city_Name, name;
    String myString;
    LottieAnimationView iconAnim, lottieAnimationView;
    WeatherView weatherView;
    SharedPreferences preferences;
    MainActivity mainActivity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            showBottomDialog();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mainActivity = (MainActivity) getActivity();
        //ERROR mainActivity.binding.bottomNavigationView.setSelectedItemId(R.id.home);
        mainActivity.binding.fab.setVisibility(View.VISIBLE);
        mainActivity.binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomDialog();
            }
        });

        relativeLayout = view.findViewById(R.id.rl_home2);
        cityName_tv = view.findViewById(R.id.city_name);
        temp_tv = view.findViewById(R.id.temp_city);
        condition_tv = view.findViewById(R.id.condition_city);
        feels_tv = view.findViewById(R.id.feels_like);
        date_tv = view.findViewById(R.id.current_date);
        backIv = view.findViewById(R.id.iv_bg);
        iconIv = view.findViewById(R.id.icon_weather);
        iconAnim = view.findViewById(R.id.icon_anim);
        wind_tv = view.findViewById(R.id.windkmh);
        hum_tv = view.findViewById(R.id.humidityperc);
        rain_tv = view.findViewById(R.id.rain_amount);
        weatherRV = view.findViewById(R.id.rv_weather);
        weatherRvModelArrayList = new ArrayList<>();
        weatherRvAdapter = new WeatherRvAdapter(getActivity(), weatherRvModelArrayList);
        weatherRV.setAdapter(weatherRvAdapter);
        relativeLayout1 = view.findViewById(R.id.relativelayout_card1);
        weatherView = view.findViewById(R.id.weather_view);
        lottieAnimationView = view.findViewById(R.id.sparkrain2);
        lottieAnimationView.playAnimation();
        lottieAnimationView.loop(true);

        // TODO: 4/7/2023 ADD CURRENT LOCATION THROUGH MAIN ACTIVITY OR HERE
        city_Name = "Manouba";
        preferences = mainActivity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        getWeatherInfo(city_Name);

        return view;
    }

    public void getWeatherInfo(String cityName) {
        String url = "https://api.weatherapi.com/v1/forecast.json?key=896b83b8da5f4f49955171938232903&q=" + cityName + "&days=1&aqi=yes&alerts=yes";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                lottieAnimationView.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);
                weatherRvModelArrayList.clear();
                String unit_speed = preferences.getString("unity_speed_pref","Km/h");
                String unit_temp = preferences.getString("unity_temp_pref","°C");
                try {
                    String country = response.getJSONObject("location").getString("country");
                    String name = response.getJSONObject("location").getString("name");
                    cityName_tv.setText(String.format("%s , %s", name, country));
                    String temper = response.getJSONObject("current").getString("temp_c");
                    String temper_f = response.getJSONObject("current").getString("temp_f");
                    if(unit_temp.equals("°C")){
                        temp_tv.setText(String.format("%s°", temper));
                    }
                    else{
                        temp_tv.setText(String.format("%s°", temper_f));
                    }
                    int isDay = response.getJSONObject("current").getInt("is_day");
                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    Window window = getActivity().getWindow();


                    if (isDay == 1) {
                        //morning
                        backIv.setImageResource(R.drawable.gradient_day);//gradient_day
                        relativeLayout1.setBackgroundResource(R.drawable.card_back);
                        window.setStatusBarColor(getResources().getColor(R.color.DayV2));//DayV2
                        switch (condition) {
                            case "Sunny":
                                iconAnim.setAnimation(R.raw.weatherdayclearsky);
                                iconAnim.loop(true);
                                iconAnim.playAnimation();
                                weatherView.setWeatherData(PrecipType.CLEAR);
                                condition_tv.setText("Sunny");
                                break;
                            case "Clear":
                                iconAnim.setAnimation(R.raw.weathernightclearsky);
                                iconAnim.loop(true);
                                iconAnim.playAnimation();
                                weatherView.setWeatherData(PrecipType.CLEAR);
                                condition_tv.setText("Clear");
                                break;
                            case "Partly cloudy":
                            case "Cloudy":
                            case "Overcast":
                                iconAnim.setAnimation(R.raw.weatherdayscatteredclouds);
                                iconAnim.loop(true);
                                iconAnim.playAnimation();
                                weatherView.setWeatherData(PrecipType.CLEAR);
                                condition_tv.setText("Cloudy");
                                break;
                            case "Mist":
                            case "Fog":
                            case "Freezing fog":
                                iconAnim.setAnimation(R.raw.weatherdaymist);
                                iconAnim.loop(true);
                                iconAnim.playAnimation();
                                condition_tv.setText("Fog/Mist");
                                weatherView.setWeatherData(PrecipType.CLEAR);
                                break;
                            case "Patchy light rain with thunder":
                            case "Moderate or heavy rain with thunder":
                            case "Thundery outbreaks possible":
                                iconAnim.setAnimation(R.raw.weatherdaythunderstorm);
                                iconAnim.loop(true);
                                iconAnim.playAnimation();
                                weatherView.setWeatherData(PrecipType.RAIN);
                                condition_tv.setText("Rain/Thunder");
                                break;
                            case "Patchy snow possible":
                            case "Patchy sleet possible":
                            case "Blowing snow":
                            case "Blizzard":
                            case "Patchy light snow":
                            case "Light snow":
                            case "Patchy moderate snow":
                            case "Moderate snow":
                            case "Patchy heavy snow":
                            case "Heavy snow":
                            case "Ice pellets":
                            case "Light snow showers":
                            case "Moderate or heavy snow showers":
                            case "Light showers of ice pellets":
                            case "Patchy light snow with thunder":
                            case "Moderate or heavy showers of ice pellets":
                            case "Moderate or heavy snow with thunder":
                                iconAnim.setAnimation(R.raw.weatherdaysnow);
                                iconAnim.loop(true);
                                iconAnim.playAnimation();
                                weatherView.setWeatherData(PrecipType.SNOW);
                                condition_tv.setText("Snow/Rain");
                                break;
                            default:
                                iconAnim.setAnimation(R.raw.weatherdayrain);
                                iconAnim.loop(true);
                                iconAnim.playAnimation();
                                weatherView.setWeatherData(PrecipType.RAIN);
                                condition_tv.setText("Rain/Wind");
                        }
                    } else {
                        //night
                        backIv.setImageResource(R.drawable.gradient_night);//gradient_night
                        window.setStatusBarColor(getResources().getColor(R.color.NightV2));//Nightv2
                        relativeLayout1.setBackgroundResource(R.drawable.card_back_night);
                        switch (condition) {
                            case "Sunny":
                                iconAnim.setAnimation(R.raw.weatherdayclearsky);
                                iconAnim.loop(true);
                                iconAnim.playAnimation();
                                weatherView.setWeatherData(PrecipType.CLEAR);
                                condition_tv.setText("Sunny");
                                break;
                            case "Clear":
                                iconAnim.setAnimation(R.raw.weathernightclearsky);
                                iconAnim.loop(true);
                                iconAnim.playAnimation();
                                weatherView.setWeatherData(PrecipType.CLEAR);
                                condition_tv.setText("Clear");
                                break;
                            case "Partly cloudy":
                            case "Cloudy":
                            case "Overcast":
                                iconAnim.setAnimation(R.raw.weathernightscatteredclouds);
                                iconAnim.loop(true);
                                iconAnim.playAnimation();
                                weatherView.setWeatherData(PrecipType.CLEAR);
                                condition_tv.setText("Cloudy");
                                break;
                            case "Mist":
                            case "Fog":
                            case "Freezing fog":
                                iconAnim.setAnimation(R.raw.weathernightmist);
                                iconAnim.loop(true);
                                iconAnim.playAnimation();
                                weatherView.setWeatherData(PrecipType.CLEAR);
                                condition_tv.setText("Fog/Mist");
                                break;
                            case "Patchy light rain with thunder":
                            case "Moderate or heavy rain with thunder":
                            case "Thundery outbreaks possible":
                                iconAnim.setAnimation(R.raw.weathernightthunderstorm);
                                iconAnim.loop(true);
                                iconAnim.playAnimation();
                                weatherView.setWeatherData(PrecipType.RAIN);
                                condition_tv.setText("Rain/Thunder");
                                break;
                            case "Patchy snow possible":
                            case "Patchy sleet possible":
                            case "Blowing snow":
                            case "Blizzard":
                            case "Patchy light snow":
                            case "Light snow":
                            case "Patchy moderate snow":
                            case "Moderate snow":
                            case "Patchy heavy snow":
                            case "Heavy snow":
                            case "Ice pellets":
                            case "Light snow showers":
                            case "Moderate or heavy snow showers":
                            case "Light showers of ice pellets":
                            case "Patchy light snow with thunder":
                            case "Moderate or heavy showers of ice pellets":
                            case "Moderate or heavy snow with thunder":
                                iconAnim.setAnimation(R.raw.weathernightsnow);
                                iconAnim.loop(true);
                                iconAnim.playAnimation();
                                weatherView.setWeatherData(PrecipType.SNOW);
                                condition_tv.setText("Snow/Rain");
                                break;
                            default:
                                iconAnim.setAnimation(R.raw.weathernightrain);
                                iconAnim.loop(true);
                                iconAnim.playAnimation();
                                weatherView.setWeatherData(PrecipType.RAIN);
                                condition_tv.setText("Rain/Wind");
                        }
                    }


                    String feels = response.getJSONObject("current").getString("feelslike_c");
                    String feels_f = response.getJSONObject("current").getString("feelslike_f");
                    if(unit_temp.equals("°C")){
                        feels_tv.setText(String.format("Feels like %s°C", feels));
                    }
                    else{
                        feels_tv.setText(String.format("Feels like %s°F", feels_f));
                    }


                    String dates = response.getJSONObject("location").getString("localtime");
                    SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                    SimpleDateFormat output = new SimpleDateFormat("EEE, d MMM hh:mm aa");
                    try {
                        Date t = input.parse(dates);
                        date_tv.setText(output.format(t));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }


                    String windes = response.getJSONObject("current").getString("wind_kph");
                    String windes_mph = response.getJSONObject("current").getString("wind_mph");
                    if(unit_speed.equals("Km/h")){
                        wind_tv.setText(String.format("%s km/h", windes));
                    }
                    else{
                        wind_tv.setText(String.format("%s mph", windes_mph));
                    }


                    String humis = response.getJSONObject("current").getString("humidity");
                    hum_tv.setText(String.format("%s %%", humis));


                    String rainc = response.getJSONObject("current").getString("precip_mm");
                    String rainc_in = response.getJSONObject("current").getString("precip_in");
                    if(unit_speed.equals("Km/h")){
                        rain_tv.setText(String.format("%s mm", rainc));
                    }
                    else{
                        rain_tv.setText(String.format("%s in", rainc_in));
                    }


                    JSONObject forecastObj = response.getJSONObject("forecast");
                    JSONObject forcast0 = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArray = forcast0.getJSONArray("hour");

                    for (int i = 0; i < hourArray.length(); i++) {
                        JSONObject hourObj = hourArray.getJSONObject(i);
                        String time = hourObj.getString("time");
                        String tempers_c = hourObj.getString("temp_c");
                        String tempers_f = hourObj.getString("temp_f");
                        String img = hourObj.getJSONObject("condition").getString("icon");
                        String wind = hourObj.getString("wind_kph");
                        String wind_mph = hourObj.getString("wind_mph");


                        if(isDay==1)
                        {
                            //DAY
                            //C UNIT
                            if(unit_temp.equals("°C")){
                                if(unit_speed.equals("Km/h")){
                                    weatherRvModelArrayList.add(new WeatherRvModel(time, tempers_c, img, wind,1,true,true));
                                }
                                else{
                                    weatherRvModelArrayList.add(new WeatherRvModel(time, tempers_c, img, wind_mph,1,false,true));
                                }
                            }
                            else{
                                //F unit
                                    if(unit_speed.equals("Km/h")){
                                        weatherRvModelArrayList.add(new WeatherRvModel(time, tempers_f, img, wind,1,true,false));
                                    }
                                    else{
                                        weatherRvModelArrayList.add(new WeatherRvModel(time, tempers_f, img, wind_mph,1,false,false));
                                    }
                            }
                        }
                        else {
                            //NIGHT
                            //C UNIT
                            if(unit_temp.equals("°C")){
                                if(unit_speed.equals("Km/h")){
                                    weatherRvModelArrayList.add(new WeatherRvModel(time, tempers_c, img, wind,0,true,true));
                                }
                                else{
                                    weatherRvModelArrayList.add(new WeatherRvModel(time, tempers_c, img, wind_mph,0,false,true));
                                }
                            }
                            else{
                                //F unit
                                if(unit_speed.equals("Km/h")){
                                    weatherRvModelArrayList.add(new WeatherRvModel(time, tempers_f, img, wind,0,true,false));
                                }
                                else{
                                    weatherRvModelArrayList.add(new WeatherRvModel(time, tempers_f, img, wind_mph,0,false,false));
                                }
                            }
                        }
                    }
                    weatherRvAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Please enter valid city Name...", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);


    }

    private void showBottomDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_layout);


        ImageView cancelButton = dialog.findViewById(R.id.cancel_button);
        SearchView searchView = dialog.findViewById(R.id.searchView);
        if (getArguments() != null) {
            myString = getArguments().getString("City");
            searchView.setQuery(myString, true);
        }
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter city name", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    lottieAnimationView.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.GONE);
                    getWeatherInfo(query);
                }
                // TODO: 4/7/2023 change
                if(preferences.getBoolean("notify_check",true)){
                    pushWeatherNotif(cityName_tv.getText().toString(),temp_tv.getText().toString(),R.drawable.myweather);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }

    private void pushWeatherNotif(String name, String temp, int icon_id) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "weather");
        builder.setContentTitle(name);
        builder.setContentText(temp);
        builder.setSmallIcon(icon_id);
        builder.setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getActivity());
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManagerCompat.notify(1, builder.build());
        }
    }

}