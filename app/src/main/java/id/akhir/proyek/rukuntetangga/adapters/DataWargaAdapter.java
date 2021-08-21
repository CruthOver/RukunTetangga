package id.akhir.proyek.rukuntetangga.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.models.User;

public class DataWargaAdapter extends RecyclerView.Adapter<DataWargaAdapter.ViewHolder>{

    private List<User> dataWarga;
    private AdapterListener<User> listener;
    private Context context;

    public DataWargaAdapter(List<User> dataWarga, Context context, AdapterListener<User> listener) {
        this.dataWarga = dataWarga;
        this.context = context;
        this.listener = listener;
    }

    public void setDataWarga(List<User> dataWarga) {
        this.dataWarga = dataWarga;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data_warga, parent, false);
        return new DataWargaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = dataWarga.get(position);
        if (user.getPekerjaan() == null) {
            holder.tvJob.setText("-");
        } else
            holder.tvJob.setText(user.getPekerjaan());
        holder.tvName.setText(user.getFullName());
        holder.tvStatus.setText(user.getStatusPerkawinan());
        holder.tvNomor.setText(""+(position+1));
    }

    @Override
    public int getItemCount() {
        return dataWarga.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNomor, tvName, tvStatus, tvJob, tvDetail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomor = itemView.findViewById(R.id.tv_nomor);
            tvName = itemView.findViewById(R.id.tv_nama_warga);
            tvStatus = itemView.findViewById(R.id.tv_status_warga);
            tvJob = itemView.findViewById(R.id.tv_pekerjaan_warga);
            tvDetail = itemView.findViewById(R.id.tv_detail_warga);
            tvDetail.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemSelected(dataWarga.get(getBindingAdapterPosition()));
                }
            });

            tvDetail.setOnLongClickListener((View.OnLongClickListener) v -> {
                if (listener != null)
                    listener.onItemLongSelected(dataWarga.get(getBindingAdapterPosition()));
                return true;
            });
        }
    }
}
