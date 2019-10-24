package com.example.theftscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class Statistics extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> items;
    ArrayList<String> items2;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        items = new ArrayList<>();
        items2 = new ArrayList<>();

        items.add("test");
        items.add("test");
        items2.add("test");
        items2.add("test");
        recyclerView = findViewById(R.id.recyclerview_stats);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, items, items2);
        recyclerView.setAdapter(adapter);

    }
}
