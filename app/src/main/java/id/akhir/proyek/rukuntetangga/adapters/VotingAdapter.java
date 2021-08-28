package id.akhir.proyek.rukuntetangga.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.listener.MenuListener;
import id.akhir.proyek.rukuntetangga.models.Voting;

public class VotingAdapter extends RecyclerView.Adapter<VotingAdapter.ViewHolder> {

    private List<Voting> dataVoting;
    private final AdapterListener<Voting> listener;
    private final MenuListener<Voting> menuListener;
    private final Context context;
    private final boolean isAdmin;

    public VotingAdapter(boolean isAdmin, List<Voting> dataVoting, Context context, AdapterListener<Voting> listener, MenuListener<Voting> menuListener) {
        this.dataVoting = dataVoting;
        this.listener = listener;
        this.menuListener = menuListener;
        this.context = context;
        this.isAdmin = isAdmin;
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
        if (isAdmin) {
            holder.btnMenu.setVisibility(View.VISIBLE);
        } else {
            holder.btnMenu.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return dataVoting.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestion;
        LinearLayout llListVote;
        Button btnMenu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvQuestion = itemView.findViewById(R.id.tv_question);
            llListVote = itemView.findViewById(R.id.ll_list_vote);
            btnMenu = itemView.findViewById(R.id.menu_option);

            btnMenu.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(context, btnMenu);
                popupMenu.inflate(R.menu.menu_list);
                popupMenu.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.edit) {
                        if (menuListener != null)
                            menuListener.onEdit(dataVoting.get(getBindingAdapterPosition()));
                        return true;
                    } else if (item.getItemId() == R.id.delete) {
                        if (menuListener != null)
                            menuListener.onDelete(dataVoting.get(getBindingAdapterPosition()));
                        return true;
                    }

                    return false;
                });
                popupMenu.show();
            });

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
