package id.akhir.proyek.rukuntetangga.activities;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import id.akhir.proyek.rukuntetangga.LetterListActivity;
import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.activities.user.ComplaintActionActivity;
import id.akhir.proyek.rukuntetangga.activities.user.ComplaintActivity;
import id.akhir.proyek.rukuntetangga.adapters.NotificationAdapter;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.models.ApiData;
import id.akhir.proyek.rukuntetangga.models.Notification;

public class NotificationLogActivity extends BaseActivity {

    Toolbar toolbar;
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Notification> dataNotification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_log);
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_notification));
        recyclerView = findViewById(R.id.rv_notification);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);

        adapter = new NotificationAdapter(dataNotification, context, new AdapterListener<Notification>() {
            @Override
            public void onItemSelected(Notification data) {
                mApiService.setReadNotification("Bearer "+getUserToken(), data.getNotificationId()).enqueue(setReadNotification.build());
                if (data.getReferenceName().equals("Laporan")) {
                    Intent intent;
                    if (data.getLogCode() == 1) {
                        intent = new Intent(context, ComplaintActivity.class);
                    } else {
                        intent = new Intent(context, ComplaintActionActivity.class);
                        intent.putExtra("complaint_id", data.getReferenceId());
                    }
                    startActivity(intent);
                    finish();
                } else if (data.getReferenceName().equals("Surat")) {
                    Intent intent = new Intent(context, LetterListActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(context, VotingListActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onItemLongSelected(Notification data) {
                showToast(data.getBody());
            }
        });

        showProgressBar(true);
        mApiService.getNotificationLog("Bearer " + getUserToken(), getUserSession().getUserId()).enqueue(getNotification.build());
    }


    ApiCallback getNotification = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBar(false);
            ApiData<Notification> notificationApiData = new Gson().fromJson(result, new TypeToken<ApiData<Notification>>(){}.getType());

            dataNotification = notificationApiData.getData();
            adapter.setData(dataNotification);
            recyclerView.setAdapter(adapter);
        }

        @Override
        public void onApiFailure(String errorMessage) {
            showProgressBar(false);
            showToast(errorMessage);
        }
    };

    ApiCallback setReadNotification = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            Log.d("Notification", "Set Read Success");
        }

        @Override
        public void onApiFailure(String errorMessage) {
            Log.d("Notification", "Set Read Fail");
        }
    };
}