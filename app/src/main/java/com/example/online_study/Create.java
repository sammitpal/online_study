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

public class Create extends AppCompatActivity {

    EditText email,password,confpass;
    Button register;
    FirebaseAuth mAuth;
    CardView loginpage;
    ProgressDialog progressCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        email = (EditText) findViewById(R.id.createemail);
        password = (EditText) findViewById(R.id.createpass);
        confpass = (EditText) findViewById(R.id.confpass);
        register = (Button) findViewById(R.id.create);
        loginpage = (CardView) findViewById(R.id.loginpage);
        mAuth = FirebaseAuth.getInstance();
        progressCreate = new ProgressDialog(this);
        progressCreate.setTitle("Please Wait");
        progressCreate.setMessage("Creating your Profile....");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressCreate.show();
                if(email.getText().length()!=0 && password.getText().length()!=0 && confpass.getText().length()!=0) {
                    if (password.getText().toString().equals(confpass.getText().toString())) {
                        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    sendEmailVerification();
                                    progressCreate.dismiss();
                                } else {
                                    progressCreate.dismiss();
                                    Toast.makeText(Create.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        progressCreate.dismiss();
                        Toast.makeText(Create.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    progressCreate.dismiss();
                    Toast.makeText(Create.this,"One or more field is empty",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private void sendEmailVerification()
    {
        FirebaseUser mUser = mAuth.getCurrentUser();
        if(mUser!=null)
        {
            mUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(Create.this,"Verification Mail sent",Toast.LENGTH_LONG).show();
                        Toast.makeText(Create.this,"Account Created Successfully",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Create.this,Login.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(Create.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}