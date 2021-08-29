package id.akhir.proyek.rukuntetangga.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.models.KasAdmin;

public class KasAdapter extends RecyclerView.Adapter<KasAdapter.ViewHolder> {
    private final Context context;
    private List<KasAdmin> dataKas;
    private final AdapterListener<KasAdmin> listener;

    public KasAdapter(Context context, AdapterListener<KasAdmin> listener) {
        this.context = context;
//        this.dataKas = dataKas;
        this.listener = listener;
    }

    public void setData(List<KasAdmin> dataKas) {
        this.dataKas = dataKas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kas_admin, parent, false);
        return new KasAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvNo.setText(""+(position+1));
        holder.tvName.setText(dataKas.get(position).getName());
        holder.tvNoTagihan.setText(dataKas.get(position).getNoTagihan());
        holder.tvDatePay.setText(dataKas.get(position).getDatePay());
        holder.tvStatus.setText(dataKas.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return dataKas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNo, tvName, tvNoTagihan, tvDatePay, tvStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNo = itemView.findViewById(R.id.tv_no);
            tvName = itemView.findViewById(R.id.tv_name);
            tvNoTagihan = itemView.findViewById(R.id.tv_tagihan);
            tvDatePay = itemView.findViewById(R.id.tv_date_pay);
            tvStatus = itemView.findViewById(R.id.tv_status);

            tvStatus.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemSelected(dataKas.get(getBindingAdapterPosition()));
            });

            tvStatus.setOnLongClickListener(v -> {
                if (listener!=null)
                    listener.onItemLongSelected(dataKas.get(getBindingAdapterPosition()));
                return true;
            });
        }
    }
}
