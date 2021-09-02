package id.akhir.proyek.rukuntetangga.activities.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.adapters.KasAdapter;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.listener.DialogListener;
import id.akhir.proyek.rukuntetangga.models.ApiData;
import id.akhir.proyek.rukuntetangga.models.ApiStatus;
import id.akhir.proyek.rukuntetangga.models.KasAdmin;
import id.akhir.proyek.rukuntetangga.models.Month;
import id.akhir.proyek.rukuntetangga.models.Paginator;

public class KasAdminActivity extends BaseActivity {
    Toolbar toolbar;
    Spinner spMonth;
    int monthId = 1;
    RecyclerView rvKasAdmin;
    Button btnNext, btnPrevious;
    Paginator<KasAdmin> paginator;
    List<KasAdmin> dataKasAdmin = new ArrayList<>();
    KasAdapter kasAdapter;
    KasAdmin _kasAdminSelected;

    private int totalPages = 0;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kas_admin);
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_menu_kas));
        spMonth = findViewById(R.id.spinner_month);
        rvKasAdmin = findViewById(R.id.rvKasAdmin);
        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);
        paginator = new Paginator<KasAdmin>();
        rvKasAdmin.setLayoutManager(new LinearLayoutManager(context));

        final ArrayAdapter<Month> adapter = new ArrayAdapter<Month>(context,
                R.layout.spinner_position, setDataMonth());
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spMonth.setAdapter(adapter);
        spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                monthId = setDataMonth().get(i).getId();
                showProgressBar(true);
                mApiService.getKasAdmin("Bearer " + getUserToken(), monthId).enqueue(apiCallback.build());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        kasAdapter = new KasAdapter(context, new AdapterListener<KasAdmin>() {
            @Override
            public void onItemSelected(KasAdmin data) {
                alertSubmitDone(getString(R.string.warning_title), getString(R.string.warning_confirmation_2, data.getName()), new DialogListener() {
                    @Override
                    public void onPositiveButton() {
                        _kasAdminSelected = data;
                        showProgressBar(true);
                        mApiService.addPembayaranUser("Bearer " + getUserToken(), data.getUserId(), monthId).enqueue(addCallback.build());
                    }

                    @Override
                    public void onNegativeButton() {

                    }
                });
            }

            @Override
            public void onItemLongSelected(KasAdmin data) {

            }
        });

        showProgressBar(true);
        mApiService.getKasAdmin("Bearer " + getUserToken(), monthId).enqueue(apiCallback.build());

        btnNext.setOnClickListener(v -> {
            currentPage += 1;

            kasAdapter.setData(paginator.generatePage(currentPage, dataKasAdmin));
            rvKasAdmin.setAdapter(kasAdapter);

            toggleButtons();
        });

        btnPrevious.setOnClickListener(v -> {
            currentPage -= 1;

            kasAdapter.setData(paginator.generatePage(currentPage, dataKasAdmin));
            rvKasAdmin.setAdapter(kasAdapter);

            toggleButtons();
        });
    }

    private void toggleButtons() {
        if (currentPage == totalPages) {
            btnNext.setEnabled(false);
            btnPrevious.setEnabled(true);
        } else if (currentPage == 0) {
            btnPrevious.setEnabled(false);
            btnNext.setEnabled(true);
        } else if (currentPage >= 1 && currentPage <= totalPages) {
            btnNext.setEnabled(true);
            btnPrevious.setEnabled(true);
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
            nextActivity(AddKasActivity.class);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    ApiCallback apiCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBar(false);
            ApiData<KasAdmin> apiData = new Gson().fromJson(result, new TypeToken<ApiData<KasAdmin>>(){}.getType());
            dataKasAdmin = apiData.getData();
            paginator.setTotalNumItems(dataKasAdmin.size());
            totalPages = paginator.getTotalNumItems() / Paginator.ITEMS_PER_PAGE;
            kasAdapter.setData(paginator.generatePage(currentPage, dataKasAdmin));
            rvKasAdmin.setAdapter(kasAdapter);

            btnPrevious.setEnabled(false);
            if (dataKasAdmin.size() <= Paginator.ITEMS_PER_PAGE) {
                btnNext.setEnabled(false);
            }
        }

        @Override
        public void onApiFailure(String errorMessage) {
            showProgressBar(false);
            showToast(errorMessage);
        }
    };

    ApiCallback addCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
//            showProgressBar(false);
//            ApiStatus status = new Gson().fromJson(result, ApiStatus.class);
//            showToast(status.getMessage());
            showProgressBar(true);
            mApiService.getKasAdmin("Bearer " + getUserToken(), monthId).enqueue(apiCallback.build());
        }

        @Override
        public void onApiFailure(String errorMessage) {
            showProgressBar(false);
            showToast(errorMessage);
        }
    };
}