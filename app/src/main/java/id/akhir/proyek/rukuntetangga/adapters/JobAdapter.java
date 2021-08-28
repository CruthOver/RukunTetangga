package id.akhir.proyek.rukuntetangga.adapters;

import android.content.Context;
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
import id.akhir.proyek.rukuntetangga.models.Jobs;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder> {
    private List<Jobs> dataJobs;
    private Context context;

    public JobAdapter(List<Jobs> dataJobs, Context context) {
        this.dataJobs = dataJobs;
        this.context = context;
    }

    public void setData(List<Jobs> dataJobs) {
        this.dataJobs = dataJobs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand_job, parent, false);
        return new JobAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(dataJobs.get(position));
        holder.itemView.setOnClickListener(v -> {
            // Get the current state of the item
            boolean expanded = dataJobs.get(position).isExpand();
            // Change the state
            dataJobs.get(position).setExpand(!expanded);
            // Notify the adapter that item has changed
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return dataJobs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvJobName;
        ImageView ivJob;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvJobName = itemView.findViewById(R.id.item_job);
            ivJob = itemView.findViewById(R.id.iv_job_list);
        }

        private void bind(Jobs jobs) {
            boolean expanded = jobs.isExpand();

            ivJob.setVisibility(expanded ? View.VISIBLE : View.GONE);

            Picasso.get().load(jobs.getImage()).placeholder(R.drawable.image_placeholder).
                    error(R.drawable.broken_image).into(ivJob);

            tvJobName.setText(jobs.getJobName());
        }
    }
}
