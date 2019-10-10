package com.dinomudrovcic.waterit.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dinomudrovcic.waterit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etPass)
    EditText etPass;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.pbLogin)
    ProgressBar pbLogin;


    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etEmail.getText().toString().equals(null) || etEmail.getText().toString().equals("")
                        || etPass.getText().toString().equals(null) || etPass.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "Email and/or password field is empty.", Toast.LENGTH_LONG).show();
                }else{
                    pbLogin.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    pbLogin.setVisibility(View.GONE);
                                    if(task.isSuccessful()){
                                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    }else{
                                        etPass.setText("");
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }

            }
        });
    }


}
