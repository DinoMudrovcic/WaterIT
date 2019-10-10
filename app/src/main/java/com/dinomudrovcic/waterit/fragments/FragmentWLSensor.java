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
import com.dinomudrovcic.waterit.adapters.WLSensorRecyclerViewAdapter;
import com.dinomudrovcic.waterit.models.WaterLevelSensor;
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

public class FragmentWLSensor extends Fragment {

    private final static String SENSOR_PATH_ADDITION = "/water_level_sensors";

    View v;
    RecyclerView recyclerView;
    WLSensorRecyclerViewAdapter adapter;
    List<WaterLevelSensor> sensors = new ArrayList<>();

    DatabaseReference locationRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.wlsensor_fragment, container, false);

        recyclerView = v.findViewById(R.id.wlSensor_recyclerview);
        adapter = new WLSensorRecyclerViewAdapter(getContext(), sensors);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWLSensorData();
    }

    private void getWLSensorData() {

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
                    Double level = ds.child("level").getValue(Double.class);
                    String picture = ds.child("picture").getValue(String.class);
                    sensors.add(new WaterLevelSensor(level, picture, sensorName, path));
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
