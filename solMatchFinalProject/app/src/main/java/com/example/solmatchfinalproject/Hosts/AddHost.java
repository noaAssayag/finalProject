package com.example.solmatchfinalproject.Hosts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.solmatchfinalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class AddHost extends AppCompatActivity {
    int PICK_IMAGE_REQUEST = 100;
    TextView mDisplayDateTime;
    Calendar mDateAndTime = Calendar.getInstance();
    EditText address;
    ImageView locationImg;
    ImageButton btnAddImg;
    Button sub;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference ref;
    String uid, email, userName, hostDate, hostAddress;
    Uri imageURILoc;
    String URL;
    String imageURLHost;
    private  Host newHost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_host);
        mDisplayDateTime = (TextView) findViewById(R.id.txtTitle);
        sub = (Button) findViewById(R.id.btnSubmit);
        address = (EditText) findViewById(R.id.hostAddress);
        locationImg = (ImageView) findViewById(R.id.imageOfLocation);
        btnAddImg = (ImageButton) findViewById(R.id.uploadImage);
        updateDateAndTimeDisplay();

        btnAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        hostAddress = address.getText().toString();
        hostDate = mDisplayDateTime.getText().toString();
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (address == null || hostAddress.isEmpty() || hostDate.isEmpty()) {
                    Toast.makeText(AddHost.this, "Please fill all the fileds", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (locationImg == null) {
                    Toast.makeText(AddHost.this, "You must upload an image!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    auth = FirebaseAuth.getInstance();
                    uid = auth.getCurrentUser().getUid();
                    db = FirebaseDatabase.getInstance();
                    ref = db.getReference().child("Users").child(uid);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            email = snapshot.child("email").getValue().toString();
                            userName = snapshot.child("userName").getValue().toString();
                            imageURLHost = snapshot.child("imageURL").getValue().toString();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
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
                                    }
                                });
                            }
                            ref = db.getReference().child("Host").child(uid);
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Host newHost = new Host(imageURLHost,userName, email, hostAddress, hostDate,URL);
                                    ref.setValue(newHost);
                                    Toast.makeText(AddHost.this, "The host Added Succefully!", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    });
                }
            }
        });
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
                    Toast.makeText(getApplicationContext(), "INVALID date", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    updateDateAndTimeDisplay();
                }
            }
        };

        new DatePickerDialog(this, mDateListener,
                mDateAndTime.get(Calendar.YEAR),
                mDateAndTime.get(Calendar.MONTH),
                mDateAndTime.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void updateDateAndTimeDisplay() {
        mDisplayDateTime.setText(DateUtils.formatDateTime(this,
                mDateAndTime.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_SHOW_TIME));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageURILoc = data.getData();
            locationImg.setImageURI(imageURILoc);
        }
    }


}