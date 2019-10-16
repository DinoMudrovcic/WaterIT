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
import android.widget.Toast;

import com.dinomudrovcic.waterit.R;
import com.dinomudrovcic.waterit.adapters.WeatherRecyclerViewAdapter;
import com.dinomudrovcic.waterit.models.Location;
import com.dinomudrovcic.waterit.models.Weather;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


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

    private final static String API_KEY = "04d8b1aadab3d9c69ade6b57542d845a";
    private final static String API_WEATHER_MAIN_URL = "http://api.openweathermap.org/data/2.5/forecast/";
    private final static String API_LOCATION_COUNTRY_MAIN_URL = "http://restcountries.eu/rest/v2/name/";
    private final static String LANGUAGES = "languages";
    private final static String COUNTRY_ISO_CODE = "iso639_1";
    private final static String API_IMG_MAIN_URL = "http://openweathermap.org/img/wn/";
    private final static String API_IMG_EXTENSION = "@2x.png";
    private final static String WEATHER_LISTS = "list";
    private final static String WEATHER_LISTS_MAIN = "main";
    private final static String WEATHER_LISTS_MAIN_TEMP = "temp";
    private final static String WEATHER_LISTS_MAIN_HUM = "humidity";
    private final static String WEATHER_LISTS_WEATHER = "weather";
    private final static String WEATHER_LISTS_WEATHER_ICON = "icon";
    private final static String WEATHER_LISTS_DT_TXT = "dt_txt";
    private final static String WEATHER_LISTS_RAIN = "rain";
    private final static String WEATHER_LISTS_RAIN_3H = "3h";
    private final static double KELVIN_TO_CELSIUS = 273.15;
    private final static String TEMPERATURE_UNITS_VALUE = "metric";


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

        String urlString = API_WEATHER_MAIN_URL + "?appid=" + API_KEY + "&q=" + city + ","
                + countryReg + "&units=" + TEMPERATURE_UNITS_VALUE;

        try {
            url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(connection.getInputStream());
            JSONParser jsonParser = new JSONParser();
            JSONObject weatherObject = (JSONObject) jsonParser.parse(new InputStreamReader(in, "UTF-8"));
            JSONArray weatherList = (JSONArray) weatherObject.get(WEATHER_LISTS);
            for(int i = 0; i < 10; i++){
                //get weather object
                JSONObject weatherObjectItem = (JSONObject) weatherList.get(i);

                //get date-time
                String dt = convertDateTime(weatherObjectItem.get(WEATHER_LISTS_DT_TXT).toString());

                //get main
                JSONObject main = (JSONObject) weatherObjectItem.get(WEATHER_LISTS_MAIN);
                String temp = main.get(WEATHER_LISTS_MAIN_TEMP).toString();
                String hum = main.get(WEATHER_LISTS_MAIN_HUM).toString();

                //get weather
                JSONArray weat = (JSONArray) weatherObjectItem.get(WEATHER_LISTS_WEATHER);
                JSONObject weatherObj = (JSONObject) weat.get(0);
                String icon = API_IMG_MAIN_URL + weatherObj.get(WEATHER_LISTS_WEATHER_ICON).toString()
                        + API_IMG_EXTENSION;

                //get percipitation
                String rainStr = "0";
                if(weatherObjectItem.containsKey(WEATHER_LISTS_RAIN)){
                    JSONObject rain = (JSONObject) weatherObjectItem.get(WEATHER_LISTS_RAIN);
                    rainStr = calculateToCm(rain.get(WEATHER_LISTS_RAIN_3H).toString());
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
            url = new URL(API_LOCATION_COUNTRY_MAIN_URL + country);
            connection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(connection.getInputStream());
            JSONParser jsonParser = new JSONParser();
            JSONArray countryObjects = (JSONArray) jsonParser.parse(new InputStreamReader(in, "UTF-8"));
            JSONObject countryObject = (JSONObject) countryObjects.get(0);
            JSONArray languages = (JSONArray) countryObject.get(LANGUAGES);
            JSONObject language = (JSONObject) languages.get(0);
            result = language.get(COUNTRY_ISO_CODE).toString();
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
