package com.example.solmatchfinalproject.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.solmatchfinalproject.Hosts.AddHost;
import com.example.solmatchfinalproject.Hosts.RecycleViewInterface;
import com.example.solmatchfinalproject.Hosts.UserHostAdapter;
import com.example.solmatchfinalproject.OnImageSelectedListener;
import com.example.solmatchfinalproject.R;
import com.example.solmatchfinalproject.addDonationActivity;
import com.example.solmatchfinalproject.cameraFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dataBase.DatabaseHelper;
import donations.donationAdapter;
import Model.donations;
import Model.Host;
import Model.UserStorageData;

public class ProfileActivity extends AppCompatActivity implements RecycleViewInterface, OnImageSelectedListener {
    ImageView userImg;
    TextView statusDon, statusHost, attributes, donationTitle;
    EditText userName, userEmail, birthDate;
    Button addHost, AddDonation;
    RecyclerView recHosts;
    RecyclerView recDonations;
    ImageView changeImage;
    List<Host> hostList = new ArrayList<>();
    List<donations> donList = new ArrayList<>();
    String uid;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    FirebaseDatabase db;
    DatabaseReference ref;
    List<Host> list = new ArrayList<>();
    List<donations> donationList = new ArrayList<>();
    String type = "soldier";
    private DatabaseHelper sqlDatabase;
    private UserStorageData user;
    int status = 0;
    private static final int PICK_IMAGE_REQUEST = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 11;

    Uri imageURIProf;
    String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseDatabase.getInstance();
        setContentView(R.layout.profilev2);
        attributes = findViewById(R.id.attributes);
        userImg = findViewById(R.id.iv_profile);
        userName = findViewById(R.id.et_name);
        userEmail = findViewById(R.id.et_email);
        changeImage = findViewById(R.id.iv_update_pic);
        donationTitle = findViewById(R.id.donationPromptProfile);
        statusDon = findViewById(R.id.statusofDonation);
        statusHost = findViewById(R.id.statusOfHost);
        birthDate = findViewById(R.id.birthDateEditTxt);
        recDonations = findViewById(R.id.donationsPromptRecycler);
        recHosts = findViewById(R.id.hostingPromptRecycler);
        addHost = findViewById(R.id.newHostingButt);
        AddDonation = findViewById(R.id.newDonationButt);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        sqlDatabase = new DatabaseHelper(this);

        ref = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid());
        LinearLayoutManager llm = new LinearLayoutManager(this);
        LinearLayoutManager llm2 = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        llm2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recDonations.setLayoutManager(llm);
        recHosts.setLayoutManager(llm2);

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraFragment frag = new cameraFragment();
                frag.show(getFragmentManager(), "dialog");
            }
        });


        firestore.collection("Users").document(auth.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            user = documentSnapshot.toObject(UserStorageData.class);
                            userName.setText(user.getUserName());
                            userEmail.setText(user.getEmail());
                            birthDate.setText(user.getBirthday());
                            if (documentSnapshot.contains("image") && documentSnapshot.getString("image") != null) {
                                Glide.with(getApplicationContext())
                                        .load(documentSnapshot.getString("image"))
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
                                        .into(userImg);
                            }
                            if (user.getInfo() != null) {
                                attributes.setText("Description: " + user.getInfo().getDescription() + "\nHobbies: " + user.getInfo().getHobbiesString());
                            } else {
                                attributes.setVisibility(View.GONE);
                            }
                            switch (user.getType().toString()) {
                                case "Soldier": {
                                    type = "soldier";
                                    presentHostSql(0, user.getUID());
                                    donationTitle.setVisibility(View.GONE);
                                    break;
                                }
                                case "Host": {
                                    type = "Host";
                                    presentHostSql(1, user.getEmail());
                                    List<donations> allDonations = new ArrayList<>();
                                    allDonations = sqlDatabase.getAllDonations();
                                    for (donations donation : allDonations) {
                                        if (donation.getEmail().equals(user.getEmail())) {
                                            donationList.add(donation);
                                        }
                                    }
                                    if (donationList.isEmpty()) {
                                        statusDon.setVisibility(View.VISIBLE);
                                        statusDon.setText("no Donations");
                                    }
                                    break;
                                }
                                default:
                                    type = "professional";
                                    break;
                            }
                            if (documentSnapshot.contains("info")) {
                                //todo check the information about myself
                            } else {
                            }
                        }
                    }
                });

        recDonations.setLayoutManager(llm);
        donationAdapter adapter = new donationAdapter(donationList, ProfileActivity.this, ProfileActivity.this);
        recDonations.setAdapter(adapter);

        /** changeImage.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) {
        // Create a dialog to let the user choose between camera capture and image selection
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Choose Image Source");
        builder.setItems(new CharSequence[]{"Camera", "Gallery"}, new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialog, int which) {
        switch (which) {
        case 0:
        // Launch camera capture
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
        break;
        case 1:
        // Launch image selection from gallery
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
        break;
        }
        }
        });
        builder.show();
        }
        });
         **/


        addHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, AddHost.class);

                startActivity(intent);
            }
        });

        AddDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, addDonationActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }

    public void presentHostSql(int userType, String Email) {
        List<Host> hosts = sqlDatabase.getAllHosts();
        List<Host> relevantHosts = new ArrayList<>();
        if (userType == 0) {
            for (Host host : hosts) {
                if (host.getListOfResidents() != null && !host.getListOfResidents().isEmpty()) {
                    for (UserStorageData user : host.getListOfResidents()) {
                        if (user.getUID().equals(Email)) {
                            relevantHosts.add(host);
                        }
                    }
                }
            }
        } else if (userType == 1) {
            for (Host host : hosts) {
                if (host.getHostEmail().equals(Email)) {
                    relevantHosts.add(host);
                }
            }
        }
        if (relevantHosts.isEmpty()) {
            statusHost.setVisibility(View.VISIBLE);
            statusHost.setText("No Hostings");
        }
        UserHostAdapter userHostAdapter = new UserHostAdapter(relevantHosts, ProfileActivity.this, ProfileActivity.this);
        recHosts.setAdapter(userHostAdapter);
    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            imageURIProf = data.getData();
//            userImg.setImageURI(imageURIProf);
//        }
//    }

    @Override
    public void onImageSelected(Uri imageUri) {

            StorageReference storageRef = FirebaseStorage.getInstance().getReference(imageUri.toString());
            storageRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    boolean check = task.isSuccessful();
                    if (task.isSuccessful()) {
                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                URL = uri.toString();
                                userImg.setImageURI(imageUri);
                                user.setImage(URL);
                                firestore.collection("Users").document(auth.getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getApplicationContext(), "image has changed", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        });
                    }
                }
            });




    }
}
