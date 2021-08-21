package id.akhir.proyek.rukuntetangga.activities.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.CursorLoader;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.apihelper.AppSession;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.helpers.DatePickerFragment;
import id.akhir.proyek.rukuntetangga.helpers.TimePicker;
import id.akhir.proyek.rukuntetangga.listener.UniversalListener;
import id.akhir.proyek.rukuntetangga.models.ApiStatus;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class KegiatanAdminActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_CHOOSE_PHOTO = 2;
    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 3;

    Toolbar toolbar;

    private EditText etDescription, etDate, etHour;
    private ImageView ivImage, ivPreviewImage, ivRemoveImage;
    private Button btnUpload;
    Uri uri;
    File _filePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kegiatan_admin);
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_menu_kegiatan));
        etDescription = findViewById(R.id.et_description_activity);
        etDate = findViewById(R.id.et_date_activity);
        etHour = findViewById(R.id.et_hour_activity);
        ivImage = findViewById(R.id.iv_kegiatan);
        ivPreviewImage = findViewById(R.id.preview_image);
        ivRemoveImage = findViewById(R.id.iv_remove_image);

        if (uri == null) {
            ivPreviewImage.setVisibility(View.GONE);
            ivRemoveImage.setVisibility(View.GONE);
        }
        ivRemoveImage.setOnClickListener(v -> {
            ivPreviewImage.setVisibility(View.GONE);
            ivRemoveImage.setVisibility(View.GONE);
            ivImage.setVisibility(View.VISIBLE);
            uri = null;
        });
        btnUpload = findViewById(R.id.btn_upload);

        btnUpload.setOnClickListener(this);
        etDate.setOnClickListener(this);
        etHour.setOnClickListener(this);
        ivImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.et_date_activity) {
            DatePickerFragment newDatePicker = new DatePickerFragment(setDateOneMonth(), setDateToday());
            newDatePicker.holder = etDate;
            newDatePicker.listener = new UniversalListener() {
                @Override
                public void onSetData() {
                    Toast.makeText(context, etDate.getText().toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResetData() {
                    Toast.makeText(context, "Pilih Tanggal", Toast.LENGTH_SHORT).show();
                }
            };
            newDatePicker.show(getSupportFragmentManager(), "datePicker");
//            newDatePicker.dpd.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if (v.getId() == R.id.et_hour_activity) {
            Toast.makeText(context, "Pilih Waktu", Toast.LENGTH_SHORT).show();
            TimePicker timePicker = new TimePicker();
            timePicker.holder = etHour;
            timePicker.listener = new UniversalListener() {
                @Override
                public void onSetData() {
                    Toast.makeText(context, etDate.getText().toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResetData() {
                    Toast.makeText(context, "Pilih Tanggal", Toast.LENGTH_SHORT).show();
                }
            };
            timePicker.show(getSupportFragmentManager(), "timePicker");
        } else if (v.getId() == R.id.btn_upload) {
            submitData();
        } else if (v.getId() == R.id.iv_kegiatan) {
            pickImageGallery();
        }
    }

    private void pickImageGallery() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkPermission(2))
                dispatchChoosePictureIntent();
        } else
            dispatchChoosePictureIntent();
    }

    private void dispatchChoosePictureIntent(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.title_choose_image)),
                REQUEST_CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (checkAllPermission()) {
                    dispatchChoosePictureIntent();
                }
            } else {
                showToast(R.string.error_permission_denied);
            }
            return;
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHOOSE_PHOTO && resultCode == RESULT_OK && data != null){
            uri = data.getData();
            if (uri != null) {
                _filePhoto = new File(getRealPathFromURI(uri));
                ivPreviewImage.setVisibility(View.VISIBLE);
                ivRemoveImage.setVisibility(View.VISIBLE);
                ivImage.setVisibility(View.GONE);
                ivPreviewImage.setImageURI(uri);
            }
        }
    }

    private void submitData() {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), _filePhoto);
//
//        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("foto_kegiatan", _filePhoto.getName(), requestFile);
//        RequestBody rb_foto = RequestBody.create(okhttp3.MultipartBody.FORM, _filePhoto);
        RequestBody rbToken = RequestBody.create(MultipartBody.FORM, appSession.getData(AppSession.TOKEN));
        RequestBody rbContentType = RequestBody.create(MultipartBody.FORM, "multipart/form-data");
        RequestBody rbDescription = RequestBody.create(MultipartBody.FORM, etDescription.getText().toString());
        RequestBody rbDateKegiatan = RequestBody.create(MultipartBody.FORM, etDate.getText().toString());
        RequestBody rbTimeKegiatan = RequestBody.create(MultipartBody.FORM, etHour.getText().toString());

        showProgressBarUpload(true);
        mApiService.addKegiatan("Bearer " + getUserToken(), rbDescription,
                rbDateKegiatan, rbTimeKegiatan, body).enqueue(addKegiatanCallback.build());
    }

    ApiCallback addKegiatanCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBarUpload(false);
            ApiStatus status = new Gson().fromJson(result, ApiStatus.class);
            Toast.makeText(context, status.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onApiFailure(String errorMessage) {
            showProgressBar(false);
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }
    };
}