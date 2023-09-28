package com.example.solmatchfinalproject;

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

import com.example.solmatchfinalproject.ChatClasses.chatMenuActivity;
import com.example.solmatchfinalproject.Hosts.RecycleViewInterface;
import com.example.solmatchfinalproject.Hosts.UserHostAdapter;
import com.example.solmatchfinalproject.Hosts.allHosts;
import com.example.solmatchfinalproject.profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Fragment.AlertDialogFragmentViewHost;
import Fragment.AlertListenerProf;
import Fragment.MyAlertDialogFragmentListenerView;
import Fragment.NotificationDialogFragment;
import Model.Host;
import Model.Professional;
import dataBase.DatabaseHelper;

public class AllProfessional extends AppCompatActivity implements RecycleViewInterface, AlertListenerProf {
    FirebaseDatabase db;
    Spinner filterByCategory;
    Spinner filterByLoc;
    Button btnFilter;
    RecyclerView recList;
    ImageView backArrow;
    List<Professional> allProfessionalList = new ArrayList<>();

    DatabaseHelper sqlDatabase;
    BottomNavigationView menu;
    private BottomNavigationHandler navigationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_professional);
        recList = findViewById(R.id.cardList);
        filterByCategory = (Spinner) findViewById(R.id.spinnerFilterByCategory);
        filterByLoc = (Spinner) findViewById(R.id.spinnerFilterByLocation);
        btnFilter = (Button) findViewById(R.id.searchbtn);
        backArrow=findViewById(R.id.backArrow);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        db = FirebaseDatabase.getInstance();

        sqlDatabase = new DatabaseHelper(this);
        allProfessionalList = sqlDatabase.getAllProfessionals();
        ProfessionalAdapter adapter = new ProfessionalAdapter(allProfessionalList, AllProfessional.this, AllProfessional.this);
        recList.setAdapter(adapter);
        ActionBar ab=getSupportActionBar();
        ab.setTitle(R.string.profTitle);
        ab.setDisplayShowHomeEnabled(true);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AllProfessional.this, Forms.class);
                        startActivity(intent);            }
                });
            }
        });
        btnFilter.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             List<Professional> filteredList = new ArrayList<>();
                                             if (!filterByLoc.getSelectedItem().toString().equals("City")) {
                                                 for (Professional pro : allProfessionalList) {
                                                     if (filterByLoc.getSelectedItem().toString().equals(pro.getAddress())) {
                                                         filteredList.add(pro);
                                                     }

                                                 }
                                             }
                                             if (!filterByCategory.getSelectedItem().toString().equals("Category")) {
                                                 for (Professional pro : allProfessionalList) {
                                                     if (pro.getCategory().equals(filterByCategory.getSelectedItem().toString())) {
                                                         filteredList.add(pro);
                                                     }


                                                 }
                                             }
                                             adapter.notifyDataSetChanged();
                                         }
                                     });
    }

@Override
public void onDialogPositiveClick(AlertDialogFragmentViewProf dialog){
        Toast.makeText(this," ",Toast.LENGTH_SHORT).show();

        }


@Override
public void onItemClick(int position){
        Professional professional=allProfessionalList.get(position);
        AlertDialogFragmentViewProf frag=new AlertDialogFragmentViewProf();
        Bundle b=new Bundle();
        b.putString("Description",professional.getDescription());
        frag.setArguments(b);
        frag.show(getFragmentManager(),"dialog");
        }

@Override
public void deleteItem(int position){

        }

@Override
public void onDonationClick(int position,View v){

        }

@Override
public void deleteDonation(int position){

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