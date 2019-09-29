package com.dinomudrovcic.waterit.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by ezmuddi on 27.9.2019..
 */

@IgnoreExtraProperties
public class SoilMoistureSensor {
    public Double humidity;

    public SoilMoistureSensor(){

    }

    public SoilMoistureSensor(Double humidity){
        this.humidity = humidity;
    }
}
