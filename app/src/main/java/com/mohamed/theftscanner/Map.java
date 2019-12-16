package com.mohamed.theftscanner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Map extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;
    LatLng tempLocation;
    boolean toAnimateStart = true;

    DatabaseReference mDatabaseReference;

    Button mSearchButton;
    EditText mSearchText;

    List<Theft> allThefts;
    Theft tempTheft;
    List<LatLng> allMarkers;
    String[] vehicleTypes;
    int[] colorLabels = {Color.rgb(229, 38, 30),
            Color.rgb(235, 117, 50),
            Color.rgb(163, 224, 71),
            Color.rgb(209, 58, 231),
            Color.rgb(67, 85, 219),
            Color.rgb(52, 187, 230),
            Color.rgb(73, 218, 154),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mSearchButton = findViewById(R.id.search_button_map);
        mSearchText = findViewById(R.id.search_text_map);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Thefts");
        vehicleTypes = getResources().getStringArray(R.array.vehicle_array);
        allThefts = new ArrayList<>();
        allMarkers = new ArrayList<>();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        showStartAnimation();
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                String inputText = mSearchText.getText().toString().toLowerCase();
                resetMap();
                getThefts(inputText);

            }
        });

    }

    // GPS LOCATION CODE

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        // Camera animates to the user's current position
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    // END GPS LOCATION

    private void showStartAnimation() {
        if (toAnimateStart) {
            LatLng schoolPosition = new LatLng(51.230176, 4.415048);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(schoolPosition)
                    .zoom(13)
                    .bearing(0)
                    .tilt(60)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            toAnimateStart = false;
        }
    }

    private void getThefts(final String inputCity) {

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String city = snapshot.child("city").getValue().toString().toLowerCase();
                    if (city.equals(inputCity)) {

                        tempTheft = snapshot.getValue(Theft.class);

                        double latitude = tempTheft.getLatitude();
                        double longitude = tempTheft.getLongitude();
                        tempLocation = new LatLng(latitude, longitude);

                        allMarkers.add(tempLocation);
                        allThefts.add(tempTheft);
                    }
                }

                drawMarkers();
                drawCircles();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void drawMarkers() {

        for (int i = 0; i < allMarkers.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(allMarkers.get(i)).title(allThefts.get(i).getType()));
        }

        if (!allMarkers.isEmpty()) {
            animateToMarker(tempLocation);
        }
    }

    private void drawCircles() {

        for (int i = 0; i < allMarkers.size(); i++) {

            for (int j = 0; j < vehicleTypes.length; j++) {

                if (allThefts.get(i).getType().equals(vehicleTypes[j])) {
                    tempLocation = allMarkers.get(i);
                    mMap.addCircle(new CircleOptions()
                            .center(tempLocation)
                            .radius(10)
                            .strokeColor(colorLabels[j])
                            .fillColor(Color.TRANSPARENT));
                }
            }
        }
    }

    private void animateToMarker(LatLng lastMarker) {

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(lastMarker)
                .zoom(13)
                .bearing(0)
                .tilt(60)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void resetMap() {
        mMap.clear();
        allMarkers.clear();
        allThefts.clear();
    }
}
