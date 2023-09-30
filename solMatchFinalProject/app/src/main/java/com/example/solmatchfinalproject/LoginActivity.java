package com.example.solmatchfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.solmatchfinalproject.ChatClasses.chatMenuActivity;
import com.example.solmatchfinalproject.Hosts.AddHost;
import com.example.solmatchfinalproject.Hosts.allHosts;
import com.example.solmatchfinalproject.profile.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import Model.Host;
import Model.Professional;
import Model.UserStorageData;
import Model.donations;
import dataBase.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    UserStorageData userSignedIn;

    ArrayList<UserStorageData> users;
    ArrayList<Host> hosts;
    ArrayList<donations> donationsList;

    ArrayList<Professional> professionalList;

    ArrayList<notifications> notificationsList;
    Button signIn, forgotPassword;
    EditText inputUserEmail, inputPassword;
    Button btnLogin;
    Button google;
    private ProgressDialog mLoadingBar;

    DatabaseHelper sqlData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputUserEmail = findViewById(R.id.et_email);
        inputPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.bt_login);
        forgotPassword = findViewById(R.id.bt_forgot_password);
        signIn = findViewById(R.id.bt_dont_have_account);
        mLoadingBar = new ProgressDialog(this);
        users = new ArrayList<>();
        hosts = new ArrayList<>();
        donationsList = new ArrayList<>();
        notificationsList = new ArrayList<>();
        professionalList = new ArrayList<>();
        sqlData = new DatabaseHelper(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkCredentials()) {
                    mLoadingBar.setTitle("Login");
                    mLoadingBar.setMessage("Please wait while we check your credentials");
                    mLoadingBar.setCanceledOnTouchOutside(false);
                    mLoadingBar.show();

                    auth.signInWithEmailAndPassword(inputUserEmail.getText().toString(), inputPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        String UID = user.getUid();

                                        database.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                        for(QueryDocumentSnapshot snapshot:queryDocumentSnapshots) {
                                                            UserStorageData userData = snapshot.toObject(UserStorageData.class);
                                                            users.add(userData);
                                                            if(userData.getUID().equals(UID))
                                                            {
                                                                userSignedIn = userData;
                                                            }

                                                            // Rest of your code for user data retrieval
                                                            // You can access userData fields like this:
                                                            // String userName = userData.getUserName();
                                                        }
                                                            sqlData.compareAndUpdateUsers(users);
                                                            database.collection("Host").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                    for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots)
                                                                    {
                                                                        Host host = snapshot.toObject(Host.class);
                                                                        host.setUid(snapshot.getId());
                                                                        hosts.add(host);
                                                                    }
                                                                    sqlData.compareAndUpdateHosts(hosts);

                                                                    database.collection("professional").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                            for(QueryDocumentSnapshot snapshot:queryDocumentSnapshots)
                                                                            {
                                                                                Professional professional = snapshot.toObject(Professional.class);
                                                                                professional.setUID(snapshot.getId());
                                                                                professionalList.add(professional);
                                                                            }
                                                                            sqlData.compareAndUpdateProfessionals(professionalList);


                                                                    database.collection("Donations").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                            for(QueryDocumentSnapshot snapshot:queryDocumentSnapshots)
                                                                            {
                                                                                donations donation = snapshot.toObject(donations.class);
                                                                                donation.setUid(snapshot.getId());
                                                                                donationsList.add(donation);
                                                                            }
                                                                            sqlData.compareAndUpdateDonations(donationsList);

                                                                            database.collection("Notifications").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                                @Override
                                                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                                    for(QueryDocumentSnapshot snapshot:queryDocumentSnapshots)
                                                                                    {
                                                                                        notifications notification = snapshot.toObject(notifications.class);
                                                                                        notificationsList.add(notification);
                                                                                    }
                                                                                    sqlData.compareAndUpdateNotifications(notificationsList);
                                                                                }
                                                                            });

                                                                            if (userSignedIn.getInfo() !=null) {
                                                                                Toast.makeText(getApplicationContext(), "Login was successful", Toast.LENGTH_SHORT).show();
                                                                                Intent intent = new Intent(LoginActivity.this, EditPersonalDetails.class);
                                                                                intent.putExtra("UserEmail", inputUserEmail.getText().toString());
                                                                                startActivity(intent);
                                                                            } else {
                                                                                // Redirect to personalQuestionsActivity
                                                                                Intent intent = new Intent(LoginActivity.this, personalQuestionsActivity.class);
                                                                                startActivity(intent);
                                                                            }
                                                                        }
                                                                    });
                                                                        }
                                                                    });
                                                                }

                                                            });
                                                            // Fetch other data here (hosts, donations, etc.)

                                                            // Check if user has userInfo


                                                    }
                                                });

                                    } else {
                                        Toast.makeText(getApplicationContext(), "The credentials don't match any user", Toast.LENGTH_SHORT).show();
                                        mLoadingBar.hide();
                                    }
                                }
                            });

                } else {
                    mLoadingBar.hide();
                    Toast.makeText(getApplicationContext(), "The credentials don't match any user", Toast.LENGTH_SHORT).show();
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgetPassword.class);
                startActivity(intent);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean checkCredentials() {
        String email = inputUserEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (email.isEmpty() || !email.contains("@")) {
            showError(inputUserEmail, "Email is not valid!");
            return false;
        } else if (password.isEmpty() || password.length() < 7) {
            showError(inputPassword, "Password must be at least 7 characters");
            return false;
        }
        return true;
    }

    private static void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.notificationIcon:
            {
                return true;
            }
            case R.id.chatIcon:
            {
                Intent i = new Intent(this, chatMenuActivity.class);
                startActivity(i);
                return true;

            }
        }
        return false;
    }
}