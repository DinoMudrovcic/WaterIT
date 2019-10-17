package com.dinomudrovcic.waterit.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dinomudrovcic.waterit.R;
import com.dinomudrovcic.waterit.models.Weather;
import com.dinomudrovcic.waterit.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ezmuddi on 16.10.2019..
 */

public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherRecyclerViewAdapter.WeatherViewHolder> {

    Context context;
    List<Weather> weatherData;

    public WeatherRecyclerViewAdapter(Context context, List<Weather> weatherData) {
        this.context = context;
        this.weatherData = weatherData;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_weather_report, viewGroup, false);
        WeatherViewHolder holder = new WeatherViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder weatherViewHolder, int position) {

        weatherViewHolder.tvWeatherTime.setText(weatherData.get(position).time);
        weatherViewHolder.tvWeatherTemperature.setText(weatherData.get(position).temperature + Constants.TEMPERATURE_MARK);
        weatherViewHolder.tvWeatherPercipitation.setText(weatherData.get(position).percipitation + Constants.PERCIPITATION_MARK);
        weatherViewHolder.tvWeatherHumidity.setText(weatherData.get(position).humidity + Constants.HUMIDITY_MARK);

        Glide.with(context)
                .load(weatherData.get(position).picture)
                .into(weatherViewHolder.ivWeather);

    }

    @Override
    public int getItemCount() {
        return weatherData.size();
    }

    public static class WeatherViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.ivWeather)
        ImageView ivWeather;

        @BindView(R.id.tvWeatherTime)
        TextView tvWeatherTime;

        @BindView(R.id.tvWeatherTemperature)
        TextView tvWeatherTemperature;

        @BindView(R.id.tvWeatherPercipitation)
        TextView tvWeatherPercipitation;

        @BindView(R.id.tvWeatherHumidity)
        TextView tvWeatherHumidity;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
