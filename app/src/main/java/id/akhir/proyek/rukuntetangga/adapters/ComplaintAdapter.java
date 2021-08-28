package id.akhir.proyek.rukuntetangga.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.models.Complaint;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ViewHolder> {

    private List<Complaint> dataComplaint;
    private final AdapterListener<Complaint> listener;
    private final Context context;
    private final boolean canUpdateStatus;

    public ComplaintAdapter(boolean canUpdateStatus, List<Complaint> dataComplaint, Context context, AdapterListener<Complaint> listener) {
        this.dataComplaint = dataComplaint;
        this.listener = listener;
        this.context = context;
        this.canUpdateStatus = canUpdateStatus;
    }

    public void setData(List<Complaint> dataComplaint) {
        this.dataComplaint = dataComplaint;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ComplaintAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_complaint, parent, false);
        return new ComplaintAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintAdapter.ViewHolder holder, int position) {
        Complaint complaint = dataComplaint.get(position);
        holder.tvTitleComplaint.setText(complaint.getTitleComplaint());
        holder.tvDateComplaint.setText(complaint.getDateComplaint());
        Picasso.get().load(complaint.getImage())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.broken_image).into(holder.ivImageComplaint);
        holder.btnStatusComplaint.setBackgroundColor(statusTypeColor(complaint.getStatusComplaint()));
        holder.btnStatusComplaint.setText(statusType(complaint.getStatusComplaint()));
        if (canUpdateStatus) {
            holder.tvDateComplaint.setVisibility(View.GONE);
            holder.btnStatusComplaint.setEnabled(true);
            holder.btnStatusComplaint.setClickable(true);
        } else {
            holder.tvDateComplaint.setVisibility(View.VISIBLE);
//            holder.btnStatusComplaint.setEnabled(false);
//            holder.btnStatusComplaint.setClickable(false);
        }
    }

    private String statusType(int status) {
        if (status == 0) {
            return "Diajukan";
        } else if (status == 1) {
            return "Proses";
        } else {
            return "Selesai";
        }
    }

    private int statusTypeColor(int status) {
        if (status == 1) {
            return context.getResources().getColor(R.color.colorGray);
        } else if (status == 2) {
            return context.getResources().getColor(R.color.colorSecondary);
        } else {
            return context.getResources().getColor(R.color.colorRed);
        }
    }

    @Override
    public int getItemCount() {
        return dataComplaint.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitleComplaint, tvStatusComplaint, tvDateComplaint;
        ImageView ivImageComplaint;
        Button btnStatusComplaint;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitleComplaint = itemView.findViewById(R.id.tv_title_complaint);
            tvDateComplaint = itemView.findViewById(R.id.tv_complaint_date);
            ivImageComplaint = itemView.findViewById(R.id.iv_complaint);
            btnStatusComplaint = itemView.findViewById(R.id.btn_status_complaint);

            btnStatusComplaint.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemSelected(dataComplaint.get(getBindingAdapterPosition()));
            });

            btnStatusComplaint.setOnLongClickListener(v -> {
                if (listener != null)
                    listener.onItemLongSelected(dataComplaint.get(getBindingAdapterPosition()));
                return true;
            });
        }
    }
}