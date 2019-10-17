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
import com.dinomudrovcic.waterit.models.WaterLevelSensor;
import com.dinomudrovcic.waterit.utils.Constants;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ezmuddi on 11.10.2019..
 */

public class WLSensorRecyclerViewAdapter extends RecyclerView.Adapter<WLSensorRecyclerViewAdapter.WLSensorViewHolder> {

    Context context;
    List<WaterLevelSensor> sensors;

    public WLSensorRecyclerViewAdapter(Context context, List<WaterLevelSensor> sensors) {
        this.context = context;
        this.sensors = sensors;
    }

    @NonNull
    @Override
    public WLSensorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_wlsensor, viewGroup, false);
        WLSensorViewHolder holder = new WLSensorViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull WLSensorViewHolder wlSensorViewHolder, int position) {
        wlSensorViewHolder.tvWLSensorName.setText(sensors.get(position).sensorName);
        wlSensorViewHolder.tvWLSensorWaterLevelData.setText(sensors.get(position).level + Constants.WATER_LEVEL_MARK);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(sensors.get(position).picture);

        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(wlSensorViewHolder.ivWLSensor);
    }

    @Override
    public int getItemCount() {
        return sensors.size();
    }

    public static class WLSensorViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.ivWLSensor)
        ImageView ivWLSensor;

        @BindView(R.id.tvWLSensorName)
        TextView tvWLSensorName;

        @BindView(R.id.tvWLSensorWaterLevelData)
        TextView tvWLSensorWaterLevelData;

        public WLSensorViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }
}
