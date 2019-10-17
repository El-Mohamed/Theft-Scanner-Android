package com.example.theftscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.text.MeasuredText;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class Authentication extends AppCompatActivity {

    private FirebaseAuth mAuth;

    String email, password;
    EditText mPassword, mEmail;
    Button mButton;
    TextView mText1, mText2;

    String ToSignIn = "Already have an account? Log in here";
    String ToSignUp = "New user? Create account here";
    String GuestLogin = "Continue as Guest";
    String ButtonLogIn = "LOG IN";
    String ButtonCreateAccount = "CREATE";
    Boolean OnLogInScreen;


    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(Authentication.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_authentication);

        mAuth = FirebaseAuth.getInstance();
        mPassword = findViewById(R.id.password);
        mEmail = findViewById(R.id.email);
        mButton = findViewById(R.id.auth_button);

        mText1 = findViewById(R.id.text1);
        mText2 = findViewById(R.id.text2);

        mText1.setText(ToSignUp);
        mText2.setText(GuestLogin);
        mButton.setText(ButtonLogIn);
        OnLogInScreen = true;

        mText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPassword.setText("");
                mEmail.setText("");
                if (mText1.getText().toString().equals(ToSignUp)) {
                    mText1.setText(ToSignIn);
                    mButton.setText(ButtonCreateAccount);
                    OnLogInScreen = false;
                } else {
                    mText1.setText(ToSignUp);
                    mButton.setText(ButtonLogIn);
                    OnLogInScreen = true;
                }
            }
        });

        mText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Authentication.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = mEmail.getText().toString();
                password = mPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Authentication.this, R.string.message_empty_fields, Toast.LENGTH_SHORT).show();
                } else {

                    if (OnLogInScreen) {
                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Authentication.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Logger", "signInWithEmail:success");
                                    Intent intent = new Intent(Authentication.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {

                                    Log.w("Logger", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(Authentication.this, "Authentication failed",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } else {

                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Authentication.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Authentication.this, "Account created, Log in now", Toast.LENGTH_SHORT).show();
                                    mPassword.setText("");
                                    mEmail.setText("");
                                    mText1.setText(ToSignUp);
                                    mButton.setText(ButtonLogIn);
                                    OnLogInScreen = true;
                                    Log.d("Logger", "createUserWithEmail:success");
                                } else {
                                    Log.w("Logger", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(Authentication.this, "Authentication failed",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }


                }

            }
        });

    }
}
