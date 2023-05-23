package com.example.solmatchfinalproject;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.net.Uri;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import Model.UserInfo;
import Model.UserStorageData;
import dataBase.MyInfoManager;
//import notification.notificationService;

public class EditPersonalDetails extends Activity {
    private static final int REQUEST_IMAGE_CAPTURE = 11;
    private ImageView image;
    private BottomNavigationView bottomNavigationView;
    private TextView textTitleViewName;

    private ListView listView;
    private UserInfoListAdapter adapter;
    private String email;
    private MyInfoManager myInfoManager = MyInfoManager.getInstance();
    Uri uri = null;

    /**
     * OnCreate function initiates the app with all variables
     * after initialization, we load the toolbar menu
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editdetails);
        myInfoManager.openDataBase(EditPersonalDetails.this);

        bottomNavigationView = findViewById(R.id.menu);
        image = findViewById(R.id.profImg);
        textTitleViewName = findViewById(R.id.textViewTitleName);
        bottomNavigationView.setOnItemReselectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.myHome: {
                    startActivity(new Intent(getApplicationContext(), EditPersonalDetails.class));
                    overridePendingTransition(0, 0);
                    break;
                }
                case R.id.calInvite: {
                    startActivity(new Intent(getApplicationContext(), profileActivity.class));
                    overridePendingTransition(0, 0);
                    break;
                }
                /*
                case R.id.search: {
                    startActivity(new Intent(getApplicationContext(), profileActivity.class));
                    overridePendingTransition(0, 0);
                    break;
                }
                */

                case R.id.logOut: {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    overridePendingTransition(0, 0);
                    break;
                }
            }
        });

        Intent intent = getIntent();
        email = intent.getStringExtra("UserEmail");
        UserStorageData user = myInfoManager.readUserByEmail(email);
        //update the listview
        List<UserInfo> userInfos = new ArrayList<>();
        if (user != null) {
            if (user.getUserName() == null) {
                userInfos.add(new UserInfo(R.string.name, "" + R.string.namePro));
            } else {
                userInfos.add(new UserInfo(R.string.name, user.getUserName()));
            }
            if (user.getEmail() == null) {
                userInfos.add(new UserInfo(R.string.email, "" + R.string.emailPro));
            } else {
                userInfos.add(new UserInfo(R.string.email, user.getEmail()));
            }
            if (user.getPassword() == null) {
                userInfos.add(new UserInfo(R.string.password, "" + R.string.passwordPro));
            } else {
                userInfos.add(new UserInfo(R.string.password, user.getPassword()));
            }
            if (user.getGen() == null) {
                userInfos.add(new UserInfo(R.string.gender, "" + R.string.genderPro));
            } else {
                userInfos.add(new UserInfo(R.string.gender, user.getGen()));
            }
            if (user.getBirthday() == null) {
                userInfos.add(new UserInfo(R.string.birthdate, "" + R.string.birthdayPro));
            } else {
                userInfos.add(new UserInfo(R.string.birthdate, user.getBirthday()));
            }
            if(user.getType()==null)
            {
                userInfos.add(new UserInfo(R.string.type, "" + R.string.typePro));
            } else {
                userInfos.add(new UserInfo(R.string.type, user.getType()));
            }
        }
        listView = (ListView) findViewById(R.id.listOfDetailsToEdit);
        adapter = new UserInfoListAdapter(EditPersonalDetails.this, userInfos);
        listView.setAdapter(adapter);
        textTitleViewName.setText(user.getUserName());
        if (user.getImage() == null) {
            if (user.getGen() != null) {
                if (user.getGen().equals("Female")) {
                    image.setImageResource(R.drawable.anonymouswoman);
                } else if (user.getGen().equals("Male")) {
                    image.setImageResource(R.drawable.anonymousman);
                }
            }
        } else {
            image.setImageBitmap(user.getImage());
        }
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                if (imageBitmap != null) {
                    image.setImageBitmap(imageBitmap);
                }
            }
        }
    }

}




