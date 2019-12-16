package com.mohamed.theftscanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Authentication extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;

    EditText mPassword, mEmail;
    String password, email;

    Button mAuthButton;
    TextView mMessageText, mGuestText;

    String toSignIn, toSignUp, buttonSignIn, buttonSignUp;
    Boolean onLogInScreen, isValid;


    @Override
    protected void onStart() {
        super.onStart();
        if (mFirebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(Authentication.this, Dashboard.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_authentication);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mPassword = findViewById(R.id.password);
        mEmail = findViewById(R.id.email);
        mAuthButton = findViewById(R.id.auth_button);
        mMessageText = findViewById(R.id.messageText);
        mGuestText = findViewById(R.id.guestText);

        toSignIn = getResources().getString(R.string.message_signIn);
        toSignUp = getResources().getString(R.string.message_signUp);
        buttonSignIn = getResources().getString(R.string.signInUser);
        buttonSignUp = getResources().getString(R.string.signUpUser);

        mMessageText.setText(toSignUp);
        mAuthButton.setText(buttonSignIn);
        onLogInScreen = true;

        mMessageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateScreen();
            }
        });

        mGuestText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Authentication.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        mAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isValid = validateFields();

                if (isValid) {

                    if (onLogInScreen) {
                        singInUser();
                    } else {
                        singUpUser();
                    }
                }
            }
        });
    }

    private void updateScreen() {

        mPassword.setText("");
        mEmail.setText("");

        if (mMessageText.getText().toString().equals(toSignUp)) {
            mMessageText.setText(toSignIn);
            mAuthButton.setText(buttonSignUp);
            onLogInScreen = false;
        } else {
            mMessageText.setText(toSignUp);
            mAuthButton.setText(buttonSignIn);
            onLogInScreen = true;
        }
    }

    private Boolean validateFields() {

        email = mEmail.getText().toString();
        password = mPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(Authentication.this, R.string.message_empty_fields, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void singUpUser() {

        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Authentication.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Authentication.this, "Account created, Log in now", Toast.LENGTH_SHORT).show();
                    mPassword.setText("");
                    mEmail.setText("");
                    mMessageText.setText(toSignUp);
                    mAuthButton.setText(buttonSignIn);
                    onLogInScreen = true;
                    Log.d("Logger", "createUserWithEmail:success");
                } else {
                    Log.w("Logger", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(Authentication.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void singInUser() {

        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Authentication.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("Logger", "signInWithEmail:success");
                    Intent intent = new Intent(Authentication.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.w("Logger", "signInWithEmail:failure", task.getException());
                    Toast.makeText(Authentication.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
