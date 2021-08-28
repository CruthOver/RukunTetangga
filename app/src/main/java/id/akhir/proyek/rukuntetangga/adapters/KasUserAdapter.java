package id.akhir.proyek.rukuntetangga.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.models.KasStatus;
import id.akhir.proyek.rukuntetangga.models.KasUser;
import id.akhir.proyek.rukuntetangga.models.MenuGrid;
import id.akhir.proyek.rukuntetangga.models.Month;

public class KasUserAdapter extends RecyclerView.Adapter<KasUserAdapter.GridViewHolder> {

    private List<KasStatus> dataMonth;
    private final Context context;

    public KasUserAdapter(List<KasStatus> dataMonth, Context context) {
        this.dataMonth = dataMonth;
        this.context = context;
    }

    public void setData(List<KasStatus> dataMonth) {
        this.dataMonth = dataMonth;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_month, parent, false);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridViewHolder holder, int position) {
        holder.tvMonth.setText(dataMonth.get(position).getBulan());
        if (dataMonth.get(position).isStatus()) {
            holder.llBackground.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            holder.llBackground.setBackgroundColor(context.getResources().getColor(R.color.colorGray));
        }
    }

    @Override
    public int getItemCount() {
        return dataMonth.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llBackground;
        TextView tvMonth;
        public GridViewHolder(@NonNull View itemView) {
            super(itemView);
            llBackground = itemView.findViewById(R.id.ll_background_month);
            tvMonth = itemView.findViewById(R.id.tv_month);
        }
    }
}
