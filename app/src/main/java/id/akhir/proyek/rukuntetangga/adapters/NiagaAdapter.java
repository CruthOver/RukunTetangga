package id.akhir.proyek.rukuntetangga.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.models.Niaga;

public class NiagaAdapter extends RecyclerView.Adapter<NiagaAdapter.ViewHolder> {

    private List<Niaga> dataNiaga;
    private final AdapterListener<Niaga> listener;
    private Context context;

    public NiagaAdapter(List<Niaga> dataNiaga, Context context, AdapterListener<Niaga> listener) {
        this.dataNiaga = dataNiaga;
        this.listener = listener;
        this.context = context;
    }

    public void setData(List<Niaga> dataNiaga) {
        this.dataNiaga = dataNiaga;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_niaga, parent, false);
        return new NiagaAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Niaga niaga = dataNiaga.get(position);
        holder.tvTitleNiaga.setText(niaga.getNiagaName());
        holder.tvDescriptionNiaga.setText(niaga.getNiagaDescription());
        Picasso.get().load(niaga.getImageUrl())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.broken_image).into(holder.ivNiagaImage);
    }

    @Override
    public int getItemCount() {
        return dataNiaga.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitleNiaga, tvDescriptionNiaga;
        ImageView ivNiagaImage;
        Button btnBuy;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescriptionNiaga = itemView.findViewById(R.id.tv_description);
            tvTitleNiaga = itemView.findViewById(R.id.tv_jualan);
            ivNiagaImage = itemView.findViewById(R.id.iv_niaga_image);
            btnBuy = itemView.findViewById(R.id.btn_buy);

            btnBuy.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemSelected(dataNiaga.get(getBindingAdapterPosition()));
            });

            btnBuy.setOnLongClickListener(v -> {
                if (listener != null)
                    listener.onItemLongSelected(dataNiaga.get(getBindingAdapterPosition()));
                return true;
            });
        }
    }
}
