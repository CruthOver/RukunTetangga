package id.akhir.proyek.rukuntetangga.activities.user;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.models.ApiData;
import id.akhir.proyek.rukuntetangga.models.Structure;

public class StructureUserActivity extends BaseActivity {
    Toolbar toolbar;
    TextView tvKetuaRT, tvSekretaris, tvBendahara,
            tvInfrastruktur, tvKerohanian, tvPKK, tvKeamanan,
            tvPemudaOlahraga, tvPubDok, tvKebersihan, tvKesehatan;
    TextView tvNamaKetuaRT, tvNamaSekretaris, tvNamaBendahara,
            tvNamaInfrastruktur, tvNamaKerohanian, tvNamaPKK, tvNamaKeamanan,
            tvNamaPemudaOlahraga, tvNamaPubDok, tvNamaKebersihan, tvNamaKesehatan;
    TextView tvEmptyData;
    CircleImageView ivKetuaRT, ivSekretaris, ivBendahara,
            ivInfrastruktur, ivKerohanian, ivPKK, ivKeamanan,
            ivPemudaOlahraga, ivPubDok, ivKebersihan, ivKesehatan;

    CardView cvKetuaRT, cvSekretaris, cvBendahara,
            cvInfrastruktur, cvKerohanian, cvPKK, cvKeamanan,
            cvPemudaOlahraga, cvPubDok, cvKebersihan, cvKesehatan;

    List<Structure> dataStructure = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_structure_user);
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_menu_structure));

        tvKetuaRT = findViewById(R.id.tv_ketua_rt);
        tvSekretaris = findViewById(R.id.tv_sekretaris);
        tvBendahara = findViewById(R.id.tv_bendahara);
        tvInfrastruktur = findViewById(R.id.tv_infrastruktur);
        tvKerohanian = findViewById(R.id.tv_kerohanian);
        tvPKK = findViewById(R.id.tv_pkk);
        tvKeamanan = findViewById(R.id.tv_keamanan);
        tvPemudaOlahraga = findViewById(R.id.tv_pemuda);
        tvPubDok = findViewById(R.id.tv_pubdok);
        tvKebersihan = findViewById(R.id.tv_kebersihan);
        tvKesehatan = findViewById(R.id.tv_kesehatan);

        tvNamaKetuaRT = findViewById(R.id.tv_name_rt);
        tvNamaSekretaris = findViewById(R.id.tv_name_sekretaris);
        tvNamaBendahara = findViewById(R.id.tv_name_bendahara);
        tvNamaInfrastruktur = findViewById(R.id.tv_name_infrastruktur);
        tvNamaKerohanian = findViewById(R.id.tv_name_kerohanian);
        tvNamaPKK = findViewById(R.id.tv_name_pkk);
        tvNamaKeamanan = findViewById(R.id.tv_name_keamanan);
        tvNamaPemudaOlahraga = findViewById(R.id.tv_name_pemuda);
        tvNamaPubDok = findViewById(R.id.tv_name_pubdok);
        tvNamaKebersihan = findViewById(R.id.tv_name_kebersihan);
        tvNamaKesehatan = findViewById(R.id.tv_name_kesehatan);

        ivKetuaRT = findViewById(R.id.iv_ketua_rt);
        ivSekretaris = findViewById(R.id.iv_sekretaris);
        ivBendahara = findViewById(R.id.iv_bendahara);
        ivInfrastruktur = findViewById(R.id.iv_infrastruktur);
        ivKerohanian = findViewById(R.id.iv_kerohanian);
        ivPKK = findViewById(R.id.iv_pkk);
        ivKeamanan = findViewById(R.id.iv_keamanan);
        ivPemudaOlahraga = findViewById(R.id.iv_pemuda);
        ivPubDok = findViewById(R.id.iv_pubdok);
        ivKebersihan = findViewById(R.id.iv_kebersihan);
        ivKesehatan = findViewById(R.id.iv_kesehatan);

        cvKetuaRT = findViewById(R.id.cv_ketua_rt);
        cvSekretaris = findViewById(R.id.cv_sekretaris);
        cvBendahara = findViewById(R.id.cv_bendahara);
        cvInfrastruktur = findViewById(R.id.cv_infrastruktur);
        cvKerohanian = findViewById(R.id.cv_kerohanian);
        cvPKK = findViewById(R.id.cv_pkk);
        cvKeamanan = findViewById(R.id.cv_keamanan);
        cvPemudaOlahraga = findViewById(R.id.cv_pemuda);
        cvPubDok = findViewById(R.id.cv_pubdok);
        cvKebersihan = findViewById(R.id.cv_kebersihan);
        cvKesehatan = findViewById(R.id.cv_kesehatan);

        showProgressBar(true);
        mApiService.getStructure("Bearer " + getUserToken()).enqueue(structureCallback.build());
    }

    private void setData() {
        for (Structure data : dataStructure) {
            switch (data.getPositionId()) {
                case 1:
                    tvKetuaRT.setText(data.getPositionName());
                    tvNamaKetuaRT.setText(data.getFullName());
                    if (!data.getUserPhoto().isEmpty()) {
                        setPicasso(data.getUserPhoto(), ivKetuaRT);
                    } else {
                        ivKetuaRT.setImageResource(R.drawable.ic_account);
                    }
                    break;
                case 2:
                    tvSekretaris.setText(data.getPositionName());
                    tvNamaSekretaris.setText(data.getFullName());
                    if (!data.getUserPhoto().isEmpty()) {
                        setPicasso(data.getUserPhoto(), ivSekretaris);
                    } else {
                        ivSekretaris.setImageResource(R.drawable.ic_account);
                    }
                    break;
                case 3:
                    tvBendahara.setText(data.getPositionName());
                    tvNamaBendahara.setText(data.getFullName());
                    if (!data.getUserPhoto().isEmpty()) {
                        setPicasso(data.getUserPhoto(), ivBendahara);
                    } else {
                        ivBendahara.setImageResource(R.drawable.ic_account);
                    }
                    break;
                case 4:
                    tvInfrastruktur.setText(data.getPositionName());
                    tvNamaInfrastruktur.setText(data.getFullName());
                    if (!data.getUserPhoto().isEmpty()) {
                        setPicasso(data.getUserPhoto(), ivInfrastruktur);
                    } else {
                        ivInfrastruktur.setImageResource(R.drawable.ic_account);
                    }
                    break;
                case 5:
                    tvKerohanian.setText(data.getPositionName());
                    tvNamaKerohanian.setText(data.getFullName());
                    if (!data.getUserPhoto().isEmpty()) {
                        setPicasso(data.getUserPhoto(), ivKerohanian);
                    } else {
                        ivKerohanian.setImageResource(R.drawable.ic_account);
                    }
                    break;
                case 6:
                    tvKeamanan.setText(data.getPositionName());
                    tvNamaKeamanan.setText(data.getFullName());
                    if (!data.getUserPhoto().isEmpty()) {
                        setPicasso(data.getUserPhoto(), ivKeamanan);
                    } else {
                        ivKeamanan.setImageResource(R.drawable.ic_account);
                    }
                    break;
                case 7:
                    tvPKK.setText(data.getPositionName());
                    tvNamaPKK.setText(data.getFullName());
                    if (!data.getUserPhoto().isEmpty()) {
                        setPicasso(data.getUserPhoto(), ivPKK);
                    } else {
                        ivPKK.setImageResource(R.drawable.ic_account);
                    }
                    break;
                case 8:
                    tvPemudaOlahraga.setText(data.getPositionName());
                    tvNamaPemudaOlahraga.setText(data.getFullName());
                    if (!data.getUserPhoto().isEmpty()) {
                        setPicasso(data.getUserPhoto(), ivPemudaOlahraga);
                    } else {
                        ivPemudaOlahraga.setImageResource(R.drawable.ic_account);
                    }
                    break;
                case 9:
                    tvPubDok.setText(data.getPositionName());
                    tvNamaPubDok.setText(data.getFullName());
                    if (!data.getUserPhoto().isEmpty()) {
                        setPicasso(data.getUserPhoto(), ivPubDok);
                    } else {
                        ivPubDok.setImageResource(R.drawable.ic_account);
                    }
                    break;
                case 10:
                    tvKebersihan.setText(data.getPositionName());
                    tvNamaKebersihan.setText(data.getFullName());
                    if (!data.getUserPhoto().isEmpty()) {
                        setPicasso(data.getUserPhoto(), ivKebersihan);
                    } else {
                        ivKebersihan.setImageResource(R.drawable.ic_account);
                    }
                    break;
                case 11:
                    tvKesehatan.setText(data.getPositionName());
                    tvNamaKesehatan.setText(data.getFullName());
                    if (!data.getUserPhoto().isEmpty()) {
                        setPicasso(data.getUserPhoto(), ivKesehatan);
                    } else {
                        ivKesehatan.setImageResource(R.drawable.ic_account);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void setPicasso(String url, ImageView imageView) {
        Picasso.get().load(url).into(imageView);
    }

    private void setEmptyData() {
        if (dataStructure.isEmpty()) {
            cvKetuaRT.setVisibility(View.GONE);
            cvSekretaris.setVisibility(View.GONE);
            cvBendahara.setVisibility(View.GONE);
            cvInfrastruktur.setVisibility(View.GONE);
            cvKerohanian.setVisibility(View.GONE);
            cvPKK.setVisibility(View.GONE);
            cvKeamanan.setVisibility(View.GONE);
            cvPemudaOlahraga.setVisibility(View.GONE);
            cvPubDok.setVisibility(View.GONE);
            cvKebersihan.setVisibility(View.GONE);
            cvKesehatan.setVisibility(View.GONE);

            tvEmptyData.setText(getString(R.string.no_data_found, "Data Structure"));
            tvEmptyData.setVisibility(View.VISIBLE);
        } else {
            cvKetuaRT.setVisibility(View.VISIBLE);
            cvSekretaris.setVisibility(View.VISIBLE);
            cvBendahara.setVisibility(View.VISIBLE);
            cvInfrastruktur.setVisibility(View.VISIBLE);
            cvKerohanian.setVisibility(View.VISIBLE);
            cvPKK.setVisibility(View.VISIBLE);
            cvKeamanan.setVisibility(View.VISIBLE);
            cvPemudaOlahraga.setVisibility(View.VISIBLE);
            cvPubDok.setVisibility(View.VISIBLE);
            cvKebersihan.setVisibility(View.VISIBLE);
            cvKesehatan.setVisibility(View.VISIBLE);

            tvEmptyData.setVisibility(View.GONE);
        }
    }

    ApiCallback structureCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBar(false);
            ApiData<Structure> apiStructure = new Gson().fromJson(result, new TypeToken<ApiData<Structure>>(){}.getType());
            dataStructure = apiStructure.getData();
            if (dataStructure.size() == 0) {
                setEmptyData();
            } else {
                setData();
            }

        }

        @Override
        public void onApiFailure(String errorMessage) {
            showProgressBar(false);
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }
    };
}