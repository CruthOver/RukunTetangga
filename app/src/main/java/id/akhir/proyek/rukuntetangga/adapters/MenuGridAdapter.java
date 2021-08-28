package id.akhir.proyek.rukuntetangga.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.models.MenuGrid;

public class MenuGridAdapter extends RecyclerView.Adapter<MenuGridAdapter.GridViewHolder> {

    private List<MenuGrid> dataMenuGrid;
    private AdapterListener<MenuGrid> listener;
    private Context context;

    public MenuGridAdapter(Context context, List<MenuGrid> dataMenuGrid, AdapterListener<MenuGrid> listener) {
        this.dataMenuGrid = dataMenuGrid;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridViewHolder holder, int position) {
        holder.imgIconMenu.setImageResource(dataMenuGrid.get(position).getImageIcon());
        if (dataMenuGrid.get(position).getTitle().equals(context.getString(R.string.title_menu_musrenbang))) {
            holder.titleMenu.setMaxLines(1);
        }
        holder.titleMenu.setText(dataMenuGrid.get(position).getTitle());
    }

    public void setData(List<MenuGrid> dataMenuGrid) {
        this.dataMenuGrid = dataMenuGrid;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataMenuGrid.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIconMenu;
        TextView titleMenu;
        public GridViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIconMenu = itemView.findViewById(R.id.icon_menu);
            titleMenu = itemView.findViewById(R.id.title_menu);

            itemView.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemSelected(dataMenuGrid.get(getBindingAdapterPosition()));
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null)
                    listener.onItemLongSelected(dataMenuGrid.get(getBindingAdapterPosition()));
                return true;
            });
        }
    }
}
