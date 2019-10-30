package com.example.theftscanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;


public class Form extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DatabaseReference MyReference;

    Spinner mSpinner;
    Button mSendButton;
    EditText mOwner, mBrand, mModel, mStreet, mCity;
    String Owner, Type, Brand, Model, Street, City;
    double Latitude, Longitude;
    Theft theft;
    long NumberOfChilds = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        MyReference = FirebaseDatabase.getInstance().getReference().child("Thefts");

        mSpinner = findViewById(R.id.spinner_types);
        mSendButton = findViewById(R.id.send_button);
        mOwner = findViewById(R.id.owner);
        mBrand = findViewById(R.id.brand);
        mModel = findViewById(R.id.model);
        mStreet = findViewById(R.id.street);
        mCity = findViewById(R.id.city);

        if (mSpinner != null) {
            mSpinner.setOnItemSelectedListener(this);
        }

        ArrayAdapter<CharSequence> myAdapter = ArrayAdapter.createFromResource(this, R.array.vehicle_array, R.layout.spinner_layout);
        myAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);

        if (mSpinner != null) {
            mSpinner.setAdapter(myAdapter);
        }


        MyReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    NumberOfChilds = (dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Type = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void ConvertToCoordinates(String InputStreet) {

        String location = InputStreet;
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            Latitude = address.getLatitude();
            Longitude = address.getLongitude();
        }
    }

    public void SendToDatabase(View view) {
        mCity.onEditorAction(EditorInfo.IME_ACTION_DONE);
        Owner = mOwner.getText().toString();
        Brand = mBrand.getText().toString();
        Model = mModel.getText().toString();
        Street = mStreet.getText().toString();
        City = mCity.getText().toString();


        if (Owner.isEmpty() || Type.isEmpty() || Brand.isEmpty() || Model.isEmpty() || Street.isEmpty() || City.isEmpty()) {
            Toast.makeText(Form.this, R.string.message_empty_fields, Toast.LENGTH_LONG).show();
        } else {

            AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(Form.this);
            myAlertBuilder.setTitle("Confirm");
            myAlertBuilder.setMessage("Are u sure you want to send?");

            myAlertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    ConvertToCoordinates(Street + City);
                    theft = new Theft(Owner, Type, Brand, Model, Street, City, Latitude, Longitude);
                    MyReference.child(String.valueOf(NumberOfChilds + 1)).setValue(theft);

                    Intent intent = new Intent(Form.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(Form.this, R.string.message_successfully, Toast.LENGTH_LONG).show();
                }
            });

            myAlertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            myAlertBuilder.show();

        }

    }

}
