package com.example.solmatchfinalproject.Hosts;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.solmatchfinalproject.ChatClasses.chatMenuActivity;
import com.example.solmatchfinalproject.Forms;
import com.example.solmatchfinalproject.LoginActivity;
import com.example.solmatchfinalproject.R;
import com.example.solmatchfinalproject.notifications;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.List;

import Fragment.AlertDialogFragmentViewHost;
import Fragment.MyAlertDialogFragmentListenerView;
import Fragment.NotificationDialogFragment;
import Model.Host;
import Model.UserStorageData;
import dataBase.DatabaseHelper;

public class allHosts extends AppCompatActivity implements RecycleViewInterface, MyAlertDialogFragmentListenerView, NavigationView.OnNavigationItemSelectedListener{
    FirebaseDatabase db;
    DatabaseReference ref,refUsers;
    ImageView img;
    private DrawerLayout drawerLayout;
    Spinner filterByGen;
    Spinner filterByLoc;
    Button btnFilter;
    RecyclerView recList;

    List<Host> list = new ArrayList<>();

    private List<Host> originalHostsList;

    private String filterGen="";
    private String filterLoc="";

    private DatabaseHelper sqlDatabase;

    private List<Host> allHostsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_hosts);
        recList = findViewById(R.id.cardList);
        filterByGen = (Spinner) findViewById(R.id.spinnerFilterByGender);
        filterByLoc = (Spinner) findViewById(R.id.spinnerFilterByLocation);
        btnFilter = findViewById(R.id.searchbtn);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        db = FirebaseDatabase.getInstance();
        sqlDatabase = new DatabaseHelper(this);
        allHostsList = sqlDatabase.getAllHosts();
        originalHostsList = new ArrayList<>(allHostsList);
        UserHostAdapter userHostAdapter = new UserHostAdapter(allHostsList, allHosts.this, allHosts.this, true);
        recList.setAdapter(userHostAdapter);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_gradient));
            ab.setTitle(R.string.hostTitle);
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
        filterByLoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                filterLoc = selectedItem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filterLoc = "city";
            }
        });
        filterByGen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                filterGen = selectedItem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filterGen = "Gender";
            }
        });


        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Host> filteredList = new ArrayList<>();
                boolean shouldAdd = false;
                if (!filterLoc.equals("City") || !filterGen.equals("Gender")) {
                    for (Host host : originalHostsList) {
                         shouldAdd = false;

                        if (!filterLoc.equals("City")) {
                            if (filterByLoc.getSelectedItem().toString().equals(host.getHostAddress().split(",")[0])) {
                                shouldAdd = true;
                            }
                        }

                        if (!filterGen.equals("Gender")) {
                            if (sqlDatabase.getUserByEmail(host.getHostEmail()).getGen().equals(filterGen)) {
                                shouldAdd = true;
                            }
                        }

                        if (shouldAdd) {
                            filteredList.add(host);
                        }
                    }

                    allHostsList.clear();
                    allHostsList.addAll(filteredList);
                    userHostAdapter.notifyDataSetChanged();
                }
            }
        });
    }
        @Override
    public void onItemClick(int position) {
        Host newHost = allHostsList.get(position);
        AlertDialogFragmentViewHost frag = new AlertDialogFragmentViewHost();
        Bundle b = new Bundle();
        b.putSerializable("Host", newHost);
        frag.setArguments(b);
        frag.show(getFragmentManager(), "dialog");
    }

    @Override
    public void deleteItem(int position) {

    }

    @Override
    public void onDonationClick(int position,View v) {

    }

    @Override
    public void deleteDonation(int position) {

    }

    @Override
    public void AddComments(int position) {

    }

    @Override
    public void onDialogPositiveClick(AlertDialogFragmentViewHost dialog) {

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
                Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.addEvent:
                intent = new Intent(allHosts.this, Forms.class);
                intent.putExtra("Search",false);
                startActivity(intent);

                break;
            case R.id.bt_search:
                intent = new Intent(allHosts.this, Forms.class);
                intent.putExtra("Search",true);
                startActivity(intent);
                break;

            case R.id.bt_history:
                Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();

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
                } catch(Exception e) {
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
                        Intent intent1=new Intent(allHosts.this, LoginActivity.class);
                        startActivity(intent1);
                        finish();
                        Toast.makeText(allHosts.this, "Logout!", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();        }

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


}


