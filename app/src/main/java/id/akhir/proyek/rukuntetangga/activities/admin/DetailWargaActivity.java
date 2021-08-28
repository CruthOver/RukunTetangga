package id.akhir.proyek.rukuntetangga.activities.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.activities.user.KegiatanUserActivity;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.listener.DialogListener;
import id.akhir.proyek.rukuntetangga.models.ApiStatus;
import id.akhir.proyek.rukuntetangga.models.ApiUser;
import id.akhir.proyek.rukuntetangga.models.User;

public class DetailWargaActivity extends BaseActivity {
    Toolbar toolbar;
    TextView tvName, tvPhoneNumber, tvAddress, tvGender,
        tvDateBirth, tvBirthPlace, tvNik, tvReligion, tvStatus, tvPendidikan;

    User detailUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_warga);
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_detail_warga));

        detailUser = getIntent().getParcelableExtra("user");
        tvName = findViewById(R.id.tv_name);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);
        tvAddress = findViewById(R.id.tv_address);
        tvDateBirth = findViewById(R.id.tv_birth_date);
        tvBirthPlace  = findViewById(R.id.tv_birth_place);
        tvNik = findViewById(R.id.tv_nik);
        tvReligion = findViewById(R.id.tv_religion);
        tvStatus = findViewById(R.id.tv_status);
        tvPendidikan = findViewById(R.id.tv_pendidikan);
        tvGender = findViewById(R.id.tv_gender);
        setData();
    }

    private void setData() {
        String gender = detailUser.getGender().equals("0") ? "Laki-laki" :"Perempuan";
        Toast.makeText(context, detailUser.getDateBirth(), Toast.LENGTH_SHORT).show();
        tvName.setText(detailUser.getFullName());
        tvPhoneNumber.setText(detailUser.getPhoneNumber());
        tvAddress.setText(detailUser.getAddress());
        tvDateBirth.setText(detailUser.getDateBirth());
        tvBirthPlace.setText(detailUser.getBirthPlace());
        tvNik.setText(detailUser.getNikWarga());
        tvGender.setText(gender);
        tvReligion.setText(detailUser.getAgama());
        tvStatus.setText(detailUser.getStatusPerkawinan());
        tvPendidikan.setText(detailUser.getPendidikan());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            alertSubmitDone(getString(R.string.warning_title), getString(R.string.delete_confirmation, "Warga"), new DialogListener() {
                @Override
                public void onPositiveButton() {
                    showProgressBar(true);
                    mApiService.deleteUser("Bearer " + getUserToken(), detailUser.getUserId()).enqueue(deleteUserCallback.build());
                }

                @Override
                public void onNegativeButton() {

                }
            });
            return true;
        } else if (item.getItemId() == R.id.edit) {
            Intent intent = new Intent(context, AddDataWargaActivity.class);
            intent.putExtra("edit_user", detailUser);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgressBar(true);
        mApiService.getWargaById("Bearer "+ getUserToken(), detailUser.getUserId()).enqueue(getUserIdCallback.build());
    }

    ApiCallback deleteUserCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBar(false);
            ApiStatus status = new Gson().fromJson(result, ApiStatus.class);
            showToast(status.getMessage());
            finish();
        }

        @Override
        public void onApiFailure(String errorMessage) {
            showProgressBar(false);
            showToast(errorMessage);
        }
    };

    ApiCallback getUserIdCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBar(false);
            ApiUser<User> apiUser = new Gson().fromJson(result, new TypeToken<ApiUser<User>>(){}.getType());
            detailUser = apiUser.getData();
            setData();
        }

        @Override
        public void onApiFailure(String errorMessage) {
            showProgressBar(false);
            showToast(errorMessage);
        }
    };
}