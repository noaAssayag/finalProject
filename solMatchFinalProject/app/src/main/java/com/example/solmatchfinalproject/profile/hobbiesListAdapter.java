package com.example.solmatchfinalproject.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solmatchfinalproject.R;

import java.util.List;

public class hobbiesListAdapter extends RecyclerView.Adapter<hobbiesListAdapter.ViewHolder> {
    private List<String> items;

    public hobbiesListAdapter(List<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.personal_hobbit_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = items.get(position);
        holder.hobbies.setText(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView hobbies;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hobbies = itemView.findViewById(R.id.hobbiesTextView);
        }
    }
}
