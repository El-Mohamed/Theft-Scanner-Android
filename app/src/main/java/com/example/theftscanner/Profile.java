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

    private FirebaseAuth mAuth;

    TextView mCurrentAccount;
    TextView mCurrentID;
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        mCurrentAccount = findViewById(R.id.current_mail);
        mCurrentID = findViewById(R.id.email_id);
        mButton = findViewById(R.id.logout_login);

        if (user != null) {
            String CurrentEmail = user.getEmail();
            String CurrentEmailID = user.getUid();
            mCurrentAccount.setText(CurrentEmail);
            mCurrentID.setText(CurrentEmailID);
        } else {
            Intent intent = new Intent(Profile.this, Authentication.class);
            startActivity(intent);
            finish();
        }

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mAuth.getCurrentUser() != null) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(Profile.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                }

            }

        });

    }

}
