package com.dinomudrovcic.waterit.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dinomudrovcic.waterit.R;
import com.dinomudrovcic.waterit.models.TemperatureAndHumiditySensor;
import com.dinomudrovcic.waterit.utils.Constants;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ezmuddi on 9.10.2019..
 */

public class THSensorRecyclerViewAdapter extends RecyclerView.Adapter<THSensorRecyclerViewAdapter.THSensorViewHolder> {

    Context context;
    List<TemperatureAndHumiditySensor> thSensors;

    public THSensorRecyclerViewAdapter(Context context, List<TemperatureAndHumiditySensor> thSensors) {
        this.context = context;
        this.thSensors = thSensors;
    }

    @NonNull
    @Override
    public THSensorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_thsensor, viewGroup, false);
        THSensorViewHolder holder = new THSensorViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull THSensorViewHolder viewHolder, int position) {

        viewHolder.tvSensorName.setText(thSensors.get(position).sensorName.toString());
        viewHolder.tvTemperatureData.setText(thSensors.get(position).temperature.toString() + Constants.TEMPERATURE_MARK);
        viewHolder.tvHumidityData.setText(thSensors.get(position).humidity.toString() + Constants.HUMIDITY_MARK);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(thSensors.get(position).picture);

        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(viewHolder.ivSensorImg);
    }

    @Override
    public int getItemCount() {
        return thSensors.size();
    }


    public static class THSensorViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvSensorName)
        TextView tvSensorName;

        @BindView(R.id.tvTemperatureData)
        TextView tvTemperatureData;

        @BindView(R.id.tvHumidityData)
        TextView tvHumidityData;

        @BindView(R.id.ivSensorImg)
        ImageView ivSensorImg;


        public THSensorViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);


        }
    }
}
