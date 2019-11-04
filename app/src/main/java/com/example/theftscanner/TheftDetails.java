package com.example.theftscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TheftDetails extends AppCompatActivity {

    RecyclerView recyclerView;
    Adapter adapter;

    DatabaseReference mDatabaseRef;
    List<Theft> mThefts;
    Theft myTheft;
    EditText mCity;
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theft_details);
        mButton = findViewById(R.id.filter_button_details);
        mCity = findViewById(R.id.preferred_city_details);
        recyclerView = findViewById(R.id.recyclerview_details);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mThefts = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Thefts");


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String City = mCity.getText().toString().toLowerCase();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            String city = snapshot.child("city").getValue().toString().toLowerCase();
                            if (city.equals(City)) {

                                myTheft = snapshot.getValue(Theft.class);
                                mThefts.add(myTheft);
                                String type = snapshot.child("type").getValue().toString();
                                Log.d("testje", type);


                            }


                        }

                        adapter = new Adapter(TheftDetails.this, mThefts);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });


    }


}
