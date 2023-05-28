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

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import Model.UserInfo;
import Model.UserStorageData;

public class UserInfoListAdapter extends ArrayAdapter<UserInfo> {
    private List<UserInfo> dataList = null;
    private Context context = null;
    boolean isEditing = true;

    FirebaseDatabase db;
    DatabaseReference ref;
    FirebaseAuth firebaseAuth;

    private boolean valid = true;
    private UserStorageData currentUser;


    public UserInfoListAdapter(Context context, List<UserInfo> dataList) {
        super(context, R.layout.userinfolist, dataList);
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
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
                    extratxt.setAlpha(1f);
                    extratxt.setTextColor(context.getResources().getColor(R.color.black));
                    extratxt.setEnabled(false);
                    btnEdit.setImageResource(R.drawable.baseline_mode_edit_24); // Set the image when editing

                    firebaseAuth = FirebaseAuth.getInstance();
                    currentUser = new UserStorageData();
                    String uid = firebaseAuth.getCurrentUser().getUid();
                    db = FirebaseDatabase.getInstance();
                    ref = db.getReference().child("Users").child(uid);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            currentUser.setUserName(snapshot.child("userName").getValue().toString());
                            currentUser.setEmail(snapshot.child("email").getValue().toString());
                            currentUser.setPassword(snapshot.child("password").getValue().toString());
                            currentUser.setGen(snapshot.child("gen").getValue().toString());
                            currentUser.setType(snapshot.child("type").getValue().toString());
                            currentUser.setBirthday(snapshot.child("birthday").getValue().toString());
                            currentUser.setImage(snapshot.child("image").getValue().toString());
                            switch (userInfo.getTitle()) {
                                case R.string.name:
                                    if (TextUtils.isEmpty(extratxt.getText()) || extratxt.getText().length() < 7 || extratxt.getText().toString().contains(" ")) {
                                        Toast.makeText(context, "INVALID user name", Toast.LENGTH_SHORT).show();
                                        valid = false;
                                    } else {
                                        currentUser.setUserName(extratxt.getText().toString());

                                    }
                                    break;
                                case R.string.email:
                                    if (TextUtils.isEmpty(extratxt.getText()) || !extratxt.getText().toString().contains("@") || !extratxt.getText().toString().matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")) {
                                        Toast.makeText(context, "INVALID Email", Toast.LENGTH_SHORT).show();
                                        valid = false;
                                    } else {
                                        currentUser.setEmail(extratxt.getText().toString());
                                    }
                                    break;
                                case R.string.gender:
                                    if (extratxt.getText().equals("Male") || extratxt.getText().equals("Female") || extratxt.getText().equals("Other")) {
                                        currentUser.setGen(extratxt.getText().toString());
                                        valid = false;
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
                                        valid = false;
                                    } else {
                                        currentUser.setPassword(extratxt.getText().toString());
                                    }
                                    break;
                            }
                            if(valid) {
                                db.getReference().child("Users").child(uid).setValue(currentUser);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }
        });
        return rowView;
    }
}
