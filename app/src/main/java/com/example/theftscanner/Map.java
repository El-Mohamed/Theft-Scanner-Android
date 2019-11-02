package com.example.theftscanner;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

        if(ToAnimateStart) {
            LatLng APHogeschool = new LatLng( 51.230176, 4.415048);
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
                        LatLng tempLocation = new LatLng(51.230176,4.415048);
                        Circle tempCircle;

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            String type = snapshot.child("type").getValue().toString().toLowerCase();
                            String city = snapshot.child("city").getValue().toString().toLowerCase();

                            String strLatitude = snapshot.child("latitude").getValue().toString();
                            String strLongitude = snapshot.child("longitude").getValue().toString();

                            double Latitude = Double.parseDouble(strLatitude);
                            double Longitude = Double.parseDouble(strLongitude);


                            if(city.equals(Filter) && !Filter.isEmpty()) {

                                 tempLocation = new LatLng(Latitude, Longitude);

                                 if(type.equals("bike")){
                                     tempCircle = mMap.addCircle(new CircleOptions()
                                             .center(tempLocation)
                                             .radius(10)
                                             .strokeColor(Color.YELLOW)
                                             .fillColor(Color.TRANSPARENT));
                                }
                                 else if (type.equals("scooter")) {

                                     tempCircle = mMap.addCircle(new CircleOptions()
                                             .center(tempLocation)
                                             .radius(20)
                                             .strokeColor(Color.rgb(255,165,0) )
                                             .fillColor(Color.TRANSPARENT));
                                 }
                                 else if (type.equals("motorcycle")){

                                     tempCircle = mMap.addCircle(new CircleOptions()
                                             .center(tempLocation)
                                             .radius(30)
                                             .strokeColor(Color.RED)
                                             .fillColor(Color.TRANSPARENT));
                                 }
                                 else
                                 {
                                     tempCircle = mMap.addCircle(new CircleOptions()
                                             .center(tempLocation)
                                             .radius(15)
                                             .strokeColor(Color.GRAY)
                                             .fillColor(Color.TRANSPARENT));
                                 }
                                 
                                mMap.addMarker(new MarkerOptions().position(tempLocation).title(type));
                            }

                            Log.d("FirebaseLogger", Filter);
                            Log.d("FirebaseLogger", type);
                            Log.d("FirebaseLogger", city);
                            Log.d("FirebaseLogger", strLatitude);
                            Log.d("FirebaseLogger", strLongitude);
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
