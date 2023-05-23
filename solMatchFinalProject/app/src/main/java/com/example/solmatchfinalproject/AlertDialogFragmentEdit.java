package com.example.solmatchfinalproject;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Model.UserStorageData;
import dataBase.MyInfoManager;

public class AlertDialogFragmentEdit extends DialogFragment {
    private TextView titleField;
    private TextView lastValField;
    private EditText newValField;
    private EditText reNewValField;
    private Button saveBtn;
    private Button cancelBtn;
    // Use this instance of the interface to deliver action events
    MyAlertDialogFragmentListenerEdit mListener;
    private MyInfoManager myInfoManager = MyInfoManager.getInstance();
    private UserStorageData user;

    // Override the Fragment.onAttach() method to instantiate the MyAlertDialogFragmentListenerEdit
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // this.activity = activity;
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the MyAlertDialogFragmentListener so we can send events to the host
            mListener = (MyAlertDialogFragmentListenerEdit) activity;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle b = getArguments();
        View v = inflater.inflate(R.layout.activity_alert_dialog_fragment_edit, null);
        newValField = (EditText) v.findViewById(R.id.editTextPass);
        reNewValField = (EditText) v.findViewById(R.id.editTextRePass);
        saveBtn = (Button) v.findViewById(R.id.btnSubmitResetPass);
        cancelBtn = (Button) v.findViewById(R.id.btnCanc);
        if (b != null) {
            String userEmail = b.getString("Email");
            user = myInfoManager.readUserByEmail(userEmail);
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newValField != null && !(newValField.getText().toString().isEmpty())) {
                    if (reNewValField != null && !(reNewValField.getText().toString().isEmpty())) {
                        String password = newValField.getText().toString();
                        String rePassword = reNewValField.getText().toString();
                        if (password.equals(rePassword)) {
                            if(!password.equals(user.getPassword())) {
                                user.setPassword(password);
                                myInfoManager.updateUser(user);
                                mListener.onDialogPositiveClick(AlertDialogFragmentEdit.this);
                            }else {
                                Toast.makeText((Activity) mListener, "The password has been used before", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } else {
                            Toast.makeText((Activity) mListener, "The passwords dont match!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        Toast.makeText((Activity) mListener, "Please fill all the fileds", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText((Activity) mListener, "Please fill all the fileds", Toast.LENGTH_SHORT).show();
                    return;
                }
                mListener.onDialogPositiveClick(AlertDialogFragmentEdit.this);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDialogNegativeClick(AlertDialogFragmentEdit.this);
                dismiss();
            }
        });
        return v;

    }


}