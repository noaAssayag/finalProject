package com.example.solmatchfinalproject.profile;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.solmatchfinalproject.ChatClasses.chatMenuActivity;
import com.example.solmatchfinalproject.EditPersonalDetails;
import com.example.solmatchfinalproject.Hosts.RecycleViewInterface;
import com.example.solmatchfinalproject.Hosts.UserHostAdapter;
import com.example.solmatchfinalproject.Hosts.allHosts;
import com.example.solmatchfinalproject.LoginActivity;
import com.example.solmatchfinalproject.R;
import com.example.solmatchfinalproject.profileActivity;
import com.example.solmatchfinalproject.searchNavigationMenue;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import donations.donationAdapter;
import Model.donations;
import Model.Host;
import Model.UserStorageData;
import Model.donations;

public class ProfileActivity extends AppCompatActivity implements RecycleViewInterface {
    ImageView userImg;
    TextView userName, aboutme, titleDonations;
    EditText userEmail, birthDate;
    RecyclerView recDonations;
    BottomNavigationView menu;
    RecyclerView recHosts;
    List<Host> hostList = new ArrayList<>();
    List<donations> donList = new ArrayList<>();
    String uid;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference ref;
    DatabaseReference hostsRef;
    List<Host> list = new ArrayList<>();
    List<donations> donationList = new ArrayList<>();
    boolean solider = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilev2);
        userImg = findViewById(R.id.userImg);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        titleDonations = findViewById(R.id.donationPromptProfile);
        aboutme = findViewById(R.id.aboutMe);
        birthDate = findViewById(R.id.birthDateEditTxt);
        recDonations = findViewById(R.id.donationsPromptRecycler);
        recHosts = findViewById(R.id.hostingPromptRecycler);
        menu = findViewById(R.id.menu);
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        LinearLayoutManager llm2 = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        llm2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recDonations.setLayoutManager(llm);
        recHosts.setLayoutManager(llm2);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //we havent phone number and address for Users
                userName.setText(snapshot.child("userName").getValue().toString());
                userEmail.setText(snapshot.child("email").getValue().toString());
                aboutme.setText(snapshot.child("userInfo").child("description").getValue().toString());
                birthDate.setText(snapshot.child("birthday").getValue().toString());
                if (snapshot.child("type").getValue().toString().equals("Solider")) {
                    solider = true;
                    hostsRef = db.getReference("Users").child(uid).child("Host");

                } else {
                    solider = false;
                    hostsRef = db.getReference("Host");
                }
                presentHost(hostsRef);
                db = FirebaseDatabase.getInstance();


                if (snapshot.hasChild("image")) {
                    Glide.with(getApplicationContext())
                            .load(snapshot.child("image").getValue().toString())
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
                if (!solider) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Donations");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                if (child.child("email").getValue().toString().equals(userEmail.getText().toString())) {
                                    donations donation = new donations(child.child("name").getValue().toString(), child.child("adress").getValue().toString(), child.child("catagory").getValue().toString(), child.child("description").getValue().toString(), child.child("img").getValue().toString(), child.child("email").getValue().toString());
                                    donationList.add(donation);
                                }
                            }
                            recDonations.setLayoutManager(llm);
                            donationAdapter adapter = new donationAdapter(donationList, getApplicationContext());
                            recDonations.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    ArrayList<String> hobbies = new ArrayList<>();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("userInfo")
                            .child("hobbies");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                hobbies.add(snap.getValue().toString());
                            }
                            recDonations.setLayoutManager(llm);
                            hobbiesListAdapter adapter = new hobbiesListAdapter(hobbies);
                            recDonations.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }


        });





        menu.setOnItemReselectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.myHome: {
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    overridePendingTransition(0, 0);
                    break;
                }
                case R.id.chatMenu: {
                    startActivity(new Intent(getApplicationContext(), chatMenuActivity.class));
                    overridePendingTransition(0, 0);
                    break;
                }
                case R.id.search: {
                    startActivity(new Intent(getApplicationContext(), searchNavigationMenue.class));
                    overridePendingTransition(0, 0);
                    break;
                }
                case R.id.logOut: {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    overridePendingTransition(0, 0);
                    break;
                }
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }

    public void presentHost(DatabaseReference hostsRef)
    {
        hostsRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy, h:mm a", Locale.US);
                    try {
                        Date date = dateFormat.parse(snap.child("hostingDate").getValue(String.class));
                        Calendar currentDate = Calendar.getInstance();
                        Date today = currentDate.getTime();
                        if (date.after(today)) {
                            if (snap.getKey().equals(uid) || solider) {
                                Host newHost = new Host();
                                newHost.setHostName(snap.child("hostName").getValue(String.class));
                                newHost.setHostEmail(snap.child("hostEmail").getValue(String.class));
                                newHost.setHostAddress(snap.child("hostAddress").getValue(String.class));
                                newHost.setHostingDate(snap.child("hostingDate").getValue(String.class));
                                String imageUrl = snap.child("hostImg").getValue(String.class);
                                newHost.setHostImg(imageUrl);
                                newHost.setAccommodation((boolean) snap.child("accommodation").getValue());
                                newHost.setPets((boolean) snap.child("pets").getValue());
                                newHost.setPrivateRoom((boolean) snap.child("privateRoom").getValue());
                                newHost.setSecureEnv((boolean) snap.child("secureEnv").getValue());
                                newHost.setDescription(snap.child("description").getValue().toString());


                                list.add(newHost);
                            }
                        } else {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference ref = database.getReference().child("Host");
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnap : snapshot.getChildren()) {
                                        if (dataSnap.getKey().equals(snap.getKey())) {
                                            hostsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                    } else {
                                                        // Data removal failed
                                                        // Handle the error
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }

                            });
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                // Populate the RecyclerView with the retrieved list of hosts
                UserHostAdapter userHostAdapter = new UserHostAdapter(list, ProfileActivity.this, ProfileActivity.this);
                recHosts.setAdapter(userHostAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}
