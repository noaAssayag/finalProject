package com.example.solmatchfinalproject;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.solmatchfinalproject.ChatClasses.chatMenuActivity;
import com.example.solmatchfinalproject.Hosts.AddHost;
import com.example.solmatchfinalproject.profile.ProfileActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import Fragment.NotificationDialogFragment;
import Model.donations;
import dataBase.DatabaseHelper;

public class addDonationActivity extends AppCompatActivity {
    int PICK_IMAGE_REQUEST = 100;
    EditText itemName,ItemDescription,streets,apartNum,autoCompleteLocation;
    Spinner cities;
    private FusedLocationProviderClient fusedLocationClient;
    ImageView itemImage;
    ImageButton uploadImage;
    Button addItem;
    Spinner spinner;
    String selectedItem = null;
    String email;
    String userId;
    Uri imageURI;
    String URL;

    DatabaseHelper sqlDatabase;
    int i = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        long count = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_donation_page);
        Places.initialize(getApplicationContext(), "AIzaSyCS00xXyYjpPy7c51Yo9CFgr6Xia1ZMRF8");
        itemName = findViewById(R.id.inputItemName);
        ItemDescription = findViewById(R.id.inputItemDescription);
        cities =(Spinner) findViewById(R.id.hostAddress);
        streets=(EditText)findViewById(R.id.hostStreet);
        apartNum=(EditText)findViewById(R.id.hostApartmentNum);
        itemImage = findViewById(R.id.imageViewer);
        uploadImage = findViewById(R.id.uploadImageButt);
        addItem = findViewById(R.id.btnAddDonation);
        spinner = findViewById(R.id.catagorySpinner);
        autoCompleteLocation = findViewById(R.id.autoCompleteLocationDonation);
        autoCompleteLocation.setEnabled(false);
        sqlDatabase = new DatabaseHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        autoCompleteLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(autoCompleteLocation.getText().toString().isEmpty())
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


        String[] options = {"home cooking", "furniture", "food supplies","other"};
//        navigationHandler = new BottomNavigationHandler(this,getApplicationContext());
//        bottomNavigationView.setOnNavigationItemSelectedListener(navigationHandler);
// Create an ArrayAdapter to populate the Spinner with the options
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// Set the adapter for the Spinner
        spinner.setAdapter(adapter);

// Set a listener to handle selection events
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle the selected item
                selectedItem = options[position];
                // Do something with the selected item
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedItem = options[3];
            }
        });
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(autoCompleteLocation.getText().toString().isEmpty()) {
                    if (cities == null && cities.getSelectedItem() == null) {
                        Toast.makeText(addDonationActivity.this, "Please choose a city", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (streets == null || streets.getText().toString().isEmpty() || apartNum == null || apartNum.getText().toString().isEmpty()) {
                        Toast.makeText(addDonationActivity.this, "Please fill all the fileds and check they are valid", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                // Upload the image to Firebase Storage
                StorageReference storageRef = FirebaseStorage.getInstance().getReference(imageURI.toString());
                storageRef.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        boolean check = task.isSuccessful();
                        if (task.isSuccessful()) {
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    URL = uri.toString();
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db.collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            String address = "";
                                            email = documentSnapshot.getString("email");
                                            if(autoCompleteLocation.getText().toString().isEmpty()) {
                                                address = cities.getSelectedItem().toString() + ", " + streets.getText().toString() + ", " + apartNum.getText().toString();
                                            }
                                            else{
                                                address = autoCompleteLocation.getText().toString().split(",")[0];
                                            }
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            userId = user.getUid();
                                            donations formData = new donations(itemName.getText().toString(), address, selectedItem, ItemDescription.getText().toString(), URL, email);
                                            formData.setUid(userId);
                                            db.collection("Donations").document(userId).set(formData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    notifications noti = new notifications(userId,"you have created a donation post for the item: "+ itemName.getText().toString());
                                                    db.collection("Notifications").add(noti).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            sqlDatabase.insertNotificationData(noti);
                                                            Intent intent = new Intent(addDonationActivity.this, EditPersonalDetails.class);
                                                            startActivity(intent);

                                                        }
                                                    });
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
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageURI = data.getData();
            itemImage.setImageURI(imageURI);
        }
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
                                autoCompleteLocation.setText(fullAddress);
                                autoCompleteLocation.setEnabled(true);
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
        switch(item.getItemId()) {
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
                Intent iProfile= new Intent(this, EditPersonalDetails.class);
                startActivity(iProfile);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        DatabaseHelper helper = new DatabaseHelper(this);
        List<notifications> notifications = helper.getNotificationsByUserID(FirebaseAuth.getInstance().getUid());
        if(!notifications.isEmpty())
        {
            MenuItem item = menu.findItem(R.id.notificationIcon);
            item.setIcon(R.drawable.notification_icon_full);

        }
        return super.onPrepareOptionsMenu(menu);
    }

}