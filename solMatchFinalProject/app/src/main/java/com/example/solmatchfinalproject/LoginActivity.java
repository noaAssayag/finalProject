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

import Model.UserStorageData;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    TextView signIn, forgotPassword;
    EditText inputUserEmail, inputpassword;
    Button btnLogin;
    Button google;
    private ProgressDialog mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signIn = (TextView) findViewById(R.id.textViewSignUp);
        inputUserEmail = (EditText) findViewById(R.id.inputLogEmail);
        inputpassword = (EditText) findViewById(R.id.inputLogPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        google = findViewById(R.id.googleButt);
        forgotPassword = findViewById(R.id.textViewForgetPassword);
        mLoadingBar = new ProgressDialog(this);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkCredentials()) {
                    mLoadingBar.setTitle("Login");
                    mLoadingBar.setMessage("Please wait while we check your credentials");
                    mLoadingBar.setCanceledOnTouchOutside(false);
                    mLoadingBar.show();
                    auth.signInWithEmailAndPassword(inputUserEmail.getText().toString(), inputpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                String UID = user.getUid();
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(UID);
                                ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.hasChild("userInfo")) {
                                            Toast.makeText(getApplicationContext(), "login was good", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                                            intent.putExtra("UserEmail", inputUserEmail.getText().toString());
                                            intent.putExtra("UID",UID);
                                            startActivity(intent);
                                        } else {

                                            Intent intent = new Intent(LoginActivity.this, personalQuestionsActivity.class);
                                            setContentView(R.layout.personal_questions_layout);
                                            startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            } else {
                                Toast.makeText(getApplicationContext(), "the credentials dont match any user", Toast.LENGTH_SHORT).show();
                                mLoadingBar.hide();
                                return;
                            }
                        }
                    });

                } else {
                    mLoadingBar.hide();
                    Toast.makeText(getApplicationContext(), "the credentials dont match any user", Toast.LENGTH_SHORT).show();
                }
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.sendPasswordResetEmail(inputUserEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "email was sent to you with information", Toast.LENGTH_LONG);
                        }
                    }
                });
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
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, googleSignIn.class);
                startActivity(intent);
            }
        });
    }

    private boolean checkCredentials() {
        String email = inputUserEmail.getText().toString();
        String password = inputpassword.getText().toString();

        if (email.isEmpty() || !email.contains("@")) {
            showError(inputUserEmail, "Email in not valid!");
            return false;
        } else if (password.isEmpty() || password.length() < 7) {
            showError(inputpassword, "password must be 7 character");
            return false;
        }
        return true;
    }

    private static void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}
