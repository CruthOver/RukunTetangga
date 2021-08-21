package id.akhir.proyek.rukuntetangga.activities.admin;

import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.helpers.DatePickerFragment;
import id.akhir.proyek.rukuntetangga.listener.UniversalListener;
import id.akhir.proyek.rukuntetangga.models.ApiData;
import id.akhir.proyek.rukuntetangga.models.ApiStatus;
import id.akhir.proyek.rukuntetangga.models.Letter;
import id.akhir.proyek.rukuntetangga.models.Position;

public class AddDataWargaActivity extends BaseActivity implements View.OnClickListener {

    Toolbar toolbar;
    EditText etName, etBirthPlace, etBirthDate, etNik;
    EditText etNoTelp, etEmail, etAddress;
    Spinner spReligion, spGender, spMarriedStatus, spPendidikan;
    Button btnUpload;

    String religion, statusMarried, pendidikan;
    int gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data_warga);
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_add_data_warga));

        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etNoTelp = findViewById(R.id.et_phone_number);
        etBirthDate = findViewById(R.id.et_birth_date);
        etBirthPlace = findViewById(R.id.et_birth_place);
        etAddress = findViewById(R.id.et_address);
        etNik = findViewById(R.id.et_nik);
        spReligion = findViewById(R.id.sp_religion);
        spPendidikan = findViewById(R.id.sp_pendidikan);
        spGender = findViewById(R.id.sp_gender);
        spMarriedStatus = findViewById(R.id.sp_status_married);
        btnUpload = findViewById(R.id.btn_upload);

        setSpinner();
        etBirthDate.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
    }

    private void setSpinner() {
        final ArrayAdapter<String> adapterReligion = new ArrayAdapter<String>(context,
                R.layout.spinner_position, getResources().getStringArray(R.array.agama));
        adapterReligion.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spReligion.setAdapter(adapterReligion);

        spReligion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                religion = spReligion.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        final ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(context,
                R.layout.spinner_position, getResources().getStringArray(R.array.gender));
        adapterGender.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spGender.setAdapter(adapterGender);

        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spGender.getSelectedItem().toString().equals("Perempuan")) {
                    gender = 1;
                } else
                    gender = 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final ArrayAdapter<String> adapterStatus = new ArrayAdapter<String>(context,
                R.layout.spinner_position, getResources().getStringArray(R.array.status_perkawinan));
        adapterStatus.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spMarriedStatus.setAdapter(adapterStatus);

        spMarriedStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                statusMarried = spMarriedStatus.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final ArrayAdapter<String> adapterPendidikan = new ArrayAdapter<String>(context,
                R.layout.spinner_position, getResources().getStringArray(R.array.pendidikan));
        adapterPendidikan.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spPendidikan.setAdapter(adapterPendidikan);

        spPendidikan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pendidikan = spPendidikan.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.et_birth_date) {
            DatePickerFragment newDatePicker = new DatePickerFragment(setDateToday(), 0);
            newDatePicker.holder = etBirthDate;
            newDatePicker.listener = new UniversalListener() {
                @Override
                public void onSetData() {
                    Toast.makeText(context, etBirthDate.getText().toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResetData() {
                    Toast.makeText(context, "Pilih Tanggal", Toast.LENGTH_SHORT).show();
                }
            };
            newDatePicker.show(getSupportFragmentManager(), "datePicker");
        } else if (v.getId() == R.id.btn_upload) {
            submitData();
        }
    }

    private boolean validateData(String fullName, String address, String phoneNumber, String nik, String birthDate, String birthPlace, String email) {
        if (fullName.trim().isEmpty()) {
            etName.setError(getString(R.string.error_textfield_empty, "Nama Lengkap"));
            etName.requestFocus();
            return false;
        }

        if (phoneNumber.trim().isEmpty()) {
            etNoTelp.setError(getString(R.string.error_textfield_empty, "Nomor Telepon"));
            etNoTelp.requestFocus();
            return false;
        }

        if (nik.trim().isEmpty()) {
            etNik.setError(getString(R.string.error_textfield_empty, "NIK"));
            etNik.requestFocus();
            return false;
        }

        if (birthDate.trim().isEmpty()) {
            etBirthDate.setError(getString(R.string.error_textfield_empty, "Tanggal lahir"));
            etBirthDate.requestFocus();
            return false;
        }

        if (birthPlace.trim().isEmpty()) {
            etBirthPlace.setError(getString(R.string.error_textfield_empty, "Tempat lahir"));
            etBirthPlace.requestFocus();
            return false;
        }

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.trim().isEmpty()) {
            etEmail.setError(getString(R.string.error_textfield_empty, "Email"));
            etEmail.requestFocus();
            return false;
        }

        if (address.trim().isEmpty()) {
            etAddress.setError(getString(R.string.error_textfield_empty, "Alamat"));
            etAddress.requestFocus();
            return false;
        }

//        if (email.trim().matches(emailPattern)) {
//            etEmail.setError(getString(R.string.error_email_invalid));
//            etEmail.requestFocus();
//            return false;
//        }

        return true;
    }

    private void submitData() {
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String phoneNumber = etNoTelp.getText().toString();
        String birthDate = etBirthDate.getText().toString();
        String birthPlace = etBirthPlace.getText().toString();
        String nik = etNik.getText().toString();
        String address = etAddress.getText().toString();

        if (!validateData(name, address, phoneNumber, nik, birthDate, birthPlace, email)) return;

        Log.d("Name", name);
        Log.d("Phone Number", name);
        Log.d("Email", name);
        Log.d("address", name);
        Log.d("nik", name);
        showProgressBarUpload(true);
        mApiService.addWarga("Bearer " + getUserToken(), name, address, phoneNumber, nik, birthDate, birthPlace, email,
                gender, religion, statusMarried, pendidikan).enqueue(addWargaCallback.build());
    }

    ApiCallback addWargaCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBarUpload(false);
            ApiStatus status = new Gson().fromJson(result, new TypeToken<ApiStatus>(){}.getType());
            Toast.makeText(context, status.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onApiFailure(String errorMessage) {
            showProgressBarUpload(false);
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }
    };
}