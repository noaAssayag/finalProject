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

import com.example.solmatchfinalproject.profile.ProfileActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
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

import Model.users;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private TextView signIn, forgotPassword;
    private EditText inputUserEmail, inputPassword;
    private Button btnLogin, google;
    private ProgressDialog mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        signIn = findViewById(R.id.textViewSignUp);
        inputUserEmail = findViewById(R.id.inputLogEmail);
        inputPassword = findViewById(R.id.inputLogPassword);
        btnLogin = findViewById(R.id.btnLogin);
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

                    auth.signInWithEmailAndPassword(inputUserEmail.getText().toString(), inputPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        String UID=auth.getUid();
                                        db.collection("Users").document(UID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                users user = documentSnapshot.toObject(users.class);
                                                if(user.getInfo()==null)
                                                {
                                                    Intent intent = new Intent(LoginActivity.this, personalQuestionsActivity.class);
                                                    setContentView(R.layout.personal_questions_layout);
                                                    startActivity(intent);
                                                }
                                                else{
                                                    Toast.makeText(getApplicationContext(), "login was good", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                                                    intent.putExtra("UserEmail", inputUserEmail.getText().toString());
                                                    intent.putExtra("UID",UID);
                                                    startActivity(intent);
                                                }

                                            }
                                        });
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Credentials don't match any user", Toast.LENGTH_SHORT).show();
                                        mLoadingBar.dismiss();
                                    }
                                }
                            });
                } else {
                    mLoadingBar.dismiss();
                    Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getApplicationContext(), "Email was sent with instructions", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to send email", Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(LoginActivity.this, GoogleSignIn.class);
                startActivity(intent);
            }
        });
    }

    private boolean checkCredentials() {
        String email = inputUserEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (email.isEmpty() || !email.contains("@")) {
            showError(inputUserEmail, "Invalid email!");
            return false;
        } else if (password.isEmpty() || password.length() < 7) {
            showError(inputPassword, "Password must be at least 7 characters");
            return false;
        }
        return true;
    }

    private static void showError(EditText input, String message) {
        input.setError(message);
        input.requestFocus();
    }
}
