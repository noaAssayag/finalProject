package com.example.solmatchfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EditPersonalDetailsActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_details);
        bottomNavigationView.setOnItemReselectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.myHome: {
                    startActivity(new Intent(getApplicationContext(), profileActivity.class));
                    overridePendingTransition(0, 0);
                    setContentView(R.layout.activity_profile);
                    break;
                }
                case R.id.calInvite:
//                    startActivity(new Intent(getApplicationContext(),profileActivity.class));
//                    overridePendingTransition(0,0);
//                    setContentView(R.layout.activity_profile);
                    break;
                case R.id.search:
//                    startActivity(new Intent(getApplicationContext(),profileActivity.class));
//                    overridePendingTransition(0,0);
//                    setContentView(R.layout.activity_profile);
                    break;
                case R.id.logOut:
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    overridePendingTransition(0, 0);
                    setContentView(R.layout.activity_login);
                    break;
            }
        });
    }
}