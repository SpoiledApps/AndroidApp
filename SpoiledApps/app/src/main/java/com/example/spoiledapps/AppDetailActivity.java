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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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

    public String fetchAuthUserID()
    {
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        String authUserID = fAuth.getCurrentUser().getUid();
        return authUserID;
    }
    public String fetchUserDocumentId(String authUserID)
    {
        final String[] returnValue = new String[1];
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersRef = db.collection("Users");
        Task<QuerySnapshot> idQuery = usersRef.whereEqualTo("True User ID", authUserID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document: task.getResult())
                    {
                        //Map targetDocument = document.getData();
                        //usersDocID = targetDocument.toString();
                        String usersDocID = document.getData().toString();
                        returnValue[0] = usersDocID;
                    }
                }
            }
        });
        return returnValue[0];
    }
    public double fetchVoterReputationScore(String userDocNumber)
    {
        final double[] returnValue = new double[1];
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference reputationScoreRef = db.collection("Users").document(userDocNumber);
        reputationScoreRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        double reputationScore = (double)documentSnapshot.get("Reputation_Score");
                        returnValue[0] = reputationScore;
                    }
                });
                return returnValue[0];
    }
    //Feed this method whichever ID is easily accessible from the vote click (Authid or docID)
    public void updateReviewAuthorRepuationScore(String docID, boolean favorable, double voterWeightValue)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final boolean positive = favorable;
        final double weight = voterWeightValue;
        final DocumentReference authorReputationRef = db.collection("Users").document(docID);
        authorReputationRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        double currentReputationScore = (double)documentSnapshot.get("Reputation_Score");
                        double newReputationScore;
                        if(positive)
                        {
                            newReputationScore = (currentReputationScore+weight);
                        }
                        else
                        {
                            newReputationScore = (currentReputationScore-weight);
                        }
                        authorReputationRef.update("Reputation_Score", newReputationScore);

                    }
                });
    }
    public void VoteAction(boolean favorable, String authorDocID)
    {
        double voterWeightValue;
        //Fetching Voter's Reputation Score
        String userAuthID = fetchAuthUserID();
        String userDocNum = fetchUserDocumentId(userAuthID);
        double voterReputation = fetchVoterReputationScore(userDocNum);
        if(voterReputation == 0.0)
        {
            voterWeightValue = 0.1;
        }
        else if(voterReputation < 0.0)
        {
            voterWeightValue = 0.01;
        }
        else
        {
            voterWeightValue = (voterReputation/10) + 0.1;
        }

        updateReviewAuthorRepuationScore(authorDocID,favorable, voterWeightValue);

    }
}
