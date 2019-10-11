package com.example.theftscanner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    private CardView card1;
    private CardView card2;
    private CardView card3;
    private CardView card4;
    private CardView card5;
    private CardView card6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        card1 = findViewById(R.id.map_card);
        card2 = findViewById(R.id.stats_card);
        card3 = findViewById(R.id.list_card);
        card4 = findViewById(R.id.add_card);
        card5 = findViewById(R.id.help_card);
        card6 = findViewById(R.id.donate_card);


        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Help.class);
                startActivity(intent);
            }
        });


        card6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String DonateURL = "https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=RUT5WG33KK3VN&source=url";
                Uri WebPage = Uri.parse(DonateURL);
                Intent intent = new Intent(Intent.ACTION_VIEW, WebPage);
                startActivity(intent);
            }
        });


    }


}
