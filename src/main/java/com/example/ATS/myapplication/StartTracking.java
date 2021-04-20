package com.example.ATS.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by Nikhil on 3/18/2016.
 */
public class StartTracking extends Activity {
    ImageButton imageButton;
    SQLiteDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_tracking);

        addListenerOnButton();

    }

    public void addListenerOnButton() {

        imageButton = (ImageButton) findViewById(R.id.myButton);

        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Toast.makeText(StartTracking.this, "ImageButton (selector) is clicked!",
                        Toast.LENGTH_SHORT).show();

                Intent intent1 = getIntent();
                String email = intent1.getStringExtra("Email");
                Intent i = new Intent(
                        StartTracking.this,
                        MapsActivity.class);
                //Log.e("Email2",""+em);

                i.putExtra("Email", email);
                startActivity(i);
            }

        });

    }

}
