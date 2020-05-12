package com.example.spoiledapps;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

public class AppDetailActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private LinearLayout scrollView;
    private ArrayList<Review> reviewsList;
    private TextView appTitleLabel;
    private TextView companyLabel;
    private Button writeReviewButton;
    private String documentID;
    private int topMargin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail);



        documentID = getIntent().getStringExtra("documentID");
        System.out.println("Swapped Intent ID: " + documentID);

        reviewsList = new ArrayList<Review>();
        scrollView = findViewById(R.id.reviewlistscrolllayout);

        writeReviewButton = findViewById(R.id.writereviewbutton);

        appTitleLabel = findViewById(R.id.AppTitle);
        companyLabel = findViewById(R.id.CompanyName);

        System.out.println("Init");
        System.out.println(appTitleLabel == null);
        System.out.println(companyLabel == null);

        auth = FirebaseAuth.getInstance();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("Apps").document(documentID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String appName = documentSnapshot.get("App_Title").toString();
                        String companyName = documentSnapshot.get("Company_Name").toString();
                        appTitleLabel.setText(appName);
                        companyLabel.setText("Company Name: " + companyName);
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
                                String authorid = documentSnapshot.get("Author_ID").toString();
                                String pros = documentSnapshot.get("Pros").toString();
                                String cons = documentSnapshot.get("Cons").toString();
                                String favorite = documentSnapshot.get("Favorite Feature").toString();
                                String leastFavorite = documentSnapshot.get("Least Favorite Feature").toString();
                                int authorReview = (int) Float.parseFloat(documentSnapshot.get("Author Reputation Score at Time of Review").toString());
                                float rating = Float.parseFloat(documentSnapshot.get("Rating").toString());



                                Review review = new Review(authorid, headline, reviewText, pros, cons, favorite, leastFavorite, rating, authorReview);

                                System.out.println(review);

                                reviewsList.add(review);
                            }

                            placeReviews(0);
                        }
                    }
                });
        writeReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent writeReviewIntent = new Intent(getApplicationContext(),WriteReviewPage.class);
                writeReviewIntent.putExtra("documentID", documentID);
                startActivity(writeReviewIntent);
            }
        });

    }

    private void placeReviews(final int i) {
        if(i >= reviewsList.size()) {
            return;
        }
        final Review review = reviewsList.get(i);
        final RelativeLayout appLayout = new RelativeLayout(this.getBaseContext());
        RelativeLayout.LayoutParams linearParams = new RelativeLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,600);
        appLayout.setLayoutParams(linearParams);
        scrollView.addView(appLayout);
        topMargin = 5;


        final TextView headlineView = getTextView(review.getHeadline(), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, 10, topMargin, Color.BLACK, 20);
        appLayout.addView(headlineView);
        headlineView.post(new Runnable() {
            @Override
            public void run() {
                topMargin += headlineView.getHeight();
                final TextView generalView = getTextView(" General Review: " + review.getGeneralReview(), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, 10, topMargin, Color.BLACK, 15);
                appLayout.addView(generalView);
                generalView.post(new Runnable() {
                    @Override
                    public void run() {
                        topMargin += generalView.getHeight();
                        final TextView prosView = getTextView(" Pros: " + review.getPros(), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, 10, topMargin, Color.BLACK, 15);
                        appLayout.addView(prosView);
                        prosView.post(new Runnable() {
                            @Override
                            public void run() {
                                topMargin += prosView.getHeight();
                                final TextView consView = getTextView(" Cons: " + review.getCons(), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, 10, topMargin, Color.BLACK, 15);
                                appLayout.addView(consView);
                                consView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        topMargin += consView.getHeight();
                                        final TextView favoriteView = getTextView(" Favorite Feature: " + review.getFavoriteFeature(), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, 10, topMargin, Color.BLACK, 15);
                                        appLayout.addView(favoriteView);
                                        favoriteView.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                topMargin += favoriteView.getHeight();
                                                final TextView leastView = getTextView(" Least Favorite Feature: " + review.getLeastFavoriteFeature(), WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, 10, topMargin, Color.BLACK, 15);
                                                appLayout.addView(leastView);
                                                leastView.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        topMargin += leastView.getHeight();
                                                        String rating = ((int) (review.getRating() * 100)) + "%";
                                                        System.out.println("Rating: " + rating);
                                                        appLayout.addView(getTextView("Rating: " + rating, 500, 60, 10, topMargin + 20, Color.BLACK, 15));
                                                        Button upvote = createButton("▲", 150, 100, 300, topMargin, Color.BLACK, 15);
                                                        Button downvote = createButton("▼", 150, 100, 500, topMargin, Color.BLACK, 15);
                                                        appLayout.addView(upvote);
                                                        appLayout.addView(downvote);
                                                        upvote.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                VoteAction(true, review.getAuthorID());
                                                                Toast.makeText(AppDetailActivity.this, "Upvote Recorded!", Toast.LENGTH_LONG).show();
                                                            }
                                                        });

                                                        downvote.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                VoteAction(false, review.getAuthorID());
                                                                Toast.makeText(AppDetailActivity.this, "Downvote Recorded!", Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                                        appLayout.addView(getTextView("Author Rep: " + review.getAuthorScore(), 500, 60, 700, topMargin + 20, Color.BLACK, 15));
                                                        topMargin += 105;
                                                        appLayout.addView(createLine(WindowManager.LayoutParams.MATCH_PARENT, 5, 0, topMargin,  Color.RED));
                                                        appLayout.setMinimumHeight(topMargin + 5);
                                                        placeReviews(i + 1);


                                                    }
                                                });


                                            }
                                        });

                                    }
                                });

                            }
                        });

                    }
                });
            }
        });


        /*appLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appDetailIntent = new Intent(getApplicationContext(),AppDetailActivity.class);
                appDetailIntent.putExtra("documentID", v.getTag().toString());
                startActivity(appDetailIntent);
            }
        });*/
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

        final DocumentReference reputationScoreRef = db.collection("Users").document(userDocNumber);
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
        System.out.println("Auth: " + userAuthID);
        //String userDocNum = fetchUserDocumentId(userAuthID);
        System.out.println("Doc: " + userAuthID);
        double voterReputation = fetchVoterReputationScore(userAuthID);
        System.out.println("Rep: " + voterReputation);
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

    protected View createLine(int width, int height, int leftMargin, int topMargin, int color) {
        View view = new View(this.getBaseContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        params.leftMargin = leftMargin;
        params.topMargin = topMargin;
        view.setBackgroundColor(color);
        view.setLayoutParams(params);
        return view;
    }

    protected Button createButton(String text, int width, int height, int leftMargin, int topMargin, int color, int textSize) {
        Button button = new Button(this.getBaseContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        params.leftMargin = leftMargin;
        params.topMargin = topMargin;
        button.setTextColor(color);
        button.setTextSize(textSize);
        button.setLayoutParams(params);
        button.setText(text);
        return button;
    }

    protected TextView getTextView(String text, int width, int height, int leftMargin, int topMargin, int color, int textSize) {
        TextView view = new TextView(this.getBaseContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        params.leftMargin = leftMargin;
        params.topMargin = topMargin;
        view.setTextColor(color);
        view.setTextSize(textSize);
        view.setLayoutParams(params);
        view.setText(text);
        return view;
    }
}