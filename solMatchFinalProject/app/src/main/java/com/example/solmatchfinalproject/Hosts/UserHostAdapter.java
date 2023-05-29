package com.example.solmatchfinalproject.Hosts;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import Model.Host;
import com.example.solmatchfinalproject.R;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserHostAdapter extends RecyclerView.Adapter<UserHostAdapter.UserHostViewHolder> {
    private static List<Host> hostList;
    private final RecycleViewInterface recycleViewInterface;
    Context context;
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
