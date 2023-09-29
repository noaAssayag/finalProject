package com.example.solmatchfinalproject.ChatClasses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.solmatchfinalproject.EditPersonalDetails;
import com.example.solmatchfinalproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class chatListAdapter extends ArrayAdapter<chatItemInfo> {
    private List<chatItemInfo> dataList = null;

    String name;
    private Context context = null;
    Intent intent = null;

    public chatListAdapter(Context context, List<chatItemInfo> dataList) {
        super(context, R.layout.chat_message_layout);
        this.dataList = dataList;
        this.context = context;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public chatItemInfo getItem(int position) {
        return dataList.get(position);
    }
    @Override
    public View getView(final int position, View rowView, ViewGroup parent) {

        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            rowView = inflater.inflate(R.layout.chat_message_layout, null, false);
        }

        TextView txtTitle = rowView.findViewById(R.id.senderName);
        TextView extratxt = rowView.findViewById(R.id.messageContent);

        final chatItemInfo itemInfo = dataList.get(position);
        txtTitle.setText(itemInfo.getName());
        extratxt.setText(itemInfo.getMessage());

        // Add onClick listener to the rowView
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle item click
                // You can perform actions based on the clicked item
                // For example, you can get the clicked item's data using itemInfo
                name = itemInfo.getName();
                String message = itemInfo.getMessage();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
                        reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot child: snapshot.getChildren())
                        {
                            if(child.child("userName").getValue().toString().equals(name))
                            {
                                intent = new Intent(getContext(), EditPersonalDetails.class);
                                intent.putExtra("UID", child.child("userName").getValue().toString());
                                getContext().startActivity(intent);

                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        return rowView;
    }

}
