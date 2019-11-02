package com.example.theftscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class TheftDetails extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> items;
    ArrayList<String> items2;
    ArrayList<String> items3;
    ArrayList<String> items4;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theft_details);

        items = new ArrayList<>();
        items2 = new ArrayList<>();
        items3 = new ArrayList<>();
        items4 = new ArrayList<>();

        items.add("test");
        items.add("test");
        items2.add("test");
        items2.add("test");
        items3.add("test");
        items3.add("test");
        items4.add("test");
        items4.add("test");

        recyclerView = findViewById(R.id.recyclerview_details);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, items, items2, items3, items4);
        recyclerView.setAdapter(adapter);

    }
}
