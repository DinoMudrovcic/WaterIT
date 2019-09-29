package com.dinomudrovcic.waterit;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import com.dinomudrovcic.waterit.models.Location;
import com.dinomudrovcic.waterit.models.SoilMoistureSensor;
import com.dinomudrovcic.waterit.models.TemperatureAndHumiditySensor;
import com.dinomudrovcic.waterit.models.User;
import com.dinomudrovcic.waterit.models.Valve;
import com.dinomudrovcic.waterit.models.WaterLevelSensor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity {

    boolean doubleBackToExitPressedOnce = false;

    @BindView(R.id.tvKorisnik)
    TextView tvKorisnik;

    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        dbRef = FirebaseDatabase.getInstance().getReference();

        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("/users/" + UID);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    User user = dataSnapshot.getValue(User.class);

                    String outputText = "Ulogirani korisnik je: " + user.display_name
                            + "\nEmail: " + user.email;

                    for(String location : user.locations.keySet()){
                        outputText += "\nLokacija: ";
                        outputText += location;

                        for(Location loc : user.locations.values()){
                            outputText += "\nGrad: ";
                            outputText += loc.city;
                            outputText += "\nDrzava: ";
                            outputText += loc.state;
                            outputText += "\nSenzori za zemlju: ";
                            int i = 0;
                            for(SoilMoistureSensor sms : loc.soil_moisture_sensors.values()){
                                i++;
                                outputText += "\nSenzor" + i + ": ";
                                outputText += "\nvlaznost " + sms.humidity;
                            }
                            outputText += "\nSenzori za zrak: ";
                            i = 0;
                            for(TemperatureAndHumiditySensor ths : loc.temperature_and_humidity_sensors.values()){
                                i++;
                                outputText += "\nSenzor" + i + ": ";
                                outputText += "\nTemperatura " + ths.humidity;
                                outputText += "\nVlaznost " + ths.humidity;
                            }outputText += "\nSenzori za visinu tekućine u zemlji: ";
                            i = 0;
                            for(WaterLevelSensor wls : loc.water_level_sensors.values()){
                                i++;
                                outputText += "\nSenzor" + i + ": ";
                                outputText += "\nLevel " + wls.level;
                            }
                            outputText += "\nVentili ";
                            i = 0;
                            for(Valve valve : loc.valves.values()){
                                i++;
                                outputText += "\nVentil" + i + ": ";
                                outputText += "\nStatus " + valve.status;
                            }
                        }
                    }

                    tvKorisnik.setText(outputText);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            FirebaseAuth.getInstance().signOut();
            finishAffinity();
            moveTaskToBack(true);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
