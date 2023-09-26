package com.example.solmatchfinalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.solmatchfinalproject.Hosts.RecycleViewInterface;

import java.util.ArrayList;
import java.util.List;

import Fragment.AlertDialogFragmentViewDonations;
import Fragment.AletListener;
import Model.donations;
import donations.donationAdapter;
import dataBase.DatabaseHelper;

public class All_donation_activity extends AppCompatActivity implements RecycleViewInterface, AletListener {
    private ImageView backArrow;
    private EditText nameDon;
    private Button searchBtn;
    private RecyclerView recyclerViewDon;
    private Spinner categories;
    String filterSelected;
    List<donations> donationList = new ArrayList<>();

    DatabaseHelper sqlDatabase;
    boolean useName=false;
    boolean useCategory=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_donation);
        nameDon = findViewById(R.id.name_donation);
        searchBtn = findViewById(R.id.searchbtn);
        recyclerViewDon = findViewById(R.id.recyclerViewDon);
        categories = findViewById(R.id.catagorySpinner);

        sqlDatabase = new DatabaseHelper(this);
        donationList = sqlDatabase.getAllDonations();

        GridLayoutManager llm = new GridLayoutManager(All_donation_activity.this, 1);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewDon.setLayoutManager(llm);
        donationAdapter adapter = new donationAdapter(donationList, getApplicationContext(), (RecycleViewInterface) All_donation_activity.this, true);
        recyclerViewDon.setAdapter(adapter);

        categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                filterSelected = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<donations> filterDonationList = new ArrayList<>();
                if (filterSelected == null || filterSelected.equals("Category")) {
                    useCategory = false;
                } else {
                    useCategory = true;
                }
                if (nameDon.getText().toString().isEmpty() || nameDon.getText().toString() == null||nameDon.getText().toString().equals("")) {
                    useName = false;
                }
                else {
                    useName = true;
                }
                if (!useName && !useCategory) {
                    filterDonationList.addAll(donationList);
                } else {
                    if (useCategory) {
                        for (donations donation : donationList) {
                            if (donation.getCatagory().equals(filterSelected)) {
                                filterDonationList.add(donation);
                            }
                        }
                    }
                    if (useName) {
                        for (donations donation : donationList) {
                            if (donation.getName().toLowerCase().startsWith(nameDon.getText().toString().toLowerCase())) {
                                filterDonationList.add(donation);
                            }
                        }
                    }
                }

                donationAdapter adapter = new donationAdapter(filterDonationList, getApplicationContext(), All_donation_activity.this, true);
                recyclerViewDon.setAdapter(adapter);
                return;
            }
        });
    }
    @Override
    public void onDialogPositiveClick(AlertDialogFragmentViewDonations dialog) {

    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void deleteItem(int position) {

    }

    @Override
    public void onDonationClick(int position,View v) {
        donations newDonations = donationList.get(position);
        AlertDialogFragmentViewDonations frag = new AlertDialogFragmentViewDonations();
        Bundle b = new Bundle();
        b.putSerializable("Donation", newDonations);
        frag.setArguments(b);
        frag.show(getFragmentManager(), "dialog");
    }

    @Override
    public void deleteDonation(int position) {

    }
}