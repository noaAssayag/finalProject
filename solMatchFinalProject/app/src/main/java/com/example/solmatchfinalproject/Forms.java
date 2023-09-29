package com.example.solmatchfinalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
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
import com.example.solmatchfinalproject.Hosts.allHosts;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import Fragment.NotificationDialogFragment;
import Model.UserStorageData;
import dataBase.DatabaseHelper;

public class Forms extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private CardView professionalCardView;
    private DrawerLayout drawerLayout;
    private CardView hostCardView;
    private CardView donationCardView;
    private TextView textProf, textHost, textDon;
    private DatabaseHelper helper;
    FirebaseAuth mAuth;
    UserStorageData user;
    FirebaseFirestore firestore;
    String action = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms);
        professionalCardView = findViewById(R.id.cardProf);
        hostCardView = findViewById(R.id.cardHost);
        donationCardView = findViewById(R.id.cardDon);
        textProf = findViewById(R.id.textProf);
        textHost = findViewById(R.id.texthost);
        textDon = findViewById(R.id.textDon);
        helper = new DatabaseHelper(this);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_gradient));
            ab.setTitle(R.string.searchTitle);
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

        hostCardView.setVisibility(View.VISIBLE);
        donationCardView.setVisibility(View.VISIBLE);
        professionalCardView.setVisibility(View.VISIBLE);
        textProf.setText("Search Proffessional");
        textHost.setText("Search host");
        textDon.setText("Search donation");
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
                UserStorageData user = helper.getUserByUID(FirebaseAuth.getInstance().getUid());
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
                intent = new Intent(Forms.this, MainActivity2.class);
                intent.putExtra("Search", false);
                break;

            case R.id.addEvent:
                intent = new Intent(Forms.this, AddEvent.class);
                startActivity(intent);

                break;
            case R.id.bt_search:
                intent = new Intent(Forms.this, Forms.class);
                startActivity(intent);
                break;

            case R.id.bt_Profile:
                intent = new Intent(Forms.this, EditPersonalDetails.class);
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
                        Intent intent1 = new Intent(Forms.this, LoginActivity.class);
                        startActivity(intent1);
                        finish();
                        Toast.makeText(Forms.this, "Logout!", Toast.LENGTH_SHORT).show();

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

    public void clickPro() {
        Intent intent = new Intent(Forms.this, AddDocprofessional.class);
        startActivity(intent);
    }
}