package com.example.theftscanner;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
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

public class Map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Circle tempCircle;
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

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                String inputText = mSearchText.getText().toString().toLowerCase();
                mMap.clear();
                allMarkers.clear();
                allThefts.clear();
                getThefts(inputText);

            }
        });

    }

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

        for (int i = 0; i < allThefts.size(); i++) {

            for (int j = 0; j < vehicleTypes.length; j++) {

                if (allThefts.get(i).getType().equals(vehicleTypes[j])) {
                    tempCircle = mMap.addCircle(new CircleOptions()
                            .center(tempLocation)
                            .radius(10)
                            .strokeColor(colorLabels[j])
                            .fillColor(Color.TRANSPARENT));
                }

            }

            mMap.addMarker(new MarkerOptions().position(tempLocation).title(allThefts.get(i).getType()));
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


}
