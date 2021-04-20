package com.example.ATS.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Nikhil on 3/16/2016.
 */
public class Sign_up extends Activity {

    EditText name, email, pass;
    Button btn;
    SQLiteDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        db = openOrCreateDatabase("ATS", Context.MODE_PRIVATE, null);

        name = (EditText) findViewById(R.id.input_name);
        email = (EditText) findViewById(R.id.input_email);
        pass = (EditText) findViewById(R.id.input_password);

        btn = (Button) findViewById(R.id.btn_signup);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (name.getText().toString().trim().length() == 0 ||
                        email.getText().toString().trim().length() == 0 ||
                        pass.getText().toString().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "please enter all the fields"
                            , Toast.LENGTH_SHORT).show();
                    return;
                }
                Cursor c = db.rawQuery("SELECT * FROM user WHERE email='" + email.getText() +
                        "' AND password='" + pass.getText() + "'", null);
                if(c.moveToFirst())
                {
                    Toast.makeText(getApplicationContext(), "Email Id already exists", Toast.LENGTH_SHORT).show();
                }
                else {
                    db.execSQL("INSERT INTO user (email,name,password) VALUES('" + email.getText() +
                            "','" + name.getText() + "','" + pass.getText() + "');");
                    Toast.makeText(getApplicationContext(), "Sign up successful", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(
                            Sign_up.this,
                            LoginPage.class);
                    startActivity(i);
                }
            }
        });
    }
    /*
    public void onClick(View view) {

        if(view==btn) {
            if (name.getText().toString().trim().length() == 0 ||
                    email.getText().toString().trim().length() == 0 ||
                    pass.getText().toString().trim().length() == 0) {
                //Toast.makeText(getApplicationContext(), "please enter all the fields", Toast.LENGTH_SHORT).show();
                showMessage("Error", "Please enter all values");
                return;
            }
                db.execSQL("CREATE TABLE IF NOT EXISTS " + email.getText() + "(email VARCHAR,name VARCHAR,password VARCHAR);");
                db.execSQL("INSERT INTO " + email.getText() + " VALUES('" + email.getText() + "','" + name.getText() + "','" + pass.getText() + "');");
                Toast.makeText(getApplicationContext(), "Sign up successful", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(
                        Sign_up.this,
                        MapsActivity.class);
                startActivity(i);
        }
    }
    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void clearText()
    {
        pass.setText("");
    }*/
}
