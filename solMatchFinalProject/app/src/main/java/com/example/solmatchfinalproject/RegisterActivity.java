package com.example.solmatchfinalproject;

import static com.example.solmatchfinalproject.R.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    TextView btn;
    private Spinner Spinner;
    private EditText inputUserName,inputEmail,inputPassword,inputRePassword;
    private Button btnRegister,btnFinish;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoadingBar;
    private DatePicker age;
    private String userName;
    private String email;
    private String password;
    private String rePassword;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_register);
        btn = findViewById(id.label);
        inputUserName = findViewById(id.inputUserName);
        inputEmail = findViewById(id.inputEmail);
        inputPassword = findViewById(id.inputPassword);
        inputRePassword = findViewById(id.inputRePassword);
        mAuth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(RegisterActivity.this);
        btnRegister = findViewById(id.btnRegister);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCredentials();

            }
        });
    }
    private void checkCredentials() {
        this.userName=inputUserName.getText().toString();
        this.email=inputEmail.getText().toString();
        this.password=inputPassword.getText().toString();
        this.rePassword=inputRePassword.getText().toString();


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
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void checkSecond()
    {
        // add checks of date and host/user
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

    private static void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}