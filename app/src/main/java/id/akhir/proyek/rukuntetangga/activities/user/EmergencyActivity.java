package id.akhir.proyek.rukuntetangga.activities.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.adapters.EmergencyAdapter;
import id.akhir.proyek.rukuntetangga.apihelper.AppSession;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.models.ApiData;
import id.akhir.proyek.rukuntetangga.models.Service;

public class EmergencyActivity extends BaseActivity {

    private RecyclerView rvEmergency;
    private EmergencyAdapter adapter;
    private List<Service> dataService = new ArrayList<>();

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

//        dataService.add(new Service(1, "Polisi", R.drawable.ic_police_station, R.drawable.ic_home));
//        dataService.add(new Service(2, "Ambulance", R.drawable.ic_ambulance, R.drawable.ic_home));
//        dataService.add(new Service(3, "Pemadam Kebakaran", R.drawable.ic_firefighter, R.drawable.ic_home));
//        dataService.add(new Service(4, "Satpam", R.drawable.ic_security, R.drawable.ic_home));
//        dataService.add(new Service(5, "Puskesmas", R.drawable.ic_hospital, R.drawable.ic_home));
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_menu_darurat));
        rvEmergency = findViewById(R.id.rv_emergency);
        rvEmergency.setLayoutManager(new LinearLayoutManager(context));
        adapter = new EmergencyAdapter(dataService, true, new AdapterListener<Service>() {
            @Override
            public void onItemSelected(Service data) {
                Toast.makeText(context, data.getServiceName(), Toast.LENGTH_SHORT).show();
                Uri number = Uri.parse("tel:"+data.getPhoneNumber());
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
            }

            @Override
            public void onItemLongSelected(Service data) {
                Toast.makeText(context, data.getServiceName(), Toast.LENGTH_SHORT).show();
            }
        });
        showProgressBar(true);
        mApiService.getService("Bearer " + appSession.getData(AppSession.TOKEN), 1).enqueue(emergencyCallback.build());
        rvEmergency.setAdapter(adapter);
    }

    ApiCallback emergencyCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {

            showProgressBar(false);
            ApiData<Service> apiService = new Gson().fromJson(result, new TypeToken<ApiData<Service>>(){}.getType());
            dataService = apiService.getData();
            adapter.setData(dataService);
        }

        @Override
        public void onApiFailure(String errorMessage) {
            showProgressBar(false);
            showToast(errorMessage);
        }
    };
}