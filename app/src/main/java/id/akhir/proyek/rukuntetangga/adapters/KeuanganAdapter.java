package id.akhir.proyek.rukuntetangga.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.models.Keuangan;
import id.akhir.proyek.rukuntetangga.models.Month;

public class KeuanganAdapter extends RecyclerView.Adapter<KeuanganAdapter.ViewHolder> {
    private List<Keuangan> dataJobs;
    private final List<Month> dataMonth;
    private Context context;

    public KeuanganAdapter(List<Keuangan> dataJobs, List<Month> dataMonth, Context context) {
        this.dataJobs = dataJobs;
        this.dataMonth = dataMonth;
        this.context = context;
    }

    public void setData(List<Keuangan> dataJobs) {
        this.dataJobs = dataJobs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public KeuanganAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand_keuangan, parent, false);
        return new KeuanganAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KeuanganAdapter.ViewHolder holder, int position) {
        holder.bind(dataJobs.get(position));
        holder.itemView.setOnClickListener(v -> {
            // Get the current state of the item
            boolean expanded = dataJobs.get(position).isExpanded();
            // Change the state
            dataJobs.get(position).setExpanded(!expanded);
            // Notify the adapter that item has changed
            notifyItemChanged(position);
        });
    }

    private String formatRupiah(int price) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(price);
    }

    @Override
    public int getItemCount() {
        return dataJobs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvMonth, tvIncome, tvExpense;
        LinearLayout linearDetail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearDetail = itemView.findViewById(R.id.linear_detail);
            tvMonth = itemView.findViewById(R.id.tv_month);
            tvIncome = itemView.findViewById(R.id.tv_income);
            tvExpense = itemView.findViewById(R.id.tv_expense);
        }

        private void bind(Keuangan keuangan) {
            boolean expanded = keuangan.isExpanded();
            String month = dataMonth.get((keuangan.getBulanId()-1)).getMonthName();
            tvMonth.setText(month);
            linearDetail.setVisibility(expanded ? View.VISIBLE : View.GONE);
            tvIncome.setText(formatRupiah(keuangan.getIncome()));
            tvExpense.setText(formatRupiah(keuangan.getExpense()));

        }
    }
}
