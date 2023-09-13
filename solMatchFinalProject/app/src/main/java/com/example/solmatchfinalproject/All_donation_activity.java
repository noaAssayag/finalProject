package com.example.solmatchfinalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class All_donation_activity extends AppCompatActivity {
    private ImageView backArrow;
    private EditText nameDon;
    private Button searchBtn;
    private RecyclerView recyclerViewDon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_donation);
        nameDon=findViewById(R.id.name_donation);
        searchBtn=findViewById(R.id.searchbtn);
        recyclerViewDon=findViewById(R.id.recyclerViewDon);


    }
}