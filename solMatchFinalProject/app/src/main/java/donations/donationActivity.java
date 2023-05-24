package donations;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solmatchfinalproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import donations.donationAdapter;
import Model.donations;

public class donationActivity extends Activity {

    RecyclerView donationsView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.donations_layout);
     //   donations donation = new donations("test","kkal 16", "home cooking","test object", R.drawable.anonymousman);
    //    donations donation2 = new donations("test","kkal 16", "home cooking","test object", drawable.);
        List<donations> donationList = new ArrayList<>();
   //     donations.add(donation);
    //    donations.add(donation2);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Donations");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren())
                {
                    donations donation = new donations(child.child("name").getValue().toString(),child.child("adress").getValue().toString(),child.child("catagory").getValue().toString(),child.child("description").getValue().toString(),child.child("img").getValue().toString(),child.child("username").getValue().toString());
                    donationList.add(donation);
                }

                donationsView = findViewById(R.id.donationsRecycler);
                GridLayoutManager llm = new GridLayoutManager(donationActivity.this, 2);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                donationsView.setLayoutManager(llm);
                donationAdapter adapter = new donationAdapter(donationList,getApplicationContext());
                donationsView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}
