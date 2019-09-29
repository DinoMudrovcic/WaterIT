package com.dinomudrovcic.waterit.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by ezmuddi on 27.9.2019..
 */

@IgnoreExtraProperties
public class Valve {
    public String status;

    public Valve(){}

    public Valve(String status) {
        this.status = status;
    }
}
