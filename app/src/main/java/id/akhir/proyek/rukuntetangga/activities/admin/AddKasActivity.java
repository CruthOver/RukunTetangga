package id.akhir.proyek.rukuntetangga.activities.admin;

import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.models.ApiStatus;
import id.akhir.proyek.rukuntetangga.models.Month;

public class AddKasActivity extends BaseActivity {

    Toolbar toolbar;
    EditText etSaldo, etIncome, etExpense, etKebutuhan;
    Spinner spMonth;
    Button btnUpload;

    int monthId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kas);
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_menu_kas));
        etSaldo = findViewById(R.id.et_saldo);
        etIncome = findViewById(R.id.et_income);
        etExpense = findViewById(R.id.et_expense);
        etKebutuhan = findViewById(R.id.et_kebutuhan);
        spMonth = findViewById(R.id.spinner_month);
        btnUpload = findViewById(R.id.btn_upload);

        final ArrayAdapter<Month> adapter = new ArrayAdapter<>(context,
                R.layout.spinner_position, setDataMonth());
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spMonth.setAdapter(adapter);
        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                monthId = setDataMonth().get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnUpload.setOnClickListener(v -> {
            String saldo = etSaldo.getText().toString();
            String income = etIncome.getText().toString();
            String expense = etExpense.getText().toString();
            String kebutuhan = etKebutuhan.getText().toString();
            showProgressBarUpload(true);
            mApiService.addInfoKeuangan("Bearer " + getUserToken(), saldo, monthId, kebutuhan,
                    income, expense).enqueue(addKeuanganCallback.build());
        });
    }

    ApiCallback addKeuanganCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBarUpload(false);
            ApiStatus status = new Gson().fromJson(result, ApiStatus.class);
            Toast.makeText(context, status.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onApiFailure(String errorMessage) {
            showProgressBar(false);
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }
    };
}