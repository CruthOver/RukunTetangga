package id.akhir.proyek.rukuntetangga.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.apihelper.AppSession;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.helpers.DatePickerFragment;
import id.akhir.proyek.rukuntetangga.listener.UniversalListener;
import id.akhir.proyek.rukuntetangga.models.ApiStatus;
import id.akhir.proyek.rukuntetangga.models.ApiUser;
import id.akhir.proyek.rukuntetangga.models.User;

public class EditProfileActivity extends BaseActivity {
    private User user;

    private EditText etName, etEmail, etPhoneNumber, etAddress
            , etBirthPlace, etBirthDate, etNik, etJob;
    private Spinner spGender, spReligion, spPendidikan, spStatus;
    ArrayAdapter<String> adapterReligion, adapterPendidikan
            , adapterGender, adapterStatus;
    String religion, statusMarried, pendidikan;
    int gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initData();
    }

    private void initData() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_edit_profile));
        user = getUserSession();

        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etAddress = findViewById(R.id.et_address);
        etNik = findViewById(R.id.et_nik);
        etJob = findViewById(R.id.et_job);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        etBirthDate = findViewById(R.id.et_birth_date);
        etBirthPlace = findViewById(R.id.et_birth_place);
        spGender = findViewById(R.id.sp_gender);
        spReligion = findViewById(R.id.sp_religion);
        spPendidikan = findViewById(R.id.sp_pendidikan);
        spStatus = findViewById(R.id.sp_status_married);
        Button btnUpdate = findViewById(R.id.btn_update_profile);

        setSpinner();
        setData();

        etBirthDate.setOnClickListener(v -> {
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
        });

        btnUpdate.setOnClickListener(v -> submitData());
    }

    private void setData() {
        etName.setText(user.getFullName());
        etEmail.setText(user.getEmail());
        etPhoneNumber.setText(user.getPhoneNumber());
        etBirthPlace.setText(user.getBirthPlace());
        etBirthDate.setText(user.getDateBirth());
        etNik.setText(user.getNikWarga());
        etJob.setText(user.getPekerjaan());
        etAddress.setText(user.getAddress());

        if (!user.getAgama().equals("") || user.getAgama() != null) {
            int spinnerPosition = adapterReligion.getPosition(user.getAgama());
            spReligion.setSelection(spinnerPosition);
        } else {
            spReligion.setSelection(0);
        }

        Log.d("Pendidikan", user.getGender());
        if (!user.getPendidikan().equals("") || user.getPendidikan() != null) {
            int spinnerPosition = adapterPendidikan.getPosition(user.getPendidikan());
            spPendidikan.setSelection(spinnerPosition);
        } else {
            spPendidikan.setSelection(0);
        }

        if (!user.getGender().equals("") || user.getGender() != null) {
            String genderName = user.getGender().equals("1") ? "Perempuan" : "Laki-laki";
            int spinnerPosition = adapterGender.getPosition(genderName);
            spGender.setSelection(spinnerPosition);
            if (spGender.getSelectedItem().toString().equals("Perempuan")) {
                gender = 1;
            } else
                gender = 0;
        } else {
            spGender.setSelection(0);
        }

        if (!user.getStatusPerkawinan().equals("") || user.getStatusPerkawinan() != null) {
            int spinnerPosition = adapterStatus.getPosition(user.getStatusPerkawinan().toUpperCase());
            spStatus.setSelection(spinnerPosition);
        } else {
            spStatus.setSelection(0);
        }
    }

    private void setSpinner() {
        adapterReligion = new ArrayAdapter<>(context,
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


        adapterGender = new ArrayAdapter<>(context,
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

        adapterStatus = new ArrayAdapter<>(context,
                R.layout.spinner_position, getResources().getStringArray(R.array.status_perkawinan));
        adapterStatus.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spStatus.setAdapter(adapterStatus);

        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                statusMarried = spStatus.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adapterPendidikan = new ArrayAdapter<>(context,
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

    private boolean validateData(String fullName, String email, String address, String nik, String phoneNumber,
                                 String birthPlace, String birthDate, String job) {
        if (fullName.trim().isEmpty() || fullName.equals("")) {
            etName.setError(getString(R.string.error_textfield_empty, "Nama"));
            etName.requestFocus();
            return false;
        }

        if (email.trim().isEmpty() || email.equals("")) {
            etEmail.setError(getString(R.string.error_textfield_empty, "Email"));
            etEmail.requestFocus();
            return false;
        }

        if (phoneNumber.trim().isEmpty() || phoneNumber.equals("")) {
            etPhoneNumber.setError(getString(R.string.error_textfield_empty, "Nomor Telepon"));
            etPhoneNumber.requestFocus();
            return false;
        }

        if (address.trim().isEmpty() || address.equals("")) {
            etAddress.setError(getString(R.string.error_textfield_empty, "Alamat"));
            etAddress.requestFocus();
            return false;
        }

        if (nik.trim().isEmpty() || nik.equals("")) {
            etNik.setError(getString(R.string.error_textfield_empty, "NIK"));
            etNik.requestFocus();
            return false;
        }

        if (birthPlace.trim().isEmpty() || birthPlace.equals("")) {
            etBirthPlace.setError(getString(R.string.error_textfield_empty, "Tempat Lahir"));
            etBirthPlace.requestFocus();
            return false;
        }

        if (birthDate.trim().isEmpty() || birthDate.equals("")) {
            etBirthDate.setError(getString(R.string.error_textfield_empty, "Tanggal Lahir"));
            etBirthDate.requestFocus();
            return false;
        }

        if (job.trim().isEmpty() || job.equals("")) {
            etJob.setError(getString(R.string.error_textfield_empty, "Pekerjaan"));
            etJob.requestFocus();
            return false;
        }
        return true;
    }

    private void submitData() {
        String fullName = etName.getText().toString();
        String email = etEmail.getText().toString();
        String address = etAddress.getText().toString();
        String nik = etNik.getText().toString();
        String birthPlace = etBirthPlace.getText().toString();
        String birthDate = etBirthDate.getText().toString();
        String job = etJob.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();

        if (!validateData(fullName, email, address, nik, phoneNumber, birthPlace, birthDate, job)) return;

        showProgressBar(true);
        mApiService.updateProfile("Bearer "+ getUserToken(), user.getUserId(), fullName, email, birthDate,
                birthPlace, nik, address, phoneNumber, gender, religion, pendidikan, statusMarried, job).enqueue(updateProfileCallback.build());
    }

    ApiCallback updateProfileCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            ApiStatus apiStatus = new Gson().fromJson(result, ApiStatus.class);
            Toast.makeText(context, apiStatus.getMessage(), Toast.LENGTH_SHORT).show();
            ApiUser<User> apiUser = new Gson().fromJson(result, new TypeToken<ApiUser<User>>(){}.getType());
            appSession.setData(AppSession.USER, new Gson().toJson(apiUser.getData()));
            showProgressBar(false);
            finish();
        }

        @Override
        public void onApiFailure(String errorMessage) {
            showProgressBar(false);
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }
    };
}