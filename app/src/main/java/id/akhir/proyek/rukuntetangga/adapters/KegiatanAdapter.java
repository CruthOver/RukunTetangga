package id.akhir.proyek.rukuntetangga.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.listener.MenuListener;
import id.akhir.proyek.rukuntetangga.models.Activities;

public class KegiatanAdapter extends RecyclerView.Adapter<KegiatanAdapter.ViewHolder> {

    private List<Activities> dataActivities;
    private final AdapterListener<Activities> listener;
    private final MenuListener<Activities> menuListener;
    private final Context context;
    private final boolean isAdmin;

    public KegiatanAdapter(boolean isAdmin, List<Activities> dataActivities, Context context, AdapterListener<Activities> listener, MenuListener<Activities> menuListener) {
        this.dataActivities = dataActivities;
        this.isAdmin = isAdmin;
        this.context = context;
        this.listener = listener;
        this.menuListener = menuListener;
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
        if (!isAdmin) {
            holder.btnOption.setVisibility(View.GONE);
        } else {
            holder.btnOption.setVisibility(View.VISIBLE);
        }
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
        Button btnOption;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvActivityDate = itemView.findViewById(R.id.tv_date_activity);
            tvActivityTime = itemView.findViewById(R.id.tv_time_activity);
            tvActivityName = itemView.findViewById(R.id.tv_name_activity);
            ivActivity = itemView.findViewById(R.id.iv_activity);
            btnOption = itemView.findViewById(R.id.menu_option);

            btnOption.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(context, btnOption);
                popupMenu.inflate(R.menu.menu_list);
                popupMenu.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.edit) {
                        if (menuListener != null)
                            menuListener.onEdit(dataActivities.get(getBindingAdapterPosition()));
                        return true;
                    } else if (item.getItemId() == R.id.delete) {
                        if (menuListener != null)
                            menuListener.onDelete(dataActivities.get(getBindingAdapterPosition()));
                        return true;
                    }

                    return false;
                });
                popupMenu.show();
            });

            itemView.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemSelected(dataActivities.get(getBindingAdapterPosition()));
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null)
                    listener.onItemLongSelected(dataActivities.get(getBindingAdapterPosition()));
                return true;
            });
        }
    }
}