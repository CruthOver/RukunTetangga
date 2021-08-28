package id.akhir.proyek.rukuntetangga.activities.user;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.adapters.KasUserAdapter;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.models.ApiUser;
import id.akhir.proyek.rukuntetangga.models.KasStatus;
import id.akhir.proyek.rukuntetangga.models.KasUser;

public class KasUserActivity extends BaseActivity {
    Toolbar toolbar;

    TextView tvTotalTagihan, dueDateTagihan;
    Button btnPay, btnConfirmationPay;
    RecyclerView rvMonth;
    KasUserAdapter adapter;
    KasUser dataKasUser;
    List<KasStatus> dataStatusKas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kas_user);
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_menu_kas));

        tvTotalTagihan = findViewById(R.id.tv_tagihan);
        dueDateTagihan = findViewById(R.id.tv_due_date);
        rvMonth = findViewById(R.id.rv_month);
        rvMonth.setLayoutManager(new GridLayoutManager(context, 2));
        adapter = new KasUserAdapter(dataStatusKas, context);
        btnPay = findViewById(R.id.btn_how_to_pay);
        btnConfirmationPay = findViewById(R.id.btn_confirmation);

        btnPay.setOnClickListener(v -> {
            Intent intent = new Intent(context, HowToPayActivity.class);
            intent.putExtra("nomor_rekening", dataKasUser.getNomorRekening());
            intent.putExtra("bank_name", dataKasUser.getBankName());
            startActivity(intent);
        });

        btnConfirmationPay.setOnClickListener(v -> {
            openWhatsApp(dataKasUser.getNoTelp());
        });

        showProgressBar(true);
        mApiService.getKasUser("Bearer " + getUserToken(), getUserSession().getUserId()).enqueue(kasUserCallback.build());
    }

    private void openWhatsApp(String toNumber){
        try {
            String text = "This is a test";// Replace with your message.

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        menu.findItem(R.id.menu_toolbar).setIcon(R.drawable.ic_baseline_menu_24);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_toolbar) {
            nextActivity(InfoKeuanganActivity.class);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    ApiCallback kasUserCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBar(false);

            Calendar c = Calendar.getInstance();
            String monthNow = String.format(Locale.US,"%tB",c);

            Log.d(TAG, result);
            ApiUser<KasUser> apiService = new Gson().fromJson(result, new TypeToken<ApiUser<KasUser>>(){}.getType());
            dataKasUser = apiService.getData();
            tvTotalTagihan.setText(formatRupiah(dataKasUser.getTagihan()));
            dueDateTagihan.setText("Batas Pembayaran " + dataKasUser.getDuDatePay()+" "+monthNow);
            adapter.setData(dataKasUser.getDataBulan());
            rvMonth.setAdapter(adapter);
        }

        @Override
        public void onApiFailure(String errorMessage) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }
    };
}