package com.example.solmatchfinalproject;

import static com.example.solmatchfinalproject.R.*;

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

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private TextView btn;
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
    private String Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_register);
        btn = (TextView) findViewById(R.id.label);
        inputUserName =(EditText) findViewById(R.id.inputUserName);
        inputEmail =(EditText) findViewById(R.id.inputEmail);
        inputPassword = (EditText)findViewById(R.id.inputPassword);
        inputRePassword = (EditText) findViewById(R.id.inputRePassword);
        mAuth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(RegisterActivity.this);
        btnRegister = findViewById(id.btnRegister);



        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                userName=inputUserName.getText().toString();
                email=inputEmail.getText().toString();
                password=inputPassword.getText().toString();
                rePassword=inputRePassword.getText().toString();
                checkCredentials();

                if (email.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$"))
                {
                    Intent intent = new Intent(RegisterActivity.this, RegSecActivity.class);
                    intent.putExtra("userName", getUserName());
                    intent.putExtra("email", getEmail());
                    intent.putExtra("password",getPassword());

                    startActivity(intent);
                    setContentView(layout.activity_reg_sec);
                }
                else
                {
                    inputEmail.setError("Invalid Email");
                }
            }
        });
    }
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

    private void checkCredentials() {
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

    }

    private static void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }

}