package com.example.solmatchfinalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class userHostAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context context;
    List<UserHosting> UserHosts;

    public userHostAdapter(Context context, List<UserHosting> UserHosts) {
        this.context = context;
        this.UserHosts = UserHosts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.user_host_view,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nameView.setText(UserHosts.get(position).getName());
        holder.dateView.setText(UserHosts.get(position).getDateHost());
        holder.imageView.setImageResource(UserHosts.get(position).getImg());
    }

    @Override
    public int getItemCount() {
        return UserHosts.size();
    }
}
