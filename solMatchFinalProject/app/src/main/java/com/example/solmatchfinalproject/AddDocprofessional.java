package com.example.solmatchfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.solmatchfinalproject.ChatClasses.chatMenuActivity;
import com.example.solmatchfinalproject.Hosts.AddHost;
import com.example.solmatchfinalproject.profile.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import Model.Professional;
import dataBase.DatabaseHelper;

public class AddDocprofessional extends AppCompatActivity {
    private Spinner professCategory;
    private Spinner professAddress;
    private EditText professDescription,autoCompleteLocationPro;
    private EditText professPhoneNum;
    TextView percentage;
    private ProgressBar progressBar;
    private SeekBar seekBar;
    private Button btnSubmit;
    private BottomNavigationView bottomNavigationView;
    FirebaseAuth auth;
    FirebaseFirestore db;
    private String uid,email,userName,imageUser,address;
    DatabaseHelper sqlDataBase;
    String precentageAva="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_docprofessional);
        professCategory = findViewById(R.id.professCategory);
        professAddress = findViewById(R.id.professAddress);
        professDescription = findViewById(R.id.professDescription);
        professPhoneNum = findViewById(R.id.ProfessphoneNum);
        percentage=findViewById(R.id.percentage);
        progressBar=findViewById(R.id.prograssBar);
        seekBar = findViewById(R.id.seek_bar);
        btnSubmit=findViewById(R.id.btnSubmit);
        autoCompleteLocationPro = findViewById(R.id.autoCompleteLocationPro);
        autoCompleteLocationPro.setEnabled(false);
        ActionBar ab=getSupportActionBar();
        ab.setTitle(R.string.proTitle);
        ab.setDisplayShowHomeEnabled(true);
        sqlDataBase = new DatabaseHelper(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressBar.setProgress(progress);
                percentage.setText(""+progress+"%");
                precentageAva=""+progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        autoCompleteLocationPro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               if(autoCompleteLocationPro.getText().toString().isEmpty())
               {
                   professAddress.setEnabled(true);
               }
               else{
                   professAddress.setEnabled(false);
               }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(autoCompleteLocationPro.getText().toString().isEmpty()) {
                    if (professAddress == null || professAddress.getSelectedItem().toString().equals("Filter By city") || professAddress.getSelectedItem().toString().isEmpty()) {
                        Toast.makeText(AddDocprofessional.this, "Please choose a area", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (professCategory == null || professCategory.getSelectedItem().toString().equals("Filter by category") || professCategory.getSelectedItem().toString().isEmpty()) {
                    Toast.makeText(AddDocprofessional.this, "Please choose a category", Toast.LENGTH_SHORT).show();
                    return;
                }  else if (professDescription.getText().toString().isEmpty() || professPhoneNum.getText().toString().isEmpty()) {
                    Toast.makeText(AddDocprofessional.this, "Please fill all the fileds", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(professPhoneNum.getText().toString().length()!=10){
                    Toast.makeText(AddDocprofessional.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                }else {
                    auth = FirebaseAuth.getInstance();
                    uid = auth.getCurrentUser().getUid();
                    db = FirebaseFirestore.getInstance();

                    db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for(QueryDocumentSnapshot doc: task.getResult())
                            {
                                if(doc.getId().equals(uid))
                                {
                                    email = doc.get("email").toString();
                                    userName = doc.get("userName").toString();
                                    if(!doc.get("image").toString().isEmpty())
                                    {
                                        imageUser = doc.get("image").toString();
                                    }
                                }
                            }
                        }
                    });
                    if(autoCompleteLocationPro.getText().toString().isEmpty()) {
                        address = professAddress.getSelectedItem().toString();
                    }
                    else{
                        address = autoCompleteLocationPro.getText().toString();
                    }

                    Professional professional = new Professional(email,userName,imageUser,professCategory.getSelectedItem().toString()
                            ,address
                            ,professPhoneNum.getText().toString()
                            ,professDescription.getText().toString(),precentageAva,uid);
                    db.collection("professional").document(uid).set(professional).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            sqlDataBase.insertProfessionalData(professional);
                            Toast.makeText(getApplicationContext(),"succes",Toast.LENGTH_LONG);
                            Intent intent = new Intent(getApplicationContext(),profileActivity.class);
                            startActivity(intent);
                        }
                    });

                }
            }

    });
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();
        switch(item.getItemId()) {
            case R.id.notificationIcon:
                return true;
            case R.id.chatIcon:
                Intent i = new Intent(this, chatMenuActivity.class);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}