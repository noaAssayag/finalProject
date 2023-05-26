package com.example.solmatchfinalproject;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class personalQuestionsAdapter extends RecyclerView.Adapter<personalQuestionsAdapter.personalViewHolder> {

    private List<String> hobbiesList;
    private List<String> selectedItemsList;
    private Context context;

    public personalQuestionsAdapter(List<String> hobbiesList, Context context) {
        this.hobbiesList = hobbiesList;
        this.context = context;
        selectedItemsList = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return hobbiesList.size();
    }

    @NonNull
    @Override
    public personalQuestionsAdapter.personalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.personal_hobbit_item, parent, false);
        return new personalQuestionsAdapter.personalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final personalQuestionsAdapter.personalViewHolder holder, final int position) {
        final String s1 = hobbiesList.get(position);
        holder.setData(s1);

        // Set item click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle item selection
                if (selectedItemsList.contains(s1)) {
                    selectedItemsList.remove(s1);
                    holder.itemView.setSelected(false);
                } else {
                    selectedItemsList.add(s1);
                    holder.itemView.setSelected(true);
                }
                updateItemBackground(holder.itemView, selectedItemsList.contains(s1));
            }


        });
        updateItemBackground(holder.itemView, selectedItemsList.contains(s1));

        // Set item selection state
        if (selectedItemsList.contains(s1)) {
            holder.itemView.setSelected(true);
        } else {
            holder.itemView.setSelected(false);
        }

    }

    public List<String> getSelectedItems() {
        return selectedItemsList;
    }
    public class personalViewHolder extends RecyclerView.ViewHolder {
        TextView item;

        public personalViewHolder(@NonNull View rowView) {
            super(rowView);
            item = rowView.findViewById(R.id.hobbiesTextView);
        }

        public void setData(String hobbieItem) {
            item.setText(hobbieItem);
        }
    }
    private void updateItemBackground(View itemView, boolean isSelected) {
        if (isSelected) {
            itemView.setBackgroundColor(Color.YELLOW);
        } else {
        itemView.setBackgroundColor(0);
        }
    }
}
