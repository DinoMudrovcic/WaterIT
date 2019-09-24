package com.dinomudrovcic.waterit.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

/**
 * Created by ezmuddi on 25.9.2019..
 */

@IgnoreExtraProperties
public class User {
    public String display_name;
    public String email;
    public String full_name_or_company_name;

    public User(){}

    public User(String displayName, String email, String fullNameOrCompanyName){
        this.display_name = displayName;
        this.email = email;
        this.full_name_or_company_name = fullNameOrCompanyName;
    }
}
