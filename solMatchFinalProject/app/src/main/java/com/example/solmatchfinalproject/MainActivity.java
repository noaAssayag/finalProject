package com.example.solmatchfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   Intent intent = new Intent(this, profileActivity.class);
       // startActivity(intent);
        setContentView(R.layout.user_profile);
    }


}