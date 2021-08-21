package id.akhir.proyek.rukuntetangga.activities.user;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.adapters.KegiatanAdapter;
import id.akhir.proyek.rukuntetangga.apihelper.AppSession;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.models.Activities;
import id.akhir.proyek.rukuntetangga.models.ApiData;

public class KegiatanUserActivity extends BaseActivity {
    Toolbar toolbar;
    private RecyclerView rvActivity;
    private KegiatanAdapter adapter;
    private List<Activities> dataActivity = new ArrayList<>();

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
        adapter = new KegiatanAdapter(dataActivity, new AdapterListener<Activities>() {
            @Override
            public void onItemSelected(Activities data) {
                Toast.makeText(context, data.getTitleActivity(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongSelected(Activities data) {
                Toast.makeText(context, data.getTitleActivity(), Toast.LENGTH_SHORT).show();
            }
        });
        showProgressBar(true);
        mApiService.getActivity("Bearer " + appSession.getData(AppSession.TOKEN)).enqueue(activityCallback.build());
        rvActivity.setAdapter(adapter);
    }

    ApiCallback activityCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {

            showProgressBar(false);
            ApiData<Activities> apiService = new Gson().fromJson(result, new TypeToken<ApiData<Activities>>(){}.getType());
            dataActivity = apiService.getData();
            adapter.setData(dataActivity);
        }

        @Override
        public void onApiFailure(String errorMessage) {
            showProgressBar(false);
            showToast(errorMessage);
        }
    };
}
