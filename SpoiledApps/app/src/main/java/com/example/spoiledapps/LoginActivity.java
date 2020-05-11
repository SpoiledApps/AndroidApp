package com.example.spoiledapps;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private Button mLogin, mRegister, mResetPassword;
    private EditText mUsername, mPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLogin = findViewById(R.id.LoginButton);
        mRegister = findViewById(R.id.RegisterButton);
        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        mResetPassword = findViewById(R.id.forgotPassword);
        progressBar = findViewById(R.id.progressBar2);
        auth = FirebaseAuth.getInstance();

        //Allows for a logged in user to remain logged in!
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
            finish();
        }

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        });//end Register Button implementation.

        mResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText someUserEmail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetMessage = new AlertDialog.Builder(v.getContext());
                passwordResetMessage.setTitle("Reset your Password!");
                passwordResetMessage.setMessage("Please enter the email used for your account to reset account.");
                passwordResetMessage.setView(someUserEmail);


                //When user chooses "Yes" to reset password
                passwordResetMessage.setPositiveButton("Yes please!", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String emailAddress = someUserEmail.getText().toString();
                        if (TextUtils.isEmpty(emailAddress)) {
                            someUserEmail.setError("First name is required!");
                            return;
                        }

                        auth.sendPasswordResetEmail(emailAddress).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginActivity.this, "The Reset Email Link has been sent! Check your inbox. ", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "The Reset Email Link could not send! " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                //When user chooses "No" to reset password
                passwordResetMessage.setNegativeButton("Never mind!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            //We do nothing!!
                    }
                });
                passwordResetMessage.create().show();

            }//end onClick
        });//end Reset Password Button implementation.

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsername.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(username)) {
                    mUsername.setError("Username is required!");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required!");
                    return;
                }
                if (password.length() < 8) {
                    mPassword.setError("Your password must be at least 8 characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //We begin to authenticate username and password credentials.
              //  if(someUser.isEmailVerified()) {
                    auth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser someUser = auth.getCurrentUser();
                                if(someUser.isEmailVerified()) {
                                    startActivity(new Intent(getApplicationContext(), HomePageActivity.class));

                                    Toast.makeText(LoginActivity.this, "You're now logged in!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }//end mini if statement
                                else {
                                    someUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(LoginActivity.this, "You are currently NOT VERIFIED which you need to do in order to log in! Another email link has been sent to your email. Please check your inbox. ", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {

                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(LoginActivity.this, "You are currently NOT VERIFIED! However, we could not send another verification email link to you. " + e.getMessage(), Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    });
                                }//end mini else statement
                            } else {
                                Toast.makeText(LoginActivity.this, "There was a problem logging in! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });//end implementation of auth.signInWithEmailAndPassword!
                //}//end if(someUser.isEmailVerified())

            }//end onClick
        });//end Login Button implementation

        getSupportActionBar().hide();
    }
}//end class

