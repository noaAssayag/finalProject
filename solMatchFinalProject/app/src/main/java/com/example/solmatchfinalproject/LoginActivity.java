package com.example.solmatchfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.solmatchfinalproject.ChatClasses.chatActivity;
import com.example.solmatchfinalproject.Hosts.AddHost;
import com.example.solmatchfinalproject.Hosts.allHosts;
import com.example.solmatchfinalproject.profile.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.solmatchfinalproject.ChatClasses.chatMenuActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import donations.donationActivity;

import java.util.ArrayList;

import Model.Host;
import Model.UserStorageData;
import Model.donations;
import dataBase.DatabaseHelper;
import donations.donationActivity;
public class LoginActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore database = FirebaseFirestore.getInstance();

    ArrayList<UserStorageData> users;
    ArrayList<Host> hosts;
    ArrayList<donations> donationsList;
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
        sqlData = new DatabaseHelper(this);

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
                                        database.collection("Users").document(UID).get()
                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        if (documentSnapshot.exists()) {
                                                            UserStorageData userData = documentSnapshot.toObject(UserStorageData.class);
                                                            users.add(userData);

                                                            // Rest of your code for user data retrieval
                                                            // You can access userData fields like this:
                                                            // String userName = userData.getUserName();

                                                            sqlData.compareAndUpdateUsers(users);
                                                            database.collection("Host").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                    for(QueryDocumentSnapshot snapshot: queryDocumentSnapshots)
                                                                    {
                                                                        Host host = snapshot.toObject(Host.class);
                                                                        hosts.add(host);
                                                                    }
                                                                    sqlData.compareAndUpdateHosts(hosts);
                                                                    database.collection("Donations").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                            for(QueryDocumentSnapshot snapshot:queryDocumentSnapshots)
                                                                            {
                                                                                donations donation = snapshot.toObject(donations.class);
                                                                                donationsList.add(donation);
                                                                            }
                                                                            sqlData.compareAndUpdateDonations(donationsList);
                                                                            if (documentSnapshot.contains("info") && documentSnapshot.get("info")!=null) {
                                                                                Toast.makeText(getApplicationContext(), "Login was successful", Toast.LENGTH_SHORT).show();
                                                                                Intent intent = new Intent(LoginActivity.this, allHosts.class);
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
                                                            // Fetch other data here (hosts, donations, etc.)

                                                            // Check if user has userInfo

                                                        } else {
                                                            // Handle the case where the document doesn't exist
                                                            Toast.makeText(getApplicationContext(), "User data not found", Toast.LENGTH_SHORT).show();
                                                        }
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
}