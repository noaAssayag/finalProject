package com.example.solmatchfinalproject.ChatClasses;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.solmatchfinalproject.BottomNavigationHandler;
import com.example.solmatchfinalproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dataBase.DatabaseHelper;

public class chatMenuActivity extends AppCompatActivity {
    chatMenueListAdapter adapter;
    ListView chatList;
    FirebaseApp app = FirebaseApp.getInstance();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    ArrayList<String> chatNames = null;
    String userName = null;

    DatabaseHelper sqlDataBase;
    private BottomNavigationHandler navigationHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_menu);
        FirebaseAuth auth = FirebaseAuth.getInstance(app);
        FirebaseUser user = auth.getCurrentUser();
        String id = user.getUid();
        sqlDataBase = new DatabaseHelper(this);
        List<chatItemInfo> userChats = new ArrayList<chatItemInfo>();
        chatList = findViewById(R.id.chatList);
        ActionBar ab=getSupportActionBar();
        ab.setTitle(R.string.chatTitle);
        ab.setDisplayShowHomeEnabled(true);
        // we use valueEvent listener to recive the logged in user userName so we can get his chat history
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatNames = new ArrayList<>();
                userName = sqlDataBase.getUserByUID(id).getEmail().replace("@", "").replace(".", "");
                for(DataSnapshot child: snapshot.child("chats").getChildren())
                {
                    if(child.getValue().toString()!=null) {
                        String chatName = child.getKey();
                        if (chatName.contains(userName))
                        {
                         chatNames.add(chatName);
                        }
                    }
                    }
                        for(String name: chatNames) {
                            String[] parts = name.split("-");
                            String user1 = parts[0].trim();
                            String user2 = parts[1].trim();
                            if(user1.equals(userName)) {
                                // we need option to start chat from noa implemented aspects, as of right now we present a sample
                                String from = user2;
                                String message = (String) snapshot.child(name).child("chats").child("1").child("message").getValue();
                                userChats.add(new chatItemInfo(from, R.drawable.myicon));
                                chatMenueListAdapter adapter = new chatMenueListAdapter(chatMenuActivity.this, userChats);
                                chatList.setAdapter(adapter);
                            }
                            if(user2.equals(userName)) {
                                // we need option to start chat from noa implemented aspects, as of right now we present a sample
                                String from = user1;
                                String message = (String) snapshot.child(name).child("chats").child("1").child("message").getValue();
                                userChats.add(new chatItemInfo(from, R.drawable.myicon));
                                chatMenueListAdapter adapter = new chatMenueListAdapter(chatMenuActivity.this, userChats);
                                chatList.setAdapter(adapter);
                            }
                        }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    public String chatNameFinder(String name)
    {
        for(String chat:chatNames)
        {
            if(chat.contains(name))
            {
                return chat;
            }
        }
        return null;
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

    public String getUserName() {
        return userName;
    }
}

    // puting this code to add chat to data base for later

 //   DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
 //   DatabaseReference itemsRef = databaseRef.child("chats");
 //   chat test = new chat("eidostern","noa assayag","test massage");
  //  Map<String,chat> item = new HashMap<>();
  //      item.put(test.getFrom()+"-"+test.getTo(),test);
   //             itemsRef.setValue(item);