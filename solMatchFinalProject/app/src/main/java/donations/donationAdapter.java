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
import com.example.solmatchfinalproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import Model.donations;

public class donationAdapter extends RecyclerView.Adapter<donationAdapter.donationViewHolder> {
    private List<donations> donationsList;
    Context context;

    String userToSendMessage;

    String username;

    String fullName;
    // Return the size of your dataset (invoked by the layout manager)
    public donationAdapter(List<donations> donationsList,Context context)
    {
        this.donationsList = donationsList;
        this.context = context;

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
        username = contactViewHolder.userDonated;
        Log.i("adapter", "onBindViewHolder done!" + "position="+position);
        contactViewHolder.startchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                      if(snapshot.child("userName").getValue().toString().equals(username))
                      {
                          Toast.makeText(context,"you cant start chatting with yourself", Toast.LENGTH_SHORT);

                      }
                      else{
                         userToSendMessage = snapshot.child("userName").getValue().toString();
                          DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference().child("chats");
                          chatReference.addValueEventListener(new ValueEventListener() {
                              @Override
                              public void onDataChange(@NonNull DataSnapshot snapshot) {

                                  for(DataSnapshot child: snapshot.getChildren())
                                  {
                                      fullName = child.getKey();
                                      String[] parts = fullName.split("-");
                                      String user1 = parts[0].trim();
                                      String user2 = parts[1].trim();
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
                                  chatReference.child(username+"-"+userToSendMessage).setValue(null);
                                  Intent intent = new Intent(context, chatActivity.class);
                                  intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                  intent.putExtra("chatID", username+"-"+userToSendMessage);
                                  intent.putExtra("from", userToSendMessage);
                                  intent.putExtra("to",username);
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

    // Create new views (invoked by the layout manager)
    @Override
    public donationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // Create a new view, which defines the UI of the list item
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.donation_item, viewGroup, false);
        Log.i("adapter", "ContactViewHolder done!");

        return new donationViewHolder(itemView);
    }




    public class donationViewHolder extends RecyclerView.ViewHolder{

        private  ImageView donationImage;
        private TextView donator;
        private TextView location;
        private TextView catagory;
        private TextView desc;
        private donations di = null;
        private Button startchat;

        String userDonated;

        public donationViewHolder(@NonNull View rowView) {
            super(rowView);
            donationImage = rowView.findViewById(R.id.imgDonation);
            donator = rowView.findViewById(R.id.Donatorname);
            location = rowView.findViewById(R.id.donationLocation);
            catagory = rowView.findViewById(R.id.donationCatagory);
            desc = rowView.findViewById(R.id.donationDescription);
            startchat = rowView.findViewById(R.id.btnStartChat);


        }

        public String getUserDonated() {
            return userDonated;
        }

        public void setData(donations di)
        {

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
