package com.example.solmatchfinalproject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import Model.UserInfo;

public class UserInfoListAdapter extends ArrayAdapter<UserInfo>{
    private List<UserInfo> dataList=null;
    private Context context=null;

    public UserInfoListAdapter(Context context, List<UserInfo> dataList) {
        super(context, R.layout.userinfolist, dataList);
        this.context = context;
        this.dataList = dataList;
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
    public View getView( int position, View rowView, ViewGroup parent) {

        if(rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            rowView = inflater.inflate(R.layout.userinfolist, null, false);
        }
        TextView txtTitle = (TextView) rowView.findViewById(R.id.userInfoTitle);
        EditText extratxt = (EditText) rowView.findViewById(R.id.userInfoData);
        extratxt.setTextColor(context.getResources().getColor(R.color.black));
        extratxt.setEnabled(false);
        ImageButton btnEdit=(ImageButton)rowView.findViewById(R.id.btnEditDetails);
        btnEdit.setImageResource(R.drawable.baseline_mode_edit_24); // Set the image when editing
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isEditing = true;
                if (isEditing) {
                    extratxt.setAlpha(0.5f); // Example: sets the EditText to 50% transparency
                    extratxt.setTextColor(context.getResources().getColor(R.color.disabledTextColor));
                    extratxt.setEnabled(true);
                    extratxt.setHint(dataList.get(position).getValue());
                    btnEdit.setImageResource(R.drawable.baseline_save_24);

                   // Set the image when not editing
                }
                else {
                    btnEdit.setImageResource(R.drawable.baseline_mode_edit_24); // Set the image when editing
                    dataList.get(position).setValue(extratxt.getText().toString());
                }
                isEditing = false;
                // Toggle the button state
            }
        });
        UserInfo userInfo = dataList.get(position);
        txtTitle.setText(userInfo.getTitle());
        extratxt.setText(userInfo.getValue().toString());
        return rowView;

    };
}

