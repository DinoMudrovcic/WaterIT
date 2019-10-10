package com.dinomudrovcic.waterit.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by ezmuddi on 27.9.2019..
 */

@IgnoreExtraProperties
public class Valve {
    public String picture;
    public String status;
    public String valveName;
    public String path;

    public Valve(){}

    public Valve(String valveName, String status, String picture) {this.valveName = valveName;this.status = status;this.picture = picture;}

    public Valve(String valveName, String status, String picture, String path) {this.valveName = valveName;this.status = status;this.picture = picture;this.path = path;}
}
