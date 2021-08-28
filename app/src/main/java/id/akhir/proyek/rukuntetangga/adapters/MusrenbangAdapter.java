package id.akhir.proyek.rukuntetangga.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.models.Musrenbang;

public class MusrenbangAdapter extends RecyclerView.Adapter<MusrenbangAdapter.ViewHolder> {

    private List<Musrenbang> dataMusrenbang;
    private final AdapterListener<Musrenbang> listener;
    private Context context;

    public MusrenbangAdapter(List<Musrenbang> dataMusrenbang, Context context, AdapterListener<Musrenbang> listener) {
        this.dataMusrenbang = dataMusrenbang;
        this.listener = listener;
        this.context = context;
    }

    public void setData(List<Musrenbang> dataMusrenbang) {
        this.dataMusrenbang = dataMusrenbang;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MusrenbangAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_musrenbang, parent, false);
        return new MusrenbangAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusrenbangAdapter.ViewHolder holder, int position) {
        Musrenbang musrenbang = dataMusrenbang.get(position);
        if (position % 2 == 1) {
            holder.llListVote.setBackground(context.getResources().getDrawable(R.drawable.background_gray));
        }
        holder.tvQuestion.setText(musrenbang.getFileName());
    }

    @Override
    public int getItemCount() {
        return dataMusrenbang.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        TextView tvQuestion;
        LinearLayout llListVote;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvQuestion = itemView.findViewById(R.id.tv_question);
            llListVote = itemView.findViewById(R.id.ll_list_vote);

            itemView.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemSelected(dataMusrenbang.get(getBindingAdapterPosition()));
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null)
                    listener.onItemLongSelected(dataMusrenbang.get(getBindingAdapterPosition()));
                return true;
            });
        }
    }
}
