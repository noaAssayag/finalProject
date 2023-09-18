package com.example.solmatchfinalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.solmatchfinalproject.Hosts.allHosts;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class searchNavigationMenue extends Activity {


    ImageButton hosts,donations,proffesionalButton;
    private BottomNavigationHandler navigationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_hostings_donations);
        hosts = findViewById(R.id.hostButton);
        donations = findViewById(R.id.donationsButton);
        BottomNavigationView bottomNavigationView = findViewById(R.id.menu);
        proffesionalButton = findViewById(R.id.proffesionalButton);
        navigationHandler = new BottomNavigationHandler(this,getApplicationContext());
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationHandler);


        hosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(searchNavigationMenue.this, allHosts.class);
                setContentView(R.layout.activity_all_hosts);
                startActivity(intent);
            }
        });


        donations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(searchNavigationMenue.this, All_donation_activity.class);
                setContentView(R.layout.add_donation_page);
                startActivity(intent);
            }
        });

        proffesionalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(searchNavigationMenue.this, AllProfessional.class);
                setContentView(R.layout.activity_all_professional);
                startActivity(intent);

            }
        });

    }


}
