package com.example.ATS.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class Distance extends Activity {
    TextView t1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statsdisplay);
        t1 = (TextView) findViewById(R.id.textview);
        MapsActivity m=new MapsActivity();
        float x=m.distance;
        t1.setText(Float.toString(x));
    }
}
