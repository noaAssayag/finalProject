package com.example.solmatchfinalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import com.example.solmatchfinalproject.Hosts.RecycleViewInterface;
import com.example.solmatchfinalproject.Hosts.UserHostAdapter;
import com.example.solmatchfinalproject.Hosts.allHosts;
import com.example.solmatchfinalproject.profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Fragment.AlertDialogFragmentViewDonations;
import Fragment.AlertDialogFragmentViewHost;
import Fragment.AlertListenerProf;
import Fragment.MyAlertDialogFragmentListenerView;
import Fragment.NotificationDialogFragment;
import Model.Host;
import Model.Professional;
import Model.Review;
import Model.UserStorageData;
import Model.donations;
import dataBase.DatabaseHelper;

public class AllProfessional extends AppCompatActivity implements RecycleViewInterface, AlertListenerProf,NavigationView.OnNavigationItemSelectedListener {
    FirebaseDatabase db;
    private DrawerLayout drawerLayout;
    Spinner filterByCategory;
    Spinner filterByLoc;
    Button btnFilter;
    RecyclerView recList;
    private List<Review> reviews;
    List<Professional> allProfessionalList = new ArrayList<>();

    List<Professional> allProfWithReviews = new ArrayList<>();

    DatabaseHelper sqlDatabase;
    BottomNavigationView menu;
    private List<Professional> originalProfessionalList;
    ProfessionalAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_professional);
        recList = findViewById(R.id.cardList);
        filterByCategory = (Spinner) findViewById(R.id.spinnerFilterByCategory);
        filterByLoc = (Spinner) findViewById(R.id.spinnerFilterByLocation);
        btnFilter = (Button) findViewById(R.id.searchbtn);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        db = FirebaseDatabase.getInstance();






        sqlDatabase = new DatabaseHelper(this);
        allProfessionalList = sqlDatabase.getAllProfessionals();
        originalProfessionalList = new ArrayList<>(allProfessionalList);

         reviews = new ArrayList<>();
        for(Professional professional: allProfessionalList)
        {

           reviews = sqlDatabase.getProfessionalReviewsByUserID(professional.getUID());
           if(reviews == null)
           {
               professional.setReviews(new ArrayList<>());
           }
           else {
               professional.setReviews(reviews);
           }
           allProfWithReviews.add(professional);

        }

        adapter = new ProfessionalAdapter(allProfWithReviews, AllProfessional.this, AllProfessional.this);
        recList.setAdapter(adapter);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_gradient));
            ab.setTitle(R.string.profTitle);
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

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Professional> filteredList = new ArrayList<>();
                boolean useCategory;
                boolean useCity;
                String category=  filterByCategory.getSelectedItem().toString();
                String city = filterByLoc.getSelectedItem().toString();
                List<donations> filterDonationList = new ArrayList<>();
                if (filterByCategory == null || filterByCategory.getSelectedItem().toString().equals("Category")) {
                    useCategory = false;
                } else {
                    useCategory = true;
                }
                if (filterByLoc.getSelectedItem().toString().equals("City") || filterByLoc == null) {
                    useCity = false;
                }
                else {
                    useCity = true;
                }
                if (!useCity && !useCategory) {
                    filteredList.addAll(allProfessionalList);
                } else {
                    if (useCategory) {
                        for (Professional professional : allProfessionalList) {
                            if (professional.getCategory().equals(filterByCategory)) {
                                filteredList.add(professional);
                            }
                        }
                    }
                    if (useCity) {
                        for (Professional professional : allProfessionalList) {
                            if (professional.getAddress().equals(filterByLoc.getSelectedItem().toString())) {
                                filteredList.add(professional);
                            }
                        }
                    }
                }
                adapter = new ProfessionalAdapter(filteredList, AllProfessional.this, AllProfessional.this);
                recList.setAdapter(adapter);
                return;
            }
    });
    }

    @Override
    public void onDialogPositiveClick(AlertDialogFragmentViewProf dialog) {
        Toast.makeText(this, " ", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onItemClick(int position) {
        Professional professional = allProfessionalList.get(position);
        AlertDialogFragmentViewProf frag = new AlertDialogFragmentViewProf();
        Bundle b = new Bundle();
        b.putString("Description", professional.getDescription());
        frag.setArguments(b);
        frag.show(getFragmentManager(), "dialog");
    }

    @Override
    public void deleteItem(int position) {

    }

    @Override
    public void onDonationClick(int position, View v) {

    }

    @Override
    public void deleteDonation(int position) {


    }

    @Override
    public void AddComments(int position) {
        Professional professional = allProfessionalList.get(position);
        Intent i = new Intent(this, ReviewProffessionalActivity.class);
        i.putExtra("professional",professional);
        startActivity(i);

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
                intent = new Intent(AllProfessional.this, MainActivity2.class);
                startActivity(intent);
                break;

            case R.id.addEvent:
                intent = new Intent(AllProfessional.this, AddEvent.class);
                startActivity(intent);

                break;
            case R.id.bt_search:
                intent = new Intent(AllProfessional.this, Forms.class);
                startActivity(intent);
                break;

            case R.id.bt_Profile:
                intent = new Intent(AllProfessional.this, EditPersonalDetails.class);
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
                        Intent intent1 = new Intent(AllProfessional.this, LoginActivity.class);
                        startActivity(intent1);
                        finish();
                        Toast.makeText(AllProfessional.this, "Logout!", Toast.LENGTH_SHORT).show();

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


}