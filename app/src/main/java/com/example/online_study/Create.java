package com.example.online_study;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Create extends AppCompatActivity {

    EditText email,password,confpass;
    Button register;
    FirebaseAuth mAuth;
    CardView loginpage;
    ProgressDialog progressCreate;
    String TAG = MainActivity.class.getSimpleName();
    String SITE_KEY = "6LdtjcoZAAAAAP0I-peNeY8bSFXU2D1rxDYjQCjl";
    String SECRET_KEY = "6LdtjcoZAAAAAKF8jlmxC4EdrwWflwQbF6W0BI10";
    RequestQueue queue;

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
        queue = Volley.newRequestQueue(getApplicationContext());

    }
    private void reg(){
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
                        Intent intent = new Intent(Create.this,Complete_Profile.class);
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

    public void onClick(View view) {
        SafetyNet.getClient(this).verifyWithRecaptcha(SITE_KEY)
                .addOnSuccessListener(this, new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                        if (!response.getTokenResult().isEmpty()) {
                            handleSiteVerify(response.getTokenResult());
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            Log.d(TAG, "Error message: " +
                                    CommonStatusCodes.getStatusCodeString(apiException.getStatusCode()));
                        } else {
                            Log.d(TAG, "Unknown type of error: " + e.getMessage());
                        }
                    }
                });

    }
    protected  void handleSiteVerify(final String responseToken){
        String url = "https://www.google.com/recaptcha/api/siteverify";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getBoolean("success")){
                                reg();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),String.valueOf(jsonObject.getString("error-codes")),Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception ex) {

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error message: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("secret", SECRET_KEY);
                params.put("response", responseToken);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
}
