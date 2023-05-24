package com.example.solmatchfinalproject;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.net.Uri;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Model.UserInfo;
import Model.UserStorageData;
import dataBase.MyInfoManager;
//import notification.notificationService;

public class EditPersonalDetails extends Activity {
    private static final int REQUEST_IMAGE_CAPTURE = 11;
    private ImageView image;
    private BottomNavigationView bottomNavigationView;
    private TextView textTitleViewName;

    private ListView listView;
    private UserInfoListAdapter adapter;
    private String email;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase db;
    DatabaseReference ref;
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
        bottomNavigationView.setOnItemReselectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.myHome: {
                    startActivity(new Intent(getApplicationContext(), EditPersonalDetails.class));
                    overridePendingTransition(0, 0);
                    break;
                }
                case R.id.calInvite: {
                    startActivity(new Intent(getApplicationContext(), profileActivity.class));
                    overridePendingTransition(0, 0);
                    break;
                }
                /*
                case R.id.search: {
                    startActivity(new Intent(getApplicationContext(), profileActivity.class));
                    overridePendingTransition(0, 0);
                    break;
                }
                */

                case R.id.logOut: {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    overridePendingTransition(0, 0);
                    break;
                }
            }
        });

        String uid=firebaseAuth.getCurrentUser().getUid();
        UserStorageData currentUser=new UserStorageData();
        //update the listview
        List<UserInfo> userInfos = new ArrayList<>();
        ref=db.getReference().child("Users").child(uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser.setUserName(snapshot.child("userName").getValue().toString());
                currentUser.setEmail(snapshot.child("email").getValue().toString());
                currentUser.setPassword(snapshot.child("password").getValue().toString());
                currentUser.setGen(snapshot.child("gen").getValue().toString());
                currentUser.setType(snapshot.child("type").getValue().toString());
                currentUser.setBirthday(snapshot.child("birthday").getValue().toString());
                String imageUrl = snapshot.child("imageUrl").getValue().toString();
                Bitmap imageBitmap = convertBase64ToBitmap(imageUrl);
                currentUser.setImage(imageBitmap);            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (currentUser != null) {
            if (currentUser.getUserName() == null) {
                userInfos.add(new UserInfo(R.string.name, "" + R.string.namePro));
            } else {
                userInfos.add(new UserInfo(R.string.name, currentUser.getUserName()));
            }
            if (currentUser.getEmail() == null) {
                userInfos.add(new UserInfo(R.string.email, "" + R.string.emailPro));
            } else {
                userInfos.add(new UserInfo(R.string.email, currentUser.getEmail()));
            }
            if (currentUser.getPassword() == null) {
                userInfos.add(new UserInfo(R.string.password, "" + R.string.passwordPro));
            } else {
                userInfos.add(new UserInfo(R.string.password, currentUser.getPassword()));
            }
            if (currentUser.getGen() == null) {
                userInfos.add(new UserInfo(R.string.gender, "" + R.string.genderPro));
            } else {
                userInfos.add(new UserInfo(R.string.gender, currentUser.getGen()));
            }
            if (currentUser.getBirthday() == null) {
                userInfos.add(new UserInfo(R.string.birthdate, "" + R.string.birthdayPro));
            } else {
                userInfos.add(new UserInfo(R.string.birthdate, currentUser.getBirthday()));
            }
            if(currentUser.getType()==null)
            {
                userInfos.add(new UserInfo(R.string.type, "" + R.string.typePro));
            } else {
                userInfos.add(new UserInfo(R.string.type, currentUser.getType()));
            }
        }
        listView = (ListView) findViewById(R.id.listOfDetailsToEdit);
        adapter = new UserInfoListAdapter(EditPersonalDetails.this, userInfos,currentUser.getEmail());
        listView.setAdapter(adapter);
        textTitleViewName.setText(currentUser.getUserName());
        if (currentUser.getImage() == null) {
            if (currentUser.getGen() != null) {
                if (currentUser.getGen().equals("Female")) {
                    image.setImageResource(R.drawable.anonymouswoman);
                } else if (currentUser.getGen().equals("Male")) {
                    image.setImageResource(R.drawable.anonymousman);
                }
            }
        }
        else
        {
            image.setImageBitmap(currentUser.getImage());
        }
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
    // Helper method to convert Base64 string to Bitmap
    private Bitmap convertBase64ToBitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
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




