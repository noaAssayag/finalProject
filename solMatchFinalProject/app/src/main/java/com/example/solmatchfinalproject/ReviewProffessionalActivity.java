package com.example.solmatchfinalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import Model.Professional;
import Model.Review;
import Model.UserStorageData;
import dataBase.DatabaseHelper;

public class ReviewProffessionalActivity extends AppCompatActivity {

    RatingBar ratingBar;
    EditText name;
    EditText et_comments;
    TextView description;
    Button btn_cancel, btn_submit;
    ImageView proImg;
    Professional professional;
    Professional updateProfessional;


    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    List<Professional> allProfessionalList = new ArrayList<>();
    DatabaseHelper sqlDatabase;
    float rate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_proffessional);
        ratingBar = findViewById(R.id.rating_bar);
        name = findViewById(R.id.your_name);
        et_comments = findViewById(R.id.et_comments);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_submit = findViewById(R.id.btn_submit);
        description = findViewById(R.id.description);
        proImg = findViewById(R.id.professionImg);
        sqlDatabase = new DatabaseHelper(this);
        allProfessionalList = sqlDatabase.getAllProfessionals();
        professional = getIntent().getParcelableExtra("professional");
        if (professional.getImageUrl() != null && !professional.getImageUrl().isEmpty()) {
            Glide.with(getApplicationContext())
                    .load(professional.getImageUrl())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, com.bumptech.glide.request.target.Target<Drawable> target, boolean isFirstResource) {
                            // Handle image loading failure
                            Log.e("Glide", "Image loading failed: " + e.getMessage());
                            return false; // Return false to allow Glide to handle the error and show any error placeholder you have set
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            // Image successfully loaded
                            return false; // Return false to allow Glide to handle the resource and display it
                        }
                    })
                    .into(proImg);
        }

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rate = v;
                if (v == 0) {
                    description.setText("Very Dissatisfied");
                } else if (v == 1) {
                    description.setText("Dissatisfied");

                } else if (v == 2 || v == 3) {
                    description.setText("OK");

                } else if (v == 4) {
                    description.setText("Staisfied");
                } else {
                    description.setText("Very Staisfied");

                }

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentName = "";
                if (et_comments.getText().toString().isEmpty()) {
                    Toast.makeText(ReviewProffessionalActivity.this, "you must add comments", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (name.getText().toString().isEmpty()) {
                        commentName = "anonymous";
                    } else {
                        commentName = name.getText().toString();
                    }

                    Review review = new Review(commentName, rate, et_comments.getText().toString(), professional.getUID());
                    if (professional != null) {
                        ArrayList<Review> list;
                        if (professional.getReviews() != null) {
                            list = (ArrayList<Review>) professional.getReviews();
                        } else {
                            list = new ArrayList<>();

                        }
                        list.add(review);
                        professional.setReviews(list);

                    }
                    firestore.collection("professional")
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Professional currentPro = document.toObject(Professional.class);
                                            if (currentPro.getUID().equals(professional.getUID())) {
                                                firestore.collection("professional").document(document.getId()).set(professional).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        sqlDatabase.insertProfessionalReviewData(review);
                                                        Toast.makeText(ReviewProffessionalActivity.this, "Review Added Successfully", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(ReviewProffessionalActivity.this, AllProfessional.class);
                                                        startActivity(intent);
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }
                            });
                }

            }
        });

    }
}