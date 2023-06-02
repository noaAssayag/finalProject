package com.example.solmatchfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.solmatchfinalproject.Hosts.allHosts;
import com.example.solmatchfinalproject.profile.ProfileActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        setContentView(R.layout.activity_login);
    }


}