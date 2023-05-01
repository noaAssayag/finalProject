package com.example.solmatchfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotPasswordActivity extends AppCompatActivity {
    private Button resetButt;
    private EditText resetEmail;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        resetButt = findViewById(R.id.btnReset);
        resetEmail = findViewById(R.id.forgot_password_email);


        resetButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resetEmail.getText().toString()!=null) {
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.sendPasswordResetEmail(resetEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(), "if the email is in our systems we have sent you an email with reset instractions",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(forgotPasswordActivity.this,LoginActivity.class);
                                startActivity(intent);
                            }
                            else
                                Toast.makeText(getApplicationContext(), "something went wrong",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(forgotPasswordActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }

        });

    }


    }

