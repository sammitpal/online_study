package com.example.online_study;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText email,password;
    CardView create;
    Button login;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (EditText) findViewById(R.id.lemail);
        password = (EditText) findViewById(R.id.lpass);
        login = (Button) findViewById(R.id.login);

        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setTitle("Loading...");
        progressDialog.setMessage("Please Wait...");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if(mUser!=null && mUser.isEmailVerified())
        {
            finish();
            Intent intent = new Intent(Login.this,Home.class);
            startActivity(intent);
        }

        create = (CardView) findViewById(R.id.createaccount);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Create.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().length()!=0 && password.getText().length()!=0)
                {
                    progressDialog.show();
                    mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                checkEmailVerification();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(Login.this,"One or more field is empty",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void checkEmailVerification()
    {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        Boolean ef = firebaseUser.isEmailVerified();
        if(ef)
        {
            Toast.makeText(Login.this, "Logged in successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Login.this,Home.class);
            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(Login.this,"Verify your email",Toast.LENGTH_LONG).show();
        }
    }
}