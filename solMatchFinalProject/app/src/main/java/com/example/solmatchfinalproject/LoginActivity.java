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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import Model.UserStorageData;
import dataBase.MyInfoManager;
import donations.donationActivity;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    TextView signIn, forgotPassword;
    EditText inputUserEmail, inputpassword;
    Button btnLogin;
    Button google;
    private ProgressDialog mLoadingBar;
    private MyInfoManager myInfoManager = MyInfoManager.getInstance();

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
                // first we check the info
//              if (checkCredentials()) {
                    mLoadingBar.setTitle("Login");
                    mLoadingBar.setMessage("Please wait while we check your credentials");
                    mLoadingBar.setCanceledOnTouchOutside(false);
                    mLoadingBar.show();
                    auth.signInWithEmailAndPassword(inputUserEmail.getText().toString(),inputpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"login was good",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this,AddHost.class);
                                intent.putExtra("UserEmail", inputUserEmail.getText().toString());
                                startActivity(intent);
                                setContentView(R.layout.activity_add_host);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "the credentials dont match any user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
//                    Intent intent = new Intent(LoginActivity.this, EditPersonalDetails.class);
//                    intent.putExtra("UserEmail", inputUserEmail.getText().toString());
//                    startActivity(intent);
//                    setContentView(R.layout.activity_editdetails);
               // }
//                   else {
//                    mLoadingBar.hide();
//                    Toast.makeText(getApplicationContext(), "the credentials dont match any user", Toast.LENGTH_SHORT).show();
//                }
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.sendPasswordResetEmail(inputUserEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"email was sent to you with information", Toast.LENGTH_LONG);
                        }
                    }
                });
                if (inputUserEmail.getText().toString() != null&&(!inputUserEmail.getText().toString().isEmpty())) {
                    AlertDialogFragmentEdit frag = new AlertDialogFragmentEdit();
                    Bundle b = new Bundle();
                    b.putString("Email", inputUserEmail.getText().toString());
                    frag.setArguments(b);
                    frag.show(getFragmentManager(), "dialog");
                } else {
                    Toast.makeText(getApplicationContext(), "You must enter an email for reset password", Toast.LENGTH_SHORT).show();
                    return;
                }
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
        List<UserStorageData> userList = myInfoManager.getAllUser();
        String email = inputUserEmail.getText().toString();
        String password = inputpassword.getText().toString();

        if (email.isEmpty() || !email.contains("@")) {
            showError(inputUserEmail, "Email in not valid!");
            return false;
        } else if (password.isEmpty() || password.length() < 7) {
            showError(inputpassword, "password must be 7 character");
            return false;
        }
        for (UserStorageData user : userList) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    private static void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}
