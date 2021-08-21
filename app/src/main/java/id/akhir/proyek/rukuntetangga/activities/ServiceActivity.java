package id.akhir.proyek.rukuntetangga.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import id.akhir.proyek.rukuntetangga.MainActivity;
import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.activities.admin.AddServiceActivity;
import id.akhir.proyek.rukuntetangga.adapters.EmergencyAdapter;
import id.akhir.proyek.rukuntetangga.apihelper.AppSession;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.models.ApiData;
import id.akhir.proyek.rukuntetangga.models.ApiUser;
import id.akhir.proyek.rukuntetangga.models.Service;
import id.akhir.proyek.rukuntetangga.models.User;

public class ServiceActivity extends BaseActivity {

    Toolbar toolbar;
    private RecyclerView rvEmergency;
    private EmergencyAdapter adapter;
    private List<Service> dataService = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

//        dataService.add(new Service(1, "Cleaning Service", R.drawable.ic_cleaning_service, R.drawable.ic_whatsapp));
//        dataService.add(new Service(2, "Sedot WC", R.drawable.ic_sedot_wc, R.drawable.ic_whatsapp));
//        dataService.add(new Service(3, "Tandon", R.drawable.ic_tandon, R.drawable.ic_whatsapp));
//        dataService.add(new Service(4, "Air Galon", R.drawable.ic_galon, R.drawable.ic_whatsapp));
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_menu_jasa));
        rvEmergency = findViewById(R.id.rv_emergency);
        rvEmergency.setLayoutManager(new LinearLayoutManager(context));
        adapter = new EmergencyAdapter(dataService, false, new AdapterListener<Service>() {
            @Override
            public void onItemSelected(Service data) {
                Toast.makeText(context, data.getServiceName(), Toast.LENGTH_SHORT).show();
                openWhatsApp(data.getPhoneNumber());
            }

            @Override
            public void onItemLongSelected(Service data) {
                Toast.makeText(context, data.getServiceName(), Toast.LENGTH_SHORT).show();
            }
        });
        showProgressBar(true);
        mApiService.getService("Bearer " + appSession.getData(AppSession.TOKEN), 2).enqueue(serviceCallback.build());
        rvEmergency.setAdapter(adapter);
    }

    public void openWhatsApp(String toNumber){
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
        if (appSession.isAdmin()) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_add, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_toolbar) {
            nextActivity(AddServiceActivity.class);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    ApiCallback serviceCallback = new ApiCallback() {
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