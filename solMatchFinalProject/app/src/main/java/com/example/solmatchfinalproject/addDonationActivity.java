package com.example.solmatchfinalproject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class addDonationActivity extends Activity {
    int PICK_IMAGE_REQUEST = 100;
    EditText itemName,ItemDescription,adress;
    ImageView itemImage;
    ImageButton uploadImage;
    Button addItem;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_donation_page);
        itemName = findViewById(R.id.inputItemName);
        ItemDescription = findViewById(R.id.inputItemDescription);
        adress = findViewById(R.id.inputLocation);
        itemImage = findViewById(R.id.imageViewer);
        uploadImage = findViewById(R.id.uploadImageButt);
        addItem = findViewById(R.id.btnAddDonation);

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // noa needs to implement adding data to database
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            itemImage.setImageURI(imageUri);
        }
    }

}
