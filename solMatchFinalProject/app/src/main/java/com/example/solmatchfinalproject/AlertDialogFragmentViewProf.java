package com.example.solmatchfinalproject;


import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.solmatchfinalproject.R;

import Fragment.AlertListenerProf;
import Model.Professional;
import Model.donations;


public class AlertDialogFragmentViewProf extends DialogFragment {
    TextView description;
    Button btnOk;
    AlertListenerProf mListener;
    private Professional professional1;

    // Override the Fragment.onAttach() method to instantiate the MyAlertDialogFragmentListenerEdit
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // this.activity = activity;
        try {
            mListener = (AlertListenerProf) activity;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle b = getArguments();
        View v = inflater.inflate(R.layout.activity_alert_dialog_fragment_view_prof, null);
        description = v.findViewById(R.id.descriptionProfessional);
        btnOk = v.findViewById(R.id.btnOk);

        if (b != null) {
            description.setText(b.getString("Description"));
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDialogPositiveClick(AlertDialogFragmentViewProf.this);
                dismiss();
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}