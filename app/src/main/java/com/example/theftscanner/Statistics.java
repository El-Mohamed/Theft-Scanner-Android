package com.example.theftscanner;

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

public class Statistics extends AppCompatActivity {

    private DatabaseReference Reference;
    Adapter adapter;

    RecyclerView recyclerView;
    Button mButton;
    EditText mFilter;

    String[] Types;
    ArrayList<String> Results;
    int[] Counts;
    String Filter;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Reference = FirebaseDatabase.getInstance().getReference().child("Thefts");
        Types = getResources().getStringArray(R.array.vehicle_array);
        Results = new ArrayList<>();
Counts = new int[6];
        for (int i = 0; i < 6; i++) {
            Results.add("");
            Counts[i] = 0;
        }


        mFilter = findViewById(R.id.preferred_city_stats);
        mButton = findViewById(R.id.filter_button_stats);
        recyclerView = findViewById(R.id.recyclerview_stats);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, Types, Results);
        recyclerView.setAdapter(adapter);


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Filter = mFilter.getText().toString();

                if (Filter.isEmpty()) {

                    Results.clear();
                 
                    for (int i = 0; i < 6; i++) {
                        Results.add("");
                        Counts[i] = 0;
                    }
                    recyclerView.setAdapter(adapter);

                } else {

                    Reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                String type = snapshot.child("type").getValue().toString();
                                String city = snapshot.child("city").getValue().toString();

                                if (Filter.equals(city)) {

                                    if (type.equals(Types[0])) {
                                        int oldValue = Counts[0];
                                        int newValue = oldValue + 1;
                                        Counts[0] = newValue;
                                    } else if (type.equals(Types[1])) {
                                        int oldValue = Counts[1];
                                        int newValue = oldValue + 1;
                                        Counts[1] = newValue;
                                    } else if (type.equals(Types[2])) {
                                        int oldValue = Counts[2];
                                        int newValue = oldValue + 1;
                                        Counts[2] = newValue;
                                    } else if (type.equals(Types[3])) {
                                        int oldValue = Counts[3];
                                        int newValue = oldValue + 1;
                                     Counts[3] = newValue;
                                    } else if (type.equals(Types[4])) {
                                        int oldValue = Counts[4];
                                        int newValue = oldValue + 1;
                                        Counts[4] = newValue;
                                    } else if (type.equals(Types[5])) {
                                        int oldValue = Counts[5];
                                        int newValue = oldValue + 1;
                                        Counts[5] = newValue;
                                    }

                                }
                            }

                            for (int i = 0; i < 6; i++) {
                                Results.set(i, Integer.toString(Counts[i]));
                            }


                            recyclerView.setAdapter(adapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        });


    }
}
