package com.dinomudrovcic.waterit.activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.dinomudrovcic.waterit.R;
import com.dinomudrovcic.waterit.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends BaseActivity {

    @Nullable
    @BindView(R.id.tvUsername)
    TextView tvUsername;

    @Nullable
    @BindView(R.id.tvEmail)
    TextView tvEmail;

    @Nullable
    @BindView(R.id.tvFullname)
    TextView tvFullname;

    DatabaseReference dbRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        setTitleBar("Profil", true);

        showProfileInfo();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_profile;
    }

    private void showProfileInfo() {

        dbRef = FirebaseDatabase.getInstance().getReference();

        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("/users/" + UID);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    User user = dataSnapshot.getValue(User.class);

                    tvUsername.setText(user.display_name);
                    tvEmail.setText(user.email);
                    tvFullname.setText(user.full_name_or_company_name);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_profile_information:
                return true;
            case R.id.action_sign_out:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
