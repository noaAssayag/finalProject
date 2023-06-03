package com.example.solmatchfinalproject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.solmatchfinalproject.Hosts.RecycleViewInterface;

import java.util.List;

import Model.Host;
import Model.Professional;

public class ProfessionalAdapter extends RecyclerView.Adapter<ProfessionalAdapter.profViewHolder>{
    private final RecycleViewInterface recycleViewInterface;
    Context context;
    List<Professional> list;

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
        return new ProfessionalAdapter.profViewHolder(parent, recycleViewInterface);
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
        private ImageButton hostBtnImageButton;
        private Professional professional;

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
            hostBtnImageButton = v.findViewById(R.id.hostBtn);

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
            this.professional = professional;
            txtProfNameTextView.setText(professional.getUserName());
            txtProfEmailTextView.setText(professional.getEmail());
            txtPhoneNumTextView.setText(professional.getPhoneNum());
            txtCategoryTextView.setText(professional.getCategory());
            txtHostAddressTextView.setText(professional.getAddress());
            txtAvaTextView.setText(professional.getPrecAvailability());
            Glide.with(context)
                    .load(professional.getImageUrl())
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
        }
    }

}
