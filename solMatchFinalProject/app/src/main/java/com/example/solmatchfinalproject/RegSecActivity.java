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

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_sec);

        intent = getIntent();
        setUserName(intent.getStringExtra("userName"));
        setEmail(intent.getStringExtra("email"));

         Log.d("TAG", "hey"+getEmail());
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

                    firebaseDatabase=FirebaseDatabase.getInstance();
                    reference=firebaseDatabase.getReference("Users");

                    if(gender!=null&&gender.getSelectedItem()!=null) {
                        if (type != null && type.getSelectedItem() != null) {
                            UserStorageData storageData = new UserStorageData(getUserName(), getEmail(), gen, getDate(), host);
                            reference.child(getEmail()).setValue(storageData);
                            Toast.makeText(getApplicationContext(),"Register Successfully",Toast.LENGTH_SHORT).show();
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
        password = password;
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
                        }
                        else {
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