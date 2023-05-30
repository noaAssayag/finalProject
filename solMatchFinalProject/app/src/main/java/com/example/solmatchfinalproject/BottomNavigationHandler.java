package com.example.solmatchfinalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.example.solmatchfinalproject.profile.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.solmatchfinalproject.ChatClasses.chatMenuActivity;
import com.google.firebase.auth.FirebaseAuth;

public class BottomNavigationHandler implements BottomNavigationView.OnNavigationItemSelectedListener {
    Intent intent;
    private Activity activity;
    Context context;

    public BottomNavigationHandler(Activity activity,Context context) {
        this.activity = activity;
        this.context = context;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.myHome:
                intent = new Intent(activity, ProfileActivity.class);
                intent.putExtra("UID",FirebaseAuth.getInstance().getCurrentUser().getUid());
                activity.startActivity(intent);

                return true;
            case R.id.chatMenu:
                intent = new Intent(activity, chatMenuActivity.class);
                activity.startActivity(intent);
                return true;
            case R.id.search:
                intent = new Intent(activity, searchNavigationMenue.class);
                activity.startActivity(intent);
                return true;
            case R.id.logOut:
              //  intent = new Intent(context, profileActivity.class);
               // context.startActivity(intent);
                return true;
        }
        return false;
    }


}
