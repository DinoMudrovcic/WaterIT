package com.dinomudrovcic.waterit.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dinomudrovcic.waterit.R;
import com.dinomudrovcic.waterit.adapters.SMSensorRecyclerViewAdapter;
import com.dinomudrovcic.waterit.models.SoilMoistureSensor;
import com.dinomudrovcic.waterit.models.TemperatureAndHumiditySensor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ezmuddi on 9.10.2019..
 */

public class FragmentSMSensor extends Fragment {

    private final static String SENSOR_PATH_ADDITION = "/soil_moisture_sensors";

    View v;
    RecyclerView recyclerView;
    SMSensorRecyclerViewAdapter adapter;
    List<SoilMoistureSensor> sensors = new ArrayList<>();

    DatabaseReference locationRef;

    public FragmentSMSensor(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.smsensor_fragment, container, false);
        recyclerView = v.findViewById(R.id.smSensor_recyclerview);
        adapter = new SMSensorRecyclerViewAdapter(getContext(), sensors);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSMSensorData();
    }

    private void getSMSensorData() {

        Bundle bundle = this.getArguments();
        final String path = bundle.getString("path");

        locationRef = FirebaseDatabase.getInstance().getReference(path + SENSOR_PATH_ADDITION);

        ValueEventListener sensorListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sensors.clear();
                adapter.notifyDataSetChanged();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String sensorName = ds.getKey().toString();
                    Double humidity = ds.child("humidity").getValue(Double.class);
                    String picture = ds.child("picture").getValue(String.class);
                    sensors.add(new SoilMoistureSensor(humidity, picture, sensorName, path));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        locationRef.addListenerForSingleValueEvent(sensorListener);
        locationRef.addValueEventListener(sensorListener);

    }
}
