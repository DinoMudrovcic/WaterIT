package com.dinomudrovcic.waterit.models;

/**
 * Created by ezmuddi on 11.10.2019..
 */

public class Weather {

    public String time;
    public String temperature;
    public String percipitation;
    public String humidity;
    public String picture;
    public String locationCity;
    public String locationCountry;

    public Weather(){}

    public Weather(String time, String temperature, String percipitation, String humidity, String picture, String locationCity, String locationCountry) {
        this.time = time;
        this.temperature = temperature;
        this.percipitation = percipitation;
        this.humidity = humidity;
        this.picture = picture;
        this.locationCity = locationCity;
        this.locationCountry = locationCountry;
    }
}
