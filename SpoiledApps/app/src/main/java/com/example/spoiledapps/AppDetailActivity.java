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
import com.google.android.gms.tasks.OnSuccessListener;
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
    private ArrayList<Review> reviewsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        final String documentID = getIntent().getStringExtra("documentID");
        System.out.println("Swapped Intent ID: " + documentID);

        reviewsList = new ArrayList<Review>();
        scrollView = findViewById(R.id.applistscroll);

        auth = FirebaseAuth.getInstance();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("Apps").document(documentID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String appName = documentSnapshot.get("App_Title").toString();
                        String companyName = documentSnapshot.get("Company_Name").toString();

                    }
                });

        database.collection("Reviews").whereEqualTo("App ID", documentID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                String headline = documentSnapshot.get("Review Headline").toString();
                                String reviewText = documentSnapshot.get("Freeform Section").toString();
                                String pros = documentSnapshot.get("Pros").toString();
                                String cons = documentSnapshot.get("Cons").toString();
                                String favorite = documentSnapshot.get("Favorite Feature").toString();
                                String leastFavorite = documentSnapshot.get("Least Favorite Feature").toString();
                                float rating = Float.parseFloat(documentSnapshot.get("Rating").toString());

                                System.out.println(headline + " " + reviewText + " " + pros + " " + cons + " " + favorite + " " + leastFavorite + " " + rating);

                                Review review = new Review(headline, reviewText, pros, cons, favorite, leastFavorite, rating);

                                System.out.println(review);

                                reviewsList.add(review);
                            }
                        }
                    }
                });

    }
}
