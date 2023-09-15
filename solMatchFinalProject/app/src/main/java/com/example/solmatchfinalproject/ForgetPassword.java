package com.example.solmatchfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    Button submitButton;
    TextInputEditText emailEditText;
    TextView error;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        submitButton = findViewById(R.id.bt_submit);
        emailEditText = findViewById(R.id.et_email);
        error=findViewById(R.id.tv_enter_email_tip);
        error.setVisibility(View.GONE);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = emailEditText.getText().toString();
                if(emailAddress==null||emailAddress.isEmpty())
                {
                    error.setVisibility(View.VISIBLE);
                }
                else{
                    error.setVisibility(View.GONE);
                    auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Email was sent to you with information", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(getApplicationContext(), "Failed to send email. Please check your email address.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
                // Implement your logic to send a password recovery email or perform other actions.
            }
        });
    }
}