package id.akhir.proyek.rukuntetangga.activities.user;

import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.models.ApiStatus;

public class ChangePasswordActivity extends BaseActivity {

    Toolbar toolbar;
    EditText etOldPassword, etNewPassword, etConfirmationNewPassword;
    Button btnChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_input_service));

        etOldPassword = findViewById(R.id.et_old_password);
        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmationNewPassword = findViewById(R.id.et_confirmation_new_password);
        btnChange = findViewById(R.id.btn_change);

        btnChange.setOnClickListener(v -> submitData());
    }

    private boolean validateData(String oldPassword, String newPassword, String confirmationNewPassword) {
        if (oldPassword.trim().isEmpty()) {
            etOldPassword.setError(getString(R.string.error_textfield_empty, "Kata Sandi Lama"));
            etOldPassword.requestFocus();
            return false;
        }

        if (newPassword.trim().isEmpty()) {
            etNewPassword.setError(getString(R.string.error_textfield_empty, "Kata Sandi Baru"));
            etNewPassword.requestFocus();
            return false;
        } else if (newPassword.length() < 8) {
            etNewPassword.setError(getString(R.string.error_password_length));
            etNewPassword.requestFocus();
            return false;
        }

        if (confirmationNewPassword.trim().isEmpty()) {
            etConfirmationNewPassword.setError(getString(R.string.error_textfield_empty, "Konfirmasi Kata Sandi Baru"));
            etConfirmationNewPassword.requestFocus();
            return false;
        } else if (!confirmationNewPassword.equals(newPassword)) {
            etConfirmationNewPassword.setError(getString(R.string.error_password_confirmation));
            etConfirmationNewPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void submitData() {
        String oldPassword = etOldPassword.getText().toString();
        String newPassword = etNewPassword.getText().toString();
        String confirmationPassword = etConfirmationNewPassword.getText().toString();

        if (!validateData(oldPassword, newPassword, confirmationPassword)) return;

        showProgressBar(true);
        mApiService.changePassword("Bearer "+ getUserToken(), getUserSession().getUserId(), oldPassword, newPassword)
                .enqueue(apiCallback.build());
    }

    ApiCallback apiCallback = new ApiCallback() {
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
}