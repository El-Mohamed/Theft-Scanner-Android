package com.mohamed.theftscanner;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Statistics extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;

    EditText mSearchText;
    Button mSearchButton;
    String inputText;
    PieChart pieChart;

    String[] vehicleTypes;
    int[] counts;

    PieDataSet dataSet;
    ArrayList<PieEntry> dataValues;

    Theft tempTheft;
    List<Theft> allThefts;

    int[] colorLabels = {Color.rgb(229, 38, 30),
            Color.rgb(235, 117, 50),
            Color.rgb(163, 224, 71),
            Color.rgb(209, 58, 231),
            Color.rgb(67, 85, 219),
            Color.rgb(52, 187, 230),
            Color.rgb(73, 218, 154),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Thefts");

        pieChart = findViewById(R.id.pieChart);
        mSearchText = findViewById(R.id.search_text_statistics);
        mSearchButton = findViewById(R.id.search_button_statistics);

        vehicleTypes = getResources().getStringArray(R.array.vehicle_array);
        counts = new int[7];

        for (int i = 0; i < vehicleTypes.length; i++) {
            counts[i] = 0;
        }

        allThefts = new ArrayList<>();

        pieChart.setNoDataText("Enter a city in the search bar.");

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readSearchBar();
                getStatistics(inputText);
            }
        });

    }


    public void getStatistics(final String inputCity) {

        for (int i = 0; i < vehicleTypes.length; i++) {
            counts[i] = 0;
        }

        mSearchText.onEditorAction(EditorInfo.IME_ACTION_DONE);

        if (!inputCity.isEmpty()) {

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

                    calculateStatistics();
                    setPieChartEntries();
                    setPieChart();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

    public void setPieChartEntries() {

        dataValues = new ArrayList<>();
        int[] EndColors = colorLabels;
        int ColorCounter = 0;

        for (int index = 0; index < vehicleTypes.length; index++) {

            int val = counts[index];

            if (val != 0) {

                EndColors[ColorCounter] = colorLabels[index];
                ColorCounter++;
                dataValues.add(new PieEntry(val, vehicleTypes[index]));

            }
        }
    }

    public void setPieChart() {

        dataSet = new PieDataSet(dataValues, "");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(15);
        dataSet.setColors(colorLabels);
        PieData pieData = new PieData(dataSet);

        pieChart.getDescription().setText("");
        pieChart.setEntryLabelTextSize(15);
        pieChart.setHoleRadius(50);
        pieChart.setTransparentCircleRadius(55);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setUsePercentValues(true);

        Legend legend = pieChart.getLegend();
        legend.setTextColor(Color.WHITE);
        legend.setTextSize(15);
        legend.setWordWrapEnabled(true);
        legend.setXEntrySpace(14);
        legend.setForm(Legend.LegendForm.CIRCLE);

        pieChart.setData(pieData);
        pieChart.invalidate();
        pieChart.animateXY(1500, 1500);

    }

    private void calculateStatistics() {

        for (Theft theft : allThefts) {

            for (int i = 0; i < 7; i++) {

                if (theft.getType().equals(vehicleTypes[i])) {
                    int oldValue = counts[i];
                    int newValue = oldValue + 1;
                    counts[i] = newValue;
                }
            }
        }
    }

    private void readSearchBar() {
        inputText = mSearchText.getText().toString();
    }

}



