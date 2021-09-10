package id.akhir.proyek.rukuntetangga.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.models.Notification;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<Notification> dataNotification;
    private final Context context;
    private final AdapterListener<Notification> listener;

    public NotificationAdapter(List<Notification> dataNotification, Context context, AdapterListener<Notification> listener) {
        this.dataNotification = dataNotification;
        this.context = context;
        this.listener = listener;
    }

    public void setData(List<Notification> dataNotification) {
        this.dataNotification = dataNotification;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = dataNotification.get(position);
        notification.setNotification();
        holder.onBind(notification);
    }

    @Override
    public int getItemCount() {
        return dataNotification.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvTitleNotification, tvBodyNotification;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view_notification);
            tvTitleNotification = itemView.findViewById(R.id.tv_title_notification);
            tvBodyNotification = itemView.findViewById(R.id.tv_body_notification);

            itemView.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemSelected(dataNotification.get(getBindingAdapterPosition()));
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null)
                    listener.onItemLongSelected(dataNotification.get(getBindingAdapterPosition()));
                return true;
            });
        }

        public void onBind(Notification notification) {
            tvTitleNotification.setText(notification.getTitle());
            tvBodyNotification.setText(notification.getBody());
            if (!notification.isRead()) {
                cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorGray));
            } else
                cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        }
    }
}
