package com.example.solmatch;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String Tag="MyFireBaseMsgSevice";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        //handle the message in app
        Log.v(Tag,"From: "+message.getFrom());
        //check for data payload in the message
        if(message.getData().size()>0){
            Log.v(Tag, "Message Data Payload :"+message.getData());
            if(true)
            {
                SchudleJob();
            }
            else {
                handleNow();
            }
        }
        //check for notification payload
        if(message.getNotification()!=null){
            Log.v(Tag, "message Notification Body: "+message.getNotification().getBody());
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(Tag,"onNewToken: "+token);
        sendRegistrationToServer(token);
    }
    private void SchudleJob()
    {
        OneTimeWorkRequest work= new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance(this).beginWith(work).enqueue();
    }
    private void handleNow()
    {
        Log.v(Tag,"Short lived Task is done!");
    }
    private void sendRegistrationToServer(String token)
    {

    }
    private void sendNotification(String messageBody){
        Intent i=new Intent(this,MainActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,
                i,PendingIntent.FLAG_ONE_SHOT);
        String channelId=getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder= new NotificationCompat.Builder(this,channelId).setSmallIcon((R.drawable.notification))
                .setContentTitle(getString((R.string.fcm_message)))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O ){
            NotificationChannel channel=new NotificationChannel(channelId,"Channel Human readable title",NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(0,notificationBuilder.build());

        }

    }

}
