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

import com.example.solmatchfinalproject.ChatClasses.chatActivity;
import com.example.solmatchfinalproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserHostAdapter extends RecyclerView.Adapter<UserHostAdapter.UserHostViewHolder> {
    private static List<Host> hostList;
    private final RecycleViewInterface recycleViewInterface;
    Context context;
    String username;
    String userPresented;
    String userToSendMessage;
    String fullName;
    public UserHostAdapter(List<Host> userHostingsList, Context context, RecycleViewInterface recycleViewInterface) {

        this.hostList = userHostingsList;
        this.context=context;
        this.recycleViewInterface = recycleViewInterface;
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
        holder.vBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPresented  = holder.userHosted.substring(0, atIndex);
                username = holder.userHosted.replace("@", "").replace(".", "");

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("email").getValue().toString().replace("@", "").replace(".", "").equals(username))
                        {
                            Toast.makeText(context,"you cant start chatting with yourself", Toast.LENGTH_SHORT);

                        }
                        else{
                            userToSendMessage = snapshot.child("email").getValue().toString().replace("@", "").replace(".", "");
                            DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference().child("chats");
                            chatReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    for(DataSnapshot child: snapshot.getChildren())
                                    {
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

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public UserHostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.user_host_view, viewGroup, false);
        Log.i("adapter", "ContactViewHolder done!");

        return new UserHostViewHolder(itemView, recycleViewInterface);
    }

    public class UserHostViewHolder extends RecyclerView.ViewHolder {
        private ImageView vImg;
        private TextView vName;
        private TextView vEmail;
        private TextView vAddress;
        private TextView vDate;
        private ImageButton vBtn;
        private Host userHosting = null;
        String userHosted;

        public UserHostViewHolder(View v,RecycleViewInterface recycleViewInterface) {
            super(v);
            vName = v.findViewById(R.id.txtHostName);
            vEmail = v.findViewById(R.id.txtHostEmail);
            vAddress = v.findViewById(R.id.txtHostAddress);
            vDate = v.findViewById(R.id.txtHostDate);
            vImg = v.findViewById(R.id.hostImg);
            vBtn = v.findViewById(R.id.hostBtn);
            vBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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
