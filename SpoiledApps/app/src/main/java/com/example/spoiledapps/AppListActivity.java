package com.example.spoiledapps;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class AppListActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private LinearLayout scrollView;
    private ArrayList<App> appsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        scrollView = findViewById(R.id.applistscrolllayout);
        appsList = new ArrayList<App>();

        auth = FirebaseAuth.getInstance();
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("Apps")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> apps = document.getData();
                                String appTitle = null;
                                String companyName = null;
                                String documentTag = document.getId();
                                for (Map.Entry<String,Object> entry : apps.entrySet()) {
                                    if(entry.getKey().toString().equalsIgnoreCase("App_Title")) {
                                        appTitle = entry.getValue().toString();
                                    } else if(entry.getKey().toString().equalsIgnoreCase("Company_Name")) {
                                        companyName = entry.getValue().toString();
                                    }
                                }
                                System.out.println(appTitle + " " + companyName);
                                appsList.add(new App(appTitle, companyName, documentTag));
                                System.out.println(appsList.get(appsList.size() - 1).getTitle());
                                System.out.println(appsList.get(appsList.size() - 1).getCompanyName());
                            }
                            createAppListings();
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void createAppListings() {
        int height = 350;
        for(int i = 0; i < appsList.size(); i++) {
            App app = appsList.get(i);
            RelativeLayout appLayout = new RelativeLayout(this.getBaseContext());
            RelativeLayout.LayoutParams linearParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            appLayout.setLayoutParams(linearParams);
            appLayout.setBackgroundColor(Color.parseColor("#77555555"));
            scrollView.addView(appLayout);

            appLayout.addView(getTextView(app.getTitle(), ViewGroup.LayoutParams.MATCH_PARENT, 150, 10, 10, Color.WHITE, 25));
            appLayout.addView(getTextView("Company: " + app.getCompanyName(), ViewGroup.LayoutParams.MATCH_PARENT, 150, 10, 100, Color.WHITE, 20));
            appLayout.addView(createLine(ViewGroup.LayoutParams.MATCH_PARENT, 5, 0, 345,  Color.RED));
            appLayout.setTag(app.getDocumentTag());
            appLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent appDetailIntent = new Intent(getApplicationContext(),AppDetailActivity.class);
                    appDetailIntent.putExtra("documentID", v.getTag().toString());
                    startActivity(appDetailIntent);
                }
            });
        }
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

    protected TextView getTextView(String text, int width, int height, int leftMargin, int topMargin, int color, int textSize) {
        TextView view = new TextView(this.getBaseContext());
        view.setText(text);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        params.leftMargin = leftMargin;
        params.topMargin = topMargin;
        view.setTextColor(color);
        view.setTextSize(textSize);
        view.setLayoutParams(params);
        return view;
    }
}