package com.dinomudrovcic.waterit;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends Activity {

    @BindView(R.id.tvKorisnik)
    TextView tvKorisnik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        tvKorisnik.setText("Ulogirani korisnik je: " + FirebaseAuth.getInstance().getCurrentUser().getEmail());
    }
}
