package com.dinomudrovcic.waterit.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by ezmuddi on 27.9.2019..
 */

@IgnoreExtraProperties
public class WaterLevelSensor {
    public Double level;
    public String picture;
    public String sensorName;
    public String path;

    public WaterLevelSensor() {}

    public WaterLevelSensor(Double level, String picture, String sensorName, String path) {
        this.level = level;
        this.picture = picture;
        this.sensorName = sensorName;
        this.path = path;
    }
}

