package com.example.solmatchfinalproject;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.solmatchfinalproject.ChatClasses.chatActivity;
import com.example.solmatchfinalproject.Hosts.RecycleViewInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import Model.Host;
import Model.Professional;
import Model.Review;
import Model.UserStorageData;
import dataBase.DatabaseHelper;

public class ProfessionalAdapter extends RecyclerView.Adapter<ProfessionalAdapter.profViewHolder>{
    private final RecycleViewInterface recycleViewInterface;
    Context context;
    List<Professional> list;

    String userToSendMessage;

    String username;
    String userPresented;

    String fullName;

    public ProfessionalAdapter( List<Professional> list,Context context,RecycleViewInterface recycleViewInterface ) {
        this.recycleViewInterface = recycleViewInterface;
        this.context = context;
        this.list = list;
    }

    @Override
    public profViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.professional_view, parent, false);
        Log.i("adapter", "ContactViewHolder done!");
        return new ProfessionalAdapter.profViewHolder(itemView, recycleViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfessionalAdapter.profViewHolder holder, int position) {
        Professional professional = list.get(position);
        holder.setData(professional);
        Log.i("adapter", "onBindViewHolder done!" + "position=" + position);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class profViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private ImageView profImgImageView;
        private TextView txtProfNameTextView;
        private TextView txtProfEmailTextView;
        private TextView txtPhoneNumTextView;
        private TextView txtCategoryTextView;
        private TextView txtHostAddressTextView;
        private TextView txtAvaTextView;
        TextView txtReview;
        private ImageButton hostBtnImageButton;
        private Button addReview;
        private Professional professional;
        String proffesionalEmail;

        public profViewHolder(View v,RecycleViewInterface recycleViewInterface) {
            super(v);
            titleTextView = v.findViewById(R.id.title);
            profImgImageView = v.findViewById(R.id.profImg);
            txtProfNameTextView = v.findViewById(R.id.txtProfName);
            txtProfEmailTextView = v.findViewById(R.id.txtProfEmail);
            txtPhoneNumTextView = v.findViewById(R.id.txtPhoneNum);
            txtCategoryTextView = v.findViewById(R.id.txtCategory);
            txtHostAddressTextView = v.findViewById(R.id.txtHostAddress);
            txtAvaTextView = v.findViewById(R.id.txtAva);
            txtReview=v.findViewById(R.id.txtReview);
            hostBtnImageButton = v.findViewById(R.id.hostBtn);
            addReview=v.findViewById(R.id.review);
            //todo add chat

            addReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recycleViewInterface!=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            recycleViewInterface.AddComments(position);
                        }


                    }

                }
            });
            hostBtnImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int atIndex = proffesionalEmail.indexOf("@");
                    userPresented  = proffesionalEmail.substring(0, atIndex);
                    username = proffesionalEmail.replace("@", "").replace(".", "");

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());

                    DatabaseHelper helper = new DatabaseHelper(context);
                    UserStorageData user = helper.getUserByUID(FirebaseAuth.getInstance().getUid());
                    if(user.getEmail().replace("@", "").replace(".", "").equals(username))
                    {
                        Toast.makeText(context,"you cant start chatting with yourself", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        userToSendMessage = user.getEmail().replace("@", "").replace(".", "");
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
                                        intent.putExtra("userToPresent",username);
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
                                intent.putExtra("userToPresent",username);
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

        public void setData(Professional professional) {
            float rate=0;
            this.professional = professional;
            proffesionalEmail = professional.getEmail();
            DatabaseHelper helper = new DatabaseHelper(context);
            UserStorageData user = helper.getUserByUID(professional.getUID());
            txtProfNameTextView.setText(professional.getUserName());
            txtProfEmailTextView.setText(professional.getEmail());
            txtPhoneNumTextView.setText(professional.getPhoneNum());
            txtCategoryTextView.setText(professional.getCategory());
            txtHostAddressTextView.setText(professional.getAddress());
            txtAvaTextView.setText(professional.getPrecAvailability()+"% Availability");
            Glide.with(context)
                    .load(user.getImage())
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
                    .into(profImgImageView);
            if(professional.getReviews()!=null&&!professional.getReviews().isEmpty()) {
                for (Review review : professional.getReviews()) {
                    rate += review.getRate();
                }
                rate/=(professional.getReviews().size());
                txtReview.setText("Rating"+rate+"/5 Stars");
            }
            else{
                txtReview.setText("Nobody add Reviews");
            }

        }
    }

}
