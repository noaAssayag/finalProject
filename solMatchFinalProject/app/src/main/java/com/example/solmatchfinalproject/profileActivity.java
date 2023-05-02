package com.example.solmatchfinalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.HashMap;

//import notification.notificationService;

public class profileActivity extends AppCompatActivity {
    private TextView name, descriptionText;
    private ImageView image;
    private Button btnImg;
    private BottomNavigationView bottomNavigationView;
    private static final String USERS = "Users";
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private String UID;
    private StorageReference mStorgaeRef;
    private DatabaseReference mDatabaseRef;
    private String imageURL;
    Uri uri = null;


    /**
     * OnCreate function initiates the app with all variables
     * after initialization, we load the toolbar menu
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
     /* bottomNavigationView = findViewById(R.id.bottom_menu);
        image = findViewById(R.id.profImage);
        btnImg = findViewById(R.id.btnChangeImage);
        name = findViewById(R.id.profName);
        descriptionText = findViewById(R.id.descriptionText);
        bottomNavigationView.setOnItemReselectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.myHome: {
                    startActivity(new Intent(getApplicationContext(), profileActivity.class));
                    overridePendingTransition(0, 0);
                    setContentView(R.layout.activity_profile);
                    break;
                }
                case R.id.calInvite:
//                    startActivity(new Intent(getApplicationContext(),profileActivity.class));
//                    overridePendingTransition(0,0);
//                    setContentView(R.layout.activity_profile);
                    break;
                case R.id.search:
//                    startActivity(new Intent(getApplicationContext(),profileActivity.class));
//                    overridePendingTransition(0,0);
//                    setContentView(R.layout.activity_profile);
                    break;
                case R.id.logOut:
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    overridePendingTransition(0, 0);
                    setContentView(R.layout.activity_login);
                    break;
            }
        });*/
        /**
         * getting a reference to the realTime database, and to the Storage functions provied to us by fireBase
         * After login we recive all User's details from the database including profile image.
         */
//        Intent intent = getIntent();
//        UID = intent.getStringExtra("UID");
//        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference userRef = rootRef.child(USERS);
//        Log.v("USERID", userRef.getKey());
//
//        //read from Database
//        userRef.addValueEventListener(new ValueEventListener() {
//            String userName, email, gen, birthday, type;
//
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                for (DataSnapshot keyID : snapshot.getChildren()) {
//                    if (keyID.child("uid").getValue().equals(UID)) {
//                        userName = keyID.child("userName").getValue(String.class);
//                        email = keyID.child("email").getValue(String.class);
//                        gen = keyID.child("gen").getValue(String.class);
//                        birthday = keyID.child("birthday").getValue(String.class);
//                        type = keyID.child("type").getValue(String.class);
//                        imageURL = keyID.child("imageUrl").getValue(String.class);
//                        notificationService not = new notificationService();
//                        Context con = getApplicationContext();
//                        not.sendNotification("test",intent,con);
//
//                        break;
//                    }
//                }
//                /**
//                 * after feching all the information we present them onto the layout elements
//                 */
//                name.setText(userName);
//                // Using Glide liberery to show image into the imageView using the URL
//                Glide.with(profileActivity.this)
//                        .load(imageURL)
//                        .into(image);
//                descriptionText.setText(userName + " , " + gen + " , " + type + " , " + birthday + " , " + email);
//                if (gen!=null) {
//                    if (gen.equals("Female")) {
//                        image.setImageResource(R.drawable.anonymouswoman);
//                    } else if (gen.equals("Male")) {
//                        image.setImageResource(R.drawable.anonymousman);
//                    }
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        mStorgaeRef= FirebaseStorage.getInstance().getReference();
//        mDatabaseRef=FirebaseDatabase.getInstance().getReference();
//
//        btnImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ImagePicker.with(profileActivity.this)
//                        .crop()	    			//Crop image(Optional), Check Customization for more option
//                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
//                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
//                        .start();
//            }
//
//        });
//    }
//
//    /**
//     * The function gets a ContentResolver object using the getContentResolver() method. The ContentResolver is used to access and manipulate the data in the underlying Android system.
//     * Then, it creates a MimeTypeMap object using the MimeTypeMap.getSingleton() method. This object is used to get the MIME type of a file based on its extension.
//     * Finally, it returns the extension of the file using the MimeTypeMap.getExtensionFromMimeType() method, which takes the MIME type of the file as an input parameter.
//     * @param uri
//     * @return
//     */
//    private String getFileExtension(Uri uri)
//    {
//        ContentResolver cR=getContentResolver();
//        MimeTypeMap mime=MimeTypeMap.getSingleton();
//        return mime.getExtensionFromMimeType(cR.getType(uri));
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        uri= data.getData();
//        image.setImageURI(uri);
//        uploadFile();
//    }
//
//
//    private void uploadFile()
//    {
//        if(uri!=null)
//        {
//
//            notification();
//            // creates a unique URl for the photo to then be added as the image reference in the realTime database.
//            StorageReference fileRef=mStorgaeRef.child(System.currentTimeMillis()+"."+getFileExtension(uri));
//            fileRef.putFile(uri)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            // Get the download URL of the uploaded file
//                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//                                    String imageUrl = uri.toString();
//                                    // Add the image URL to the Firebase Realtime Database
//                                    mDatabaseRef.child("Users").child(UID).child("imageUrl").setValue(imageUrl)
//                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void aVoid) {
//                                                    Toast.makeText(profileActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
//                                                }
//                                            })
//                                            .addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//                                                    Toast.makeText(profileActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                                                }
//                                            });
//                                }
//                            });
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(profileActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }
//    }
//
//    public void btnEditPersonal(View view) {
//        Intent intent = new Intent(this, EditPersonalDetailsActivity.class);
//        startActivity(intent);
//        setContentView(R.layout.activity_edit_personal_details);
//    }
//
//
//    /**
//     * writing the notification test code, after login we send a notification
//     */
//    private void notification()
//    {
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//        {
//            String channelId = getString(R.string.default_notification_channel_id);
//            String channelName = getString(R.string.default_notification_channel_name);
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(new NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_LOW));
//        }
//        // making a test on the image upload button, when user clicks on upload button we want to send notification
//        FirebaseMessaging.getInstance().subscribeToTopic("testChannel").addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                String msg = "Image Uploaded";
//                if(!task.isSuccessful())
//                {
//                    msg = "failed";
//                }
//            }
//        });
//    }
    }
}

