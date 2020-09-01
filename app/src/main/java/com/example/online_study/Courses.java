package com.example.online_study;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Courses extends AppCompatActivity {

    ImageView cpp,java;
    static int y=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        cpp = (ImageView) findViewById(R.id.cpp);
        java = (ImageView) findViewById(R.id.java);

        cpp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                y=1;
                Intent intent = new Intent(Courses.this, PDFview.class);
                startActivity(intent);
            }
        });

        java.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                y=2;
                Intent intent = new Intent(Courses.this, PDFview.class);
                startActivity(intent);
            }
        });

    }
}