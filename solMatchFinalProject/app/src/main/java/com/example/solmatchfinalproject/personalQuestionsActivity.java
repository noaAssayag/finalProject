package com.example.solmatchfinalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Model.userPersonalInfo;
import donations.donationActivity;
import donations.donationAdapter;

public class personalQuestionsActivity extends Activity {

    RecyclerView hobbitsItems;
    List<String> hobbiesList;
    List<String> selectedItemsList;

    Button next;

    EditText personalInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_questions_layout);
        next = findViewById(R.id.nextPersonal);
        personalInfo = findViewById(R.id.inputPersonalInfo);
        hobbitsItems = findViewById(R.id.hobbiesList);
        hobbiesList = new ArrayList<String>();
        selectedItemsList = new ArrayList<String>();
        Collections.addAll(hobbiesList,"soccer","football","reading","hiking","video games", "writing", "music", "resterants");
        int itemsPerRow = 3; // Number of items per row
        GridLayoutManager llm = new GridLayoutManager(personalQuestionsActivity.this, 3);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        hobbitsItems.setLayoutManager(llm);
        personalQuestionsAdapter adapter = new personalQuestionsAdapter(hobbiesList,getApplicationContext());
        hobbitsItems.setAdapter(adapter);




        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve selected items from the adapter
                selectedItemsList = adapter.getSelectedItems();
                String description = personalInfo.getText().toString();
                for (String selectedItem : selectedItemsList) {
                    Log.i("Selected Item", selectedItem);

                }
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                userPersonalInfo info = new userPersonalInfo((ArrayList<String>)selectedItemsList,description);
                reference.child("userInfo").setValue(info);
                Intent intent = new Intent(personalQuestionsActivity.this,profileActivity.class);
                setContentView(R.layout.activity_profile);
                startActivity(intent);

            }
        });
    }
}
