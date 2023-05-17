package ChatClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.solmatchfinalproject.R;

import java.util.List;

public class chatListAdapter extends ArrayAdapter<chatItemInfo> {
    private List<chatItemInfo> dataList = null;
    private Context context = null;

    public chatListAdapter(Context context, List<chatItemInfo> dataList) {
        super(context, R.layout.chat_message_layout);
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

        if(rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            rowView = inflater.inflate(R.layout.chat_message_layout, null, false);
        }
        TextView txtTitle = (TextView) rowView.findViewById(R.id.senderName);
        TextView extratxt = (TextView) rowView.findViewById(R.id.messageContent);
        chatItemInfo itemInfo = dataList.get(position);
        txtTitle.setText(itemInfo.getName());
        extratxt.setText(itemInfo.getMessage());

        return rowView;
    }
}
