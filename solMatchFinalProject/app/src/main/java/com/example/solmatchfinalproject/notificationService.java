package com.example.solmatchfinalproject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.solmatchfinalproject.MainActivity;
import com.example.solmatchfinalproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Random;

import notification.MyWorker;

public class notificationService extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "Notification_channel";
    Random random = new Random();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        createNotificationChannel();
        if (remoteMessage.getData().get("for") != null) {
            if (remoteMessage.getData().get("for").equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())) {
                showNotification(remoteMessage);
            }
        }
    }

    private void showNotification(RemoteMessage remoteMessage) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.drawable.myicon)
                .setContentText(remoteMessage.getData().get("body"))
                .setColor(ContextCompat.getColor(this,R.color.black));
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(random.nextInt() + 1000, builder.build());
    }




    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }






    @Override
    public void onNewToken(@NonNull String token)
    {
        sendRegistrationToServer(token);

    }

    private void schudleJob()
    {
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance(this).beginWith(work).enqueue();
    }


    private void handleNow()
    {

    }

    private void sendRegistrationToServer(String token)
    {

    }
/*
    public void sendNotification(String massageBody,Intent intent,Context context)
    {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_IMMUTABLE);
        Uri defaultsoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, String.valueOf(R.string.default_notification_channel_id)).setSmallIcon(R.drawable.myicon).
                setContentTitle(getString(R.string.fcm_message))
                .setContentText(massageBody)
                .setAutoCancel(true).
                setSound(defaultsoundUri)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager;
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(String.valueOf(R.string.default_notification_channel_id),"channel title",NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0,notificationBuilder.build());
    }
*/

}

