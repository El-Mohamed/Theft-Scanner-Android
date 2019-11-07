package com.example.theftscanner;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DatabaseReader {

    DatabaseReference mDatabaseRef;
    Theft theft;

    public DatabaseReader() {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Thefts");
    }

    public List<Theft> GetAllThefts(final String inputCity) {

        final List<Theft> allThefts = new ArrayList<>();

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String city = snapshot.child("city").getValue().toString().toLowerCase();
                    if (city.equals(inputCity)) {
                        theft = snapshot.getValue(Theft.class);
                        allThefts.add(theft);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return allThefts;

    }

}
