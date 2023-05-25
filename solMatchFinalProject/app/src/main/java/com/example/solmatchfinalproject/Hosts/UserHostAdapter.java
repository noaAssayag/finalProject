package com.example.solmatchfinalproject.Hosts;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.solmatchfinalproject.Hosts.Host;
import com.example.solmatchfinalproject.R;

import java.util.List;

public class UserHostAdapter extends RecyclerView.Adapter<UserHostAdapter.UserHostViewHolder> {
    private static List<Host> hostList;

    public UserHostAdapter(List<Host> userHostingsList) {
        this.hostList = userHostingsList;
    }

    @Override
    public int getItemCount() {
        return hostList.size();
    }

    @Override
    public void onBindViewHolder(UserHostViewHolder holder, int position) {
        Host userHosting = hostList.get(position);
        holder.setData(userHosting);
        Log.i("adapter", "onBindViewHolder done!" + "position=" + position);
    }

    @Override
    public UserHostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.user_host_view, viewGroup, false);
        Log.i("adapter", "ContactViewHolder done!");

        return new UserHostViewHolder(itemView);
    }

    public class UserHostViewHolder extends RecyclerView.ViewHolder {
        private ImageView vImg;
        private TextView vName;
        private TextView vEmail;
        private TextView vAddress;
        private TextView vDate;
        private Button vBtn;
        private Host userHosting = null;

        public UserHostViewHolder(View v) {
            super(v);
            vName = v.findViewById(R.id.txtHostName);
            vEmail = v.findViewById(R.id.txtHostEmail);
            vAddress = v.findViewById(R.id.txtHostAddress);
            vDate = v.findViewById(R.id.txtHostDate);
            vImg = v.findViewById(R.id.hostImg);
            vBtn = v.findViewById(R.id.hostBtn);
            vBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hostList.remove(UserHostViewHolder.this.userHosting);
                    UserHostAdapter.this.notifyDataSetChanged();
                }
            });
        }

        public void setData(Host userHosting) {
            this.userHosting = userHosting;
            vName.setText(userHosting.getHostName());
            vEmail.setText(userHosting.getHostEmail());
            vAddress.setText(userHosting.getHostAddress());
            vDate.setText(userHosting.getHostingDate());
            vImg.setImageBitmap(userHosting.getHostImage());
        }
    }
}
