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
    private DatabaseReference Reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFilterButton = findViewById(R.id.filter_button);
        Reference = FirebaseDatabase.getInstance().getReference().child("Thefts");

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            String details = snapshot.child("brand").getValue().toString();
                            details += " ";
                            details += snapshot.child("model").getValue().toString();

                            String city = snapshot.child("city").getValue().toString();

                            String strLatitude = snapshot.child("latitude").getValue().toString();
                            String strLongitude = snapshot.child("longitude").getValue().toString();

                            double Latitude = Double.parseDouble(strLatitude);
                            double Longitude = Double.parseDouble(strLongitude);

                            LatLng tempLocation = new LatLng(Latitude, Longitude);
                            mMap.addMarker(new MarkerOptions().position(tempLocation).title(details));

                            Log.d("databasereader", details);
                            Log.d("databasereader", city);
                            Log.d("databasereader", strLatitude);
                            Log.d("databasereader", strLongitude);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });



        LatLng MySchool = new LatLng(51.230149, 4.416176);
        mMap.addMarker(new MarkerOptions().position(MySchool).title("AP Hogeschool"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(MySchool));

    }
}
