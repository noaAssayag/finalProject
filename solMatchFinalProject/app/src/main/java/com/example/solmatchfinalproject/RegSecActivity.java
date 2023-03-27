package com.example.solmatchfinalproject;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
    Button finish;
    String UID;
    Intent intent;
    private String userName,email,date,password;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseAuth auth;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_sec);

        intent = getIntent();
        setUserName(intent.getStringExtra("userName"));
        setEmail(intent.getStringExtra("email"));
        setPassword(intent.getStringExtra("password"));

        type = (Spinner)findViewById(R.id.type);
        gender = (Spinner)findViewById(R.id.gender);
        dateCal =(TextView) findViewById(R.id.dateCal);
        finish = (Button) findViewById(R.id.btnSubmit);

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
                        date = Integer.toString(dayOfMonth) + "/" + Integer.toString(monthOfYear) + "/" + Integer.toString(year);
                    }
                }, year, month, day);
                pickerDialog.show();
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkDate() == true) {
                    String gen = gender.getSelectedItem().toString();
                    String host = type.getSelectedItem().toString();
                    // creating a user in the fireBase auth, and then getting that users
                    // unique id to save as key in database
                    auth = FirebaseAuth.getInstance();
                   // first we check inputs are correct
                    if(gender!=null&&gender.getSelectedItem()!=null) {
                        if (type != null && type.getSelectedItem() != null && checkDate()!= false) {
                            // @ Func createUserWithEmailAndPassword
                            // @ Parms email (users email) password (users password)
                            // function takes parms and adds then to the firebase authenticator
                            // after the user was added we take the UID that was created by the auth and
                            // adding that as the key of the user on the realtime Database.
                            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegSecActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        // after adding user to authenticator, we add his data to the realTime dataBase
                                        FirebaseUser user = auth.getCurrentUser();
                                        UID = user.getUid();
                                        firebaseDatabase=FirebaseDatabase.getInstance();
                                        reference=firebaseDatabase.getReference("Users");
                                        UserStorageData storageData = new UserStorageData(UID, getUserName(), getEmail(),gen,getDate(),getPassword(),host);
                                        reference.child(UID).setValue(storageData);
                                        Toast.makeText(getApplicationContext(),"Register Successfully",Toast.LENGTH_SHORT).show();
                                        Intent newIntent = new Intent(RegSecActivity.this,LoginActivity.class);
                                        startActivity(newIntent);
                                        setContentView(R.layout.activity_login);

                                    }
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Please choose a type of user",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please choose a gender",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String setDate(TextView dateCal) {
        Date hrini=Calendar.getInstance().getTime();
        SimpleDateFormat format=new SimpleDateFormat("d MMM yyyy");
        String date=format.format(hrini);
        dateCal.setText(date);
        return date;
    }

    public boolean checkDate()
    {
        ProgressDialog mLoadingBar = new ProgressDialog(RegSecActivity.this);
        String[] splitDate = getDate().split("/");
        String year = splitDate[2];
        int currentYear=Calendar.getInstance().get(Calendar.YEAR);
        if(currentYear-Integer.parseInt(year)<18)
        {
            mLoadingBar.setTitle("Register");
            mLoadingBar.setMessage("You are to young to register to the service");
            return false;
        }
        return true;
    }


}