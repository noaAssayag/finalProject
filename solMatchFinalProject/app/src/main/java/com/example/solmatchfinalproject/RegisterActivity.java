package com.example.solmatchfinalproject;

import static com.example.solmatchfinalproject.R.*;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import Model.UserStorageData;
import dataBase.MyInfoManager;

public class RegisterActivity extends Activity {
    private TextView linkToLogin;
    private EditText inputUserName, inputEmail, inputPassword, inputRePassword;
    private DatePicker age;
    private Button btnRegister;
    private ProgressDialog mLoadingBar;
    private String userName;
    private String email;
    private String password;
    private String rePassword;
    private String Password;
    private MyInfoManager myInfoManager = MyInfoManager.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_register);

        linkToLogin = (TextView) findViewById(R.id.textViewBackToLogin);
        inputUserName = (EditText) findViewById(R.id.inputUserName);
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        inputRePassword = (EditText) findViewById(R.id.inputRePassword);
        mLoadingBar = new ProgressDialog(RegisterActivity.this);
        btnRegister = (Button) findViewById(id.btnRegister);
        linkToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                setContentView(layout.activity_login);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userName = inputUserName.getText().toString();
                email = inputEmail.getText().toString();
                password = inputPassword.getText().toString();
                rePassword = inputRePassword.getText().toString();
                if (checkCredentials()) {
                    {
                        Intent intent = new Intent(RegisterActivity.this, RegSecActivity.class);
                        intent.putExtra("userName", getUserName());
                        intent.putExtra("email", getEmail());
                        intent.putExtra("password", getPassword());
                        startActivity(intent);
                        setContentView(layout.activity_reg_sec);
                    }
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

    private boolean checkCredentials() {
        List<UserStorageData> users = myInfoManager.getAllUser();
        if (userName.isEmpty() || userName.length() < 7 || userName.contains(" ")) {
            showError(inputUserName, "Your username is not valid!");
            return false;
        } else if (email.isEmpty() || !email.contains("@") || (!(email.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")))) {
            showError(inputEmail, "Email in not valid!");
            return false;
        } else if (password.isEmpty() || password.length() < 7) {
            showError(inputPassword, "password must be 7 character");
            return false;
        } else if (rePassword.isEmpty() || rePassword.length() < 7) {
            showError(inputRePassword, "password must be 7 character");
            return false;
        } else if (!(rePassword.equals(password))) {
            showError(inputRePassword, "Password not match!");
            return false;
        }
        for (UserStorageData user : users) {
            if (user.getEmail().equals(email)) {
                Toast.makeText(getApplicationContext(), "This email is already in use, change email to register", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private static void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }

}