package com.example.spoiledapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class verificationPage extends AppCompatActivity {
    private Button goBackToLogIn, resendEmailLink;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_page);
        goBackToLogIn = findViewById(R.id.backtoLogIn);
        resendEmailLink = findViewById(R.id.resendEmail);
        auth = FirebaseAuth.getInstance();


        goBackToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });//end Go Back To Login Page Button implementation.

        resendEmailLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser someUser = auth.getCurrentUser();
                someUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(verificationPage.this, "Email Verification has been sent again! Check your inbox.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    private String TAG = "";

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Email verification NOT sent! "+ e.getMessage());
                    }
                });
            }
        });//end resendEmailLink Button implementation.
    }//end onCreate
}//end Class