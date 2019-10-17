package com.dinomudrovcic.waterit.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dinomudrovcic.waterit.R;
import com.dinomudrovcic.waterit.models.Valve;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ezmuddi on 10.10.2019..
 */

public class ValveRecyclerViewAdapter extends RecyclerView.Adapter<ValveRecyclerViewAdapter.ValveViewHolder> {

    Context context;
    List<Valve> valves;

    public ValveRecyclerViewAdapter(Context context, List<Valve> valves) {
        this.context = context;
        this.valves = valves;
    }

    @NonNull
    @Override
    public ValveViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_valve, viewGroup, false);
        ValveViewHolder holder = new ValveViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ValveViewHolder valveViewHolder, final int position) {
        valveViewHolder.tvValveName.setText(valves.get(position).valveName);
        valveViewHolder.tvValveStatus.setText(valves.get(position).status);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(valves.get(position).picture);

        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(valveViewHolder.ivValve);

        colorText(valveViewHolder);

        final String path = valves.get(position).path;

        valveViewHolder.btnChangeValveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);
                if(valveViewHolder.btnChangeValveStatus.getText().toString().equals("Close")){
                    ref.child(valveViewHolder.tvValveName.getText().toString()).child("status").setValue("Closed");
                    valveViewHolder.btnChangeValveStatus.setText("Open");
                } else if(valveViewHolder.btnChangeValveStatus.getText().toString().equals("Open")){
                    ref.child(valveViewHolder.tvValveName.getText().toString()).child("status").setValue("Open");
                    valveViewHolder.btnChangeValveStatus.setText("Close");
                }
            }
        });
    }

    private void colorText(ValveViewHolder valveViewHolder) {
        if(valveViewHolder.tvValveStatus.getText().toString().equals("Closed")) {
            valveViewHolder.tvValveStatus.setTextColor(Color.RED);
            valveViewHolder.btnChangeValveStatus.setText("Open");
        }
        else if(valveViewHolder.tvValveStatus.getText().toString().equals("Open")) {
            valveViewHolder.tvValveStatus.setTextColor(Color.GREEN);
            valveViewHolder.btnChangeValveStatus.setText("Close");
        }
    }


    @Override
    public int getItemCount() {
        return valves.size();
    }

    public static class ValveViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivValve)
        ImageView ivValve;

        @BindView(R.id.tvValveName)
        TextView tvValveName;

        @BindView(R.id.tvValveStatus)
        TextView tvValveStatus;

        @BindView(R.id.btnChangeValveStatus)
        Button btnChangeValveStatus;

        public ValveViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
