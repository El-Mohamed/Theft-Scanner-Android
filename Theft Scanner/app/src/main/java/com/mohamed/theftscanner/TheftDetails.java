package com.mohamed.theftscanner;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TheftDetails extends AppCompatActivity {

    DatabaseReference mDatabaseReference;

    RecyclerView mRecyclerView;
    TheftAdapter mAdapter;

    EditText mSearchText;
    Button mSearchButton;
    String inputText;

    Theft tempTheft;
    List<Theft> allThefts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theft_details);

        mSearchButton = findViewById(R.id.search_button_details);
        mSearchText = findViewById(R.id.search_text_details);

        mRecyclerView = findViewById(R.id.recyclerview_details);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        allThefts = new ArrayList<>();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Thefts");

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                allThefts.clear();
                readSearchBar();
                getThefts(inputText);

            }
        });

    }

    private void getThefts(final String inputCity) {

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String city = snapshot.child("city").getValue().toString().toLowerCase();
                    if (city.equals(inputCity)) {
                        tempTheft = snapshot.getValue(Theft.class);
                        allThefts.add(tempTheft);
                    }

                }

                setRecyclerView();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setRecyclerView() {
        mAdapter = new TheftAdapter(TheftDetails.this, allThefts);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void readSearchBar() {
        KeyboardHelper.hideKeyboard(TheftDetails.this);
        inputText = mSearchText.getText().toString().toLowerCase();
    }

}
