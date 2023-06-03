package com.example.solmatchfinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.solmatchfinalproject.Hosts.AddHost;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AddDocprofessional extends AppCompatActivity {
    private Spinner professCategory;
    private Spinner professAddress;
    private EditText professDescription;
    private EditText professPhoneNum;
    TextView percentage;
    private ProgressBar progressBar;
    private SeekBar seekBar;
    Button btnSubmit;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_docprofessional);
        professCategory = findViewById(R.id.professCategory);
        professAddress = findViewById(R.id.professAddress);
        professDescription = findViewById(R.id.professDescription);
        professPhoneNum = findViewById(R.id.ProfessphoneNum);
        percentage=findViewById(R.id.percentage);
        progressBar=findViewById(R.id.prograssBar);
        seekBar = findViewById(R.id.seek_bar);
        btnSubmit=findViewById(R.id.btnSubmit);
        bottomNavigationView = findViewById(R.id.menu);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressBar.setProgress(progress);
                percentage.setText(""+progress+"%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(professCategory==null||!professCategory.getSelectedItem().toString().equals("Filter by category")||professCategory.getSelectedItem().toString().isEmpty())
                {
                    Toast.makeText(AddDocprofessional.this, "Please choose a category", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(professAddress==null||!professAddress.getSelectedItem().toString().equals("Filter By city")||professAddress.getSelectedItem().toString().isEmpty())
                {
                    Toast.makeText(AddDocprofessional.this, "Please choose a area", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(professDescription.getText().toString().isEmpty()||professPhoneNum.getText().toString().isEmpty())
                {
                    Toast.makeText(AddDocprofessional.this, "Please fill all the fileds", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{

                }
            }
        });

    }
}