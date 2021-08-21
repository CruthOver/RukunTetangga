package id.akhir.proyek.rukuntetangga.activities.user;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;

public class MusrenbangUserActivity extends BaseActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musrenbang_user);
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_menu_musrenbang));
    }
}