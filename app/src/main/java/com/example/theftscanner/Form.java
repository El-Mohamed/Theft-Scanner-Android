package com.example.theftscanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Form extends AppCompatActivity {

    DatabaseReference MyReference;

    Button mSendButton;
    EditText mOwner, mBrand, mModel, mStreet, mCity;
    String Owner, Brand, Model, Street, City;
    Theft theft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        MyReference = FirebaseDatabase.getInstance().getReference().child("Thefts");

        mSendButton = findViewById(R.id.send_button);
        mOwner = findViewById(R.id.owner);
        mBrand = findViewById(R.id.brand);
        mModel = findViewById(R.id.model);
        mStreet = findViewById(R.id.street);
        mCity = findViewById(R.id.city);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Owner = mOwner.getText().toString();
                Brand = mBrand.getText().toString();
                Model = mModel.getText().toString();
                Street = mStreet.getText().toString();
                City = mCity.getText().toString();

                theft = new Theft(Owner, Brand, Model, Street, City);
                MyReference.push().setValue(theft);

                Intent intent = new Intent(Form.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(Form.this, R.string.toast_message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
