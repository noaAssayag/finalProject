package donations;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solmatchfinalproject.BottomNavigationHandler;
import com.example.solmatchfinalproject.Hosts.RecycleViewInterface;
import com.example.solmatchfinalproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Fragment.AlertDialogFragmentViewDonations;
import Fragment.AlertDialogFragmentViewHost;
import Fragment.AletListener;
import Fragment.MyAlertDialogFragmentListenerView;
import Model.Host;
import dataBase.DatabaseHelper;
import donations.donationAdapter;
import Model.donations;

public class donationActivity extends Activity  implements RecycleViewInterface, AletListener {
    RecyclerView donationsView;
    Button filter;
    Spinner categories;
    String filterSelected;
    List<donations> donationList = new ArrayList<>();

    DatabaseHelper sqlDatabase;

    private BottomNavigationHandler navigationHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donations_layout);
        filter = findViewById(R.id.btnFilterDoantions);
        categories = findViewById(R.id.spinnerFilterCatagory);
        sqlDatabase = new DatabaseHelper(this);
        donationList = sqlDatabase.getAllDonations();
        donationsView = findViewById(R.id.donationsRecycler);
        GridLayoutManager llm = new GridLayoutManager(donationActivity.this, 1);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        donationsView.setLayoutManager(llm);
        donationAdapter adapter = new donationAdapter(donationList,getApplicationContext(),donationActivity.this,true);
        donationsView.setAdapter(adapter);
     //   donations donation = new donations("test","kkal 16", "home cooking","test object", R.drawable.anonymousman);
    //    donations donation2 = new donations("test","kkal 16", "home cooking","test object", drawable.);
   //     donations.add(donation);
    //    donations.add(donation2);
        BottomNavigationView bottomNavigationView = findViewById(R.id.menu);
        navigationHandler = new BottomNavigationHandler(this,getApplicationContext());
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationHandler);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<donations> FiltereddonationList = new ArrayList<>();
                if(filterSelected == null || filterSelected.equals("Filter by category"))
                {
                    donationAdapter adapter = new donationAdapter(donationList,getApplicationContext(),donationActivity.this,true);
                    donationsView.setAdapter(adapter);
                    return;
                }
                for(donations donation:donationList)
                {
                    if(donation.getCatagory().equals(filterSelected))
                    {
                        FiltereddonationList.add(donation);
                    }
                }
                donationAdapter adapter = new donationAdapter(FiltereddonationList,getApplicationContext(),donationActivity.this,true);
                donationsView.setAdapter(adapter);
            }
        });

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

    }

    @Override
    public void onItemClick(int position) {
        donations newDonations = donationList.get(position);
        AlertDialogFragmentViewDonations frag = new AlertDialogFragmentViewDonations();
        Bundle b = new Bundle();
        b.putSerializable("Donation", newDonations);
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

    @Override
    public void onDialogPositiveClick(AlertDialogFragmentViewDonations dialog) {
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();

    }


}
