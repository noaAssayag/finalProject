package com.example.solmatchfinalproject;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView nameView;
    TextView dateView;
    TextView addressView;

    Button btnView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.img);
        nameView=itemView.findViewById(R.id.name);
        dateView=itemView.findViewById(R.id.hostDate);
        btnView=itemView.findViewById(R.id.btn);
        addressView=itemView.findViewById(R.id.hostAddress);
    }
}
