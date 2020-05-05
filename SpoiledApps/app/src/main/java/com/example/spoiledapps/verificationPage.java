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
               auth.signOut();
               finish();
               startActivity(new Intent(getApplicationContext(),LoginActivity.class));
           }
       });

        resendEmailLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser someUser = auth.getCurrentUser();
                someUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(verificationPage.this, "Another Email Verification has been sent again! Check your inbox.", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(verificationPage.this, "Could not send another email verification email! " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });//end resendEmailLink Button implementation.

    }//end onCreate
}//end Class
