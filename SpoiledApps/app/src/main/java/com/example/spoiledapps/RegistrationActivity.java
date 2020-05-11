package com.example.spoiledapps;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.opencensus.tags.Tag;

public class RegistrationActivity extends AppCompatActivity {

    private static final String tag = "Registration_Activity";
    private static final String KEY_username = "Username";
    private static final String KEY_firstName = "First Name";
    private static final String KEY_lastName = "Last Name";
    private static final String KEY_email = "Email Address";
    private static final String KEY_password = "password";
    public static final String KEY_numReviews = "Number of Reviews";
    private static final String KEY_reputation = "Reputation_Score";
    private static final String KEY_appsReviewed = "Apps Reviewed by User:";
    private static final String KEY_trueUserId = "True User ID";
    private int numReviews;
    private double reputationScore;
    private static ArrayList<String> appsReviewed;

    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private FirebaseFirestore db;
    private ConstraintLayout background;

    //following variables are for Firebase authentication;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private Button mRegisterBtn, mgoBacktoLoginScreen;

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userID;

    public int getNumReviews(){
        return numReviews;
    }
    public double getRepScore(){
        return reputationScore;
    }
    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        db = FirebaseFirestore.getInstance();

        background = findViewById(R.id.registrationBackground);
        editTextFirstName = findViewById(R.id.firstName);
        editTextLastName = findViewById(R.id.lastName);
        editTextEmail = findViewById(R.id.emailAddress);
        editTextUsername = findViewById(R.id.editText3);
        editTextPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();
        mRegisterBtn = findViewById(R.id.registrationButton);
        mgoBacktoLoginScreen = findViewById(R.id.backtoLogin);


        Objects.requireNonNull(getSupportActionBar()).hide();

        background.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
                return true;
            }
        });

        mgoBacktoLoginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = editTextFirstName.getText().toString().trim();
                String lastName = editTextLastName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (TextUtils.isEmpty(firstName)) {
                    editTextFirstName.setError("First name is required!");
                    return;
                }
                if (TextUtils.isEmpty(lastName)) {
                    editTextLastName.setError("Last name is required!");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    editTextEmail.setError("Email address is required!");
                    return;
                }
                if (TextUtils.isEmpty(username)) {
                    editTextUsername.setError("Username is required!");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Password is required!");
                    return;
                }
                if (password.length() < 8) {
                    editTextPassword.setError("Your password must be 8 characters or more!");
                    return;
                }

                //Register the user and take them to the Home Page!
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.VISIBLE);

                        if (task.isSuccessful()) {

                            //Verification Link Code Below!
                            FirebaseUser someUser = auth.getCurrentUser();
                            someUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegistrationActivity.this, "Thanks for registering! Email Verification has been sent! Check your inbox.", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                private String TAG = "";

                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Email verification NOT sent! "+ e.getMessage());
                                }
                            });

                            submitRegistration();
                            startActivity(new Intent(getApplicationContext(), verificationPage.class));
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(RegistrationActivity.this, "There was an error! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }//end onComplete()
                });
            }
        });

    }

    //Method collects data and copies it to database!
    public void submitRegistration() {
        appsReviewed = new ArrayList<String>();
        numReviews =0;
        reputationScore =0;

        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String email = editTextEmail.getText().toString();
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        Map<String, Object> userRegistration = new HashMap<>();
        userRegistration.put(KEY_firstName, firstName);
        userRegistration.put(KEY_lastName, lastName);
        userRegistration.put(KEY_email, email);
        userRegistration.put(KEY_username, username);
        userRegistration.put(KEY_password, password);
        userRegistration.put(KEY_numReviews, numReviews);
        userRegistration.put(KEY_reputation, reputationScore);
        userRegistration.put(KEY_appsReviewed, appsReviewed);

        userRegistration.put(KEY_trueUserId, userID);

        db.collection("Users").document(userID).set(userRegistration);
    }// end submitRegistration() method!


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }


}