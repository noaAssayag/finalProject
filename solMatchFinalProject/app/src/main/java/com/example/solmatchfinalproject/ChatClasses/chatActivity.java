package com.example.solmatchfinalproject.ChatClasses;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.solmatchfinalproject.BottomNavigationHandler;

import Fragment.NotificationDialogFragment;
import Model.Host;

import com.example.solmatchfinalproject.EditPersonalDetails;
import com.example.solmatchfinalproject.LoginActivity;
import com.example.solmatchfinalproject.R;
import com.example.solmatchfinalproject.addDonationActivity;
import com.example.solmatchfinalproject.notifications;
import com.example.solmatchfinalproject.profile.ProfileActivity;
import com.example.solmatchfinalproject.searchNavigationMenue;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Model.UserStorageData;
import Model.chat;
import dataBase.DatabaseHelper;
import notification.notificationMessage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class chatActivity extends AppCompatActivity {
    String token = null;

    String UIDToSendNoti;
    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send", KEY_STRING = "key=AAAAg1pSiQ0:APA91bH3Q7WXI_bctdvipJ5QjbRhehV5_a798RBK7SEe5-US9K0OW2l3IjB1YXwF4EarvBey9dHEvSys8a0Srv1YqOtU8CJju0ecarJ6nKuvBGVrDuBPaUXYcsItkT12W8lXN4-2CDaF";

    OkHttpClient client = new OkHttpClient();

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });

    int counter = 0;

    FirebaseFirestore db;

    int i = 1;
    long size = 0;
    private ListView messages;
    private TextView writeMessage;
    private Button sendButt;

    private TextView chatterName;
    private ImageButton addHostButt;
    boolean FLAG;

    DataSnapshot addHostToReference;

    private BottomNavigationHandler navigationHandler;
    List<chatItemInfo> userChats = new ArrayList<chatItemInfo>();
    FirebaseApp app = FirebaseApp.getInstance();
    Intent intent;
    String hostNum;
    DatabaseReference Usersreference;
    BottomNavigationView menu;

    LinearLayout profileLayout;

    String userToAddHostToUID;

    String UID;

    DatabaseHelper sqlDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HashMap<String, chat> messagedSent = new HashMap<>();
        askNotificationPermission();
        i = 1;
        intent = getIntent();
        String chatId = intent.getStringExtra("chatID");
        System.out.println("the chatid is " + chatId);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("chats").child(chatId);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        writeMessage = findViewById(R.id.messageEditText);
        sendButt = findViewById(R.id.sendButton);
        BottomNavigationView bottomNavigationView = findViewById(R.id.menu);
        navigationHandler = new BottomNavigationHandler(this, getApplicationContext());
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationHandler);
        messages = findViewById(R.id.chatMessages);
        chatterName = findViewById(R.id.chatterName);
        addHostButt = findViewById(R.id.addHostButt);
        menu = findViewById(R.id.menu);
        db = FirebaseFirestore.getInstance();
        chatterName.setText(intent.getStringExtra("userToPresent"));
        profileLayout = findViewById(R.id.profileLayout);
        sqlDatabase = new DatabaseHelper(this);
        List<UserStorageData> users = sqlDatabase.getAllUsers();
        for(UserStorageData user:users)
        {
            if(user.getEmail().replace("@", "").replace(".", "").equals(intent.getStringExtra("userToPresent")))
            {
                UID = user.getUID();
            }
        }

        ActionBar ab=getSupportActionBar();

        ab.setTitle(R.string.chatTitle);
        ab.setDisplayShowHomeEnabled(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                while (snapshot.hasChild(String.valueOf(i))) {
                    if (snapshot.child(String.valueOf(i)).child("from").getValue().toString() != null && snapshot.child(String.valueOf(i)).child("message").getValue().toString() != null) {
                        String from = snapshot.child(String.valueOf(i)).child("from").getValue().toString();
                        ;
                        String messageContent = snapshot.child(String.valueOf(i)).child("message").getValue().toString();
                        chatItemInfo item = new chatItemInfo(from, messageContent);
                        userChats.add(item);
                    }
                    i++;
                }

                chatListAdapter adapter = new chatListAdapter(chatActivity.this, userChats);
                messages.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        profileLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chatActivity.this,ProfileActivity.class);
                intent.putExtra("UID", UID);
                intent.putExtra("status",1);
                startActivity(intent);
            }
        });
        sendButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (writeMessage.getText().toString() != null) {
                    // we need to know which user is signed in to send the message as of right now
                    // we do this by hand easelly fixable by identifing the user signed in
                    String from = intent.getStringExtra("from");
                    String to = intent.getStringExtra("to");
                    String notificationSender = from + "-" + to;
                    FirebaseMessaging.getInstance().subscribeToTopic(to + "-" + from);
                    send(String.format(notificationMessage.message, notificationSender, from, writeMessage.getText().toString()), new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            Toast.makeText(chatActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            chatActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (response.code() == 200) {
                                        Toast.makeText(chatActivity.this, "Notification sent", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    });
                    // need to implement which user sent the message
                    messagedSent.put(String.valueOf(i), new chat(from, to, writeMessage.getText().toString()));
                    reference.child(String.valueOf(i)).setValue(messagedSent.get(String.valueOf(i)));
                    notifications noti = new notifications(UID,""+ from +": "+ writeMessage.getText().toString());
                    db.collection("Notifications").add(noti).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            sqlDatabase.insertNotificationData(noti);

                        }
                    });
                }
            }
        });

        addHostButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSimpleAlertDialog();
            }
        });

    }

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    public void send(String message, Callback callback) {
        RequestBody reqBody = RequestBody.create(message
                , MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url(FCM_URL)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", KEY_STRING)
                .post(reqBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public void showSimpleAlertDialog() {

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.dialog_message);
        builder.setTitle(R.string.dialog_title);

        // Add the buttons
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                UserStorageData userToAddToHost = null;
                Host updatedHost = null;
                List<Host> hosts = sqlDatabase.getAllHosts();
                List<UserStorageData> users = sqlDatabase.getAllUsers();
                for(UserStorageData user: users)
                {
                    if(user.getEmail().replace("@", "").replace(".", "").equals(intent.getStringExtra("to")))
                    {
                        userToAddToHost = user;
                    }
                }
                for(Host host: hosts)
                {
                    if(host.getHostName().equals(sqlDatabase.getUserByUID(uid).getUserName())) {
                        if (!userToAddToHost.equals(null)) {
                            if (host.getListOfResidents().isEmpty()) {

                                List<UserStorageData> list = new ArrayList<>();
                                list.add(userToAddToHost);
                                host.setListOfResidents(list);
                                updatedHost = host;
                            }
                            else
                            {
                                host.getListOfResidents().add(userToAddToHost);
                                updatedHost = host;
                            }
                        }
                    }
                }

                sqlDatabase.updateHost(updatedHost);
//                Usersreference = FirebaseDatabase.getInstance().getReference().child("Users");
//                Usersreference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for(DataSnapshot addHostTo:snapshot.getChildren())
//                        {
//                            String debuging = intent.getStringExtra("to");
//                            if(addHostTo.child("email").getValue().toString().replace("@", "").replace(".", "").equals(intent.getStringExtra("to")))
//                            {
//                                DatabaseReference referenceHost = FirebaseDatabase.getInstance().getReference().child("Host")
//                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                                userToAddHostToUID = addHostTo.getKey();
//                               addHostToReference  = addHostTo;
//                                referenceHost.addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snap) {
//                                        Host newHost = new Host();
//                                        newHost.setHostName(snap.child("hostName").getValue(String.class));
//                                        newHost.setHostEmail(snap.child("hostEmail").getValue(String.class));
//                                        newHost.setHostAddress(snap.child("hostAddress").getValue(String.class));
//                                        newHost.setHostingDate(snap.child("hostingDate").getValue(String.class));
//                                        String imageUrl = snap.child("hostImg").getValue(String.class);
//                                        newHost.setHostImg(imageUrl);
//                                        newHost.setAccommodation((boolean) snap.child("accommodation").getValue());
//                                        newHost.setPets((boolean) snap.child("pets").getValue());
//                                        newHost.setPrivateRoom((boolean) snap.child("privateRoom").getValue());
//                                        newHost.setSecureEnv((boolean) snap.child("secureEnv").getValue());
//                                        newHost.setDescription(snap.child("description").getValue().toString());
//                                         Usersreference.child(addHostToReference.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
//                                             @Override
//                                             public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                if(counter == 0) {
//                                                    if (snapshot.hasChild("Host")) {
//                                                        size = snapshot.child("Host").getChildrenCount() + 1;
//                                                        hostNum = Long.toString(size);
//                                                        Usersreference.child(addHostToReference.getKey()).child("Host").child(hostNum).setValue(newHost);
//
//
//                                                    } else {
//                                                        Usersreference.child(addHostToReference.getKey()).child("Host").child("1").setValue(newHost);
//                                                    }
//                                                    counter++;
//                                                }
//                                             }
//
//                                             @Override
//                                             public void onCancelled(@NonNull DatabaseError error) {
//
//                                             }
//                                         });
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

                Toast.makeText(getApplicationContext(), "User clicked OK button", Toast.LENGTH_SHORT).show();


            }


        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Toast.makeText(getApplicationContext(), "User cancelled the dialog", Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "User clicked on Neutral Btn", Toast.LENGTH_SHORT).show();

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
