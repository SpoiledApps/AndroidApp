package com.example.spoiledapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        editTextFirstName = findViewById(R.id.firstName);
        editTextLastName = findViewById(R.id.lastName);
        editTextEmail = findViewById(R.id.emailAddress);
        editTextUsername = findViewById(R.id.editText3);
        editTextPassword = findViewById(R.id.password);
    }

    public void submitRegistration(View v)
    {
        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String email = editTextEmail.getText().toString();
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        Map<String, Object> userRegistration = new HashMap<>();
        userRegistration.put(KEY_firstName, firstName);
        userRegistration.put(KEY_lastName, lastName);
        userRegistration.put(KEY_email, email);
        userRegistration.put(KEY_username, username);
        userRegistration.put(KEY_password, password);

        db.collection("Users").document().set(userRegistration)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid)
                    {
                        Toast.makeText(RegistrationActivity.this, "User Registered", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(RegistrationActivity.this, "Registration Failed, Error", Toast.LENGTH_SHORT).show();
                        Log.d(tag, e.toString());
                    }
                });
    }
}
