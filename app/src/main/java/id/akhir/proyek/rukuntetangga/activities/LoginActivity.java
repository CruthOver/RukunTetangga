package id.akhir.proyek.rukuntetangga.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;

import id.akhir.proyek.rukuntetangga.MainActivity;
import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.listener.DialogListener;
import id.akhir.proyek.rukuntetangga.models.ApiData;
import id.akhir.proyek.rukuntetangga.models.ApiUser;
import id.akhir.proyek.rukuntetangga.models.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText _etPhone, _etPassword;
    private Button _btnLogin;
    private ImageView _eyePassword;
    private boolean _showPassword, isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkSession();
        initData();
    }

    private void initData() {
        _etPhone = findViewById(R.id.etPhone);
        _etPassword = findViewById(R.id.etPassword);
        _eyePassword = findViewById(R.id.show_pass_btn);
        _btnLogin = findViewById(R.id.btnLogin);
        _showPassword = false;
        
        _btnLogin.setOnClickListener(this);
        _eyePassword.setOnClickListener(this);
    }

    private boolean validateData(String phoneNumber, String password){
        if (phoneNumber.trim().isEmpty()) {
            _etPhone.setError(getString(R.string.error_phone_number_empty));
            _etPhone.requestFocus();
            return false;
        } else if (phoneNumber.length() < 11) {
            _etPhone.setError(getString(R.string.error_phone_number_length));
            _etPhone.requestFocus();
            return false;
        }

        if (password.trim().isEmpty()) {
            _etPassword.setError(getString(R.string.error_password_empty));
            _etPassword.requestFocus();
            return false;
        } else if (password.length() < 6) {
            _etPassword.setError(getString(R.string.error_password_length));
            _etPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void login() {
        String phoneNumber = _etPhone.getText().toString();
        String password = _etPassword.getText().toString();

        if (!validateData(phoneNumber, password)) return;

        showProgressBar(true);
        mApiService.signInRequest(phoneNumber, password).enqueue(loginCallback.build());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin) {
            login();
        } else if (v.getId() == R.id.show_pass_btn) {
            if (_showPassword) {
                _eyePassword.setImageResource(R.drawable.hide_password);
                _etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                _showPassword = false;
            } else {
                _eyePassword.setImageResource(R.drawable.view_password);
                _etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                _showPassword = true;
            }
        }
    }

    ApiCallback loginCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBar(false);
            ApiUser<User> apiUser = new Gson().fromJson(result, new TypeToken<ApiUser<User>>(){}.getType());
            User user = apiUser.getData();
            if(user.getAuthToken()!=null && user.getUserId()!=0) {
                appSession.createSession(user.getAuthToken(), user);
                nextActivity(MainActivity.class);
                finish();
            }
        }

        @Override
        public void onApiFailure(String errorMessage) {
            showProgressBar(false);
            showToast(errorMessage);
        }
    };
}