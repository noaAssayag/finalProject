package com.example.solmatchfinalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.solmatchfinalproject.Hosts.AddHost;
import com.example.solmatchfinalproject.Hosts.RecycleViewInterface;
import com.example.solmatchfinalproject.Hosts.UserHostAdapter;
import com.example.solmatchfinalproject.profile.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import Model.Host;
import Model.UserStorageData;
import Model.donations;
import dataBase.DatabaseHelper;

public class EditPersonalDetails extends AppCompatActivity implements RecycleViewInterface,OnImageSelectedListener  {
    private static final int PICK_IMAGE_REQUEST = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 11;
    ImageView userImg;
    EditText userName, userEmail, birthDate,attributes;
    Button btEdit;
    ImageView changeImage;
    RecyclerView recHosts;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    String URL;
    private DatabaseHelper sqlDatabase;
    private UserStorageData user;
    boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_details);
        attributes = findViewById(R.id.attributes);
        userImg = findViewById(R.id.iv_profile);
        userName = findViewById(R.id.et_name);
        userEmail = findViewById(R.id.et_email);
        changeImage = findViewById(R.id.iv_update_pic);
        firestore = FirebaseFirestore.getInstance();
        recHosts = findViewById(R.id.hostingPromptRecycler);
        auth = FirebaseAuth.getInstance();
        sqlDatabase = new DatabaseHelper(this);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recHosts.setLayoutManager(llm);

        switch (user.getType().toString()) {
            case "Soldier": {
                presentHostSql(0, user.getUID());
                break;
            }
            case "Host": {
                presentHostSql(1, user.getEmail());
                break;
            }
        }
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
                            if (documentSnapshot.contains("info")) {
                                //todo check the information about myself
                            } else {
                            }
                        }
                    }
                });

        btEdit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                if (!isEdit) {
                    userName.setEnabled(true);
                    userEmail.setEnabled(true);
                    btEdit.setText("Save");
                    userName.setTextColor(R.color.mild_black);
                    userEmail.setTextColor(R.color.mild_black);
                    isEdit = true;
                } else {
                    userName.setEnabled(false);
                    userEmail.setEnabled(false);
                    btEdit.setText("Edit");
                    userName.setTextColor(R.color.black);
                    userEmail.setTextColor(R.color.black);
                    if (checkCredentials()) {
                        user.setEmail(userEmail.getText().toString());
                        user.setUserName(userName.getText().toString());
                        // todo update auth email
                        firestore.collection("Users").document(auth.getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(), "The user's values were saved successfully", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                    isEdit = false;
                }
                if (!checkCredentials()) {
                    isEdit = true;
                    userName.setEnabled(true);
                    userEmail.setEnabled(true);
                    btEdit.setText("Save");
                    Toast.makeText(getApplicationContext(), "The value is invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onImageSelected(Uri imageUri) {

        StorageReference storageRef = FirebaseStorage.getInstance().getReference(imageUri.toString());
        storageRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
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
        UserHostAdapter userHostAdapter = new UserHostAdapter(relevantHosts, (Context) EditPersonalDetails.this, (RecycleViewInterface) EditPersonalDetails.this);
        recHosts.setAdapter(userHostAdapter);
    }
    private boolean checkCredentials() {
        if (userName.getText().toString().isEmpty() || userName.getText().toString().length() < 7 || !userName.getText().toString().matches("[a-zA-Z ]+")) {
            RegisterActivity.showError(userName, "Your username is not valid!");
            return false;
        }
        if (!RegisterActivity.isValidEmail(userEmail.getText().toString())) {
            RegisterActivity.showError(userEmail, "Your username is not valid!");
            return false;
        }
        return true;
    }

    @Override
    public void onItemClick(int position) {

    }
}
