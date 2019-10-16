package com.dinomudrovcic.waterit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.dinomudrovcic.waterit.R;
import com.dinomudrovcic.waterit.adapters.ViewPagerAdapter;
import com.dinomudrovcic.waterit.fragments.FragmentActions;
import com.dinomudrovcic.waterit.fragments.FragmentSensors;
import com.dinomudrovcic.waterit.fragments.FragmentWeather;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LocationActivity extends BaseActivity {

    @BindView(R.id.tablayout_id)
    TabLayout tabLayout;

    @BindView(R.id.viewpager_id)
    ViewPager viewPager;

    ViewPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        initFrags();

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        setTabIcons();

        String location = getLocation();

        setTitleBar(location, true);
    }

    private void setTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_sensor);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_weather);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_action);
    }

    private void initFrags() {
        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        String city = intent.getStringExtra("city");
        String country = intent.getStringExtra("country");

        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        bundle.putString("city", city);
        bundle.putString("country", country);

        FragmentSensors sensors = new FragmentSensors();
        sensors.setArguments(bundle);
        FragmentWeather weather = new FragmentWeather();
        weather.setArguments(bundle);
        FragmentActions actions = new FragmentActions();
        actions.setArguments(bundle);

        adapter.AddFragment(sensors, "");
        adapter.AddFragment(weather, "");
        adapter.AddFragment(actions, "");
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_location;
    }


    public String getLocation() {
        Intent intent = getIntent();
        String result = intent.getStringExtra("location");
        return result;
    }
}
