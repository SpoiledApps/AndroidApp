package com.example.spoiledapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private static final String tag = "Registration_Activity";
    private static final String KEY_username = "Username";
    private static final String KEY_firstName = "First_Name";
    private static final String KEY_lastName = "Last_Name";
    private static final String KEY_email = "Email_Address";
    private static final String KEY_password = "password";
    private static final String KEY_authenticated = "Authenticated?";
    private static final String KEY_numReviews = "Number_of_Reviews";
    private static final String KEY_reputation = "Reputation_Score";
    private static final String KEY_appsReviewed = "Apps_Reviewed_by_User:";

    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private FirebaseFirestore db;

    //following variables are for Firebase authentication;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private Button mRegisterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        db = FirebaseFirestore.getInstance();

        editTextFirstName = findViewById(R.id.firstName);
        editTextLastName = findViewById(R.id.lastName);
        editTextEmail = findViewById(R.id.emailAddress);
        editTextUsername = findViewById(R.id.editText3);
        editTextPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();
        mRegisterBtn = findViewById(R.id.registrationButton);

        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),HomePageActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = editTextFirstName.getText().toString().trim();
                String lastName = editTextLastName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String username = editTextUsername.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if(TextUtils.isEmpty(firstName)){
                    editTextFirstName.setError("First name is required!");
                    return;
                }
                if(TextUtils.isEmpty(lastName)){
                    editTextLastName.setError("Last name is required!");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    editTextEmail.setError("Email address is required!");
                    return;
                }
                if(TextUtils.isEmpty(username)){
                    editTextUsername.setError("Username is required!");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    editTextPassword.setError("Password is required!");
                    return;
                }
                if(password.length()<8){
                    editTextPassword.setError("Your password must be 8 characters or more!");
                    return;
                }

                //Register the user and take them to the Home Page!
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.VISIBLE);

                        if(task.isSuccessful()){
                            Toast.makeText(RegistrationActivity.this, "User Registered!", Toast.LENGTH_SHORT).show();
                            submitRegistration();
                            startActivity(new Intent (getApplicationContext(), HomePageActivity.class));
                        }
                        else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(RegistrationActivity.this, "There was an error! " +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }//end onComplete()
                });
            }
        });

    }
//Method collects data and copies it to database!
//Method also initializes new user values for reputation score, reviews, and apps reviewed

public void submitRegistration()
{
    String firstName = editTextFirstName.getText().toString();
    String lastName = editTextLastName.getText().toString();
    String email = editTextEmail.getText().toString();
    String username = editTextUsername.getText().toString();
    String password = editTextPassword.getText().toString();

    int numReviews = 0;
    double reputationScore = 0.0;
    String[] appsReviewed = new String[1000];

    Map<String, Object> userRegistration = new HashMap<>();
    userRegistration.put(KEY_firstName, firstName);
    userRegistration.put(KEY_lastName, lastName);
    userRegistration.put(KEY_email, email);
    userRegistration.put(KEY_username, username);
    userRegistration.put(KEY_password, password);

    //Putting in deafult New User Values for numReviews,reputation score, and apps reviewed
    userRegistration.put(KEY_numReviews, numReviews);
    userRegistration.put(KEY_reputation, reputationScore);
    userRegistration.put(KEY_appsReviewed, appsReviewed);

    db.collection("Users").document().set(userRegistration);
}// end submitRegistration() method!
}