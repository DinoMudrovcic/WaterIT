package com.dinomudrovcic.waterit.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dinomudrovcic.waterit.R;
import com.dinomudrovcic.waterit.adapters.ViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ezmuddi on 9.10.2019..
 */

public class FragmentSensors extends Fragment {


    @BindView(R.id.sensor_tablayout_id)
    TabLayout sensor_tabLayout;

    @BindView(R.id.sensor_viewpager_id)
    ViewPager sensor_viewPager;

    ViewPagerAdapter adapter;

    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.sensors_fragment, container, false);

        ButterKnife.bind(this, v);

        adapter = new ViewPagerAdapter(getChildFragmentManager());

        initFrags();

        sensor_viewPager.setAdapter(adapter);
        sensor_tabLayout.setupWithViewPager(sensor_viewPager);

        return v;
    }

    private void initFrags() {

        FragmentSMSensor smSensor = new FragmentSMSensor();
        smSensor.setArguments(this.getArguments());

        FragmentTHSensor thSensor = new FragmentTHSensor();
        thSensor.setArguments(this.getArguments());

        FragmentWLSensor wlSensor = new FragmentWLSensor();
        wlSensor.setArguments(this.getArguments());


        adapter.AddFragment(smSensor, "Soil");
        adapter.AddFragment(thSensor, "Air");
        adapter.AddFragment(wlSensor, "Water Level");
    }
}
