package com.example.solmatchfinalproject.Hosts;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.solmatchfinalproject.AddEvent;
import com.example.solmatchfinalproject.ChatClasses.chatMenuActivity;
import com.example.solmatchfinalproject.EditPersonalDetails;
import com.example.solmatchfinalproject.Forms;
import com.example.solmatchfinalproject.LoginActivity;
import com.example.solmatchfinalproject.MainActivity2;
import com.example.solmatchfinalproject.notifications;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.example.solmatchfinalproject.R;
import com.example.solmatchfinalproject.profile.ProfileActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import Fragment.NotificationDialogFragment;
import Model.Host;
import Model.UserStorageData;
import dataBase.DatabaseHelper;

public class AddHost extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    int PICK_IMAGE_REQUEST = 100;
    TextView mDisplayDateTime, preTime;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;

    private FusedLocationProviderClient fusedLocationClient;

    Calendar mDateAndTime = Calendar.getInstance();

    private DrawerLayout drawerLayout;
    Spinner cities;
    EditText streets,autoCompleteLocationHost;
    EditText apartNum;
    EditText description;
    ImageView locationImg;
    ImageButton btnAddImg;
    Switch accommodationSwitch;
    Switch petsSwitch;
    Switch privateRoomSwitch;
    Switch secureEnvSwitch;
    Button sub ;
    ImageView back;
    FirebaseAuth auth;
    String uid, email, userName, hostDate, hostAddress, hostDescription;
    String accommodation = "false";
    String pets = "false";
    String privateRoom = "false";
    String secureEnv = "false";
    Uri imageURILoc;
    ImageView imgLocation;
    String URL;
    String imageURLHost;
    private boolean validDate = true;

    private DatabaseHelper sqlDataBase;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Places.initialize(getApplicationContext(), "AIzaSyCS00xXyYjpPy7c51Yo9CFgr6Xia1ZMRF8");
        setContentView(R.layout.activity_add_host);
        sub = (Button) findViewById(R.id.btnSubmit);
        cities =(Spinner) findViewById(R.id.hostAddress);
        streets=(EditText)findViewById(R.id.hostStreet);
        apartNum=(EditText)findViewById(R.id.hostApartmentNum);
        description=(EditText)findViewById(R.id.hostDescription);
        locationImg = (ImageView) findViewById(R.id.imageOfLocation);
        btnAddImg = (ImageButton) findViewById(R.id.uploadImage);
        accommodationSwitch=(Switch)findViewById(R.id.accommodationSwitch);
        petsSwitch=(Switch)findViewById(R.id.petsSwitch);
        privateRoomSwitch=(Switch)findViewById(R.id.privateRoomSwitch);
        secureEnvSwitch=(Switch)findViewById(R.id.secureEnvSwitch);
        mDisplayDateTime=findViewById(R.id.valueDate);
        preTime=findViewById(R.id.valueTime);
        updateDateAndTimeDisplay();
        imgLocation = findViewById(R.id.imgLocation);
        sqlDataBase = new DatabaseHelper(this);
        autoCompleteLocationHost = findViewById(R.id.autoCompleteLocationHost);
        autoCompleteLocationHost.setEnabled(false);
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
        autoCompleteLocationHost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(autoCompleteLocationHost.getText().toString().isEmpty())
                {
                    cities.setEnabled(true);
                    streets.setEnabled(true);
                    apartNum.setEnabled(true);
                }
                else{
                    cities.setEnabled(false);
                    streets.setEnabled(false);
                    apartNum.setEnabled(false);
                }

            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        btnAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        imgLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlacePicker();
            }
        });
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hostDate = mDisplayDateTime.getText().toString();
                if(autoCompleteLocationHost.getText().toString().isEmpty()) {
                    if (cities == null && cities.getSelectedItem() == null) {
                        Toast.makeText(AddHost.this, "Please choose a city", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (streets == null || streets.getText().toString().isEmpty() || apartNum == null || apartNum.getText().toString().isEmpty() || hostDate.isEmpty() || validDate == false) {
                        Toast.makeText(AddHost.this, "Please fill all the fileds and check they are valid", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (locationImg.getDrawable() == null) {
                    Toast.makeText(AddHost.this, "You must upload an image!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    auth = FirebaseAuth.getInstance();
                    uid = auth.getCurrentUser().getUid();
                    FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
                    dbFirestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for(QueryDocumentSnapshot doc: task.getResult())
                            {
                                if(doc.getId().equals(uid))
                                {
                                    email = doc.get("email").toString();
                                    userName = doc.get("userName").toString();
                                    if(doc.get("image") != null) {
                                        if (!doc.get("image").toString().isEmpty()) {
                                            imageURLHost = doc.get("image").toString();
                                        }
                                    }
                                }

                            }
                        }
                    });
                    // Upload the image to Firebase Storage
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference(imageURILoc.toString());
                    storageRef.putFile(imageURILoc).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            boolean check = task.isSuccessful();
                            if (task.isSuccessful()) {
                                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        URL = uri.toString();

                                        if(autoCompleteLocationHost.getText().toString().isEmpty()) {
                                            hostAddress = cities.getSelectedItem().toString() + ", " + streets.getText().toString() + ", " + apartNum.getText().toString();
                                        }
                                        else {
                                            hostAddress = autoCompleteLocationHost.getText().toString();
                                        }
                                        if(!description.getText().toString().isEmpty())
                                        {
                                            hostDescription=description.getText().toString();
                                        }
                                        else
                                        {
                                            hostDescription="";
                                        }
                                        Host newHost = new Host(imageURLHost, userName, email, hostAddress, hostDate, URL,hostDescription,accommodation,pets,privateRoom,secureEnv);
                                        dbFirestore.collection("Host").document(uid).set(newHost).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                sqlDataBase.insertHostData(newHost);
                                                Toast.makeText(getApplicationContext(),"added host",Toast.LENGTH_LONG).show();
                                                notifications noti = new notifications(uid,"you have created a hosting event for the date:" + hostDate);
                                                dbFirestore.collection("Notifications").add(noti).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        sqlDataBase.insertNotificationData(noti);
                                                        Intent intent = new Intent(AddHost.this, EditPersonalDetails.class);
                                                        startActivity(intent);

                                                    }
                                                });

                                            }
                                        });

                                    }
                                });
                            }


                        }
                    });
                }
            }
        });
    }
    public void onAccommodationSwitch(View v)
    {
        if(accommodationSwitch.isChecked())
        {
            accommodation="true";
        }

    }
    public void onPetsSwitch(View v)
    {
        if(petsSwitch.isChecked())
        {
            pets="true";
        }

    }
    public void onPrivateRoomSwitch(View v)
    {
        if(privateRoomSwitch.isChecked())
        {
            privateRoom="true";
        }

    }
    public void onSecureEnvSwitch(View v)
    {
        if(secureEnvSwitch.isChecked())
        {
            secureEnv="true";
        }

    }

    public void onTimeClicked(View v) {

        TimePickerDialog.OnTimeSetListener mTimeListener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mDateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mDateAndTime.set(Calendar.MINUTE, minute);
                updateDateAndTimeDisplay();
            }
        };

        new TimePickerDialog(this, mTimeListener,
                mDateAndTime.get(Calendar.HOUR_OF_DAY),
                mDateAndTime.get(Calendar.MINUTE), true).show();
    }

    public void onDateClicked(View v) {

        DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                Calendar currentDate = Calendar.getInstance();
                mDateAndTime.set(Calendar.YEAR, year);
                mDateAndTime.set(Calendar.MONTH, monthOfYear);
                mDateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if (mDateAndTime.after(currentDate)) {
                    validDate = true;
                    updateDateAndTimeDisplay();
                } else {
                    Toast.makeText(getApplicationContext(), "INVALID date", Toast.LENGTH_SHORT).show();
                    validDate = false;
                }
            }
        };

        new DatePickerDialog(this, mDateListener,
                mDateAndTime.get(Calendar.YEAR),
                mDateAndTime.get(Calendar.MONTH),
                mDateAndTime.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void updateDateAndTimeDisplay() {
        // Format and display date
        String date = DateUtils.formatDateTime(this,
                mDateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE);
        mDisplayDateTime.setText(date);

        // Format and display time
        String time = DateUtils.formatDateTime(this,
                mDateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_TIME);
        preTime.setText(time);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageURILoc = data.getData();
            locationImg.setImageURI(imageURILoc);
        }

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " + place.getLatLng());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void openPlacePicker() {
        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }
    private void getLastLocation(){
        // Check permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions if needed
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        // Get last known location
        fusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Location lastKnownLocation = task.getResult();

                            // Use the location object to get latitude and longitude
                            double latitude = lastKnownLocation.getLatitude();
                            double longitude = lastKnownLocation.getLongitude();
                            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                            List<Address> addresses = null;

                            try {
                                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (addresses != null && !addresses.isEmpty()) {
                                Address address = addresses.get(0);
                                // Here are some examples of what you can get from the Address object:
                                String addressLine = address.getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                String city = address.getLocality();
                                String state = address.getAdminArea();
                                String country = address.getCountryName();
                                String postalCode = address.getPostalCode();
                                String knownName = address.getFeatureName(); // Only if available else return NULL

                                String fullAddress = addressLine + ", " + city + ", " + state + ", " + country + ", " + postalCode;
                                autoCompleteLocationHost.setText(fullAddress);
                                autoCompleteLocationHost.setEnabled(true);
                            }
                        }
                    }
                });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                // Permission denied
            }
        }
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
                intent = new Intent(AddHost.this, MainActivity2.class);
                intent.putExtra("Search", false);
                break;

            case R.id.addEvent:
                intent = new Intent(AddHost.this, AddEvent.class);
                startActivity(intent);

                break;
            case R.id.bt_search:
                intent = new Intent(AddHost.this, Forms.class);
                startActivity(intent);
                break;

            case R.id.bt_Profile:
                intent = new Intent(AddHost.this, EditPersonalDetails.class);
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
                        Intent intent1 = new Intent(AddHost.this, LoginActivity.class);
                        startActivity(intent1);
                        finish();
                        Toast.makeText(AddHost.this, "Logout!", Toast.LENGTH_SHORT).show();

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