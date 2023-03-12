package com.example.solmatchfinalproject;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    TextView btn;
    EditText inputEmail,inputpassword;
    Button btnLogin;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn=(TextView)findViewById(R.id.textViewSignUp);
        inputEmail=findViewById(R.id.inputEmail);
        inputpassword=findViewById(R.id.inputpassword);
        btnLogin=findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCredentials();
            }
        });

        btn.setOnClickListener(new View.OnClickListener(){
           @Override
           public  void onClick(View v)
           {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
           }
        });
        mAuth= FirebaseAuth.getInstance();
        mLoadingBar=new ProgressDialog(LoginActivity.this);
    }
    private void checkCredentials() {
        String email=inputEmail.getText().toString();
        String password=inputpassword.getText().toString();

        if(email.isEmpty() || !email.contains("@"))
        {
            showError(inputEmail,"Email in not valid!");
        }
        else if(password.isEmpty() || password.length()<7)
        {
            showError(inputpassword,"password must be 7 character");
        }

        else
        {
            mLoadingBar.setTitle("Login");
            mLoadingBar.setMessage("please wait,while check your credentials");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        mLoadingBar.dismiss();
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

    private static void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }

}