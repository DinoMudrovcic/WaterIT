package com.dinomudrovcic.waterit.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dinomudrovcic.waterit.R;

/**
 * Created by ezmuddi on 9.10.2019..
 */

public class FragmentSMSensor extends Fragment {
    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.smsensor_fragment, container, false);
        return v;
    }
}
