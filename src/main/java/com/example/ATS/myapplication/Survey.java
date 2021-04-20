package com.example.ATS.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Nikhil on 3/22/2016.
 */
public class Survey extends Activity {

    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey);

        b=(Button)findViewById(R.id.survey_button);
        b.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {

                Uri uri = Uri.parse("https://docs.google.com/forms/d/1ZEhZO5DLPmI1NgKtWzbg7KLchZ9_gxBOpwC-LLortH8/viewform"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }

        });

        //Intent intent = new Intent(this, LoginActivity.class);
        //startActivity(intent);
    }
}
