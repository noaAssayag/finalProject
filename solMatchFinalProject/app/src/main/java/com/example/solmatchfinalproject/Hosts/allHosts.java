package com.example.solmatchfinalproject.Hosts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.solmatchfinalproject.R;
import com.example.solmatchfinalproject.UserInfoListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class allHosts extends AppCompatActivity {
    FirebaseDatabase db;
    DatabaseReference ref;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_hosts);
        RecyclerView recList = findViewById(R.id.cardList);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        List<Host> list = new ArrayList<>();
        db = FirebaseDatabase.getInstance();
        ref = db.getReference("Host");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Host newHost = new Host();
                    newHost.setHostName(snap.child("hostName").getValue(String.class));
                    newHost.setHostEmail(snap.child("hostEmail").getValue(String.class));
                    newHost.setHostAddress(snap.child("hostAddress").getValue(String.class));
                    newHost.setHostingDate(snap.child("hostingDate").getValue(String.class));
                    String imageUrl = snap.child("hostImage").getValue(String.class);
                    loadImage(imageUrl, newHost);
                    list.add(newHost);
                }
                // Populate the RecyclerView with the retrieved list of hosts
                UserHostAdapter userHostAdapter = new UserHostAdapter(list);
                recList.setAdapter(userHostAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
    private void loadImage(String imageUrl, Host host) {
        Picasso.get().load(imageUrl).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // Set the loaded bitmap to the host object
                host.setHostImage(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                // Handle the failure case if needed
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                // Handle the image loading progress if needed
            }
        });
    }
}