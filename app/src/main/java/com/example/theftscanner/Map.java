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

public class Map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button mFilterButton;
    EditText mFilter;
    String Filter;

    private DatabaseReference Reference;

    boolean ToAnimateStart = true;

    int[] ColorLabels = {Color.rgb(229, 38, 30),
            Color.rgb(235, 117, 50),
            Color.rgb(163, 224, 71),
            Color.rgb(209, 58, 231),
            Color.rgb(67, 85, 219),
            Color.rgb(52, 187, 230),
            Color.rgb(73, 218, 154),
    };


    String[] VehicleTypes;
    Circle tempCircle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFilterButton = findViewById(R.id.search_button_map);
        mFilter = findViewById(R.id.search_text_map);
        Reference = FirebaseDatabase.getInstance().getReference().child("Thefts");
        VehicleTypes = getResources().getStringArray(R.array.vehicle_array);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ToAnimateStart) {
            LatLng APHogeschool = new LatLng(51.230176, 4.415048);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(APHogeschool)
                    .zoom(13)
                    .bearing(0)
                    .tilt(60)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            ToAnimateStart = false;
        }


        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFilter.onEditorAction(EditorInfo.IME_ACTION_DONE);
                Filter = mFilter.getText().toString().toLowerCase();
                mMap.clear();

                Reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        LatLng tempLocation = new LatLng(51.230176, 4.415048);

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            String type = snapshot.child("type").getValue().toString();
                            String city = snapshot.child("city").getValue().toString().toLowerCase();

                            String strLatitude = snapshot.child("latitude").getValue().toString();
                            String strLongitude = snapshot.child("longitude").getValue().toString();

                            double Latitude = Double.parseDouble(strLatitude);
                            double Longitude = Double.parseDouble(strLongitude);


                            if (city.equals(Filter) && !Filter.isEmpty()) {


                                tempLocation = new LatLng(Latitude, Longitude);

                                for (int i = 0; i < VehicleTypes.length; i++) {

                                    if (type.equals(VehicleTypes[i])) {
                                        tempCircle = mMap.addCircle(new CircleOptions()
                                                .center(tempLocation)
                                                .radius(10)
                                                .strokeColor(ColorLabels[i])
                                                .fillColor(Color.TRANSPARENT));
                                    }

                                }

                                mMap.addMarker(new MarkerOptions().position(tempLocation).title(type));
                            }

                        }


                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(tempLocation)
                                .zoom(13)
                                .bearing(0)
                                .tilt(60)
                                .build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

}
