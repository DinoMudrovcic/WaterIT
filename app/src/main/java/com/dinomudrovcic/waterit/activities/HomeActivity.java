package com.dinomudrovcic.waterit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.dinomudrovcic.waterit.R;
import com.dinomudrovcic.waterit.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeActivity extends BaseActivity {

    boolean doubleBackToExitPressedOnce = false;

    @BindView(R.id.spLocation)
    Spinner spLocation;

    @BindView(R.id.btnGOLoc)
    Button btnGOLoc;

    @BindView(R.id.pbHome)
    ProgressBar pbHome;

    DatabaseReference dbRef;
    DatabaseReference userRef;

    List<String> locations = new ArrayList<String>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        setTitleBar("Dobrodošli", false);


        locations.add("Choose location...");

        dbRef = FirebaseDatabase.getInstance().getReference();

        final String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userRef = FirebaseDatabase.getInstance().getReference("/users/" + UID);

        showLocations(UID);

        btnGOLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spLocation.getSelectedItemId() != 0) {

                    String text = "/users/" + UID + "/locations/" + spLocation.getSelectedItem().toString();
                    Intent intent = new Intent(HomeActivity.this, LocationActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("path", text);
                    bundle.putString("location", spLocation.getSelectedItem().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Toast.makeText(HomeActivity.this, "No location selected", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void showLocations(String UID) {

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pbHome.setVisibility(View.GONE);
                btnGOLoc.setEnabled(true);
                if(dataSnapshot.exists()){
                    User user = dataSnapshot.getValue(User.class);
                    for(String location : user.locations.keySet()){
                        locations.add(location);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(HomeActivity.this, R.layout.item, R.id.spinnerItem, locations);
                    adapter.setDropDownViewResource(R.layout.item);
                    spLocation.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_home;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            FirebaseAuth.getInstance().signOut();
            finishAffinity();
            moveTaskToBack(true);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
