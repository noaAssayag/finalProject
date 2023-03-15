package com.example.solmatchfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegSecActivity extends AppCompatActivity {
    Spinner type,gender;
    TextView dateCal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_sec);
        type = findViewById(R.id.type);
        gender = findViewById(R.id.gender);
        dateCal = findViewById(R.id.dateCal);

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
                    }
                }, year, month, day);
                pickerDialog.show();
            }
        });
    }
    private void setDate(TextView dateCal) {
        Date hrini=Calendar.getInstance().getTime();
        SimpleDateFormat format=new SimpleDateFormat("d MMM yyyy");
        String date=format.format(hrini);
        dateCal.setText(date);
    }
}