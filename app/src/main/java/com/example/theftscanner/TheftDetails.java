package com.example.theftscanner;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TheftDetails extends AppCompatActivity {

    RecyclerView recyclerView;
    Adapter adapter;

    EditText mSearchText;
    Button mSearchButton;

    DatabaseReader databaseReader;
    List<Theft> mThefts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theft_details);
        mSearchButton = findViewById(R.id.search_button_details);
        mSearchText = findViewById(R.id.search_text_details);

        recyclerView = findViewById(R.id.recyclerview_details);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mThefts = new ArrayList<>();
        databaseReader = new DatabaseReader();

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String City = mSearchText.getText().toString();
                mThefts = databaseReader.GetAllThefts(City);
                KeyboardHelper.hideKeyboard(TheftDetails.this);
                adapter = new Adapter(TheftDetails.this, mThefts);
                recyclerView.setAdapter(adapter);

            }
        });

    }

}
