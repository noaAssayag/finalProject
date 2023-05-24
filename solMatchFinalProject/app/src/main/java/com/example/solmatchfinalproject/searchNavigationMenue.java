package com.example.solmatchfinalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import donations.donationActivity;

public class searchNavigationMenue extends Activity {


    Button hosts,donations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_hostings_donations);
        hosts = findViewById(R.id.hostButton);
        donations = findViewById(R.id.donationsButton);


        hosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // need to make hosting page
            }
        });


        donations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(searchNavigationMenue.this, donationActivity.class);
                setContentView(R.layout.donations_layout);
                startActivity(intent);
            }
        });

    }


}
