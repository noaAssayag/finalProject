package com.example.solmatchfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class profileActivity extends AppCompatActivity {
    Intent intent;
    TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
       name = findViewById(R.id.textView14);
        setContentView(R.layout.activity_profile);
        String UID = intent.getStringExtra("UID");
        System.out.println("the UID is"+ UID);

    }
}