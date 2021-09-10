package id.akhir.proyek.rukuntetangga.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.activities.VotingListActivity;
import id.akhir.proyek.rukuntetangga.activities.admin.AddMusrenbangActivity;
import id.akhir.proyek.rukuntetangga.adapters.MusrenbangAdapter;
import id.akhir.proyek.rukuntetangga.adapters.VotingAdapter;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.models.ApiData;
import id.akhir.proyek.rukuntetangga.models.Musrenbang;
import id.akhir.proyek.rukuntetangga.models.Voting;

public class MusrenbangActivity extends BaseActivity {

    Toolbar toolbar;
    private RecyclerView rvMusrenbang;
    private List<Musrenbang> dataMusrenbang = new ArrayList<>();
    private MusrenbangAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musrenbang_admin);
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_menu_musrenbang));
        rvMusrenbang = findViewById(R.id.rv_musrenbang);
        rvMusrenbang.setLayoutManager(new LinearLayoutManager(context));
        adapter = new MusrenbangAdapter(dataMusrenbang, context, new AdapterListener<Musrenbang>() {
            @Override
            public void onItemSelected(Musrenbang data) {
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.getUrlBerkas()));
                Intent intent = new Intent(context, PdfViewActivity.class);
                intent.putExtra("pdf_url", data.getUrlBerkas());
                startActivity(intent);
            }

            @Override
            public void onItemLongSelected(Musrenbang data) {

            }
        });

        showProgressBar(true);
        mApiService.getMusrenbang("Bearer "+ getUserToken()).enqueue(musrenbangCallback.build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgressBar(true);
        mApiService.getMusrenbang("Bearer "+ getUserToken()).enqueue(musrenbangCallback.build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getIsAdmin()) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_add, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_toolbar) {
            nextActivity(AddMusrenbangActivity.class);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    ApiCallback musrenbangCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBar(false);
            ApiData<Musrenbang> apiMusrenbang = new Gson().fromJson(result, new TypeToken<ApiData<Musrenbang>>(){}.getType());
            dataMusrenbang = apiMusrenbang.getData();
            adapter.setData(dataMusrenbang);
            rvMusrenbang.setAdapter(adapter);
        }

        @Override
        public void onApiFailure(String errorMessage) {
            showProgressBar(false);
            showToast(errorMessage);
        }
    };
}