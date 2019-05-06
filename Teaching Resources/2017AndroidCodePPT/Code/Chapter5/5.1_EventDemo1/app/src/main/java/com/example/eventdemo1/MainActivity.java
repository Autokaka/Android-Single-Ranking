package com.example.eventdemo1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //--- find both the buttons---
        Button sButton = (Button) findViewById(R.id.button);
        Button lButton = (Button) findViewById(R.id.button2);

        // -- register click event with first button ---
        sButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // --- find the text view --
                TextView txtView = (TextView) findViewById(R.id.textView);
                // -- change text size --
                txtView.setTextSize(14);
            }
        });

        // -- register click event with second button ---
        lButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // --- find the text view --
                TextView txtView = (TextView) findViewById(R.id.textView);
                // -- change text size --
                txtView.setTextSize(24);
            }
        });

    }
}
