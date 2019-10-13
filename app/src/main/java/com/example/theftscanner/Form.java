package com.example.theftscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Form extends AppCompatActivity {

    Button SendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);


        SendButton = findViewById(R.id.send_button);

        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Form.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(Form.this, R.string.toast_message, Toast.LENGTH_LONG).show();
            }
        });


    }
}
