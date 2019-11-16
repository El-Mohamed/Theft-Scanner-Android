package com.example.theftscanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class Dashboard extends AppCompatActivity {

    CardView mCardMap, mCardForm, mCardList, mCardStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCardMap = findViewById(R.id.map_card);
        mCardForm = findViewById(R.id.form_card);
        mCardList = findViewById(R.id.list_card);
        mCardStats = findViewById(R.id.stats_card);

        mCardMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Map.class);
                startActivity(intent);
            }
        });

        mCardForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Form.class);
                startActivity(intent);
            }
        });

        mCardList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, TheftDetails.class);
                startActivity(intent);
            }
        });

        mCardStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Statistics.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_profile:
                Intent intent = new Intent(Dashboard.this, Profile.class);
                startActivity(intent);
                return true;
            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);

    }

}


