package com.example.solmatchfinalproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {
    Context context;
    String[] titles;
    int[] images;
    LayoutInflater inflater;

    public GridAdapter(Context context, String[] titles, int[] image) {
        this.context = context;
        this.titles = titles;
        this.images = image;
    }


    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null) {
            view = inflater.inflate(R.layout.grid_item, null);

        }
        ImageView img = view.findViewById(R.id.img);
        TextView title = view.findViewById(R.id.title);
        img.setImageResource(images[position]);
        title.setText(titles[position]);
        return view;

    }
}
