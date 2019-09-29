package com.dinomudrovcic.waterit;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

public class BaseActivity extends AppCompatActivity {


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_profile_information:
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
//                transaction.add(R.id.ProfileFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
            case R.id.action_sign_out:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
