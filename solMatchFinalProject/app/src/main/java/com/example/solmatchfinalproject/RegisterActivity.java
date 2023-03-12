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

public class RegisterActivity extends AppCompatActivity {
    TextView btn;
    private EditText inputUserName,inputEmail,inputPassword,inputRePassword;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn=findViewById(R.id.label);
        inputUserName=findViewById(R.id.inputUserName);
        inputEmail=findViewById(R.id.inputEmail);
        inputPassword =findViewById(R.id.inputPassword);
        inputRePassword =findViewById(R.id.inputRePassword);
        mAuth=FirebaseAuth.getInstance();
        mLoadingBar=new ProgressDialog(RegisterActivity.this);
        btnRegister=findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCredentials();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void checkCredentials() {
        String userName=inputUserName.getText().toString();
        String email=inputEmail.getText().toString();
        String password=inputPassword.getText().toString();
        String rePassword=inputRePassword.getText().toString();

        if(userName.isEmpty() || userName.length()<7)
        {
            showError(inputUserName,"Your username is not valid!");
        }
        else if(email.isEmpty() || !email.contains("@"))
        {
            showError(inputEmail,"Email in not valid!");
        }
        else if(password.isEmpty() || password.length()<7)
        {
            showError(inputPassword,"password must be 7 character");
        }
        else if(rePassword.isEmpty() || !rePassword.equals(password))
        {
            showError(inputRePassword,"Password not match!");
        }
        else
        {
           mLoadingBar.setTitle("Registeration");
           mLoadingBar.setMessage("please wait,while check your credentials");
           mLoadingBar.setCanceledOnTouchOutside(false);
           mLoadingBar.show();

           mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful())
                   {
                       Toast.makeText(RegisterActivity.this,"Successfuly registration",Toast.LENGTH_SHORT).show();

                       mLoadingBar.dismiss();
                       Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                       startActivity(intent);
                   }
                   else
                   {
                       Toast.makeText(RegisterActivity.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
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