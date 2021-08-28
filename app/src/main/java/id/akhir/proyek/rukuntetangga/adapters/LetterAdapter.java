package id.akhir.proyek.rukuntetangga.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.net.HttpURLConnection;
import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.listener.MenuListener;
import id.akhir.proyek.rukuntetangga.models.Letter;

public class LetterAdapter extends RecyclerView.Adapter<LetterAdapter.ViewHolder>{

    private List<Letter> dataLetter;
    private final AdapterListener<Letter> listener;
    private MenuListener<Letter> deleteListener;
    private final boolean canUpdateStatus;
    private final Context context;

    public LetterAdapter(Context context, boolean canUpdateStatus, List<Letter> dataLetter, AdapterListener<Letter> listener, MenuListener<Letter> deleteListener) {
        this.dataLetter = dataLetter;
        this.listener = listener;
        this.canUpdateStatus = canUpdateStatus;
        this.context = context;
        this.deleteListener = deleteListener;
    }

    public LetterAdapter(Context context, boolean canUpdateStatus, List<Letter> dataLetter, AdapterListener<Letter> listener) {
        this.dataLetter = dataLetter;
        this.listener = listener;
        this.canUpdateStatus = canUpdateStatus;
        this.context = context;
    }

    public void setData(List<Letter> dataLetter) {
        this.dataLetter = dataLetter;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_letter, parent, false);
        return new LetterAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Letter letter = dataLetter.get(position);
        holder.tvLetterType.setText(letter.getLetterType());
        holder.tvApplicant.setText(letter.getUser().getFullName());
        holder.tvDateBirth.setText(letter.getUser().getDateBirth());
        holder.tvDateNeed.setText(letter.getDateNeeded());
        holder.tvAddress.setText(letter.getUser().getCurrentAddress());
        holder.tvDescription.setText(letter.getDescription());
        holder.tvEmail.setText(letter.getUser().getEmail());
        holder.btnStatus.setBackgroundColor(statusTypeColor(letter.getStatusLetter()));
        holder.btnStatus.setText(statusType(letter.getStatusLetter()));
        if (canUpdateStatus) {
            if (letter.getStatusLetter() == 2) {
                holder.btnDelete.setVisibility(View.VISIBLE);
            } else {
                holder.btnDelete.setVisibility(View.GONE);
            }

            holder.tvEmail.setLinksClickable(true);
            holder.tvEmail.setOnClickListener(v -> {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {holder.tvEmail.getText().toString()}); // recipients
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, letter.getLetterType());
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
//                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));
                context.startActivity(emailIntent);
            });
            holder.btnStatus.setEnabled(true);
            holder.btnStatus.setClickable(true);
        } else {
            holder.tvEmail.setLinksClickable(false);
            holder.btnStatus.setEnabled(false);
            holder.btnStatus.setClickable(false);
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

    private String statusType(int status) {
        if (status == 0) {
            return "Diajukan";
        } else if (status == 1) {
            return "Proses";
        } else {
            return "Selesai";
        }
    }

    @Override
    public int getItemCount() {
        return dataLetter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLetterType, tvApplicant, tvDateBirth, tvDateNeed, tvAddress
                , tvDescription, tvEmail;
        Button btnStatus, btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvLetterType = itemView.findViewById(R.id.tv_type_letter);
            tvApplicant = itemView.findViewById(R.id.tv_applicant_name);
            tvDateBirth = itemView.findViewById(R.id.tv_date_of_birth);
            tvDateNeed = itemView.findViewById(R.id.tv_date_need);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvEmail = itemView.findViewById(R.id.tv_email);
            btnStatus = itemView.findViewById(R.id.btn_status_letter);
            btnDelete = itemView.findViewById(R.id.btn_delete_letter);

            btnDelete.setOnClickListener(v -> {
                if (deleteListener !=null)
                    deleteListener.onDelete(dataLetter.get(getBindingAdapterPosition()));
            });

            btnStatus.setOnClickListener(v -> {
                if (listener != null)
                    listener.onItemSelected(dataLetter.get(getBindingAdapterPosition()));
            });

            btnStatus.setOnLongClickListener(v -> {
                if (listener != null)
                    listener.onItemLongSelected(dataLetter.get(getBindingAdapterPosition()));
                return true;
            });
        }
    }
}
