package ChatClasses;

import android.content.Context;
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
	public View getView( int position, View rowView, ViewGroup parent) {


		if(rowView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			rowView = inflater.inflate(R.layout.mylist, null, false);
		}
		TextView txtTitle = (TextView) rowView.findViewById(R.id.itemInfo);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		TextView extratxt = (TextView) rowView.findViewById(R.id.extratext);
		chatItemInfo itemInfo = dataList.get(position);
		txtTitle.setText(itemInfo.getName());
		imageView.setImageResource(itemInfo.getImgId());
		extratxt.setText("Description "+ itemInfo.getName());

		return rowView;

	}
}
