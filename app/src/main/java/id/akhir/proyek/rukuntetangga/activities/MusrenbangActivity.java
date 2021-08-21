package id.akhir.proyek.rukuntetangga.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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
        setData();
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

            }

            @Override
            public void onItemLongSelected(Musrenbang data) {

            }
        });
        rvMusrenbang.setAdapter(adapter);
    }

    private void setData() {
        dataMusrenbang.add(new Musrenbang("Musrenbang 2021", 20));
        dataMusrenbang.add(new Musrenbang("Musrenbang 2020", 20));
        dataMusrenbang.add(new Musrenbang("Musrenbang 2019", 20));
        dataMusrenbang.add(new Musrenbang("Musrenbang 2018", 20));
        dataMusrenbang.add(new Musrenbang("Musrenbang 2017", 20));
        dataMusrenbang.add(new Musrenbang("Musrenbang 2016", 20));
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
}