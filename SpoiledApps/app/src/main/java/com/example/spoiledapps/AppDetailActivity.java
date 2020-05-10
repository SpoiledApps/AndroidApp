package com.example.spoiledapps;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class AppDetailActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private LinearLayout scrollView;
    private ArrayList<App> appsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        String documentID = getIntent().getStringExtra("documentID");
        System.out.println("Swapped Intent ID: " + documentID);

        scrollView = findViewById(R.id.applistscroll);

        auth = FirebaseAuth.getInstance();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("Apps").document(documentID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        
                    }
                });

    }
}
