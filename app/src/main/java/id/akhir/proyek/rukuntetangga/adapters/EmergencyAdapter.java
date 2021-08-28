package id.akhir.proyek.rukuntetangga.adapters;

import android.util.Log;
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
import id.akhir.proyek.rukuntetangga.models.MenuGrid;
import id.akhir.proyek.rukuntetangga.models.Service;

public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.ViewHolder> {

    private List<Service> dataService;
    private final AdapterListener<Service> listener;
    boolean isEmergency;

    public EmergencyAdapter(List<Service> dataService, boolean isEmergency, AdapterListener<Service> listener) {
        this.dataService = dataService;
        this.listener = listener;
        this.isEmergency = isEmergency;
    }

    public void setData(List<Service> dataService) {
        this.dataService = dataService;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new EmergencyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Service service = dataService.get(position);
        if (!isEmergency) {
            holder.ivWhatsApp.setImageResource(R.drawable.ic_whatsapp);
        } else {
            holder.ivWhatsApp.setVisibility(View.GONE);
        }
        if (service.getServiceImage() != null || !service.getServiceImage().isEmpty()) {
            Picasso.get().load(service.getServiceImage())
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.broken_image).into(holder.ivImageService);
        } else {
            holder.ivImageService.setImageResource(R.drawable.image_placeholder);
        }
        holder.tvServiceName.setText(service.getServiceName());
    }

    @Override
    public int getItemCount() {
        return dataService.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivImageService, ivWhatsApp;
        TextView tvServiceName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImageService = itemView.findViewById(R.id.iv_service_icon);
            ivWhatsApp = itemView.findViewById(R.id.iv_whatsApp);
            tvServiceName = itemView.findViewById(R.id.tv_service_name);

            itemView.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemSelected(dataService.get(getBindingAdapterPosition()));
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null)
                    listener.onItemLongSelected(dataService.get(getBindingAdapterPosition()));
                return true;
            });
        }
    }
}
