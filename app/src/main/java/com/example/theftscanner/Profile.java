package com.example.theftscanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    FirebaseUser currentUser;
    TextView mCurrentAccount, mCurrentID;
    Button mLogoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUser = mFirebaseAuth.getCurrentUser();

        mCurrentAccount = findViewById(R.id.current_mail);
        mCurrentID = findViewById(R.id.email_id);
        mLogoutButton = findViewById(R.id.logout_button);

        checkCurrentUser();

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }

        });

    }

    private void logoutUser() {

        if (mFirebaseAuth.getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Profile.this, Dashboard.class);
            startActivity(intent);
            finish();
        }
    }

    private void checkCurrentUser() {

        if (currentUser != null) {
            String CurrentEmail = currentUser.getEmail();
            String CurrentEmailID = currentUser.getUid();
            mCurrentAccount.setText(CurrentEmail);
            mCurrentID.setText(CurrentEmailID);
        } else {
            Intent intent = new Intent(Profile.this, Authentication.class);
            startActivity(intent);
            finish();
        }
    }

}
