package com.example.solmatchfinalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solmatchfinalproject.profile.ProfileActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Model.UserStorageData;
import Model.userPersonalInfo;

public class personalQuestionsActivity extends Activity {

    RecyclerView hobbitsItems;
    List<String> hobbiesList;
    List<String> selectedItemsList;
    Button next,skip;
    EditText personalInfo;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    UserStorageData user;

    String description = "No description";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_questions_layout);
        String UID = mAuth.getUid();
        next = findViewById(R.id.nextPersonal);
        personalInfo = findViewById(R.id.inputPersonalInfo);
        hobbitsItems = findViewById(R.id.hobbiesList);
        hobbiesList = new ArrayList<String>();
        selectedItemsList = new ArrayList<String>();
        skip = findViewById(R.id.skipButt);
        Collections.addAll(hobbiesList, "soccer", "football", "reading", "hiking", "video games", "writing", "music", "resterants");
        int itemsPerRow = 3; // Number of items per row

        GridLayoutManager llm = new GridLayoutManager(personalQuestionsActivity.this, 3);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        hobbitsItems.setLayoutManager(llm);

        personalQuestionsAdapter adapter = new personalQuestionsAdapter(hobbiesList, getApplicationContext());
        hobbitsItems.setAdapter(adapter);



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve selected items from the adapter
                if(adapter.getSelectedItems().size() == 0)
                {
                    selectedItemsList.add("no Hobbies");
                }
                else {
                    selectedItemsList = adapter.getSelectedItems();
                }
                if(!personalInfo.getText().toString().isEmpty()) {
                    description = personalInfo.getText().toString();
                }
                else{
                    description = "No description";
                }
                    for (String selectedItem : selectedItemsList) {
                        Log.i("Selected Item", selectedItem);

                    }

                userPersonalInfo info = new userPersonalInfo((ArrayList<String>) selectedItemsList, description);

                db.collection("Users").document(UID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user = documentSnapshot.toObject(UserStorageData.class);
                        user.setInfo(info);
                        db.collection("Users").document(UID).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent intent = new Intent(personalQuestionsActivity.this, ProfileActivity.class);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Error writing document" + e, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });


            }
        });
    }
}

