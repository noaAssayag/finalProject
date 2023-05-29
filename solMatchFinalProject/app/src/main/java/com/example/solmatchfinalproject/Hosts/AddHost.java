package com.example.solmatchfinalproject.Hosts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.solmatchfinalproject.R;
import com.example.solmatchfinalproject.profileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

import Model.Host;

public class AddHost extends AppCompatActivity {
    int PICK_IMAGE_REQUEST = 100;
    TextView mDisplayDateTime;
    Calendar mDateAndTime = Calendar.getInstance();
    EditText address;
    EditText description;
    ImageView locationImg;
    ImageButton btnAddImg;
    Switch accommodationSwitch;
    Switch petsSwitch;
    Switch privateRoomSwitch;
    Switch secureEnvSwitch;

    Button sub;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference ref;
    String uid, email, userName, hostDate, hostAddress, hostDescription;
    boolean accommodation = false;
    boolean pets = false;
    boolean privateRoom = false;
    boolean secureEnv = false;
    Uri imageURILoc;
    String URL;
    String imageURLHost;
    private Host newHost;
    private boolean validDate = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_host);
        mDisplayDateTime = (TextView) findViewById(R.id.txtPresDateAndTime);
        sub = (Button) findViewById(R.id.btnSubmit);
        address = (EditText) findViewById(R.id.hostAddress);
        description=(EditText)findViewById(R.id.hostDescription);
        locationImg = (ImageView) findViewById(R.id.imageOfLocation);
        btnAddImg = (ImageButton) findViewById(R.id.uploadImage);
        accommodationSwitch=(Switch)findViewById(R.id.accommodationSwitch);
        petsSwitch=(Switch)findViewById(R.id.petsSwitch);
        privateRoomSwitch=(Switch)findViewById(R.id.privateRoomSwitch);
        secureEnvSwitch=(Switch)findViewById(R.id.secureEnvSwitch);
        updateDateAndTimeDisplay();

        btnAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hostDate = mDisplayDateTime.getText().toString();
                hostAddress = address.getText().toString();
                if (hostAddress.isEmpty() || hostDate.isEmpty() || validDate == false) {
                    Toast.makeText(AddHost.this, "Please fill all the fileds", Toast.LENGTH_SHORT).show();
                    return;
                } else if (locationImg.getDrawable() == null) {
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
                            if (snapshot.child("image").getValue() != null) {
                                imageURLHost = snapshot.child("image").getValue().toString();
                            }
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
                                        ref = db.getReference().child("Host").child(uid);
                                        ref.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(!description.getText().toString().isEmpty())
                                                {
                                                    hostDescription=description.getText().toString();
                                                }
                                                else
                                                {
                                                    hostDescription="";
                                                }
                                                Host newHost = new Host(imageURLHost, userName, email, hostAddress, hostDate, URL,hostDescription,accommodation,pets,privateRoom,secureEnv);
                                                ref.setValue(newHost);
                                                Toast.makeText(AddHost.this, "The host Added Succefully!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(AddHost.this, profileActivity.class);
                                                startActivity(intent);
                                                setContentView(R.layout.activity_profile);                                            }

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
            }
        });
    }
    public void onAccommodationSwitch(View v)
    {
        if(accommodationSwitch.isChecked())
        {
            accommodation=true;
        }

    }
    public void onPetsSwitch(View v)
    {
        if(petsSwitch.isChecked())
        {
            pets=true;
        }

    }
    public void onPrivateRoomSwitch(View v)
    {
        if(privateRoomSwitch.isChecked())
        {
            privateRoom=true;
        }

    }
    public void onSecureEnvSwitch(View v)
    {
        if(secureEnvSwitch.isChecked())
        {
            secureEnv=true;
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
        mDisplayDateTime.setText(DateUtils.formatDateTime(this,
                mDateAndTime.getTimeInMillis(), DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageURILoc = data.getData();
            locationImg.setImageURI(imageURILoc);
        }
    }


}