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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {
    TextView btn;
    EditText inputUserEmail,inputpassword;
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
        inputUserEmail=(EditText) findViewById(R.id.inputEmail);
        inputpassword=(EditText) findViewById(R.id.inputpassword);
        btnLogin=(Button) findViewById(R.id.btnLogin);
        google = findViewById(R.id.googleButt);
        facebook = findViewById(R.id.facebookButt);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // first we check the info
                if (checkCredentials()) {
                    mLoadingBar.setTitle("Login");
                    mLoadingBar.setMessage("please wait,while check your credentials");
                    mLoadingBar.setCanceledOnTouchOutside(false);
                    mLoadingBar.show();


                // Now, we start a FirebaseAuth instance
                mAuth = FirebaseAuth.getInstance();

                // we sign in the user using the authenticator signInWithEmailAndPassword method
                // its a built in function provided to us by firebase it allows us to check users credentials
                // and get his UID if the email and password were correct
                mAuth.signInWithEmailAndPassword(inputUserEmail.getText().toString(), inputpassword.getText().toString()).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if sign in was successful we pass the UID to the profile activity and start the activity
                            // all user info is located within the realtime DataBase, and can be accessed with the UID as the key under the users parent.
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, profileActivity.class);
                            intent.putExtra("UID", user.getUid());
                            startActivity(intent);
                            setContentView(R.layout.activity_profile);

                        } else {
                            mLoadingBar.hide();
                            Toast.makeText(getApplicationContext(), "the credentials dont match any user", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            }
            });



                // dont want to delete but ill do the login through the authenticator (thats why we use it)
               /* FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
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
        });*/

        btn.setOnClickListener(new View.OnClickListener(){
           @Override
           public  void onClick(View v)
           {

                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
           }
        });



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

    private boolean checkCredentials() {
        String email = inputUserEmail.getText().toString();
        String password = inputpassword.getText().toString();

        if (email.isEmpty() || !email.contains("@")) {
            showError(inputUserEmail, "Email in not valid!");
            return false;
        } else if (password.isEmpty() || password.length() < 7) {
            showError(inputpassword, "password must be 7 character");
            return false;
        } else {

            return true;


        }
    }

    private static void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }


    }
