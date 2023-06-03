package com.example.solmatchfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.solmatchfinalproject.Hosts.AddHost;
import com.example.solmatchfinalproject.profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Model.Professional;

public class AddDocprofessional extends AppCompatActivity {
    private Spinner professCategory;
    private Spinner professAddress;
    private EditText professDescription;
    private EditText professPhoneNum;
    TextView percentage;
    private ProgressBar progressBar;
    private SeekBar seekBar;
    private Button btnSubmit;
    private BottomNavigationView bottomNavigationView;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference refUser, refProfess;
    String uid,email,userName,imageUser;
    String precentageAva="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_docprofessional);
        professCategory = findViewById(R.id.professCategory);
        professAddress = findViewById(R.id.professAddress);
        professDescription = findViewById(R.id.professDescription);
        professPhoneNum = findViewById(R.id.ProfessphoneNum);
        percentage=findViewById(R.id.percentage);
        progressBar=findViewById(R.id.prograssBar);
        seekBar = findViewById(R.id.seek_bar);
        btnSubmit=findViewById(R.id.btnSubmit);
        bottomNavigationView = findViewById(R.id.menu);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressBar.setProgress(progress);
                percentage.setText(""+progress+"%");
                precentageAva=""+progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (professCategory == null || !professCategory.getSelectedItem().toString().equals("Filter by category") || professCategory.getSelectedItem().toString().isEmpty()) {
                    Toast.makeText(AddDocprofessional.this, "Please choose a category", Toast.LENGTH_SHORT).show();
                    return;
                } else if (professAddress == null || !professAddress.getSelectedItem().toString().equals("Filter By city") || professAddress.getSelectedItem().toString().isEmpty()) {
                    Toast.makeText(AddDocprofessional.this, "Please choose a area", Toast.LENGTH_SHORT).show();
                    return;
                } else if (professDescription.getText().toString().isEmpty() || professPhoneNum.getText().toString().isEmpty()) {
                    Toast.makeText(AddDocprofessional.this, "Please fill all the fileds", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    auth = FirebaseAuth.getInstance();
                    uid = auth.getCurrentUser().getUid();
                    db = FirebaseDatabase.getInstance();
                    refUser = db.getReference().child("Users").child(uid);
                    refUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            email = snapshot.child("email").getValue().toString();
                            userName = snapshot.child("userName").getValue().toString();
                            if (snapshot.child("image").getValue() != null) {
                                imageUser = snapshot.child("image").getValue().toString();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    refProfess = db.getReference().child("professional").child(uid);
                    refProfess.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Professional professional= new Professional(email,userName,imageUser,professCategory.getSelectedItem().toString()
                                    ,professAddress.getSelectedItem().toString()
                                    ,professPhoneNum.getText().toString()
                                    ,professDescription.getText().toString(),precentageAva);
                            refProfess.setValue(professional);
                            Toast.makeText(AddDocprofessional.this, "This is Added Succefully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddDocprofessional.this, ProfileActivity.class);
                            startActivity(intent);
                            setContentView(R.layout.profilev2);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

    });
}
}