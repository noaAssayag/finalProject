package com.example.solmatchfinalproject.Hosts;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import Model.Host;
import dataBase.DatabaseHelper;

import com.example.solmatchfinalproject.ChatClasses.chatActivity;
import com.example.solmatchfinalproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class UserHostAdapter extends RecyclerView.Adapter<UserHostAdapter.UserHostViewHolder> {
    private static List<Host> hostList;
    private final RecycleViewInterface recycleViewInterface;
    static Context context;
    private boolean view=false;
    String username;
    String userPresented;
    String userToSendMessage;
    String fullName;

    FirebaseAuth auth;
    DatabaseHelper sqlDataBase;
    public UserHostAdapter(List<Host> userHostingsList, Context context, RecycleViewInterface recycleViewInterface,boolean view) {
        this.hostList = userHostingsList;
        this.context=context;
        this.recycleViewInterface = recycleViewInterface;
        this.view=view;
        sqlDataBase = new DatabaseHelper(context);
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public int getItemCount() {
        return hostList.size();
    }

    @Override
    public void onBindViewHolder(UserHostViewHolder holder, int position) {
        Host userHosting = hostList.get(position);
        holder.setData(userHosting);
        Log.i("adapter", "onBindViewHolder done!" + "position=" + position);
        int atIndex = holder.userHosted.indexOf("@");

// Extract the substring before the "@" symbol

        Log.i("adapter", "onBindViewHolder done!" + "position="+position);

    }

    @Override
    public UserHostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.user_host_view, viewGroup, false);
        Log.i("adapter", "ContactViewHolder done!");

        return new UserHostViewHolder(itemView, recycleViewInterface,isView());
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public static class UserHostViewHolder extends RecyclerView.ViewHolder {
        private ImageView vImg;
        private TextView vName;
        private TextView vEmail;
        private TextView vAddress;
        private TextView vDate;
        private ImageButton vBtn,vRemove;
        private Host userHosting = null;
        String userHosted;

        public UserHostViewHolder(View v,RecycleViewInterface recycleViewInterface,boolean view) {
            super(v);
            vName = v.findViewById(R.id.txtHostName);
            vEmail = v.findViewById(R.id.txtHostEmail);
            vAddress = v.findViewById(R.id.txtHostAddress);
            vDate = v.findViewById(R.id.txtHostDate);
            vImg = v.findViewById(R.id.hostImg);
            vBtn = v.findViewById(R.id.hostBtn);
            vRemove=v.findViewById(R.id.removeHost);
            if(!view)
            {
                vRemove.setVisibility(View.VISIBLE);
                vRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(recycleViewInterface!=null)
                        {
                            int position=getAdapterPosition();
                            if(position!=RecyclerView.NO_POSITION)
                            {
                                recycleViewInterface.deleteItem(position);
                                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                                if(databaseHelper.removeHostById(FirebaseAuth.getInstance().getUid()))
                                {
                                    Toast.makeText(context,"host deleted succesfully", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(context,"Error deleting host", Toast.LENGTH_LONG).show();
                                }

                            }
                        }
                    }
                });
            }
            else{
                vRemove.setVisibility(View.GONE);
            }
            vBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String username;
                    String userPresented;
                    String userToSendMessage;

                    int atIndex = userHosted.indexOf("@");
                    userPresented  = userHosted.substring(0, atIndex);
                    DatabaseHelper sqlDataBase = new DatabaseHelper(context);
                    username = userHosted.replace("@", "").replace(".", "");
                    userToSendMessage = sqlDataBase.getUserByUID(FirebaseAuth.getInstance().getCurrentUser().getUid()).getEmail().replace("@", "").replace(".", "");
                    if(username.equals(userToSendMessage))
                    {
                        Toast.makeText(context,"you cant start chatting with yourself", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference().child("chats");
                        chatReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for(DataSnapshot child: snapshot.getChildren())
                                {
                                    String fullName;
                                    fullName = child.getKey();
                                    String[] parts = fullName.split("-");
                                    String user1 = parts[0].trim().replace("@", "").replace(".", "");
                                    String user2 = parts[1].trim().replace("@", "").replace(".", "");
                                    if(user1.equals(userToSendMessage) && user2.equals(username) || user1.equals(username) && user2.equals(userToSendMessage))
                                    {
                                        Intent intent = new Intent(context, chatActivity.class);
                                        intent.putExtra("chatID", fullName);
                                        intent.putExtra("from", userToSendMessage);
                                        intent.putExtra("to",username);
                                        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                        return;
                                    }
                                }
                                chatReference.child(username.replace("@", "").replace(".", "")+"-"+userToSendMessage.replace("@", "").replace(".", "")).setValue(null);
                                Intent intent = new Intent(context, chatActivity.class);
                                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("chatID", username+"-"+userToSendMessage);
                                intent.putExtra("from", userToSendMessage);
                                intent.putExtra("to",username);
                                intent.putExtra("userToPresent",userPresented);
                                context.startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }


                }
            });
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recycleViewInterface!=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            recycleViewInterface.onItemClick(position);
                        }


                    }
                }
            });
        }

        public void setData(Host userHosting) {
            this.userHosting = userHosting;
            userHosted = userHosting.getHostEmail();
            vName.setText(userHosting.getHostName());
            vEmail.setText(userHosting.getHostEmail());
            vAddress.setText(userHosting.getHostAddress());
            vDate.setText(userHosting.getHostingDate());
            Glide.with(context)
                    .load(userHosting.getHostImg())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            // Handle image loading failure
                            Log.e("Glide", "Image loading failed: " + e.getMessage());
                            return false; // Return false to allow Glide to handle the error and show any error placeholder you have set
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            // Image successfully loaded
                            return false; // Return false to allow Glide to handle the resource and display it
                        }
                    })
                    .into(vImg);
        }
    }
}
