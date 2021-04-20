package com.example.ATS.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nikhil on 3/11/2016.
 */


public class Database extends Activity {

    EditText addr, address2, second, minute, Speed, dest;
    String addr1, addr2, addr3, time;
    SQLiteDatabase db;
    Button b;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        db = openOrCreateDatabase("ATS", Context.MODE_PRIVATE, null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_disp);

        Intent intent = getIntent();
        String email = intent.getStringExtra("Email");

        Toast.makeText(getApplicationContext(), "Email :" + email, Toast.LENGTH_SHORT).show();

        b=(Button)findViewById(R.id.save);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Database.this,
                        Survey.class);
                startActivity(i);
            }
        });
        Log.e("Mail11", "" + email);
        ArrayList<LatLng> l1 = new ArrayList<LatLng>();
        l1 = (ArrayList<LatLng>) getIntent().getSerializableExtra("First");
        double stoplat = getIntent().getDoubleExtra("StopLat", 0);
        double stoplong = getIntent().getDoubleExtra("StopLong", 0);
        double seconds = getIntent().getDoubleExtra("Time", 0);
        double distance2 = getIntent().getFloatExtra("Distance", 0);
        float speed = (float) (distance2 / (float) seconds);
        Log.e("seconds", "" + seconds);

        int minutes = (int) seconds / 60;
        Log.e("MInutes", "" + minutes);
        int second2 = (int) seconds % 60;
        Log.e("Seconds", "" + second2);

        //objs=points.get(0);
        //obje=points.get(points.size() - 1);
        addr = (EditText) findViewById(R.id.addr);
        address2 = (EditText) findViewById(R.id.address2);
        second = (EditText) findViewById(R.id.Seconds);
        minute = (EditText) findViewById(R.id.Minutes);
        Speed = (EditText) findViewById(R.id.Speed);
        dest = (EditText) findViewById(R.id.dest);

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        double latitude = l1.get(0).latitude;
        minute.setText(Integer.toString(minutes));
        second.setText(Integer.toString(second2));
        //time=Integer.toString(minutes)+" min "+Integer.toString(second2)+" sec";

        double longitude = l1.get(0).longitude;
        double lat2 = l1.get(l1.size() - 1).latitude;
        double lng2 = l1.get(l1.size() - 1).longitude;
        Speed.setText(Float.toString(speed));
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            StringBuffer sb = new StringBuffer(40);
            addr1 = null;
            addr2 = null;
            for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
                addr1 = sb.append("," + addresses.get(0).getAddressLine(i)).toString(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            }

            addresses = geocoder.getFromLocation(lat2, lng2, 1);
            for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
                addr2 = sb.append("," + addresses.get(0).getAddressLine(i)).toString(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            }
            ;
            // Only if available else return NULL

            if (addr1 != "") {
                //addr.setText("Gandhi Mandapam Road ,Kotturpuram,Chennai-600085 ");
                addr.setText(addr2);
            }
            if (addr2 != "") {
                dest.setText(addr2);
            }
        } catch (Exception e) {

        }
        if (stoplat != 0 && stoplong != 0) {
            try {
                addresses = geocoder.getFromLocation(stoplat, stoplong, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                StringBuffer sb = new StringBuffer(40);
                addr3 = null;
                for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
                    addr3 = sb.append("," + addresses.get(0).getAddressLine(i)).toString(); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                }
                ;
                // Only if available else return NULL

                if (addr3 != "") {
                    //address2.setText("Vellaiyan Road,Kotturpuram,Chennai-600085");
                    address2.setText(addr3);
                }
            } catch (Exception e) {

                }
                try {
                    Cursor c = db.rawQuery("SELECT * FROM user WHERE email='" + email + "'", null);
                    if (c.moveToFirst()) {
                        db.execSQL("UPDATE user SET start='" + addr1 + "' WHERE email='" + email + "'");
                        db.execSQL("UPDATE user SET end='" + addr2 + "' WHERE email='" + email + "'");
                    db.execSQL("UPDATE user SET minutes='" + Integer.toString(minutes) + "' WHERE email='" + email + "'");
                    db.execSQL("UPDATE user SET seconds='" + Integer.toString(second2) + "' WHERE email='" + email + "'");
                    db.execSQL("UPDATE user SET speed='" + Float.toString(speed) + "' WHERE email='" + email + "'");
                    Toast.makeText(getApplicationContext(), "Database updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid email id ", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Invalid email id", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
