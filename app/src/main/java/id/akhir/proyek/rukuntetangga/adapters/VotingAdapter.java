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
import id.akhir.proyek.rukuntetangga.models.Voting;

public class VotingAdapter extends RecyclerView.Adapter<VotingAdapter.ViewHolder> {

    private List<Voting> dataVoting;
    private final AdapterListener<Voting> listener;
    private Context context;

    public VotingAdapter(List<Voting> dataVoting, Context context, AdapterListener<Voting> listener) {
        this.dataVoting = dataVoting;
        this.listener = listener;
        this.context = context;
    }

    public void setData(List<Voting> dataVoting) {
        this.dataVoting = dataVoting;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VotingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voting, parent, false);
        return new VotingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VotingAdapter.ViewHolder holder, int position) {
        Voting voting = dataVoting.get(position);
        if (position % 2 == 1) {
            holder.llListVote.setBackground(context.getResources().getDrawable(R.drawable.background_gray));
        }
        holder.tvQuestion.setText(voting.getQuestion());
    }

    @Override
    public int getItemCount() {
        return dataVoting.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestion;
        LinearLayout llListVote;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvQuestion = itemView.findViewById(R.id.tv_question);
            llListVote = itemView.findViewById(R.id.ll_list_vote);

            itemView.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemSelected(dataVoting.get(getBindingAdapterPosition()));
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null)
                    listener.onItemLongSelected(dataVoting.get(getBindingAdapterPosition()));
                return true;
            });
        }
    }
}
