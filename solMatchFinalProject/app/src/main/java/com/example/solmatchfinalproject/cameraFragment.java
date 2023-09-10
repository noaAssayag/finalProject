package com.example.solmatchfinalproject;

import static android.app.Activity.RESULT_OK;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class cameraFragment extends DialogFragment {
    private CardView cardGallery;
    private CardView cardCapture;
    private static final int PICK_IMAGE_REQUEST = 100;
    private static final int REQUEST_IMAGE_CAPTURE=11;
    // Use this instance of the interface to deliver action events
    OnImageSelectedListener mListener;
    Uri imageURIProf;

    // Override the Fragment.onAttach() method to instantiate the MyAlertDialogFragmentListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // this.activity = activity;
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the MyAlertDialogFragmentListener so we can send events to the host
            mListener = (OnImageSelectedListener) activity;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_camera_fragment, null);
        // Initialize references to all widgets
        cardGallery = v.findViewById(R.id.card_gallery);
        cardCapture = v.findViewById(R.id.card_capture);

        // Set click listeners if needed
        cardGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle gallery card click
                // Launch image selection from gallery
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);            }
        });

        cardCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch camera capture
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }

            }
        });
        return v;

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the selected image URI
            imageURIProf = data.getData();
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if (imageBitmap != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    imageURIProf = getImageUri(getContext(), imageBitmap);
                }
            }
        }
        mListener.onImageSelected(imageURIProf);
        dismiss();
    }
    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Image", null);
        return Uri.parse(path);
    }

}
