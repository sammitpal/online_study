package com.example.online_study;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class about extends AppCompatActivity {

    TextView version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        version = (TextView) findViewById(R.id.version);
        version.setText("Version Name: "+BuildConfig.VERSION_NAME);
    }
}