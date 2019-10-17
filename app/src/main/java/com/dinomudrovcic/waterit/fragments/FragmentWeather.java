package com.dinomudrovcic.waterit.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dinomudrovcic.waterit.R;
import com.dinomudrovcic.waterit.adapters.WeatherRecyclerViewAdapter;
import com.dinomudrovcic.waterit.models.Weather;
import com.dinomudrovcic.waterit.utils.Constants;
import com.google.firebase.database.DatabaseReference;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ezmuddi on 9.10.2019..
 */

public class FragmentWeather extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    View v;
    RecyclerView recyclerView;
    WeatherRecyclerViewAdapter adapter;
    List<Weather> weatherData = new ArrayList<>();

    DatabaseReference locationRef;

    @BindView(R.id.weather_refresh)
    SwipeRefreshLayout weather_refresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            v = inflater.inflate(R.layout.weather_fragment, container, false);

            ButterKnife.bind(this, v);
            weather_refresh.setOnRefreshListener(FragmentWeather.this);

            recyclerView = v.findViewById(R.id.weather_recyclerview);
            adapter = new WeatherRecyclerViewAdapter(getContext(), weatherData);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);
        }



        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            getLocationData();
        }
    }

    public void getLocationData() {
        Bundle bundle = getArguments();
        String city = bundle.getString("city");
        String country = bundle.getString("country");

        weatherData = setupWeatherArray(city, country);
    }

    private List<Weather> setupWeatherArray(String city, String country) {

        List<Weather> data = new ArrayList<>();

        String countryReg = getCountryReg(country);

        URL url;
        HttpURLConnection connection = null;

        String urlString = Constants.API_WEATHER_MAIN_URL + "?appid=" + Constants.API_KEY + "&q=" + city + ","
                + countryReg + "&units=" + Constants.TEMPERATURE_UNITS_VALUE;

        try {
            url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(connection.getInputStream());
            JSONParser jsonParser = new JSONParser();
            JSONObject weatherObject = (JSONObject) jsonParser.parse(new InputStreamReader(in, "UTF-8"));
            JSONArray weatherList = (JSONArray) weatherObject.get(Constants.WEATHER_LISTS);
            for(int i = 0; i < 10; i++){
                //get weather object
                JSONObject weatherObjectItem = (JSONObject) weatherList.get(i);

                //get date-time
                String dt = convertDateTime(weatherObjectItem.get(Constants.WEATHER_LISTS_DT_TXT).toString());

                //get main
                JSONObject main = (JSONObject) weatherObjectItem.get(Constants.WEATHER_LISTS_MAIN);
                String temp = main.get(Constants.WEATHER_LISTS_MAIN_TEMP).toString();
                String hum = main.get(Constants.WEATHER_LISTS_MAIN_HUM).toString();

                //get weather
                JSONArray weat = (JSONArray) weatherObjectItem.get(Constants.WEATHER_LISTS_WEATHER);
                JSONObject weatherObj = (JSONObject) weat.get(0);
                String icon = Constants.API_IMG_MAIN_URL + weatherObj.get(Constants.WEATHER_LISTS_WEATHER_ICON).toString()
                        + Constants.API_IMG_EXTENSION;

                //get percipitation
                String rainStr = "0";
                if(weatherObjectItem.containsKey(Constants.WEATHER_LISTS_RAIN)){
                    JSONObject rain = (JSONObject) weatherObjectItem.get(Constants.WEATHER_LISTS_RAIN);
                    rainStr = calculateToCm(rain.get(Constants.WEATHER_LISTS_RAIN_3H).toString());
                }

                data.add(new Weather(dt, temp, rainStr, hum, icon, city, country));
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return data;
    }

    private String calculateToCm(String s) {

        Double percipitation = Double.parseDouble(s);

        return "" + (percipitation * 10);
    }

    private String convertDateTime(String s){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = null;
        try {
            d = sdf.parse(s);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        sdf.applyPattern("dd.MM HH:mm");
        return sdf.format(d).toString();
    }

    private String getCountryReg(String country) {

        String result = "";

        URL url;
        HttpURLConnection connection = null;

        try {
            url = new URL(Constants.API_LOCATION_COUNTRY_MAIN_URL + country);
            connection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(connection.getInputStream());
            JSONParser jsonParser = new JSONParser();
            JSONArray countryObjects = (JSONArray) jsonParser.parse(new InputStreamReader(in, "UTF-8"));
            JSONObject countryObject = (JSONObject) countryObjects.get(0);
            JSONArray languages = (JSONArray) countryObject.get(Constants.LANGUAGES);
            JSONObject language = (JSONObject) languages.get(0);
            result = language.get(Constants.COUNTRY_ISO_CODE).toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }

        return result;
    }

    @Override
    public void onRefresh() {
        //Toast.makeText(getActivity(), "Refresh", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getLocationData();
                weather_refresh.setRefreshing(false);
            }
        }, 2000);
    }
}
