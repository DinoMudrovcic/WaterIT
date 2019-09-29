package com.dinomudrovcic.waterit.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

/**
 * Created by ezmuddi on 27.9.2019..
 */

@IgnoreExtraProperties
public class Location {
    public String city;
    public String state;
    public HashMap<String, SoilMoistureSensor> soil_moisture_sensors;
    public HashMap<String, TemperatureAndHumiditySensor> temperature_and_humidity_sensors;
    public HashMap<String, Valve> valves;
    public HashMap<String, WaterLevelSensor> water_level_sensors;

    public Location(){

    }

    public Location(String city, String state, HashMap<String, SoilMoistureSensor> smSensors,
                    HashMap<String, TemperatureAndHumiditySensor> tmSensors,
                    HashMap<String, Valve> valves, HashMap<String, WaterLevelSensor> wlSensors) {
        this.city = city;
        this.state = state;
        this.soil_moisture_sensors = smSensors;
        this.temperature_and_humidity_sensors = tmSensors;
        this.valves = valves;
        this.water_level_sensors = wlSensors;
    }
}
