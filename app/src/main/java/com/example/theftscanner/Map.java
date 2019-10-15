package com.example.theftscanner;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button mFilterButton;
    EditText mFilter;
    String Filter;
    private DatabaseReference Reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFilterButton = findViewById(R.id.filter_button);
        mFilter = findViewById(R.id.preferred_city);
        Reference = FirebaseDatabase.getInstance().getReference().child("Thefts");

    }


    @Override
    public void onMapReady( GoogleMap googleMap) {
        mMap = googleMap;

        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Filter = mFilter.getText().toString().toLowerCase();
                mMap.clear();

                Reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            String type = snapshot.child("type").getValue().toString().toUpperCase();
                            String city = snapshot.child("city").getValue().toString().toLowerCase();

                            String strLatitude = snapshot.child("latitude").getValue().toString();
                            String strLongitude = snapshot.child("longitude").getValue().toString();

                            double Latitude = Double.parseDouble(strLatitude);
                            double Longitude = Double.parseDouble(strLongitude);

                            if(city.equals(Filter) ) {
                                LatLng tempLocation = new LatLng(Latitude, Longitude);
                                mMap.addMarker(new MarkerOptions().position(tempLocation).title(type));
                            }

                            Log.d("FirebaseLogger", Filter);
                            Log.d("FirebaseLogger", type);
                            Log.d("FirebaseLogger", city);
                            Log.d("FirebaseLogger", strLatitude);
                            Log.d("FirebaseLogger", strLongitude);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

}
