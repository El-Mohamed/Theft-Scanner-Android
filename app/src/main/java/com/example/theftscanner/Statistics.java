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

    EditText mFilter;

    String[] Types;
    int[] Counts;
    String Filter;

    PieChart pieChart;
    PieDataSet dataSet;
    ArrayList<PieEntry> dataValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Reference = FirebaseDatabase.getInstance().getReference().child("Thefts");
        pieChart = findViewById(R.id.pieChart);
        mFilter = findViewById(R.id.preferred_city_stats);

        Types = getResources().getStringArray(R.array.vehicle_array);
        Counts = new int[6];

        for (int i = 0; i < 6; i++) {
            Counts[i] = 0;
        }

    }

    
    public void SetChart(View view) {

        for (int i = 0; i < 6; i++) {
            Counts[i] = 0;
        }
        mFilter.onEditorAction(EditorInfo.IME_ACTION_DONE);
        Filter = mFilter.getText().toString();

        if (!Filter.isEmpty()) {

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

                    dataValues = new ArrayList<>();

                    for (int index = 0; index < 6; index++) {

                        int val = Counts[index];

                        if (val != 0) {
                            dataValues.add(new PieEntry(val, Types[index]));
                        }
                    }

                    dataSet = new PieDataSet(dataValues, "");
                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    dataSet.setValueTextSize(15);


                    PieData pieData = new PieData(dataSet);
                    pieChart.getDescription().setText("");
                    pieChart.setEntryLabelTextSize(15);
                    pieChart.setHoleRadius(50);
                    pieChart.setTransparentCircleRadius(55);
                    pieChart.setHoleColor(Color.TRANSPARENT);
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

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }
}



