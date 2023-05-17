package com.customlistviewimagetext;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidinterview.com.customlistviewimagetext.R;

public class MainActivity extends Activity {

	private ListView list;
	private CustomListAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		List<ItemInfo> itemInfos = new ArrayList<ItemInfo>();
		itemInfos.add(new ItemInfo("item1",R.drawable.pic1));
		itemInfos.add(new ItemInfo("item2",R.drawable.pic2));
		itemInfos.add(new ItemInfo("itme 3",R.drawable.pic3));
		itemInfos.add(new ItemInfo("item 4",R.drawable.pic4));
		itemInfos.add(new ItemInfo("item 5",R.drawable.pic5));
		itemInfos.add(new ItemInfo("item 6",R.drawable.pic6));
		itemInfos.add(new ItemInfo("item 7",R.drawable.pic7));
		itemInfos.add(new ItemInfo("item 8",R.drawable.pic8));


		list = (ListView) findViewById(R.id.list);

		adapter = new CustomListAdapter(this, itemInfos);


		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ItemInfo selecteditem = adapter.getItem(position);
				Toast.makeText(getApplicationContext(), selecteditem.getName(),
						Toast.LENGTH_SHORT).show();
			}
		});


		list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				ItemInfo selecteditem = adapter.getItem(position);
				Toast.makeText(getApplicationContext(), "long  " + selecteditem.getName(),
						Toast.LENGTH_SHORT).show();
				return true;
			}
		});
	}
}
