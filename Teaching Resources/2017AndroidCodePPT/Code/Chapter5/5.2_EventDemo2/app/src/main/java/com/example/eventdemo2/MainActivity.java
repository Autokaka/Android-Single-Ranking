package com.example.eventdemo2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //--- find both the buttons---
        Button sButton = (Button) findViewById(R.id.button);
        Button lButton = (Button) findViewById(R.id.button2);


        // -- register click event with first button ---
        sButton.setOnClickListener(this);
        // -- register click event with second button ---
        lButton.setOnClickListener(this);
    }

    //--- Implement the OnClickListener callback
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            // --- find the text view --
            TextView txtView = (TextView) findViewById(R.id.textView);
            // -- change text size --
            txtView.setTextSize(14);
            return;
        }
        if (v.getId() == R.id.button2) {
            // --- find the text view --
            TextView txtView = (TextView) findViewById(R.id.textView);
            // -- change text size --
            txtView.setTextSize(24);
            return;
        }
    }
}
