package com.example.solmatchfinalproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

import com.example.solmatchfinalproject.profile.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import Model.donations;

public class addDonationActivity extends Activity {
    int PICK_IMAGE_REQUEST = 100;
    EditText itemName,ItemDescription,adress;
    ImageView itemImage;
    ImageButton uploadImage;
    Button addItem;
    Spinner spinner;
    String selectedItem = null;
    String email;
    String userId;
    Uri imageURI;
    String URL;
    int i = 0;

    private BottomNavigationHandler navigationHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        long count = 0;
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_donation_page);
        itemName = findViewById(R.id.inputItemName);
        ItemDescription = findViewById(R.id.inputItemDescription);
        adress = findViewById(R.id.inputLocation);
        itemImage = findViewById(R.id.imageViewer);
        uploadImage = findViewById(R.id.uploadImageButt);
        addItem = findViewById(R.id.btnAddDonation);
        spinner = findViewById(R.id.catagorySpinner);
        String[] options = {"home cooking", "furniture", "food supplies","other"};
        BottomNavigationView bottomNavigationView = findViewById(R.id.menu);
        navigationHandler = new BottomNavigationHandler(this,getApplicationContext());
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationHandler);
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
                int id = R.drawable.anonymousman;
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
                                            email = documentSnapshot.getString("email");
                                            donations formData = new donations(itemName.getText().toString(), adress.getText().toString(), selectedItem, ItemDescription.getText().toString(), URL, email);

                                            db.collection("donations").add(formData)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            Intent intent = new Intent(addDonationActivity.this, ProfileActivity.class);
                                                            startActivity(intent);
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(addDonationActivity.this, "Failed to add donation data", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    });

                                }
                            });
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            userId = user.getUid();
                        }
                    }
                });
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = R.drawable.anonymousman;
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
                                    FirebaseFirestore addDonationToFireStore = FirebaseFirestore.getInstance();
                                    email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                                    donations formData = new donations(itemName.getText().toString(), adress.getText().toString(), selectedItem, ItemDescription.getText().toString(), URL, email);
                                    addDonationToFireStore.collection("Donations").add(formData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getApplicationContext(),"added donation succefully",Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(),profileActivity.class);
                                            startActivity(intent);
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


}