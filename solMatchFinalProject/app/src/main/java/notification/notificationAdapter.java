package notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.solmatchfinalproject.R;
import com.example.solmatchfinalproject.notifications;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import dataBase.DatabaseHelper;

public class notificationAdapter extends RecyclerView.Adapter<notificationAdapter.NotificationViewHolder> {

    private List<notifications> notificationList;
    private Context context;

    public notificationAdapter(List<notifications> notificationList,Context context) {
        this.notificationList = notificationList;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        notifications notification = notificationList.get(position);
        holder.tvNotificationMessage.setText(notification.getMessage());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView tvNotificationMessage;

        NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNotificationMessage = itemView.findViewById(R.id.tvNotificationMessage);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifications notifications = new notifications(FirebaseAuth.getInstance().getUid(),tvNotificationMessage.getText().toString());
                    DatabaseHelper helper = new DatabaseHelper(context);
                    helper.removeNotification(notifications);
                }
            });
        }

    }
}
