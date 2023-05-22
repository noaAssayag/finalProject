package ChatClasses;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.solmatchfinalproject.R;
import com.example.solmatchfinalproject.profileActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Model.chat;
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

    int i = 1;
    private ListView messages;
    private TextView writeMessage;
    private Button sendButt;
    List<chatItemInfo> userChats = new ArrayList<chatItemInfo>();
    FirebaseApp app = FirebaseApp.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        HashMap<String,chat> messagedSent = new HashMap<>();
        askNotificationPermission();
        i =1;
        Intent intent = getIntent();
        String chatId = intent.getStringExtra("chatID");
        System.out.println("the chatid is " +chatId);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("chats").child(chatId);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        writeMessage = findViewById(R.id.messageEditText);
        sendButt = findViewById(R.id.sendButton);

        messages = findViewById(R.id.chatMessages);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                while(snapshot.hasChild(String.valueOf(i)))
                {
                    if(snapshot.child(String.valueOf(i)).child("from").getValue().toString()!= null && snapshot.child(String.valueOf(i)).child("message").getValue().toString()!= null) {
                        String from = snapshot.child(String.valueOf(i)).child("from").getValue().toString();;
                        String messageContent = snapshot.child(String.valueOf(i)).child("message").getValue().toString();
                        chatItemInfo item = new chatItemInfo(from, messageContent);
                        userChats.add(item);
                    }
                    i++;
                }

                chatListAdapter adapter = new chatListAdapter(chatActivity.this,userChats);
                messages.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        sendButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(writeMessage.getText().toString()!= null)
                {
                    // we need to know which user is signed in to send the message as of right now
                    // we do this by hand easelly fixable by identifing the user signed in
                    String from = intent.getStringExtra("from");
                    String to = intent.getStringExtra("to");
                    String notificationSender = from+"-"+to;
                    FirebaseMessaging.getInstance().subscribeToTopic(to+"-"+from);
                    send(String.format(notificationMessage.message,notificationSender, from, writeMessage.getText().toString()), new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            Toast.makeText(chatActivity.this,"error",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            chatActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(response.code()==200){
                                        Toast.makeText(chatActivity.this, "Notification sent", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    });
                    // need to implement which user sent the message
                    messagedSent.put(String.valueOf(i),new chat(from,to,writeMessage.getText().toString()));
                    reference.child(String.valueOf(i)).setValue(messagedSent.get(String.valueOf(i)));
                }
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
}