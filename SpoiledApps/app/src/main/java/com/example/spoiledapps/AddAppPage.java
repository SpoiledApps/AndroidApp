package com.example.spoiledapps;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class AddAppPage extends AppCompatActivity
{
    private static final String tag = "Add_App_Activity";
    private static final String KEY_AppName = "App_Title";
    private static final String KEY_CompanyName = "Company_Name";

    private EditText editTextAppName;
    private EditText editTextCompanyName;

    private FirebaseFirestore db;

    private Button addAppButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_app_page);

        db = FirebaseFirestore.getInstance();

        editTextAppName = findViewById(R.id.enterAppNameField);
        editTextCompanyName = findViewById(R.id.enterAppCompanyField);

        addAppButton = findViewById(R.id.AddNewAppButton);

        addAppButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String AppName = editTextAppName.getText().toString().trim();
                                                String CompanyName = editTextCompanyName.getText().toString().trim();

                                                Map<String, Object> addNewAppToListing = new HashMap<>();

                                                addNewAppToListing.put(KEY_AppName, AppName);
                                                addNewAppToListing.put(KEY_CompanyName, CompanyName);

                                                db.collection("Apps").document(AppName).set(addNewAppToListing);
                                                Toast.makeText(AddAppPage.this, "App has been added! ", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(getApplicationContext(),HomePageActivity.class));
                                            }
                                        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
