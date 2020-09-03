package com.example.online_study;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class Complete_Profile extends AppCompatActivity {

    EditText username,useremail,userphone;
    ImageView uploadImage;
    FirebaseDatabase mData;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    Button upload;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Uri imagePath;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==123 && resultCode == RESULT_OK && data.getData()!=null)
        {
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
                uploadImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete__profile);

        username = (EditText) findViewById(R.id.username);
        useremail = (EditText) findViewById(R.id.useremail);
        userphone = (EditText) findViewById(R.id.phone);
        upload = (Button) findViewById(R.id.upload);
        uploadImage = (ImageView) findViewById(R.id.uimage);

        mAuth = FirebaseAuth.getInstance();
        mData = FirebaseDatabase.getInstance();
        mRef = mData.getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),123);
            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(useremail.getText().length()!=0 && username.getText().length()!=0 && userphone.getText().length()!=0)
                {
                    StorageReference imageRef = storageReference.child(mAuth.getUid());
                    UploadTask uploadTask = imageRef.putFile(imagePath);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Complete_Profile.this, "Upload Failed", Toast.LENGTH_LONG).show();
                        }
                    });
                    UserProfile userProfile = new UserProfile(username.getText().toString(),useremail.getText().toString(),userphone.getText().toString());
                    mRef.child(mAuth.getUid()).setValue(userProfile);
                    Toast.makeText(Complete_Profile.this, "Uploaded Successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Complete_Profile.this, Login.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(Complete_Profile.this, "One or More Field is empty", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}