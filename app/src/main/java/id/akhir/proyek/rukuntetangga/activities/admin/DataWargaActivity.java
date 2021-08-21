package id.akhir.proyek.rukuntetangga.activities.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.adapters.DataWargaAdapter;
import id.akhir.proyek.rukuntetangga.adapters.EmergencyAdapter;
import id.akhir.proyek.rukuntetangga.apihelper.AppSession;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.models.ApiData;
import id.akhir.proyek.rukuntetangga.models.Service;
import id.akhir.proyek.rukuntetangga.models.User;

public class DataWargaActivity extends BaseActivity {
    Toolbar toolbar;

    private RecyclerView rvDataWarga;
    private List<User> dataUser = new ArrayList<>();
    private DataWargaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_warga);
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_menu_warga));
        rvDataWarga = findViewById(R.id.rv_data_warga);
        rvDataWarga.setLayoutManager(new LinearLayoutManager(context));
        adapter = new DataWargaAdapter(dataUser, context, new AdapterListener<User>() {
            @Override
            public void onItemSelected(User data) {
                Intent intent = new Intent(context, DetailWargaActivity.class);
                intent.putExtra("user", data);
                startActivity(intent);
            }

            @Override
            public void onItemLongSelected(User data) {
                Toast.makeText(context, data.getFullName(), Toast.LENGTH_SHORT).show();
            }
        });
        showProgressBar(true);
        mApiService.getWarga("Bearer " + appSession.getData(AppSession.TOKEN)).enqueue(getWargaCallback.build());
        rvDataWarga.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_toolbar) {
            nextActivity(AddDataWargaActivity.class);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    ApiCallback getWargaCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBar(false);
            ApiData<User> apiService = new Gson().fromJson(result, new TypeToken<ApiData<User>>(){}.getType());
            dataUser = apiService.getData();
            Log.d(TAG, dataUser.toString());
            adapter.setDataWarga(dataUser);
        }

        @Override
        public void onApiFailure(String errorMessage) {
            showProgressBar(false);
//            showToast(errorMessage);
        }
    };
}