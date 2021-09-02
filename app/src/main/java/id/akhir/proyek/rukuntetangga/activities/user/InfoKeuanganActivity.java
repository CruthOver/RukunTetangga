package id.akhir.proyek.rukuntetangga.activities.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.adapters.KeuanganAdapter;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.models.ApiData;
import id.akhir.proyek.rukuntetangga.models.Keuangan;

public class InfoKeuanganActivity extends BaseActivity {

    Toolbar toolbar;
    RecyclerView rvKeuangan;
    TextView tvSaldo;
    KeuanganAdapter adapter;
    List<Keuangan> dataKeuangan = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_keuangan);
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.label_info_keuangan));

        tvSaldo = findViewById(R.id.tv_saldo);
        rvKeuangan = findViewById(R.id.rv_keuangan);
        rvKeuangan.setLayoutManager(new LinearLayoutManager(context));
        rvKeuangan.setHasFixedSize(true);
        adapter = new KeuanganAdapter(dataKeuangan, setDataMonth(), context);

        showProgressBar(true);
        mApiService.getInfoKeuangan("Bearer " + getUserToken()).enqueue(getInfoKeuangan.build());
    }

    ApiCallback getInfoKeuangan = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBar(false);
            ApiData<Keuangan> apiKeuangan = new Gson().fromJson(result, new TypeToken<ApiData<Keuangan>>(){}.getType());
            dataKeuangan = apiKeuangan.getData();

            int saldo = 0;
            for (int i=0; i<dataKeuangan.size(); i++) {
                saldo += dataKeuangan.get(i).getIncome() - dataKeuangan.get(i).getExpense();
            }
            tvSaldo.setText(formatRupiah(saldo));
            adapter.setData(dataKeuangan);
            rvKeuangan.setAdapter(adapter);
        }

        @Override
        public void onApiFailure(String errorMessage) {

        }
    };
}