package com.example.spoiledapps;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class WriteReviewPage extends AppCompatActivity {

    private static final String tag = "Write_Review_Activity";
    private static final String KEY_authorID = "Author_ID";
    private static final String KEY_appID = "App_ID";
    private static final String KEY_headline = "Review Headline";
    private static final String KEY_rating = "Rating";
    private static final String KEY_pros = "Pros";
    private static final String KEY_cons = "Cons";
    private static final String KEY_favFeature = "Favorite Feature";
    private static final String KEY_leastFavFeature = "Least Favorite Feature";
    private static final String KEY_freeformSection = "Freeform Section";

    private EditText editTextHeadline;
    private EditText editTextRating;
    private EditText editTextPros;
    private EditText editTextCons;
    private EditText editTextFavFeature;
    private EditText editTextleastFavFeature;
    private EditText editTextFreeform;

    private FirebaseFirestore db;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userID;
    //private TextView textViewData;

    private Button submitReviewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review_page);

        db = FirebaseFirestore.getInstance();

        editTextHeadline = findViewById(R.id.ReviewHeadline);
        editTextRating = findViewById(R.id.RatingAsDecimal);
        editTextPros = findViewById(R.id.ProsFillable);
        editTextCons = findViewById(R.id.ConsFillable);
        editTextFavFeature = findViewById(R.id.FavFeatureFillable);
        editTextleastFavFeature = findViewById(R.id.leastFavFeatureFillable);
        editTextFreeform = findViewById(R.id.FreeformReviewFillable);


        submitReviewButton = findViewById(R.id.SubmitReviewButton);


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

        submitReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String headline = editTextHeadline.getText().toString().trim();
                String rating = editTextRating.getText().toString().trim();
                String pros = editTextPros.getText().toString().trim();
                String cons = editTextCons.getText().toString().trim();
                String favFeat = editTextFavFeature.getText().toString().trim();
                String leastFavFeat = editTextleastFavFeature.getText().toString().trim();
                String freeform = editTextFreeform.getText().toString().trim();

                //Insert Logic to Fetch authorID here
                fAuth = FirebaseAuth.getInstance();
                fStore = FirebaseFirestore.getInstance();
                userID = fAuth.getCurrentUser().getUid();
                //Insert Logic to Fetch AppID here

                Map<String, Object> reviewSubmission = new HashMap<>();
                //reviewSubmission.put(KEY_appID, insertFetchedAppID);
                //reviewSubmission.put(KEY_authorID, insertFetchedAuthorID);
                reviewSubmission.put(KEY_headline, headline);
                reviewSubmission.put(KEY_rating, rating);
                reviewSubmission.put(KEY_pros, pros);
                reviewSubmission.put(KEY_cons, cons);
                reviewSubmission.put(KEY_favFeature, favFeat);
                reviewSubmission.put(KEY_leastFavFeature, leastFavFeat);
                reviewSubmission.put(KEY_freeformSection, freeform);
                reviewSubmission.put(KEY_authorID, userID);

                db.collection("Reviews").document().set(reviewSubmission);
                Toast.makeText(WriteReviewPage.this, "Review submitted! ", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),HomePageActivity.class));
               /* db.collection("Users").document(userID).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists())
                                {
                                    Map<String, Object> user = documentSnapshot.getData();
                                    int currentNumReviews = user.get(KEY_numReviews);
                                }
                                else
                                {
                                    Toast.makeText(WriteReviewPage.this, "Document Doesn't Exist", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                db.collection("Users").document(userID).update({)
                
                */
            }


    });

    }
}
