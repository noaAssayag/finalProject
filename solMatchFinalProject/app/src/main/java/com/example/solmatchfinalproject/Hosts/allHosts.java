package com.example.solmatchfinalproject.Hosts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.solmatchfinalproject.All_donation_activity;
import com.example.solmatchfinalproject.BottomNavigationHandler;
import com.example.solmatchfinalproject.ChatClasses.chatMenuActivity;
import com.example.solmatchfinalproject.EditPersonalDetails;
import com.example.solmatchfinalproject.Forms;
import com.example.solmatchfinalproject.R;
import com.example.solmatchfinalproject.notifications;
import com.example.solmatchfinalproject.profile.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
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
import Fragment.NotificationDialogFragment;
import Model.Host;
import dataBase.DatabaseHelper;

public class allHosts extends AppCompatActivity implements RecycleViewInterface, MyAlertDialogFragmentListenerView {
    FirebaseDatabase db;
    DatabaseReference ref,refUsers;
    ImageView img;
    Spinner filterByGen;
    Spinner filterByLoc;
    Button btnFilter;
    RecyclerView recList;

    ImageView backArrow;
    List<Host> list = new ArrayList<>();
    private BottomNavigationHandler navigationHandler;

    private List<Host> originalHostsList;

    private String filterGen="";
    private String filterLoc="";

    private DatabaseHelper sqlDatabase;

    private List<Host> allHostsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_hosts);
        recList = findViewById(R.id.cardList);
        filterByGen = (Spinner) findViewById(R.id.spinnerFilterByGender);
        filterByLoc = (Spinner) findViewById(R.id.spinnerFilterByLocation);
        btnFilter = findViewById(R.id.searchbtn);
        backArrow = findViewById(R.id.backArrow);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        db = FirebaseDatabase.getInstance();
        sqlDatabase = new DatabaseHelper(this);
        allHostsList = sqlDatabase.getAllHosts();
        originalHostsList = new ArrayList<>(allHostsList);
        UserHostAdapter userHostAdapter = new UserHostAdapter(allHostsList, allHosts.this, allHosts.this, true);
        recList.setAdapter(userHostAdapter);
        ActionBar ab=getSupportActionBar();
        ab.setTitle(R.string.hostTitle);
        ab.setDisplayShowHomeEnabled(true);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(allHosts.this, Forms.class);
                        startActivity(intent);            }
                });
            }
        });
        filterByLoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                filterLoc = selectedItem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                filterLoc = "city";
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
                filterGen = "Gender";
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EditPersonalDetails.class);
                startActivity(i);
            }
        });
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Host> filteredList = new ArrayList<>();
                boolean shouldAdd = false;
                if (!filterLoc.equals("City") || !filterGen.equals("Gender")) {
                    for (Host host : originalHostsList) {
                         shouldAdd = false;

                        if (!filterLoc.equals("City")) {
                            if (filterByLoc.getSelectedItem().toString().equals(host.getHostAddress().split(",")[0])) {
                                shouldAdd = true;
                            }
                        }

                        if (!filterGen.equals("Gender")) {
                            if (sqlDatabase.getUserByEmail(host.getHostEmail()).getGen().equals(filterGen)) {
                                shouldAdd = true;
                            }
                        }

                        if (shouldAdd) {
                            filteredList.add(host);
                        }
                    }

                    allHostsList.clear();
                    allHostsList.addAll(filteredList);
                    userHostAdapter.notifyDataSetChanged();
                }
            }
        });
    }
        @Override
    public void onItemClick(int position) {
        Host newHost = allHostsList.get(position);
        AlertDialogFragmentViewHost frag = new AlertDialogFragmentViewHost();
        Bundle b = new Bundle();
        b.putSerializable("Host", newHost);
        frag.setArguments(b);
        frag.show(getFragmentManager(), "dialog");
    }

    @Override
    public void deleteItem(int position) {

    }

    @Override
    public void onDonationClick(int position,View v) {

    }

    @Override
    public void deleteDonation(int position) {

    }

    @Override
    public void AddComments(int position) {

    }

    @Override
    public void onDialogPositiveClick(AlertDialogFragmentViewHost dialog) {

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
        DatabaseHelper helper = new DatabaseHelper(this);
        switch(item.getItemId()) {
            case R.id.notificationIcon:
                List<notifications> notificationsList = helper.getNotificationsByUserID(FirebaseAuth.getInstance().getUid());
                NotificationDialogFragment dialogFragment = new NotificationDialogFragment(notificationsList);
                dialogFragment.show(getSupportFragmentManager(), "NotificationDialogFragment");

                return true;
            case R.id.chatIcon:
                Intent i = new Intent(this, chatMenuActivity.class);
                startActivity(i);
                return true;

            case R.id.profileIcon:
                Intent iProfile= new Intent(this, EditPersonalDetails.class);
                startActivity(iProfile);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        DatabaseHelper helper = new DatabaseHelper(this);
        List<notifications> notifications = helper.getNotificationsByUserID(FirebaseAuth.getInstance().getUid());
        if(!notifications.isEmpty())
        {
            MenuItem item = menu.findItem(R.id.notificationIcon);
            item.setIcon(R.drawable.notification_icon_full);

        }
        return super.onPrepareOptionsMenu(menu);
    }

}


