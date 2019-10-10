package com.dinomudrovcic.waterit.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by ezmuddi on 27.9.2019..
 */

@IgnoreExtraProperties
public class SoilMoistureSensor {
    public Double humidity;
    public String picture;
    public String sensorName;
    public String path;

    public SoilMoistureSensor(){

    }

    public SoilMoistureSensor(Double humidity){
        this.humidity = humidity;
    }

    public SoilMoistureSensor(Double humidity, String picture, String sensorName, String path) {
        this.humidity = humidity;
        this.picture = picture;
        this.sensorName = sensorName;
        this.path = path;
    }
}
