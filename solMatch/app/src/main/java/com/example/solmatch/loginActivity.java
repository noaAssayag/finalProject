package com.example.solmatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {

    ImageView logo;
    TextView title;
    EditText email, password;
    Button btSumbit;
    Button btPassReg;
    String email_text,pass_text;

    //FireBase
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logo=findViewById(R.id.imageView2);
        title=findViewById(R.id.title);
        email=findViewById(R.id.EmailLogText);
        password=findViewById(R.id.PasswordLogText);
        btSumbit=(Button) findViewById(R.id.BtLogin);
        btPassReg=(Button)findViewById(R.id.BtpassToRegister);
        btPassReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(loginActivity.this,registerActivity.class);
                startActivity(i);
            }
        });
        //FireBase Auth
        auth=FirebaseAuth.getInstance();
        btSumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email_text=email.getText().toString();
                pass_text=password.getText().toString();
                if(TextUtils.isEmpty(email_text)||TextUtils.isEmpty(pass_text))
                {
                    Toast.makeText(loginActivity.this,"please fill the fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    auth.signInWithEmailAndPassword(email_text,pass_text).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Intent i= new Intent(loginActivity.this,loginActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            }
                            else{
                                Toast.makeText(loginActivity.this,"Login failed", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });

    }
}