package com.example.solmatchfinalproject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import Model.Host;
import Model.Professional;
import Model.Review;
import Model.UserStorageData;
import dataBase.DatabaseHelper;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.reviewViewHolder> {
    Context context;
    List<Review> list;

    public ReviewAdapter(List<Review> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @Override
    public reviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.review_pro, parent, false);
        Log.i("adapter", "ContactViewHolder done!");
        return new ReviewAdapter.reviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.reviewViewHolder holder, int position) {
        Review review = list.get(position);
        holder.setData(review);
        Log.i("adapter", "onBindViewHolder done!" + "position=" + position);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class reviewViewHolder extends RecyclerView.ViewHolder {
        private TextView titlename;
        private TextView txtcomments;
        private TextView txtRating;
        private Review review;

        public reviewViewHolder(View v) {
            super(v);
            titlename = v.findViewById(R.id.name);
            txtcomments = v.findViewById(R.id.comments);
            txtRating = v.findViewById(R.id.rating);

        }

        public void setData(Review review) {
            titlename.setText(review.getNameReview());
            txtcomments.setText(review.getComments());
            txtRating.setText("Rating:"+review.getRate()+"/5 stars");
        }
    }

}
