package com.example.solmatchfinalproject;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView btn=(TextView)findViewById(R.id.textViewSignUp);
        Button bt=findViewById(R.id.btnLogin);
        System.out.println("hello");

        bt.setOnClickListener(new View.OnClickListener(){
           @Override
           public  void onClick(View v)
           {
                System.out.println("hello");
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
           }
        });
    }
}