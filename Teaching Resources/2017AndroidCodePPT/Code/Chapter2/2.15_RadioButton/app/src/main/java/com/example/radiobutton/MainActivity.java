package com.example.radiobutton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    RadioGroup rg1;
    RadioButton rb1;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListenerRadioButton();
    }
    private void addListenerRadioButton() {
        rg1 = (RadioGroup) findViewById(R.id.radiogroup1);
        b1 = (Button) findViewById(R.id.button2);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selected = rg1.getCheckedRadioButtonId();
                rb1 = (RadioButton) findViewById(selected);
                Toast.makeText(MainActivity.this, rb1.getText(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
