package com.customlistviewimagetext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidinterview.com.customlistviewimagetext.R;

public class CustomListAdapter extends ArrayAdapter<ItemInfo> {

	private List<ItemInfo> dataList = null;
	private Context context = null;
	public CustomListAdapter(Context context, List<ItemInfo> dataList) {
		super(context, R.layout.mylist);
		this.dataList = dataList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public ItemInfo getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public View getView( int position, View rowView, ViewGroup parent) {

		if(rowView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			rowView = inflater.inflate(R.layout.mylist, null, false);
		}
		TextView txtTitle = (TextView) rowView.findViewById(R.id.itemInfo);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		TextView extratxt = (TextView) rowView.findViewById(R.id.extratext);
		ItemInfo itemInfo = dataList.get(position);
		txtTitle.setText(itemInfo.getName());
		imageView.setImageResource(itemInfo.getImgId());
		extratxt.setText("Description "+ itemInfo.getName());

		return rowView;

	};
}
