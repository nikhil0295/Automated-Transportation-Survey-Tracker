package com.example.ATS.myapplication;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginPage extends Activity {

    Button b1, b2,b3;
    EditText ed1, ed2;

    TextView tx1;
    int counter = 3;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        db = openOrCreateDatabase("ATS", Context.MODE_PRIVATE, null);
        //db.execSQL("DROP TABLE user");
        //db.execSQL("ALTER TABLE user ADD COLUMN start VARCHAR");
        //db.execSQL("ALTER TABLE user ADD COLUMN start VARCHAR");
        //db.execSQL("CREATE TABLE IF NOT EXISTS user (email VARCHAR,name VARCHAR,password VARCHAR, start VARCHAR, end VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS user (email VARCHAR,name VARCHAR,password " +
                "VARCHAR, start VARCHAR, end VARCHAR, minutes VARCHAR,seconds VARCHAR, speed VARCHAR);");
        b1 = (Button) findViewById(R.id.btn_login);
        ed1 = (EditText) findViewById(R.id.log_input_email);
        ed2 = (EditText) findViewById(R.id.log_input_password);

        b2 = (Button) findViewById(R.id.btn_signup);

        b3 = (Button) findViewById(R.id.btn_viewall);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String em=ed1.getText().toString();
                //Log.e("Email",""+em);
                if (ed1.getText().toString().trim().length() == 0 ||
                        ed2.getText().toString().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "please enter all the fields",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                Cursor c = db.rawQuery("SELECT * FROM user WHERE email='" + ed1.getText() +
                        "' AND password='" + ed2.getText() + "'", null);
                if (c.moveToFirst()) {
                    Intent i = new Intent(
                            LoginPage.this,
                            StartTracking.class);
                    //Log.e("Email2",""+em);

                    i.putExtra("Email", em);
                    startActivity(i);
                    Toast.makeText(getApplicationContext(), "Welcome " + c.getString(1), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "invalid username or password ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        LoginPage.this,
                        Sign_up.class);
                startActivity(i);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Cursor c=db.rawQuery("SELECT * FROM user", null);
                if(c.getCount()==0)
                {
                    showMessage("Error", "No records found");
                    return;
                }
                StringBuffer buffer=new StringBuffer();
                while(c.moveToNext())
                {
                    buffer.append("Name    : "+c.getString(1)+"\n");
                    buffer.append("Email   : "+c.getString(0)+"\n");
                    buffer.append("Pass    : "+c.getString(2)+"\n\n");
                    buffer.append("Start   : "+c.getString(3)+"\n\n");
                    buffer.append("Dest    : "+ c.getString(4) + "\n\n");
                    buffer.append("Minutes : "+c.getString(5)+"\n\n");
                    buffer.append("Seconds : "+c.getString(6)+"\n\n");
                    buffer.append("Speed   : "+c.getString(7)+"\n\n");
                }
                showMessage("Student Details", buffer.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void clearText() {
        ed1.setText("");
        ed2.setText("");
    }
}