package com.example.theftscanner;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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

public class Statistics extends AppCompatActivity {

    private DatabaseReference Reference;

    EditText mSearchText;
    String Filter;
    PieChart pieChart;

    String[] VehicleTypes;
    int[] Counts;

    PieDataSet dataSet;
    ArrayList<PieEntry> dataValues;

    int[] ColorLabels = {Color.rgb(229, 38, 30),
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

        Reference = FirebaseDatabase.getInstance().getReference().child("Thefts");

        pieChart = findViewById(R.id.pieChart);
        mSearchText = findViewById(R.id.search_text_statistics);

        VehicleTypes = getResources().getStringArray(R.array.vehicle_array);
        Counts = new int[7];

        for (int i = 0; i < VehicleTypes.length; i++) {
            Counts[i] = 0;
        }

        pieChart.setNoDataText("Enter a city in the search bar.");

    }


    public void CalculateStatistics(View view) {

        for (int i = 0; i < VehicleTypes.length; i++) {
            Counts[i] = 0;
        }

        mSearchText.onEditorAction(EditorInfo.IME_ACTION_DONE);
        Filter = mSearchText.getText().toString();

        if (!Filter.isEmpty()) {

            Reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        String type = snapshot.child("type").getValue().toString();
                        String city = snapshot.child("city").getValue().toString();

                        if (Filter.equals(city)) {

                            for (int i = 0; i < 7; i++) {

                                if (type.equals(VehicleTypes[i])) {
                                    int oldValue = Counts[i];
                                    int newValue = oldValue + 1;
                                    Counts[i] = newValue;
                                }

                            }

                        }
                    }

                    dataValues = new ArrayList<>();
                    int[] EndColors = ColorLabels;
                    int ColorCounter = 0;

                    for (int index = 0; index < VehicleTypes.length; index++) {

                        int val = Counts[index];

                        if (val != 0) {

                            EndColors[ColorCounter] = ColorLabels[index];
                            ColorCounter++;
                            dataValues.add(new PieEntry(val, VehicleTypes[index]));

                        }
                    }

                    SetChart();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

    public void SetChart() {

        dataSet = new PieDataSet(dataValues, "");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(15);
        dataSet.setColors(ColorLabels);
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

}



