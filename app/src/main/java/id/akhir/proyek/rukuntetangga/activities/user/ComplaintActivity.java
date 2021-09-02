package id.akhir.proyek.rukuntetangga.activities.user;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.adapters.ComplaintAdapter;
import id.akhir.proyek.rukuntetangga.adapters.EmergencyAdapter;
import id.akhir.proyek.rukuntetangga.apihelper.AppSession;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.models.ApiData;
import id.akhir.proyek.rukuntetangga.models.ApiStatus;
import id.akhir.proyek.rukuntetangga.models.Complaint;
import id.akhir.proyek.rukuntetangga.models.Service;

public class ComplaintActivity extends BaseActivity {
    Toolbar toolbar;

    private RecyclerView rvComplaint;
    private ComplaintAdapter adapter;
    private List<Complaint> dataComplaint = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_menu_complaint));
        rvComplaint = findViewById(R.id.rv_complaint);
        rvComplaint.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ComplaintAdapter(appSession.isAdmin(), dataComplaint, context, new AdapterListener<Complaint>() {
            @Override
            public void onItemSelected(Complaint data) {
                if (data.getStatusComplaint() == 2) {
                    Intent intent = new Intent(context, ComplaintActionActivity.class);
                    intent.putExtra("complaint_id", data.getComplaintId());
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongSelected(Complaint data) {
                Toast.makeText(context, data.getTitleComplaint(), Toast.LENGTH_SHORT).show();
            }
        });
        showProgressBar(true);
        mApiService.getComplaint("Bearer " + appSession.getData(AppSession.TOKEN)).enqueue(complaintCallback.build());
        rvComplaint.setAdapter(adapter);
    }

    ApiCallback complaintCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBar(false);
            ApiData<Complaint> apiService = new Gson().fromJson(result, new TypeToken<ApiData<Complaint>>(){}.getType());
            dataComplaint = apiService.getData();
            adapter.setData(dataComplaint);
        }

        @Override
        public void onApiFailure(String errorMessage) {
            showProgressBar(false);
            showToast(errorMessage);
        }
    };
}