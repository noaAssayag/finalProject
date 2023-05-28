package com.example.solmatchfinalproject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Model.UserStorageData;

public class RegSecActivity extends Activity {
    Spinner sType, sGender;
    TextView dateCal;
    Button finish;
    ImageButton btnUploadImg;
    ImageView userImg;
    Intent intent;
    private String userName;
    private String email;
    private String date;
    private String password;
    private String gen;
    private String type;
    private String image;
    Bitmap bitmapImage;
    private boolean valid;
    private static final int REQUEST_IMAGE_CAPTURE = 11;
    private static final int REQUEST_IMAGE_PICK = 2;
    private StorageReference storageRef;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    Uri imageURI;
    String URL;
    UserStorageData user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_sec);
        intent = getIntent();
        setUserName(intent.getStringExtra("userName"));
        setEmail(intent.getStringExtra("email"));
        setPassword(intent.getStringExtra("password"));
        btnUploadImg = findViewById(R.id.btnUploadImg);
        userImg = findViewById(R.id.userImg);
        sType = findViewById(R.id.spinnerType);
        sGender = findViewById(R.id.spinnerGender);
        dateCal = findViewById(R.id.dateCal);
        finish = findViewById(R.id.btnSubmit);
        storageRef = FirebaseStorage.getInstance().getReference();

        dateCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog pickerDialog = new DatePickerDialog(RegSecActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        dateCal.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                    }
                }, year, month, day);
                pickerDialog.show();
            }
        });

        setDate(dateCal);
        btnUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerDialog();
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getDate() != null && !getDate().isEmpty()) {
                    if (sGender != null && sGender.getSelectedItem() != null) {
                        if (sType != null && sType.getSelectedItem() != null) {
                            if (checkDate()) {
                                if (userImg.getDrawable() == null && sType.getSelectedItem().equals("Soldier")) {
                                    Toast.makeText(getApplicationContext(), "Soldier must add a profile image!", Toast.LENGTH_SHORT).show();
                                } else {
                                    setGen(sGender.getSelectedItem().toString());
                                    setType(sType.getSelectedItem().toString());
                                    if (userImg.getDrawable() != null) {
                                        StorageReference storageRef = FirebaseStorage.getInstance().getReference(imageURI.toString());
                                        storageRef.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                boolean check = task.isSuccessful();
                                                if (task.isSuccessful()) {
                                                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            URL = uri.toString();
                                                            user = new UserStorageData(getUserName(), getEmail(), getGen(), getDate(), getPassword(), URL, getType());
                                                            auth.createUserWithEmailAndPassword(getEmail(), getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                                    if (task.isSuccessful()) {
                                                                        String UID = auth.getUid();
                                                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                                        database.getReference().child("Users").child(UID).setValue(user);
                                                                        valid = true;
                                                                    } else {
                                                                        Toast.makeText(getApplicationContext(), "A user already exists with that email", Toast.LENGTH_SHORT).show();
                                                                        valid = false;
                                                                        return;
                                                                    }
                                                                    if (valid) {
                                                                        Toast.makeText(getApplicationContext(), "User created successfully", Toast.LENGTH_SHORT).show();
                                                                        Intent newIntent = new Intent(RegSecActivity.this, LoginActivity.class);
                                                                        startActivity(newIntent);
                                                                        setContentView(R.layout.activity_login);
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            }
                                        });

                                    }
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Please pick your birthday", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please choose a type of user", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please choose a gender", Toast.LENGTH_SHORT).show();
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
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGen() {
        return gen;
    }

    public void setGen(String gen) {
        this.gen = gen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private String setDate(TextView dateCal) {
        Date hrini = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("d MMM yyyy");
        String date = format.format(hrini);
        dateCal.setText(date);
        return date;
    }

    public boolean checkDate() {
        String[] splitDate = getDate().split("/");
        String year = splitDate[2];
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (currentYear - Integer.parseInt(year) < 18 && currentYear - Integer.parseInt(year) > 120) {
            return false;
        }
        return true;
    }


    private void showImagePickerDialog() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

        if (galleryIntent.resolveActivity(getPackageManager()) != null) {
            Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Picture");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePictureIntent});
            startActivityForResult(chooserIntent, REQUEST_IMAGE_PICK);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the selected image URI
            imageURI = data.getData();
            // Set the image URI to the ImageView
            userImg.setImageURI(imageURI);

            // Upload the image to Firebase Storage
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageURI = data.getData();
            userImg.setImageBitmap(imageBitmap);
        }
    }

}
