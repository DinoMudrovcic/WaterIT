package com.dinomudrovcic.waterit.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by ezmuddi on 27.9.2019..
 */

@IgnoreExtraProperties
public class WaterLevelSensor {
    public Double level;

    public WaterLevelSensor() {}

    public WaterLevelSensor(Double level) {
        this.level = level;
    }
}
