package com.example.solmatchfinalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class profileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        RecyclerView recyclerView=findViewById(R.id.recycleView);

        List<UserHosting> listhostingForUsers=new ArrayList<>();
        listhostingForUsers.add(new UserHosting("Noa Assayag",R.drawable.anonymousman,"24/08/2002","Migdal Haemek"));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new userHostAdapter(getApplicationContext(),listhostingForUsers));
    }
}