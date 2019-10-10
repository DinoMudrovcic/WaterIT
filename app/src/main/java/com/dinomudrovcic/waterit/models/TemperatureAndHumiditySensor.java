package com.dinomudrovcic.waterit.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by ezmuddi on 27.9.2019..
 */

@IgnoreExtraProperties
public class TemperatureAndHumiditySensor {
    public Double temperature;
    public Double humidity;
    public String picture;
    public String sensorName;

    public TemperatureAndHumiditySensor(){}

    public TemperatureAndHumiditySensor(Double temp, Double hum, String pic, String name){
        this.temperature = temp;
        this.humidity = hum;
        this.picture = pic;
        this.sensorName = name;
    }
}
