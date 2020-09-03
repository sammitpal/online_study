package com.example.online_study;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    FirebaseAuth mAuth;
    RelativeLayout comp,ece,mech,bio,civ,it;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        comp = (RelativeLayout) findViewById(R.id.computersc);
        comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this,Courses.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.side_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.signout: mAuth.signOut();
                               Intent intent = new Intent(Home.this, Login.class);
                               startActivity(intent);
                               finish();
                               break;

            case R.id.about:    Intent intent1 = new Intent(Home.this, about.class);
                                startActivity(intent1);
                                break;

            case R.id.profile: Intent intent2 = new Intent(Home.this,Profile.class);
                               startActivity(intent2);
                               break;
        }
        return super.onOptionsItemSelected(item);
    }
}