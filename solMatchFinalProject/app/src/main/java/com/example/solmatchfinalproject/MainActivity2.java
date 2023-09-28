package com.example.solmatchfinalproject;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.example.solmatchfinalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Model.UserStorageData;

public class MainActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    UserStorageData user;
    /*todo change to sqllite*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        ImageView imgProf = headerView.findViewById(R.id.imgProfile);
        TextView userName = headerView.findViewById(R.id.fullName);
        TextView userEmail = headerView.findViewById(R.id.emailAddress);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        firestore.collection("Users").document(mAuth.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            user = documentSnapshot.toObject(UserStorageData.class);
                                if (userName != null) {
                                    userName.setText(user.getUserName());
                                }
                                if (userEmail != null) {
                                    userEmail.setText(user.getEmail());
                                }
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
                                        .into((ImageView) imgProf);
                            }
                        }
                    }
                });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.bt_home:
                Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.addEvent:
                 intent = new Intent(MainActivity2.this, Forms.class);
                intent.putExtra("Search",false);
                startActivity(intent);

                break;
            case R.id.bt_search:
                 intent = new Intent(MainActivity2.this, Forms.class);
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
                        mAuth.signOut();
                        Intent intent1=new Intent(MainActivity2.this,LoginActivity.class);
                        startActivity(intent1);
                        finish();
                        Toast.makeText(MainActivity2.this, "Logout!", Toast.LENGTH_SHORT).show();

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