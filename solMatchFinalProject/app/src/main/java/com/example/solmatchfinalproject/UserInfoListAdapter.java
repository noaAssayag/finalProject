package com.example.solmatchfinalproject;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import Model.UserInfo;
import Model.UserStorageData;
import dataBase.MyInfoManager;

public class UserInfoListAdapter extends ArrayAdapter<UserInfo> {
    private List<UserInfo> dataList = null;
    private Context context = null;
    private String email;
    boolean isEditing = true;


    public UserInfoListAdapter(Context context, List<UserInfo> dataList, String email) {
        super(context, R.layout.userinfolist, dataList);
        this.context = context;
        this.dataList = dataList;
        this.email = email;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public UserInfo getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            rowView = inflater.inflate(R.layout.userinfolist, null, false);
        }
        TextView txtTitle = (TextView) rowView.findViewById(R.id.userInfoTitle);
        EditText extratxt = (EditText) rowView.findViewById(R.id.userInfoData);
        extratxt.setTextColor(context.getResources().getColor(R.color.black));
        extratxt.setEnabled(false);
        ImageButton btnEdit = (ImageButton) rowView.findViewById(R.id.btnEditDetails);
        btnEdit.setImageResource(R.drawable.baseline_mode_edit_24); // Set the image when editing

        UserInfo userInfo = dataList.get(position);
        txtTitle.setText(userInfo.getTitle());
        extratxt.setText(userInfo.getValue());
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditing) {
                    extratxt.setAlpha(0.5f); // Example: sets the EditText to 50% transparency
                    extratxt.setTextColor(context.getResources().getColor(R.color.disabledTextColor));
                    extratxt.setEnabled(true);
                    extratxt.setHint(dataList.get(position).getValue());
                    btnEdit.setImageResource(R.drawable.baseline_save_24);
                    isEditing=false;
                    // Set the image when not editing
                } else {
                    isEditing=true;
                    extratxt.setTextColor(context.getResources().getColor(R.color.black));
                    extratxt.setEnabled(false);
                    btnEdit.setImageResource(R.drawable.baseline_mode_edit_24); // Set the image when editing
                    UserInfo userInfo = dataList.get(position);
                    UserStorageData currentUser = MyInfoManager.getInstance().readUserByEmail(email);

                    switch (userInfo.getTitle()) {
                        case R.string.name:
                            if (TextUtils.isEmpty(extratxt.getText()) || extratxt.getText().length() < 7 || extratxt.getText().toString().contains(" ")) {
                                Toast.makeText(context, "INVALID user name", Toast.LENGTH_SHORT).show();
                            } else {
                                currentUser.setUserName(extratxt.getText().toString());
                            }
                            break;
                        case R.string.email:
                            if (TextUtils.isEmpty(extratxt.getText()) || !extratxt.getText().toString().contains("@") || !extratxt.getText().toString().matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")) {
                                Toast.makeText(context, "INVALID Email", Toast.LENGTH_SHORT).show();
                            } else {
                                currentUser.setEmail(extratxt.getText().toString());
                            }
                            break;
                        case R.string.gender:
                            if (extratxt.getText().equals("Male") || extratxt.getText().equals("Female") || extratxt.getText().equals("Other")) {
                                currentUser.setGen(extratxt.getText().toString());
                            } else {
                                Toast.makeText(context, "INVALID Gen", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case R.string.birthdate:
                            extratxt.setEnabled(true);
                            break;
                        case R.string.password:
                            if (TextUtils.isEmpty(extratxt.getText()) || extratxt.getText().length() < 7) {
                                Toast.makeText(context, "INVALID Password", Toast.LENGTH_SHORT).show();
                            } else {
                                currentUser.setPassword(extratxt.getText().toString());
                            }
                            break;
                    }
                    MyInfoManager.getInstance().updateUser(currentUser);
                }
            }
        });
        return rowView;
    }
}
