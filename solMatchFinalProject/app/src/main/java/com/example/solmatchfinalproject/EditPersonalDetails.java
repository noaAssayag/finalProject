package com.example.solmatchfinalproject;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
import com.google.android.material.navigation.NavigationView;
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

import Fragment.AlertDialogFragmentViewDonations;
import Fragment.AlertDialogFragmentViewHost;
import Fragment.AlertListenerProf;
import Fragment.AletListener;
import Fragment.MyAlertDialogFragmentListenerView;
import Fragment.NotificationDialogFragment;
import Model.Host;
import Model.Professional;
import Model.Review;
import Model.UserStorageData;
import Model.donations;
import dataBase.DatabaseHelper;
import donations.donationAdapter;


public class EditPersonalDetails extends AppCompatActivity implements RecycleViewInterface, OnImageSelectedListener,NavigationView.OnNavigationItemSelectedListener, MyAlertDialogFragmentListenerView, AletListener, AlertListenerProf {
    private static final int PICK_IMAGE_REQUEST = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 11;
    // todo add bell icon with notifications
    ImageView userImg;
    EditText userName, userEmail, birthDate, attributes;
    TextView donationTitle, hostTitle,reviewsTitle;

    DrawerLayout drawerLayout;
    Button btEdit;
    ImageView changeImage;
    RecyclerView recHosts, recDonations,recReviews;
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
        reviewsTitle=findViewById(R.id.reviewTitle);
        recReviews=findViewById(R.id.ReviewRecycler);
        recHosts = findViewById(R.id.hostingPromptRecycler);
        recDonations = findViewById(R.id.donationsPromptRecycler);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        sqlDatabase = new DatabaseHelper(this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recHosts.setLayoutManager(llm);

        LinearLayoutManager llm2 = new LinearLayoutManager(this);
        llm2.setOrientation(LinearLayoutManager.VERTICAL);
        recDonations.setLayoutManager(llm2);

        LinearLayoutManager llm3 = new LinearLayoutManager(this);
        llm3.setOrientation(LinearLayoutManager.VERTICAL);
        recReviews.setLayoutManager(llm3);


        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_gradient));
            ab.setTitle(R.string.profileTitle);
            ab.setDisplayShowHomeEnabled(false); // Set this to false
            ab.setDisplayHomeAsUpEnabled(false);  // Set this to false
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

// Create the ActionBarDrawerToggle but don't sync its state
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().show();
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
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
                                reviewsTitle.setVisibility(View.GONE);
                                presentHostSql(0, user.getUID());
                                donationTitle.setVisibility(View.GONE);
                                break;
                            }
                            case "Host": {
                                reviewsTitle.setVisibility(View.GONE);
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
                                DatabaseHelper databaseHelper=new DatabaseHelper(EditPersonalDetails.this);
                                List<Professional> list=databaseHelper.getAllProfessionals();
                                for(Professional professional:list)
                                {
                                    if(user.getUID().equals(professional.getUID()))
                                    {
                                        List<Review> reviews=professional.getReviews();
                                        ReviewAdapter adapter= new ReviewAdapter(reviews,EditPersonalDetails.this);
                                        recReviews.setAdapter(adapter);
                                    }
                                }

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
        sqlDatabase = new DatabaseHelper(this);
        List<donations> allDonList = sqlDatabase.getAllDonations();
        donations newDon = allDonList.get(position);
        AlertDialogFragmentViewDonations frag = new AlertDialogFragmentViewDonations();
        Bundle b = new Bundle();
        b.putSerializable("Donation", newDon);
        frag.setArguments(b);
        frag.show(getFragmentManager(), "dialog");
    }

    @Override
    public void deleteDonation(int position) {

    }

    @Override
    public void AddComments(int position) {

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
        DatabaseHelper helper = new DatabaseHelper(this);
        switch (item.getItemId()) {
            case R.id.notificationIcon:
                List<notifications> notificationsList = helper.getNotificationsByUserID(FirebaseAuth.getInstance().getUid());
                NotificationDialogFragment dialogFragment = new NotificationDialogFragment(notificationsList);
                dialogFragment.show(getSupportFragmentManager(), "NotificationDialogFragment");

                return true;
            case R.id.chatIcon:
                Intent i = new Intent(this, chatMenuActivity.class);
                startActivity(i);
                return true;

            case R.id.profileIcon:
                getSupportActionBar().hide();
                DrawerLayout drawerLayout;
                drawerLayout = findViewById(R.id.drawer_layout);
                NavigationView navigationView = findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(this);
                View headerView = navigationView.getHeaderView(0);
                ImageView imgProf = headerView.findViewById(R.id.imgProfile);
                TextView userName = headerView.findViewById(R.id.fullName);
                TextView userEmail = headerView.findViewById(R.id.emailAddress);
                UserStorageData user = sqlDatabase.getUserByUID(FirebaseAuth.getInstance().getUid());
                userName.setText(user.getUserName());
                userEmail.setText(user.getEmail());
                Glide.with(getApplicationContext())
                        .load(user.getImage())
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
                        .into((ImageView) imgProf);
                drawerLayout.openDrawer(GravityCompat.START);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        DatabaseHelper helper = new DatabaseHelper(this);
        List<notifications> notifications = helper.getNotificationsByUserID(FirebaseAuth.getInstance().getUid());
        if (!notifications.isEmpty()) {
            MenuItem item = menu.findItem(R.id.notificationIcon);
            item.setIcon(R.drawable.notification_icon_full);

        }
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.bt_home:
                intent = new Intent(EditPersonalDetails.this, MainActivity2.class);
                intent.putExtra("Search", false);
                break;

            case R.id.addEvent:
                intent = new Intent(EditPersonalDetails.this, AddEvent.class);
                startActivity(intent);

                break;
            case R.id.bt_search:
                intent = new Intent(EditPersonalDetails.this, Forms.class);
                startActivity(intent);
                break;

            case R.id.bt_Profile:
                intent = new Intent(EditPersonalDetails.this, EditPersonalDetails.class);
                startActivity(intent);
                break;

            case R.id.nav_share:
                try {
                    // Create a new intent with the action ACTION_SEND to share data.
                    Intent i = new Intent(Intent.ACTION_SEND);

                    // Set the type of data to be shared to plain text.
                    i.setType("text/plain");

                    // Set the subject of the message (optional).
                    i.putExtra(Intent.EXTRA_SUBJECT, "My app name");

                    // Create a message to be shared.
                    String strShareMessage = "\nLet me recommend you this application\n\n";

                    // Add a Play Store link to your app using your app's package name.
                    strShareMessage = strShareMessage + "https://play.google.com/store/apps/details?id=" + getPackageName();

                    // Create a Uri for an image (screenshot) to be shared.
                    Uri screenshotUri = Uri.parse("android.resource://packagename/drawable/image_name");

                    // Set the type of data to be shared to image/png.
                    i.setType("image/png");

                    // Attach the image Uri to the intent as an EXTRA_STREAM.
                    i.putExtra(Intent.EXTRA_STREAM, screenshotUri);

                    // Set the text message to be shared (includes the Play Store link).
                    i.putExtra(Intent.EXTRA_TEXT, strShareMessage);

                    // Create a chooser dialog to let the user choose which app to use for sharing.
                    startActivity(Intent.createChooser(i, "Share via"));
                } catch (Exception e) {
                    // Handle any exceptions that may occur during the sharing process.
                }

                break;

            case R.id.bt_logout:
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setMessage(R.string.dialog_messageLogOut);
                builder.setTitle(R.string.dialog_titleLogOut);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent1 = new Intent(EditPersonalDetails.this, LoginActivity.class);
                        startActivity(intent1);
                        finish();
                        Toast.makeText(EditPersonalDetails.this, "Logout!", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDialogPositiveClick(AlertDialogFragmentViewHost dialog) {

    }

    @Override
    public void onDialogPositiveClick(AlertDialogFragmentViewProf dialog) {

    }

    @Override
    public void onDialogPositiveClick(AlertDialogFragmentViewDonations dialog) {

    }
}
