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

import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {
    TextView btn;
    EditText inputUserName,inputpassword;
    Button btnLogin;
    Button google;
    LoginButton facebook;
    private FirebaseAuth mAuth;
    private ProgressDialog mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn=(TextView)findViewById(R.id.textViewSignUp);
        inputUserName=(EditText) findViewById(R.id.inputname);
        inputpassword=(EditText) findViewById(R.id.inputpassword);
        btnLogin=(Button) findViewById(R.id.btnLogin);
        google = findViewById(R.id.googleButt);
        facebook = findViewById(R.id.facebookButt);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCredentials();
                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                DatabaseReference databaseReference=firebaseDatabase.getReference("Users");

                Query check_email=databaseReference.orderByChild("userName").equalTo(inputUserName.getText().toString());
                check_email.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            inputUserName.setError(null);
                            String passwordcheck=snapshot.child(inputUserName.getText().toString()).child("password").getValue(String.class);
                            if(passwordcheck.equals(inputpassword.getText().toString())){
                                Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();

                                Intent newIntent = new Intent(LoginActivity.this,profileActivity.class);
                                newIntent.putExtra("userName",inputUserName.getText().toString());
                                startActivity(newIntent);
                                setContentView(R.layout.activity_register);

                            }
                            else
                            {
                                inputpassword.setError("Wrong Password");
                            }
                        }
                        else
                        {
                            inputUserName.setError("User doesnt exist");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

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
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,facebookSignIn.class);
                startActivity(intent);
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,googleSignIn.class);
                startActivity(intent);
            }
        });
    }
    private void checkCredentials() {
        String username = inputUserName.getText().toString();
        String password = inputpassword.getText().toString();

        if (username.isEmpty() || (username.length() <7)) {
            showError(inputUserName, "Email in not valid!");
        } else if (password.isEmpty() || password.length() < 7) {
            showError(inputpassword, "password must be 7 character");
        } else {
            mLoadingBar.setTitle("Login");
            mLoadingBar.setMessage("please wait,while check your credentials");
            mLoadingBar.setCanceledOnTouchOutside(false);
            mLoadingBar.show();


        }
    }

    private static void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }

}