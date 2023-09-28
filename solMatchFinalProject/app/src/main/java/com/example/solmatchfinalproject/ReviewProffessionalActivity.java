package com.example.solmatchfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

public class ReviewProffessionalActivity extends AppCompatActivity {

    RatingBar ratingBar;
    EditText name;
    EditText et_comments;
    Button btn_cancel,btn_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_proffessional);
        ratingBar=findViewById(R.id.rating_bar);
        name=findViewById(R.id.your_name);
        et_comments=findViewById(R.id.et_comments);
        btn_cancel=findViewById(R.id.btn_cancel);
        btn_submit=findViewById(R.id.btn_submit);

    }
}