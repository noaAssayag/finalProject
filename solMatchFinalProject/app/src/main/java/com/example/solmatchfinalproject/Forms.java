package com.example.solmatchfinalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.solmatchfinalproject.Hosts.AddHost;
import com.example.solmatchfinalproject.Hosts.allHosts;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import Model.UserStorageData;

public class Forms extends AppCompatActivity {
    private ImageView backArrow;
    private CardView professionalCardView;
    private CardView hostCardView;
    private CardView donationCardView;
    private TextView textProf, textHost, textDon;
    FirebaseAuth mAuth;
    UserStorageData user;
    FirebaseFirestore firestore;
    boolean search;
    /*todo change to sqllite*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms);
        backArrow = findViewById(R.id.backArrow);
        professionalCardView = findViewById(R.id.cardProf);
        hostCardView = findViewById(R.id.cardHost);
        donationCardView = findViewById(R.id.cardDon);
        textProf = findViewById(R.id.textProf);
        textHost = findViewById(R.id.texthost);
        textDon = findViewById(R.id.textDon);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        search = getIntent().getBooleanExtra("Search", false);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Forms.this, MainActivity2.class);
                startActivity(intent);            }
        });
        if (!search) {
            firestore.collection("Users").document(mAuth.getUid()).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                user = documentSnapshot.toObject(UserStorageData.class);
                                if (user.getType().equals("Solider")) {
                                    hostCardView.setVisibility(View.GONE);
                                    professionalCardView.setVisibility(View.GONE);
                                } else if (user.getType().equals("Host")) {
                                    professionalCardView.setVisibility(View.GONE);
                                }
                            }
                        }
                    });


            professionalCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Forms.this, AddDocprofessional.class);
                    startActivity(intent);
                }
            });
            hostCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Forms.this, AddHost.class);
                    startActivity(intent);
                }
            });
            donationCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Forms.this, addDonationActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            textProf.setVisibility(View.GONE);
            textHost.setVisibility(View.GONE);
            textDon.setVisibility(View.GONE);
            professionalCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Forms.this, AllProfessional.class);
                    startActivity(intent);
                }
            });
            hostCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Forms.this, allHosts.class);
                    startActivity(intent);
                }
            });
            donationCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Forms.this, All_donation_activity.class);
                    startActivity(intent);
                }
            });
        }

    }
}