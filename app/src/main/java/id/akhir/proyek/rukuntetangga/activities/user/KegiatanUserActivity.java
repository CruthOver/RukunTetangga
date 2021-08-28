package id.akhir.proyek.rukuntetangga.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.activities.admin.InformationActivity;
import id.akhir.proyek.rukuntetangga.activities.admin.KegiatanAdminActivity;
import id.akhir.proyek.rukuntetangga.adapters.KegiatanAdapter;
import id.akhir.proyek.rukuntetangga.apihelper.AppSession;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.listener.DialogListener;
import id.akhir.proyek.rukuntetangga.listener.MenuListener;
import id.akhir.proyek.rukuntetangga.models.Activities;
import id.akhir.proyek.rukuntetangga.models.ApiData;
import id.akhir.proyek.rukuntetangga.models.ApiStatus;

public class KegiatanUserActivity extends BaseActivity {
    Toolbar toolbar;
    private RecyclerView rvActivity;
    private KegiatanAdapter adapter;
    private List<Activities> dataActivity = new ArrayList<>();
    Activities activitySelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kegiatan_user);
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_menu_kegiatan));

        rvActivity = findViewById(R.id.rv_activity);
        rvActivity.setLayoutManager(new LinearLayoutManager(context));
        adapter = new KegiatanAdapter(appSession.isAdmin(), dataActivity, context, new AdapterListener<Activities>() {
            @Override
            public void onItemSelected(Activities data) {
                Intent intent = new Intent(context, DetailKegiatanActivity.class);
                intent.putExtra("activity", data);
                startActivity(intent);
            }

            @Override
            public void onItemLongSelected(Activities data) {
                Toast.makeText(context, data.getTitleActivity(), Toast.LENGTH_SHORT).show();
            }
        }, new MenuListener<Activities>() {
            @Override
            public void onEdit(Activities data) {
                Intent intent = new Intent(context, KegiatanAdminActivity.class);
                Toast.makeText(context, data.getActivityId()+"", Toast.LENGTH_SHORT).show();
                intent.putExtra("edit_activity", data);
                startActivity(intent);
            }

            @Override
            public void onDelete(Activities data) {
                Toast.makeText(context, data.getActivityId()+"", Toast.LENGTH_SHORT).show();
                alertSubmitDone(getString(R.string.warning_title), getString(R.string.delete_confirmation, "Kegiatan"), new DialogListener() {
                    @Override
                    public void onPositiveButton() {
                        activitySelected = data;
                        showProgressBar(true);
                        mApiService.deleteActivity("Bearer " + getUserToken(), data.getActivityId()).enqueue(deleteActivity.build());
                    }

                    @Override
                    public void onNegativeButton() {

                    }
                });
            }
        });
        showProgressBar(true);
        mApiService.getActivity("Bearer " + appSession.getData(AppSession.TOKEN)).enqueue(activityCallback.build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (appSession.isAdmin()) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_add, menu);
            return true;
        } else
            return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_toolbar) {
            nextActivity(KegiatanAdminActivity.class);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgressBar(true);
        mApiService.getActivity("Bearer " + appSession.getData(AppSession.TOKEN)).enqueue(activityCallback.build());
    }

    ApiCallback activityCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBar(false);
            ApiData<Activities> apiService = new Gson().fromJson(result, new TypeToken<ApiData<Activities>>(){}.getType());
            dataActivity = apiService.getData();
            adapter.setData(dataActivity);
            rvActivity.setAdapter(adapter);
        }

        @Override
        public void onApiFailure(String errorMessage) {
            showProgressBar(false);
            showToast(errorMessage);
        }
    };

    ApiCallback deleteActivity = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBar(false);
            ApiStatus status = new Gson().fromJson(result, ApiStatus.class);
            showToast(status.getMessage());
            dataActivity.remove(activitySelected);
            adapter.setData(dataActivity);
            rvActivity.setAdapter(adapter);
        }

        @Override
        public void onApiFailure(String errorMessage) {

        }
    };
}
