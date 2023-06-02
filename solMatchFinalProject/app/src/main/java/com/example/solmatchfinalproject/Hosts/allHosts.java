package com.example.solmatchfinalproject.Hosts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.solmatchfinalproject.BottomNavigationHandler;
import com.example.solmatchfinalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Fragment.AlertDialogFragmentViewDonations;
import Fragment.AlertDialogFragmentViewHost;
import Fragment.MyAlertDialogFragmentListenerView;
import Model.Host;

public class allHosts extends AppCompatActivity implements RecycleViewInterface, MyAlertDialogFragmentListenerView {
    FirebaseDatabase db;
    DatabaseReference ref,refUsers;
    ImageView img;
    Spinner filterByGen;
    Spinner filterByLoc;
    Button btnFilter;
    RecyclerView recList;
    List<Host> list = new ArrayList<>();
    private BottomNavigationHandler navigationHandler;
    private String filterGen;
    private String filterLoc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_hosts);
        recList = findViewById(R.id.cardList);
        filterByGen = (Spinner) findViewById(R.id.spinnerFilterByGender);
        filterByLoc = (Spinner) findViewById(R.id.spinnerFilterByLocation);
        btnFilter = (Button) findViewById(R.id.btnFilter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        db = FirebaseDatabase.getInstance();
        BottomNavigationView bottomNavigationView = findViewById(R.id.menu);
        navigationHandler = new BottomNavigationHandler(this, getApplicationContext());
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationHandler);
        ref = db.getReference("Host");

        filterByLoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                filterLoc = selectedItem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filterLoc = "noFilter";
            }
        });
        filterByGen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                filterGen = selectedItem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filterGen = "noFilter";
            }
        });
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = new ArrayList<>();

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot snap : snapshot.getChildren()) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy, h:mm a", Locale.US);
                            try {
                                Date date = dateFormat.parse(snap.child("hostingDate").getValue(String.class));
                                Calendar currentDate = Calendar.getInstance();
                                Date today = currentDate.getTime();
                                if (date.after(today)) {
                                    Host newHost = new Host();
                                    newHost.setHostName(snap.child("hostName").getValue(String.class));
                                    newHost.setHostEmail(snap.child("hostEmail").getValue(String.class));
                                    newHost.setHostAddress(snap.child("hostAddress").getValue(String.class));
                                    newHost.setHostingDate(snap.child("hostingDate").getValue(String.class));
                                    String imageUrl = snap.child("hostImg").getValue(String.class);
                                    newHost.setHostImg(imageUrl);
                                    String imageUrlLoc = snap.child("hostingLocImg").getValue(String.class);
                                    newHost.setHostingLocImg(imageUrlLoc);
                                    newHost.setAccommodation((boolean) snap.child("accommodation").getValue());
                                    newHost.setPets((boolean) snap.child("pets").getValue());
                                    newHost.setPrivateRoom((boolean) snap.child("privateRoom").getValue());
                                    newHost.setSecureEnv((boolean) snap.child("secureEnv").getValue());
                                    newHost.setDescription(snap.child("description").getValue().toString());
                                    if (filterByLoc.getSelectedItem() != null && !(filterLoc.equals("noFilter"))) {
                                        if (filterByLoc.getSelectedItem().toString().equals(newHost.getHostAddress().split(",")[0])) {
                                            list.add(newHost);
                                        }

                                    }
                                    if (filterByGen.getSelectedItem() != null && !(filterGen.equals("noFilter"))&&!filterByGen.getSelectedItem().equals("Filter By Gender")) {
                                        refUsers = db.getReference().child("Users");
                                        refUsers.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot snapUser : snapshot.getChildren()) {
                                                    if (snapUser.getKey().equals(snap.getKey())) {
                                                        if (snapUser.child("gen").getValue().toString().equals(filterByGen.getSelectedItem().toString())) {
                                                            list.add(newHost);


                                                        }
                                                    }

                                                }
                                                UserHostAdapter userHostAdapter = new UserHostAdapter(list, allHosts.this, allHosts.this);
                                                recList.setAdapter(userHostAdapter);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                    if ((filterByGen.getSelectedItem().equals("Filter By Gender")) && (filterByLoc.getSelectedItem().equals("Filter By city"))) {
                                        list.add(newHost);

                                    }

                                } else {
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference ref = database.getReference().child("Host");
                                    ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot dataSnap : snapshot.getChildren()) {
                                                if (dataSnap.getKey().equals(snap.getKey())) {
                                                    ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {

                                                            } else {
                                                                // Data removal failed
                                                                // Handle the error
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }

                                    });
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                        // Populate the RecyclerView with the retrieved list of hosts
                        UserHostAdapter userHostAdapter = new UserHostAdapter(list, allHosts.this, allHosts.this);
                        recList.setAdapter(userHostAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
            }
        });

    }
    //   public Host(String hostImg, String hostName, String hostEmail, String hostAddress, String hostingDate, String hostingLocImg, boolean accommodation, boolean pets, boolean privateRoom, boolean secureEnv) {

    @Override
    public void onItemClick(int position) {
        Host newHost = list.get(position);
        AlertDialogFragmentViewHost frag = new AlertDialogFragmentViewHost();
        Bundle b = new Bundle();
        b.putSerializable("Host", newHost);
        frag.setArguments(b);
        frag.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onDialogPositiveClick(AlertDialogFragmentViewHost dialog) {
        Toast.makeText(this, "This host addedd to wishList", Toast.LENGTH_SHORT).show();
    }



}


