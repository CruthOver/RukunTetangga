package id.akhir.proyek.rukuntetangga.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.models.Information;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder> {
    List<Information> dataInformation;
    private ViewPager2 viewPager2;
    Context context;

    int[] randomColor = {
      R.color.colorSecondary, R.color.slider1, R.color.slider2
    };

    public BannerAdapter(Context context, List<Information> dataInformation, ViewPager2 viewPager2) {
        this.dataInformation = dataInformation;
        this.viewPager2 = viewPager2;
        this.context = context;
    }

    public void setData(List<Information> dataInformation) {
        this.dataInformation = dataInformation;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BannerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider, parent, false);
        return new BannerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerAdapter.ViewHolder holder, int position) {
        Drawable drawable = holder.relativeLayout.getBackground();
        drawable = DrawableCompat.wrap(drawable);

        DrawableCompat.setTint(drawable, context.getResources().getColor(randomColor[new Random().nextInt(randomColor.length)]));
        holder.relativeLayout.setBackground(drawable);
        holder.tvInformation.setText(dataInformation.get(position).getInformation());
    }

    @Override
    public int getItemCount() {
        return dataInformation.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout relativeLayout;
        TextView tvInformation;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            relativeLayout = itemView.findViewById(R.id.relativeLayoutBackground);
            tvInformation = itemView.findViewById(R.id.tvInformation);
        }
    }
}