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
import com.dinomudrovcic.waterit.adapters.ValveRecyclerViewAdapter;
import com.dinomudrovcic.waterit.models.Valve;
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

public class FragmentActions extends Fragment {

    private final static String VALVE_PATH_ADDITION = "/valves";

    View v;
    RecyclerView recyclerView;
    ValveRecyclerViewAdapter valveRecyclerViewAdapter;
    List<Valve> valves = new ArrayList<>();

    DatabaseReference locationRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.actions_fragment, container, false);

        recyclerView = v.findViewById(R.id.valve_recyclerview);
        valveRecyclerViewAdapter = new ValveRecyclerViewAdapter(getContext(), valves);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(valveRecyclerViewAdapter);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getValvesData();
    }

    public void getValvesData() {

        Bundle bundle = getArguments();
        final String path = bundle.getString("path");

        locationRef = FirebaseDatabase.getInstance().getReference(path + VALVE_PATH_ADDITION);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                valves.clear();
                valveRecyclerViewAdapter.notifyDataSetChanged();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String valveName = ds.getKey().toString();
                    String status = ds.child("status").getValue(String.class);
                    String picture = ds.child("picture").getValue(String.class);
                    valves.add(new Valve(valveName, status, picture, path + VALVE_PATH_ADDITION));
//                    valves.add(new Valve(valveName, status, picture));
                }
                valveRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        locationRef.addListenerForSingleValueEvent(eventListener);
        locationRef.addValueEventListener(eventListener);

    }
}
