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
import com.dinomudrovcic.waterit.models.SoilMoistureSensor;
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

public class SMSensorRecyclerViewAdapter extends RecyclerView.Adapter<SMSensorRecyclerViewAdapter.SMSensorViewHolder> {

    Context context;
    List<SoilMoistureSensor> sensors;

    public SMSensorRecyclerViewAdapter(Context context, List<SoilMoistureSensor> sensors) {
        this.context = context;
        this.sensors = sensors;
    }

    @NonNull
    @Override
    public SMSensorViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_smsensor, viewGroup, false);
        SMSensorViewHolder holder = new SMSensorViewHolder(v);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SMSensorViewHolder smSensorViewHolder, int position) {

        smSensorViewHolder.tvSMSensorName.setText(sensors.get(position).sensorName);
        smSensorViewHolder.tvSMSensorHumidity.setText(sensors.get(position).humidity + Constants.HUMIDITY_MARK);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(sensors.get(position).picture);

        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(smSensorViewHolder.ivSMSensor);
    }

    @Override
    public int getItemCount() {
        return sensors.size();
    }

    public static class SMSensorViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.ivSMSensor)
        ImageView ivSMSensor;

        @BindView(R.id.tvSMSensorName)
        TextView tvSMSensorName;

        @BindView(R.id.tvSMSensorHumidity)
        TextView tvSMSensorHumidity;

        public SMSensorViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);


        }
    }
}
