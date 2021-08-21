package id.akhir.proyek.rukuntetangga.activities.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.apihelper.AppSession;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.models.ApiStatus;

public class InformationActivity extends BaseActivity {

    Toolbar toolbar;
    private EditText etInformation;
    private Button btnUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_menu_informasi));
        etInformation = findViewById(R.id.et_information);
        btnUpload = findViewById(R.id.btn_upload);

        btnUpload.setOnClickListener(v -> {
            String information = etInformation.getText().toString();

            if (!validateData(information)) return;

            showProgressBarUpload(true);
            mApiService.addInformation("Bearer " + appSession.getData(AppSession.TOKEN),information).enqueue(informationCallback.build());
        });
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
}