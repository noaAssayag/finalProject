package donations;



import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.example.solmatchfinalproject.Hosts.UserHostAdapter;
import com.example.solmatchfinalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import Model.UserStorageData;
import Model.donations;
import dataBase.DatabaseHelper;

public class donationAdapter extends RecyclerView.Adapter<donationAdapter.donationViewHolder> {
    private List<donations> donationsList;
    Context context;

    String userToSendMessage;

    String username;
    String userPresented;

    String fullName;
    private final RecycleViewInterface recycleViewInterface;
    private boolean view=false;

    // Return the size of your dataset (invoked by the layout manager)
    public donationAdapter(List<donations> donationsList,Context context,RecycleViewInterface recycleViewInterface,boolean view)
    {
        this.donationsList = donationsList;
        this.context = context;
        this.recycleViewInterface=recycleViewInterface;
        this.view=view;

    }

    @Override
    public int getItemCount() {
        return donationsList.size();
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(donationViewHolder contactViewHolder, int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        donations ci = donationsList.get(position);
        contactViewHolder.setData(ci);

        Log.i("adapter", "onBindViewHolder done!" + "position="+position);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public donationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // Create a new view, which defines the UI of the list item
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.donation_item, viewGroup, false);
        Log.i("adapter", "ContactViewHolder done!");
        return new donationViewHolder(itemView,recycleViewInterface,isView());
    }

    public boolean isView() {
        return view;
    }

    public void setView(boolean view) {
        this.view = view;
    }


    public class donationViewHolder extends RecyclerView.ViewHolder {

        private ImageView donationImage;
        private TextView donator;
        private TextView location;
        private TextView catagory;
        private TextView desc;
        private donations di = null;
        private ImageButton startchat,removeDon;

        String userDonated;

        public donationViewHolder(@NonNull View rowView, RecycleViewInterface recycleViewInterface,boolean view) {
            super(rowView);
            donationImage = rowView.findViewById(R.id.imgdonation);
            donator = rowView.findViewById(R.id.Donatorname);
            location = rowView.findViewById(R.id.donationLocation);
            catagory = rowView.findViewById(R.id.donationCatagory);
            desc = rowView.findViewById(R.id.donationDescription);
            startchat = rowView.findViewById(R.id.btnStartChat);
            removeDon=rowView.findViewById(R.id.removeDon);

            if(!view) {
                removeDon.setVisibility(View.VISIBLE);
                removeDon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            int position=getAdapterPosition();
                            if(position!=RecyclerView.NO_POSITION)
                            {
                                recycleViewInterface.deleteItem(position);
                                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                                if(databaseHelper.removeDonationById(FirebaseAuth.getInstance().getUid()))
                                {
                                    Toast.makeText(context,"host deleted succesfully", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(context,"Error deleting host", Toast.LENGTH_LONG).show();
                                }

                            }

                    }
                });
            }
            else{
                removeDon.setVisibility(View.GONE);
            }

            startchat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int atIndex = userDonated.indexOf("@");

// Extract the substring before the "@" symbol
                    userPresented  = userDonated.substring(0, atIndex);
                    username = userDonated.replace("@", "").replace(".", "");

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
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recycleViewInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            recycleViewInterface.onDonationClick(position);
                        }


                    }
                }
            });
            removeDon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                    firestore.collection("Donations").document(FirebaseAuth.getInstance().getUid()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            DatabaseHelper databaseHelper = new DatabaseHelper(context);
                            databaseHelper.removeDonationById(FirebaseAuth.getInstance().getUid());
                        }
                    });
                }
            });
        }


        public void setData(donations di) {

            this.di = di;
            userDonated = di.getEmail();
            Glide.with(context)
                    .load(di.getImg())
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
                    .into(donationImage);

            donator.setText(di.getName());
            location.setText(di.getAdress());
            catagory.setText(di.getCatagory());
            desc.setText(di.getDescription());
        }

    }
}
