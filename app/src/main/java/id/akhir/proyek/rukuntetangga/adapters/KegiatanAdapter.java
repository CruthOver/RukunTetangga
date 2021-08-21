package id.akhir.proyek.rukuntetangga.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.models.Activities;

public class KegiatanAdapter extends RecyclerView.Adapter<KegiatanAdapter.ViewHolder> {

    private List<Activities> dataActivities;
    private final AdapterListener<Activities> listener;

    public KegiatanAdapter(List<Activities> dataActivities, AdapterListener<Activities> listener) {
        this.dataActivities = dataActivities;
        this.listener = listener;
    }

    public void setData(List<Activities> dataActivities) {
        this.dataActivities = dataActivities;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public KegiatanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);
        return new KegiatanAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KegiatanAdapter.ViewHolder holder, int position) {
        Activities activities = dataActivities.get(position);
        holder.tvActivityName.setText(activities.getTitleActivity());
        holder.tvActivityDate.setText(activities.getDateActivity());
        holder.tvActivityTime.setText(activities.getHour()+" WIB");
        Picasso.get().load(activities.getImageActivity())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.broken_image).into(holder.ivActivity);

    }

    @Override
    public int getItemCount() {
        return dataActivities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvActivityName, tvActivityDate, tvActivityTime;
        ImageView ivActivity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvActivityDate = itemView.findViewById(R.id.tv_date_activity);
            tvActivityTime = itemView.findViewById(R.id.tv_time_activity);
            tvActivityName = itemView.findViewById(R.id.tv_name_activity);
            ivActivity = itemView.findViewById(R.id.iv_activity);

//            btnStatusComplaint.setOnClickListener(v -> {
//                if (listener != null)
//                    listener.onItemSelected(dataComplaint.get(getBindingAdapterPosition()));
//            });
//
//            btnStatusComplaint.setOnLongClickListener(v -> {
//                if (listener != null)
//                    listener.onItemLongSelected(dataComplaint.get(getBindingAdapterPosition()));
//                return true;
//            });
        }
    }
}