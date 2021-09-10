package id.akhir.proyek.rukuntetangga.activities.user;

import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.models.Activities;

public class DetailKegiatanActivity extends BaseActivity {

    Toolbar toolbar;
    TextView tvNamaKegiatan, tvJamKegiatan, tvLokasiKegiatan,
        tvTanggalKegiatan;
    ImageView ivFotoKegiatan;

    Activities detailActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kegiatan);

        initData();
        detailActivity = getIntent().getParcelableExtra("activity");
        if (detailActivity != null) {
            setData();
        }
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_menu_kegiatan));

        tvNamaKegiatan = findViewById(R.id.tv_name_kegiatan);
        tvJamKegiatan = findViewById(R.id.tv_hour);
        tvLokasiKegiatan = findViewById(R.id.tv_location);
        tvTanggalKegiatan = findViewById(R.id.tv_date);
        ivFotoKegiatan = findViewById(R.id.iv_activity_image);
    }

    private void setData() {
        tvNamaKegiatan.setText(detailActivity.getTitleActivity());
        tvJamKegiatan.setText(detailActivity.getHour());
        tvLokasiKegiatan.setText(detailActivity.getLocation());
        tvTanggalKegiatan.setText(detailActivity.getDateActivity());
        Picasso.get().load(detailActivity.getImageActivity())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.broken_image)
                .into(ivFotoKegiatan);
    }
}