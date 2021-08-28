package id.akhir.proyek.rukuntetangga.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.listener.MenuListener;
import id.akhir.proyek.rukuntetangga.models.Information;

public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.ViewHolder> {
    private List<Information> dataInformation;
    private final Context context;
    private final MenuListener<Information> menuListener;

    public InformationAdapter(List<Information> dataInformation, Context context,
                              MenuListener<Information> menuListener) {
        this.dataInformation = dataInformation;
        this.context = context;
        this.menuListener = menuListener;
    }

    public void setData(List<Information> dataInformation) {
        this.dataInformation = dataInformation;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InformationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_information, parent, false);
        return new InformationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InformationAdapter.ViewHolder holder, int position) {
        holder.tvInformation.setText(dataInformation.get(position).getInformation());
    }

    @Override
    public int getItemCount() {
        return dataInformation.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvInformation;
        Button tvOptionMenu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvInformation = itemView.findViewById(R.id.tvInformation);
            tvOptionMenu = itemView.findViewById(R.id.menu_option);

            tvOptionMenu.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(context, tvOptionMenu);
                popupMenu.inflate(R.menu.menu_list);
                popupMenu.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.edit) {
                        if (menuListener != null)
                            menuListener.onEdit(dataInformation.get(getBindingAdapterPosition()));
                        return true;
                    } else if (item.getItemId() == R.id.delete) {
                        if (menuListener != null)
                            menuListener.onDelete(dataInformation.get(getBindingAdapterPosition()));
                        return true;
                    }

                    return false;
                });
                popupMenu.show();
            });
        }
    }
}
