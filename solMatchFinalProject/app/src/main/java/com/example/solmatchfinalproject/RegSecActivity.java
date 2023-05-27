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
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_sec);
        intent = getIntent();
        setUserName(intent.getStringExtra("userName"));
        setEmail(intent.getStringExtra("email"));
        setPassword(intent.getStringExtra("password"));
        btnUploadImg = (ImageButton) findViewById(R.id.btnUploadImg);
        userImg = (ImageView) findViewById(R.id.userImg);
        sType = (Spinner) findViewById(R.id.spinnerType);
        sGender = (Spinner) findViewById(R.id.spinnerGender);
        dateCal = (TextView) findViewById(R.id.dateCal);
        finish = (Button) findViewById(R.id.btnSubmit);
        storageRef = FirebaseStorage.getInstance().getReference();

        dateCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calnedar = Calendar.getInstance();
                int day = calnedar.get(Calendar.DAY_OF_MONTH);
                int month = calnedar.get(Calendar.MONTH);
                int year = calnedar.get(Calendar.YEAR);

                DatePickerDialog pickerDialog = new DatePickerDialog(RegSecActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        dateCal.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        date = Integer.toString(dayOfMonth) + "/" + Integer.toString(monthOfYear) + "/" + Integer.toString(year);
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
                if (getDate() != null && (!(getDate().isEmpty()))) {
                    if (sGender != null && sGender.getSelectedItem() != null) {
                        if (sType != null && sType.getSelectedItem() != null) {
                            if (checkDate()) {
                                if (userImg.getDrawable() == null && sType.getSelectedItem().equals("Soldier")) {
                                    Toast.makeText(getApplicationContext(), "Soldier must add an profile image!", Toast.LENGTH_SHORT).show();
                                } else {
                                    setGen(sGender.getSelectedItem().toString());
                                    setType(sType.getSelectedItem().toString());
                                    if (userImg.getDrawable() == null)
                                    {
                                        if (getGen().equals("Female")) {
                                            Drawable drawableImage = getResources().getDrawable(R.drawable.anonymouswoman);
                                            bitmapImage = ((BitmapDrawable) drawableImage).getBitmap();
                                        } else {
                                            Drawable drawableImage = getResources().getDrawable(R.drawable.anonymousman);
                                            bitmapImage = ((BitmapDrawable) drawableImage).getBitmap();
                                        }
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                        byte[] imageData = baos.toByteArray();

                                         imageUri = Uri.fromFile(new File("path/to/image.jpg"));
                                        // Upload the image to Firebase Storage and save the download URL to Firestore
                                        uploadImageToFirebaseStorage(imageUri);
                                    }
                                    else {
                                        BitmapDrawable drawable = (BitmapDrawable) userImg.getDrawable();
                                        Bitmap bitmap = drawable.getBitmap();
                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                        byte[] imageData = baos.toByteArray();
                                        // Convert the byte array to a Uri
                                        imageUri = Uri.fromFile(new File("path/to/image.jpg"));
                                        // Upload the image to Firebase Storage and save the download URL to Firestore
                                        uploadImageToFirebaseStorage(imageUri);
                                    }
                                }

                                    UserStorageData user = new UserStorageData(getUserName(), getEmail(), getGen(), getDate(), getPassword(), imageUri.toString(), getType());

                                    auth.createUserWithEmailAndPassword(getEmail(), getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                String UID = auth.getUid();
                                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                database.getReference().child("Users").child(UID).setValue(user);
                                                valid = true;
                                            } else {
                                                Toast.makeText(getApplicationContext(), "users allready exists with that email", Toast.LENGTH_SHORT).show();
                                                valid = false;
                                                return;
                                            }
                                            if (valid) {
                                                Toast.makeText(getApplicationContext(), "user created successfully", Toast.LENGTH_SHORT).show();
                                                Intent newIntent = new Intent(RegSecActivity.this, LoginActivity.class);
                                                startActivity(newIntent);
                                                setContentView(R.layout.activity_login);
                                            }
                                        }


                                    });
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

        // Create a chooser intent to select between camera and gallery
        Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Picture");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePictureIntent});

        // Start the chooser activity
        startActivityForResult(chooserIntent, REQUEST_IMAGE_PICK);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the selected image URI
            Uri selectedImageUri = data.getData();

            // Set the image URI to the ImageView
            userImg.setImageURI(selectedImageUri);

            // Upload the image to Firebase Storage
            uploadImageToFirebaseStorage(selectedImageUri);
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            userImg.setImageBitmap(imageBitmap);
        }
    }


    private void uploadImageToFirebaseStorage(Uri imageUri) {
        // Create a reference to the image file in Firebase Storage
        final StorageReference imageRef = storageRef.child("images");

        // Upload the file to Firebase Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Image upload successful
                        Toast.makeText(RegSecActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();

                        // Get the download URL of the uploaded image
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // You can use the download URL to retrieve the uploaded image
                                String downloadUrl = uri.toString();
                                Toast.makeText(RegSecActivity.this, "Download URL: " + downloadUrl, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Image upload failed
                        Toast.makeText(RegSecActivity.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
