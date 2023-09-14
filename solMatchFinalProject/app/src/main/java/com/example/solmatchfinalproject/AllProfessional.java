package com.example.solmatchfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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
import Model.Host;
import Model.Professional;
import dataBase.DatabaseHelper;

public class AllProfessional extends AppCompatActivity implements RecycleViewInterface, AlertListenerProf {
    FirebaseDatabase db;
    DatabaseReference ref,refUsers;
    ImageView img;
    Spinner filterByCategory;
    Spinner filterByLoc;
    Button btnFilter;
    RecyclerView recList;
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
        btnFilter = (Button) findViewById(R.id.btnFilter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        db = FirebaseDatabase.getInstance();

        sqlDatabase = new DatabaseHelper(this);
        allProfessionalList = sqlDatabase.getAllProfessionals();
        ProfessionalAdapter adapter = new ProfessionalAdapter(allProfessionalList, AllProfessional.this, AllProfessional.this);
        recList.setAdapter(adapter);
        menu = findViewById(R.id.menu);
        menu.setOnItemReselectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.myHome: {
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    overridePendingTransition(0, 0);
                    break;
                }
                case R.id.chatMenu: {
                    startActivity(new Intent(getApplicationContext(), chatMenuActivity.class));
                    overridePendingTransition(0, 0);
                    break;
                }
                case R.id.search: {
                    startActivity(new Intent(getApplicationContext(), searchNavigationMenue.class));
                    overridePendingTransition(0, 0);
                    break;
                }
                case R.id.logOut: {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    overridePendingTransition(0, 0);
                    break;
                }
            }
        });

        ref = db.getReference("professional");
        filterByLoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        filterByCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 List<Professional> filteredList = new ArrayList<>();
                if(!filterByLoc.getSelectedItem().toString().equals("noFilter") || !filterByCategory.getSelectedItem().toString().equals("noFilter"))
                {
                    for(Professional pro: allProfessionalList)
                    {
                        if(!filterByLoc.getSelectedItem().toString().equals("noFilter"))
                        {
                            if(filterByLoc.getSelectedItem().toString().equals(pro.getAddress().split(",")[0]))
                            {
                                filteredList.add(pro);
                            }

                        }
                        if(!filterByCategory.getSelectedItem().toString().equals("noFilter"))
                        {
                            if(!filteredList.isEmpty())
                            {
                                for(Professional pro1: allProfessionalList)
                                {
                                    if(!pro1.getCategory().equals(filterByCategory.getSelectedItem().toString()))
                                    {
                                        filteredList.remove(pro1);
                                    }
                                }
                            }
                            else {
                                for(Professional pro2: allProfessionalList)
                                {
                                    if(!pro2.getCategory().equals(filterByCategory.getSelectedItem().toString()))
                                    {
                                        filteredList.add(pro2);
                                    }
                                }
                            }
                        }
                        allProfessionalList.clear();
                        allProfessionalList = filteredList;
                        adapter.notifyDataSetChanged();
                    }



                }
//                ref.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot snap : snapshot.getChildren())
//                        {
//                            Professional professional=new Professional();
//                            professional.setUserName(snap.child("userName").getValue().toString());
//                            professional.setEmail(snap.child("email").getValue().toString());
//                            professional.setPhoneNum(snap.child("phoneNum").getValue().toString());
//                            professional.setCategory(snap.child("category").getValue().toString());
//                            professional.setAddress(snap.child("address").getValue().toString());
//                            professional.setDescription(snap.child("description").getValue().toString());
//                            professional.setPrecAvailability(snap.child("precAvailability").getValue().toString());
//                            professional.setImageUrl(snap.child("imageUrl").getValue().toString());
//
//                            if (filterByLoc.getSelectedItem() != null && !(filterByLoc.getSelectedItem().toString().equals("Filter By city"))) {
//                                if (filterByLoc.getSelectedItem().toString().equals(professional.getAddress())) {
//                                    list.add(professional);
//                                }
//                            }
//                            if (filterByCategory.getSelectedItem() != null && !(filterByCategory.getSelectedItem().toString().equals("Filter by category"))) {
//                                if (filterByCategory.getSelectedItem().toString().equals(professional.getCategory())) {
//                                    list.add(professional);
//                                }
//                            }
//                            if ((filterByCategory.getSelectedItem().equals("Filter by category")) && (filterByLoc.getSelectedItem().equals("Filter By city"))) {
//                                list.add(professional);
//                            }
//                        }
//                        ProfessionalAdapter adapter = new ProfessionalAdapter(list, AllProfessional.this, AllProfessional.this);
//                        recList.setAdapter(adapter);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
            }
        });


    }

    @Override
    public void onDialogPositiveClick(AlertDialogFragmentViewProf dialog) {
        Toast.makeText(this, " ", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onItemClick(int position) {
        Professional professional = allProfessionalList.get(position);
        AlertDialogFragmentViewProf frag = new AlertDialogFragmentViewProf();
        Bundle b = new Bundle();
        b.putString("Description", professional.getDescription());
        frag.setArguments(b);
        frag.show(getFragmentManager(), "dialog");
    }

    @Override
    public void deleteItem(int position) {

    }

    @Override
    public void onDonationClick(int position) {

    }

    @Override
    public void deleteDonation(int position) {

    }


}