package id.akhir.proyek.rukuntetangga.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import id.akhir.proyek.rukuntetangga.MainActivity;
import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText _etPhone, _etPassword;
    private Button _btnLogin;
    private ImageView _eyePassword;
    private boolean _showPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews(R.layout.activity_login);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin) {
            Intent i = new Intent(context, MainActivity.class);
            startActivity(i);
            finish();
        } else if (v.getId() == R.id.show_pass_btn) {
            if (_showPassword) {
                _eyePassword.setImageResource(R.drawable.hide_password);
                _etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                _showPassword = false;
            } else {
                _eyePassword.setImageResource(R.drawable.view_password);
                _etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                _showPassword = true;
            }
        }
    }
}