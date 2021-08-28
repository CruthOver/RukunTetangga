package id.akhir.proyek.rukuntetangga.activities.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.activities.user.KegiatanUserActivity;
import id.akhir.proyek.rukuntetangga.apihelper.AppSession;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.models.ApiStatus;
import id.akhir.proyek.rukuntetangga.models.Information;

public class InformationActivity extends BaseActivity {

    Toolbar toolbar;
    private EditText etInformation;
    private Button btnUpload;
    Information information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);

        etInformation = findViewById(R.id.et_information);
        btnUpload = findViewById(R.id.btn_upload);
        if (ubahData()) {
            setToolbar(toolbar, getString(R.string.title_menu_ubah_informasi));
            btnUpload.setText(R.string.label_update);
        } else {
            setToolbar(toolbar, getString(R.string.title_menu_informasi));
            btnUpload.setText(R.string.label_upload);
        }

        btnUpload.setOnClickListener(v -> {
            String _information = etInformation.getText().toString();

            if (!validateData(_information)) return;

            showProgressBarUpload(true);
            if (ubahData()) {
                mApiService.updateInformation("Bearer " + appSession.getData(AppSession.TOKEN), information.getId() ,_information)
                        .enqueue(updateInformationCallback.build());
            } else {
                mApiService.addInformation("Bearer " + appSession.getData(AppSession.TOKEN),_information)
                        .enqueue(informationCallback.build());
            }
        });
    }

    private boolean ubahData() {
        if (getIntent().getParcelableExtra("edit_information") != null) {
            information = getIntent().getParcelableExtra("edit_information");
            etInformation.setText(information.getInformation());
            return true;
        }
        return false;
    }

    private boolean validateData(String information){
        if (information.trim().isEmpty()) {
            etInformation.setError(getString(R.string.error_textfield_empty, "Informasi"));
            etInformation.requestFocus();
            return false;
        }

        return true;
    }

    ApiCallback informationCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBarUpload(false);
            ApiStatus status = new Gson().fromJson(result, ApiStatus.class);
            Toast.makeText(context, status.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onApiFailure(String errorMessage) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }
    };

    ApiCallback updateInformationCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBarUpload(false);
            ApiStatus status = new Gson().fromJson(result, ApiStatus.class);
            Toast.makeText(context, status.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onApiFailure(String errorMessage) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }
    };
}