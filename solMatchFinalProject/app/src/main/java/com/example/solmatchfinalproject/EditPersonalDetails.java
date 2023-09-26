package com.example.solmatchfinalproject;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.example.solmatchfinalproject.ChatClasses.chatMenuActivity;
import com.example.solmatchfinalproject.Hosts.AddHost;
import com.example.solmatchfinalproject.Hosts.RecycleViewInterface;
import com.example.solmatchfinalproject.Hosts.UserHostAdapter;
import com.example.solmatchfinalproject.profile.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Fragment.AlertDialogFragmentViewHost;
import Model.Host;
import Model.UserStorageData;
import Model.donations;
import dataBase.DatabaseHelper;
import donations.donationAdapter;


public class EditPersonalDetails extends AppCompatActivity implements RecycleViewInterface, OnImageSelectedListener {
    private static final int PICK_IMAGE_REQUEST = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 11;
    // todo add bell icon with notifications
    ImageView userImg;
    EditText userName, userEmail, birthDate, attributes;
    TextView donationTitle, hostTitle;
    Button btEdit;
    ImageView changeImage;
    RecyclerView recHosts, recDonations;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    String URL;
    private DatabaseHelper sqlDatabase;
    private UserStorageData user;
    boolean isEdit = false;
    List<donations> donationList = new ArrayList<>();
    UserHostAdapter userHostAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_details);
        attributes = findViewById(R.id.attributes);
        userImg = findViewById(R.id.iv_profile);
        userName = findViewById(R.id.et_name);
        userEmail = findViewById(R.id.et_email);
        btEdit = findViewById(R.id.bt_Edit);
        birthDate = findViewById(R.id.birthDateEditTxt);
        changeImage = findViewById(R.id.iv_update_pic);
        donationTitle = findViewById(R.id.donationtitle);
        hostTitle = findViewById(R.id.hostTitle);
        recHosts = findViewById(R.id.hostingPromptRecycler);
        recDonations = findViewById(R.id.donationsPromptRecycler);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        sqlDatabase = new DatabaseHelper(this);
        ActionBar ab=getSupportActionBar();
        ab.setTitle(R.string.profileTitle);
        ab.setDisplayShowHomeEnabled(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recHosts.setLayoutManager(llm);

        LinearLayoutManager llm2 = new LinearLayoutManager(this);
        llm2.setOrientation(LinearLayoutManager.VERTICAL);
        recDonations.setLayoutManager(llm2);


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
                        switch (user.getType().toString()) {
                            case "Soldier": {
                                presentHostSql(0, user.getUID());
                                donationTitle.setVisibility(View.GONE);
                                break;
                            }
                            case "Host": {
                                presentHostSql(1, user.getEmail());
                                List<donations> allDonations = new ArrayList<>();
                                allDonations = sqlDatabase.getAllDonations();
                                for (donations donation : allDonations) {
                                    if (donation.getEmail().equals(user.getEmail())) {
                                        donationList.add(donation);
                                    }
                                }
                                donationAdapter adapter = new donationAdapter(donationList, EditPersonalDetails.this, EditPersonalDetails.this,false);
                                recDonations.setAdapter(adapter);
                                break;

                            }
                            case "Professional": {
                                donationTitle.setVisibility(View.GONE);
                                hostTitle.setVisibility(View.GONE);
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
                    userName.setTextColor(R.color.black);
                    userEmail.setTextColor(R.color.black);
                    isEdit = true;
                } else {
                    userName.setEnabled(false);
                    userEmail.setEnabled(false);
                    btEdit.setText("Edit");
                    userName.setTextColor(getResources().getColor(R.color.black));
                    userEmail.setTextColor(getResources().getColor(R.color.black));

                    if (checkCredentials()) {
                        user.setEmail(userEmail.getText().toString());
                        user.setUserName(userName.getText().toString());
                        // todo update auth email
                        firestore.collection("Users").document(auth.getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(), "The user's values were saved successfully", Toast.LENGTH_SHORT).show();
                                firestore.collection("Users").document(auth.getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getApplicationContext(), "The user's values were saved successfully", Toast.LENGTH_SHORT).show();
                                        FirebaseUser user = auth.getCurrentUser();
                                        user.updateEmail(userEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getApplicationContext(), "The user's values were saved in authenticator successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });

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
        userHostAdapter = new UserHostAdapter(relevantHosts, (Context) EditPersonalDetails.this, (RecycleViewInterface) EditPersonalDetails.this, false);
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
        sqlDatabase = new DatabaseHelper(this);
        List<Host> allHostsList = sqlDatabase.getAllHosts();
        Host newHost = allHostsList.get(position);
        AlertDialogFragmentViewHost frag = new AlertDialogFragmentViewHost();
        Bundle b = new Bundle();
        b.putSerializable("Host", newHost);
        frag.setArguments(b);
        frag.show(getFragmentManager(), "dialog");
    }

    @Override
    public void deleteItem(int position) {
        List<Host> hosts = sqlDatabase.getAllHosts();
        Host hostToDelete = hosts.get(position);

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.dialog_message);
        builder.setTitle(R.string.dialog_title);
        // Add the buttons
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Toast.makeText(getApplicationContext(), "User clicked OK button", Toast.LENGTH_SHORT).show();
                switch (user.getType().toString()) {
                    case "Soldier": {
                        // Check if the host has residents and the current user is one of them
                        if (hostToDelete.getListOfResidents() != null && !hostToDelete.getListOfResidents().isEmpty()) {
                            Iterator<UserStorageData> iterator = hostToDelete.getListOfResidents().iterator();
                            while (iterator.hasNext()) {
                                UserStorageData userlist = iterator.next();
                                if (userlist.getUID().equals(auth.getUid())) {
                                    iterator.remove(); // Remove the user from the list
                                }
                                firestore.collection("Users")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        UserStorageData currentUser = document.toObject(UserStorageData.class);
                                                        if (currentUser.getEmail().equals(hostToDelete.getHostEmail())) {
                                                            String uid = document.getId();
                                                            firestore.collection("Host").document(uid).set(hostToDelete).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    userHostAdapter.notifyDataSetChanged();
                                                                    Toast.makeText(getApplicationContext(), "The hosting has been successfully deleted, a message will be sent to the host", Toast.LENGTH_SHORT).show();
                                                                    //todo send message to host about cancel
                                                                }
                                                            });
                                                        }

                                                    }
                                                } else {
                                                    Log.w(TAG, "Error getting documents.", task.getException());
                                                }
                                            }
                                        });
                            }
                        }
                        break;
                    }
                    case "Host": {
                            List<UserStorageData> soliders = new ArrayList<>();
                            if (hostToDelete.getHostEmail().equals(auth.getCurrentUser().getEmail())) {
                                if (hostToDelete.getListOfResidents() != null && !hostToDelete.getListOfResidents().isEmpty()) {
                                    soliders = hostToDelete.getListOfResidents();
                                }
                                if (!soliders.isEmpty()) {
                                    //todo send each user message in chat that the host cancel the order
                                }

                            hosts.remove(position);

                            // Notify the RecyclerView adapter of the data change
                            userHostAdapter.notifyDataSetChanged();
                            firestore.collection("Host").document(auth.getUid()).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Notify the RecyclerView adapter of the data change
                                            Toast.makeText(getApplicationContext(), "The hosting was successfully deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error deleting document", e);
                                        }
                                    });
                        }


                    }

                }
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Toast.makeText(getApplicationContext(), "User cancelled the dialog", Toast.LENGTH_SHORT).show();

            }
        });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onDonationClick(int position,View v) {

    }

    @Override
    public void deleteDonation(int position) {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();
        switch(item.getItemId()) {
            case R.id.notificationIcon:
                return true;
            case R.id.chatIcon:
                Intent i = new Intent(this, chatMenuActivity.class);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
