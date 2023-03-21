package com.example.solmatchfinalproject;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegSecActivity extends AppCompatActivity {
    Spinner type,gender;
    TextView dateCal;
    Button fin;
    String UID;
    Intent intent;

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    private String userName,Email,Date;

    public String getDate() {
        return Date;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return Email;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_sec);
        intent = getIntent();
        userName = intent.getStringExtra("userName");
        Email = intent.getStringExtra("Email");
        type = findViewById(R.id.type);
        gender = findViewById(R.id.gender);
        dateCal = findViewById(R.id.dateCal);
        fin = findViewById(R.id.btnRegister2);


        fin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(checkDate() == true) {

                    String gen = gender.getSelectedItem().toString();
                    String host = type.getSelectedItem().toString();
                    registerUser(Email, intent.getStringExtra("pass"));
                    if(UID != null) {
                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference parentRef = rootRef.child(UID);
                        Map<String, Object> attributes = new HashMap<>();
                        attributes.put("username", userName);
                        attributes.put("birthDate", Date);
                        attributes.put("gender", gen);
                        attributes.put("type", host);
                        parentRef.setValue(attributes)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "Data added successfully.");


                                        } else {
                                            Log.e(TAG, "Error adding data to database.", task.getException());
                                        }
                                    }
                                });
                        intent = new Intent(RegSecActivity.this,LoginActivity.class);
                        startActivity(intent);
                        setContentView(R.layout.activity_login);
                    }











                }
                }
        });

        setDate(dateCal);
        dateCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calnedar = Calendar.getInstance();
                int day = calnedar.get(Calendar.DAY_OF_MONTH);
                int month = calnedar.get(Calendar.MONTH);
                int year = calnedar.get(Calendar.YEAR);

                DatePickerDialog pickerDialog = new DatePickerDialog(RegSecActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        dateCal.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        Date = Integer.toString(dayOfMonth) + "/" + Integer.toString(monthOfYear) + "/" + Integer.toString(year);
                    }
                }, year, month, day);
                pickerDialog.show();


            }
        });
    }
    private String setDate(TextView dateCal) {
        Date hrini=Calendar.getInstance().getTime();
        SimpleDateFormat format=new SimpleDateFormat("d MMM yyyy");
        String date=format.format(hrini);
        dateCal.setText(date);
        return date;
    }
    private void registerUser(String email, String password) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Intent intent = getIntent();
                            intent.putExtra("UID",mAuth.getCurrentUser().getUid());



                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegSecActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                });
        UID = mAuth.getUid();
    }

    public boolean checkDate()
    {
        ProgressDialog mLoadingBar = new ProgressDialog(RegSecActivity.this);
        String[] splitDate = getDate().split("/");
        String year = splitDate[2];
        if(Integer.parseInt(year) > 2015)
        {
            mLoadingBar.setTitle("Register");
            mLoadingBar.setMessage("You are to young to register to the service");
            return false;
        }
        return true;
    }


}