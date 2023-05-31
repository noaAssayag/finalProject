package com.example.solmatchfinalproject.ChatClasses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.solmatchfinalproject.R;

import java.util.List;

public class chatMenueListAdapter extends ArrayAdapter<chatItemInfo> {

	private List<chatItemInfo> dataList = null;
	private Context context = null;
	public chatMenueListAdapter(Context context, List<chatItemInfo> dataList) {
		super(context, R.layout.mylist);
		this.dataList = dataList;
		this.context = context;
	}




	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public chatItemInfo getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public View getView(int position, View rowView, ViewGroup parent) {
		if (rowView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			rowView = inflater.inflate(R.layout.mylist, null, false);
		}

		TextView txtTitle = rowView.findViewById(R.id.itemInfo);
		ImageView imageView = rowView.findViewById(R.id.icon);
		TextView extratxt = rowView.findViewById(R.id.extratext);

		chatItemInfo itemInfo = dataList.get(position);
		txtTitle.setText(itemInfo.getName());
		imageView.setImageResource(itemInfo.getImgId());
		extratxt.setText("Description " + itemInfo.getName());

		// Set a click listener for the list item view
		rowView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Pass the necessary information back to the activity
				String fromName = itemInfo.getName();
				String chatName = ((chatMenuActivity) context).chatNameFinder(fromName);

				Intent intent = new Intent(context, chatActivity.class);
				intent.putExtra("chatID", chatName);
				intent.putExtra("to", fromName);
				intent.putExtra("from", ((chatMenuActivity) context).getUserName());
				intent.putExtra("userToPresent", fromName);
				context.startActivity(intent);
			}
		});

		return rowView;
	}

}
