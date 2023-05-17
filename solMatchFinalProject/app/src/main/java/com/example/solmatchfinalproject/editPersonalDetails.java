package com.example.solmatchfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import android.net.Uri;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

//import com.example.solmatchfinalproject.notificationService;

public class editPersonalDetails extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 11;
    private ImageView image;
    private BottomNavigationView bottomNavigationView;
    private TextView textTitleViewName;
    private TextView textViewName;
    private TextView textViewEmail;
    private TextView textViewPassword;
    private TextView textViewGender;
    private TextView textViewBirthday;

    private static final String USERS = "Users";
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private String UID;
    private StorageReference mStorgaeRef;
    private DatabaseReference mDatabaseRef;
    private String imageURL;
    Uri uri = null;

    /**
     * OnCreate function initiates the app with all variables
     * after initialization, we load the toolbar menu
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editdetails);
        bottomNavigationView = findViewById(R.id.menu);
        image = findViewById(R.id.profImg);
        textTitleViewName = findViewById(R.id.textViewTitleName);
        textViewName = findViewById(R.id.textViewName);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewPassword = findViewById(R.id.textViewPassword);
        textViewGender = findViewById(R.id.textViewGender);
        textViewBirthday = findViewById(R.id.textViewGender);

        bottomNavigationView.setOnItemReselectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.myHome: {
                    startActivity(new Intent(getApplicationContext(), editPersonalDetails.class));
                    overridePendingTransition(0, 0);
                    setContentView(R.layout.activity_editdetails);
                    break;
                }
                case R.id.calInvite:
//                    startActivity(new Intent(getApplicationContext(),profileActivity.class));
//                    overridePendingTransition(0,0);
//                    setContentView(R.layout.activity_profile);
                    break;
                case R.id.search:
//                    startActivity(new Intent(getApplicationContext(),profileActivity.class));
//                    overridePendingTransition(0,0);
//                    setContentView(R.layout.activity_profile);
                    break;
                case R.id.logOut:
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    overridePendingTransition(0, 0);
                    setContentView(R.layout.activity_login);
                    break;
            }
        });
        /**
         * getting a reference to the realTime database, and to the Storage functions provied to us by fireBase
         * After login we recive all User's details from the database including profile image.
         */
        Intent intent = getIntent();
        UID = intent.getStringExtra("UID");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child(USERS);
        //read from Database
        userRef.addValueEventListener(new ValueEventListener() {
            String userName, email, password, gen, birthday;

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot keyID : snapshot.getChildren()) {
                    if (keyID.child("uid").getValue().equals(UID)) {
                        userName = (keyID.child("userName").getValue(String.class));
                        email = keyID.child("email").getValue(String.class);
                        password = keyID.child("password").getValue(String.class);
                        gen = keyID.child("gen").getValue(String.class);
                        birthday = keyID.child("birthday").getValue(String.class);
                        imageURL = keyID.child("imageUrl").getValue(String.class);
                      //  notificationService not = new notificationService();
                      //  Context con = getApplicationContext();
                    //    not.sendNotification("test", intent, con);
                        break;
                    }
                }
                /**
                 * after feching all the information we present them onto the layout elements
                 */
                if (userName != null) {
                    textViewName.setText(userName);
                }
                if (email != null) {
                    textViewEmail.setText(email);
                }
                ;
                if (password != null) {
                    textViewPassword.setText(password);
                }
                ;
                if (gen != null) {
                    textViewGender.setText(gen);
                }
                ;
                if (birthday != null) {
                    textViewBirthday.setText(birthday);
                }
                ;
                textTitleViewName.setText(userName);
                if (imageURL == null) {
                    if (gen != null) {
                        if (gen.equals("Female")) {
                            image.setImageResource(R.drawable.anonymouswoman);
                        } else if (gen.equals("Male")) {
                            image.setImageResource(R.drawable.anonymousman);
                        }
                    }
                } else {
                    //Using Glide liberery to show image into the imageView using the URL
                    Glide.with(editPersonalDetails.this).load(imageURL).into(image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    if (imageBitmap != null) {
                        image.setImageBitmap(imageBitmap);
                    }
                }
            }
        }

    }

    


