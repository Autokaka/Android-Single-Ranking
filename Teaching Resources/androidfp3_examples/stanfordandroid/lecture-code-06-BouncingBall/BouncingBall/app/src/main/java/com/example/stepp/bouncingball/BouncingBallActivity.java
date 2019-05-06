/*
 * CS 193A, Winter 2015, Marty Stepp
 * This activity is basically just an empty shell to hold the BouncingBallView.
 */

package com.example.stepp.bouncingball;

import android.app.Activity;
import android.os.Bundle;

public class BouncingBallActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bouncing_ball);
    }
}
