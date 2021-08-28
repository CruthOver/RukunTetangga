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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.adapters.InformationAdapter;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.listener.DialogListener;
import id.akhir.proyek.rukuntetangga.listener.MenuListener;
import id.akhir.proyek.rukuntetangga.models.ApiData;
import id.akhir.proyek.rukuntetangga.models.ApiStatus;
import id.akhir.proyek.rukuntetangga.models.Information;

public class ViewInformationActivity extends BaseActivity {

    private RecyclerView rvInformation;
    private InformationAdapter adapter;
    private List<Information> dataInformation = new ArrayList<>();
    Information information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_information);
        initData();
    }

    private void initData() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_menu_informasi));
        rvInformation = findViewById(R.id.rv_information);
        rvInformation.setLayoutManager(new LinearLayoutManager(context));
        adapter = new InformationAdapter(dataInformation, context, new MenuListener<Information>() {
            @Override
            public void onEdit(Information data) {
                Intent intent = new Intent(context, InformationActivity.class);
                intent.putExtra("edit_information", data);
                startActivity(intent);
            }

            @Override
            public void onDelete(Information data) {
                alertSubmitDone(getString(R.string.warning_title), getString(R.string.delete_confirmation, "Warga"), new DialogListener() {
                    @Override
                    public void onPositiveButton() {
                        information = data;
                        showProgressBar(true);
                        mApiService.deleteInformasi("Bearer " + getUserToken(), data.getId()).enqueue(deleteInformation.build());
                    }

                    @Override
                    public void onNegativeButton() {

                    }
                });
            }
        });

        showProgressBar(true);
        mApiService.getInformation("Bearer " + getUserToken()).enqueue(getInformation.build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mApiService.getInformation("Bearer " + getUserToken()).enqueue(getInformation.build());
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
            nextActivity(InformationActivity.class);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    ApiCallback deleteInformation = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBar(false);
            ApiStatus status = new Gson().fromJson(result, ApiStatus.class);
            showToast(status.getMessage());
            dataInformation.remove(information);
            adapter.setData(dataInformation);
            rvInformation.setAdapter(adapter);
        }

        @Override
        public void onApiFailure(String errorMessage) {
            showProgressBar(false);
            showToast(errorMessage);
        }
    };

    ApiCallback getInformation = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBar(false);
            ApiData<Information> apiInformation = new Gson().fromJson(result, new TypeToken<ApiData<Information>>(){}.getType());
            dataInformation = apiInformation.getData();
            Log.d("Informasi", dataInformation.size() + "");
            adapter.setData(dataInformation);
            rvInformation.setAdapter(adapter);
        }

        @Override
        public void onApiFailure(String errorMessage) {
            showProgressBar(false);
            showToast(errorMessage);
        }
    };
}