package id.akhir.proyek.rukuntetangga.activities.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.models.User;

public class DetailWargaActivity extends BaseActivity {
    Toolbar toolbar;
    TextView tvName, tvPhoneNumber, tvAddress,
        tvDateBirth, tvBirthPlace, tvNik, tvReligion, tvStatus, tvPendidikan;

    User detailUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_warga);
        detailUser = getIntent().getParcelableExtra("user");
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_detail_warga));

        tvName = findViewById(R.id.tv_name);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);
        tvAddress = findViewById(R.id.tv_address);
        tvDateBirth = findViewById(R.id.tv_birth_date);
        tvBirthPlace  = findViewById(R.id.tv_birth_place);
        tvNik = findViewById(R.id.tv_nik);
        tvReligion = findViewById(R.id.tv_religion);
        tvStatus = findViewById(R.id.tv_status);
        tvPendidikan = findViewById(R.id.tv_pendidikan);

        tvName.setText(detailUser.getFullName());
        tvPhoneNumber.setText(detailUser.getPhoneNumber());
        tvAddress.setText(detailUser.getAddress());
        tvDateBirth.setText(detailUser.getDateBirth());
        tvBirthPlace.setText(detailUser.getBirthPlace());
        tvNik.setText(detailUser.getNikWarga());
        tvReligion.setText(detailUser.getAgama());
        tvStatus.setText(detailUser.getStatusPerkawinan());
        tvPendidikan.setText(detailUser.getPendidikan());
    }
}