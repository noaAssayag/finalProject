package com.example.solmatchfinalproject;

import static com.google.android.material.internal.ViewUtils.hideKeyboard;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.chat;
import Model.donations;
import notification.notificationMessage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class profileActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    String userDescription = null;

    List<String> hobbiesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp app = FirebaseApp.initializeApp(this);
        bottomNavigationView = findViewById(R.id.menu);
        setContentView(R.layout.activity_profile);
        RecyclerView recyclerView=findViewById(R.id.recycleView);
        hobbiesList = new ArrayList<>();

       // List<UserHosting> listhostingForUsers=new ArrayList<>();
       // listhostingForUsers.add(new UserHosting("Noa Assayag",R.drawable.anonymousman,"24/08/2002","Migdal Haemek"));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
       // recyclerView.setAdapter(new userHostAdapter(getApplicationContext(),listhostingForUsers));


     //   FirebaseMessaging.getInstance().subscribeToTopic(msg);
        // NOA NOA NOA NOA NOA NOA NOA NOA NOA NOA NOA NOA NOA NOA NOA NOA
        // dont delete this even if you dont understand just ask me first

        // for noa after user Login, i get all info from the questions, and i give you the objects for them
        // need to implement listview horizontal present all hobbies in the form ill write right now
        // each object is in the form of the xml file personal_hobbit_item.xml i created
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userInfo");

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       userDescription = snapshot.child("description").getValue().toString();
                        for(DataSnapshot child : snapshot.child("hobbies").getChildren())
                        {
                            hobbiesList.add(child.getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                // now noa you have a list of hobbies the user gave, the description he has, all you have to do
                // is present them in a horizontal listview and in a simple textview hope you can manage


    }



}