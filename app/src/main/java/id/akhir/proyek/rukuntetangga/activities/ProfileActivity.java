package id.akhir.proyek.rukuntetangga.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;

public class ProfileActivity extends BaseActivity {

    Toolbar toolbar;
    private ImageView ivUser;
    private TextView tvUsername, tvAddress, tvGender, tvAddressKtp
            , tvDateBirth, tvEmail, tvPhoneNumber;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initData();
        setData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.label_profile));

        ivUser = findViewById(R.id.iv_user);
        tvUsername = findViewById(R.id.tv_username);
        tvAddress = findViewById(R.id.tv_address_user);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);
        tvGender = findViewById(R.id.tv_gender);
        tvAddressKtp = findViewById(R.id.tv_address_ktp);
        tvDateBirth = findViewById(R.id.tv_date_of_birth);
        tvEmail = findViewById(R.id.tv_email);
        btnLogout = findViewById(R.id.btn_logout);
    }
    
    private void setData() {
        tvUsername.setText("Admin");
        tvAddress.setText("Perumahan Permata Buah Batu C20");
        tvPhoneNumber.setText("081311305521");
        tvAddressKtp.setText("jr. Talago Jaya Nagari Sungai Patai Kecamatan Sungayang ");
        tvEmail.setText("xdamn.xd@gmail.com");
        tvGender.setText("Laki-laki");
        tvDateBirth.setText("Jakarta, 18 September 1997");
        btnLogout.setOnClickListener(v -> {
            Toast.makeText(context, "Logout Proses", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        menu.findItem(R.id.menu_toolbar).setIcon(R.drawable.ic_edit);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_toolbar) {
            Toast.makeText(context, "Edit Data", Toast.LENGTH_SHORT).show();;
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}