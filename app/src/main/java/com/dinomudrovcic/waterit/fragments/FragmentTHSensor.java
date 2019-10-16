package com.dinomudrovcic.waterit.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dinomudrovcic.waterit.R;
import com.dinomudrovcic.waterit.adapters.THSensorRecyclerViewAdapter;
import com.dinomudrovcic.waterit.models.Location;
import com.dinomudrovcic.waterit.models.TemperatureAndHumiditySensor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ezmuddi on 9.10.2019..
 */

public class FragmentTHSensor extends Fragment{

    private final static String SENSOR_PATH_ADDITION = "/temperature_and_humidity_sensors";

    View v;
    RecyclerView recyclerView;
    THSensorRecyclerViewAdapter recyclerViewAdapter;
    List<TemperatureAndHumiditySensor> sensors = new ArrayList<>();

    DatabaseReference locationRef;


    public FragmentTHSensor() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.thsensor_fragment, container, false);



        recyclerView = v.findViewById(R.id.thSensor_recyclerview);
        recyclerViewAdapter = new THSensorRecyclerViewAdapter(getContext(), sensors);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewAdapter);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getTHSensorData();


    }

    private void getTHSensorData() {

        Bundle bundle = this.getArguments();
        String path = bundle.getString("path");

        locationRef = FirebaseDatabase.getInstance().getReference(path + SENSOR_PATH_ADDITION);

        ValueEventListener sensorListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sensors.clear();
                recyclerViewAdapter.notifyDataSetChanged();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String sensorName = ds.getKey().toString();
                    Double temperature = ds.child("temperature").getValue(Double.class);
                    Double humidity = ds.child("humidity").getValue(Double.class);
                    String picture = ds.child("picture").getValue(String.class);
                    sensors.add(new TemperatureAndHumiditySensor(temperature, humidity, picture, sensorName));
                }
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        locationRef.addListenerForSingleValueEvent(sensorListener);
        locationRef.addValueEventListener(sensorListener);


    }

}
