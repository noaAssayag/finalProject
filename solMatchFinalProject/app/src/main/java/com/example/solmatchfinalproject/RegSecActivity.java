package com.example.solmatchfinalproject;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

import Model.UserStorageData;
import dataBase.MyInfoDataBase;
import dataBase.MyInfoManager;

public class RegSecActivity extends Activity {
    Spinner sType, sGender;
    TextView dateCal;
    Button finish;
    Intent intent;
    private String userName;
    private String email;
    private String date;
    private String password;
    private String gen;
    private String type;
    private Bitmap image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_sec);
        intent = getIntent();
        setUserName(intent.getStringExtra("userName"));
        setEmail(intent.getStringExtra("email"));
        setPassword(intent.getStringExtra("password"));
        sType = (Spinner) findViewById(R.id.spinnerType);
        sGender = (Spinner) findViewById(R.id.spinnerGender);
        dateCal = (TextView) findViewById(R.id.dateCal);
        finish = (Button) findViewById(R.id.btnSubmit);
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

        setDate(dateCal);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getDate() !=null&&(!(getDate().isEmpty()))) {
                    if (sGender != null && sGender.getSelectedItem() != null) {
                        if (sType != null && sType.getSelectedItem() != null) {
                            {
                                setGen(sGender.getSelectedItem().toString());
                                setType(sType.getSelectedItem().toString());
                                if(checkDate()==true)
                                {
                                    if(gen.equals("Female"))
                                    {
                                        image = BitmapFactory.decodeResource(getResources(), R.drawable.anonymouswoman);
                                    }
                                    else{
                                        image = BitmapFactory.decodeResource(getResources(), R.drawable.anonymousman);
                                    }
                                    setImage(image);
                                    UserStorageData user=new UserStorageData(getUserName(),getEmail(),getGen(),getDate(),getPassword(),getImage(),getType());
                                    MyInfoManager.getInstance().openDataBase(RegSecActivity.this);
                                    MyInfoManager.getInstance().createUser(user);
                                    Intent newIntent = new Intent(RegSecActivity.this, LoginActivity.class);
                                    startActivity(newIntent);
                                    setContentView(R.layout.activity_login);
                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please choose a type of user", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please choose a gender", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(), "Please pick your birthday", Toast.LENGTH_SHORT).show();
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
    public String getGen() {
        return gen;
    }

    public void setGen(String gen) {
        this.gen = gen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
    private String setDate(TextView dateCal) {
        Date hrini = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("d MMM yyyy");
        String date = format.format(hrini);
        dateCal.setText(date);
        return date;
    }

    public boolean checkDate() {
        String[] splitDate = getDate().split("/");
        String year = splitDate[2];
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (currentYear - Integer.parseInt(year) < 18&&currentYear - Integer.parseInt(year)>120) {
            return false;
        }
        return true;
    }


}