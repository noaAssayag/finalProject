package com.example.solmatchfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import Model.Host;

public class AddHost extends AppCompatActivity {
    TextView mDisplayDateTime;
    Calendar mDateAndTime = Calendar.getInstance();
    TextView address;
    Button sub;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference ref;
    String uid, email, userName,hostDate;

    private String hostAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_host);
        mDisplayDateTime = (TextView) findViewById(R.id.txtTitle);
        sub = (Button) findViewById(R.id.btnSubmit);
        address = (EditText) findViewById(R.id.hostAddress);
        updateDateAndTimeDisplay();
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (address == null || address.getText().toString().isEmpty()||mDisplayDateTime.getText().toString().isEmpty()) {
                    Toast.makeText(AddHost.this, "Please fill all the fileds", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    hostAddress=address.getText().toString();
                    hostDate=mDisplayDateTime.getText().toString();
                    auth = FirebaseAuth.getInstance();
                    uid = auth.getCurrentUser().getUid();
                    db = FirebaseDatabase.getInstance();
                    ref = db.getReference().child("Users");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            email = snapshot.child(uid).child("email").getValue().toString();
                            userName = snapshot.child(uid).child("userName").getValue().toString();
                            //extract the image of host
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    ref = db.getReference().child("Host").child(uid);
                    Host newHost=new Host(null,userName,email,hostAddress,hostDate);
                    ref.setValue(newHost);
                    Toast.makeText(AddHost.this, "The host Added Succefully!", Toast.LENGTH_SHORT).show();
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
                mDateAndTime.set(Calendar.YEAR, year);
                mDateAndTime.set(Calendar.MONTH, monthOfYear);
                mDateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateAndTimeDisplay();
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


}