package com.example.solmatchfinalproject;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import Model.UserStorageData;

public class registerAfterGoogFaceActivity extends AppCompatActivity {
        private EditText fullName;
        private Button btnSubmitGF;
        private TextView logo;
        private TextView genderTV;
        private TextView dateCalFG;
        private TextView textView7;
        private TextView typeTV;
        private TextView nameTV;
        private Spinner typeSpinner,genderSpinner;
        private String Date, name, gender, type,UID;




    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_reg_google_facebook);
            Intent intent = getIntent();
            UID = intent.getStringExtra("UID");
            btnSubmitGF = findViewById(R.id.btnSubmitGF);
            dateCalFG = findViewById(R.id.dateCalFG);
            typeSpinner = findViewById(R.id.typeSpinner);
            fullName = findViewById(R.id.FullName);
            genderSpinner = findViewById(R.id.genderSpinner);


        setDate(dateCalFG);
        dateCalFG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calnedar = Calendar.getInstance();
                int day = calnedar.get(Calendar.DAY_OF_MONTH);
                int month = calnedar.get(Calendar.MONTH);
                int year = calnedar.get(Calendar.YEAR);

                DatePickerDialog pickerDialog = new DatePickerDialog(registerAfterGoogFaceActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        dateCalFG.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        Date = Integer.toString(dayOfMonth) + "/" + Integer.toString(monthOfYear) + "/" + Integer.toString(year);
                    }
                }, year, month, day);
                pickerDialog.show();
            }
        });
            btnSubmitGF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference users = FirebaseDatabase.getInstance().getReference("Users");
                    UserStorageData storageData = new UserStorageData(UID, getName(), null,gender,getDate(),null,type,UID);
                    users.child(UID).setValue(storageData);
                    Toast.makeText(getApplicationContext(),"Register Successfully",Toast.LENGTH_SHORT).show();
                    Intent newIntent = new Intent(registerAfterGoogFaceActivity.this,EditPersonalDetails.class);
                    startActivity(newIntent);
                    setContentView(R.layout.activity_profile);
                }
            });
        }


    public boolean checkCredentials()
    {
        if(fullName.getText().toString().isEmpty() || !fullName.getText().toString().contains(" ") || fullName.getText().toString().length() < 5)
        {
            Toast.makeText(registerAfterGoogFaceActivity.this,"full name not valid", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(genderSpinner==null || genderSpinner.getSelectedItem()==null)
        {
            Toast.makeText(registerAfterGoogFaceActivity.this,"problem with gender selection", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(typeSpinner == null || typeSpinner.getSelectedItem() == null)
        {
            Toast.makeText(registerAfterGoogFaceActivity.this,"problem with type selection", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean checkDate()
    {
        ProgressDialog mLoadingBar = new ProgressDialog(registerAfterGoogFaceActivity.this);
        String[] splitDate = getDate().split("/");
        String year = splitDate[2];
        int currentYear= Calendar.getInstance().get(Calendar.YEAR);
        if(currentYear-Integer.parseInt(year)<18)
        {
            mLoadingBar.setTitle("Register");
            mLoadingBar.setMessage("You are to young to register to the service");
            return false;
        }
        return true;
    }



    private String setDate(TextView dateCal) {
        java.util.Date hrini=Calendar.getInstance().getTime();
        SimpleDateFormat format=new SimpleDateFormat("d MMM yyyy");
        String date=format.format(hrini);
        dateCal.setText(date);
        return date;
    }
    private static void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    }


