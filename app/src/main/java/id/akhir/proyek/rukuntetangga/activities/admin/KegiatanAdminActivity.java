package id.akhir.proyek.rukuntetangga.activities.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.CursorLoader;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.activities.user.KegiatanUserActivity;
import id.akhir.proyek.rukuntetangga.apihelper.AppSession;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.helpers.DatePickerFragment;
import id.akhir.proyek.rukuntetangga.helpers.TimePicker;
import id.akhir.proyek.rukuntetangga.listener.UniversalListener;
import id.akhir.proyek.rukuntetangga.models.Activities;
import id.akhir.proyek.rukuntetangga.models.ApiStatus;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class KegiatanAdminActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_CHOOSE_PHOTO = 2;
    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 3;

    Toolbar toolbar;

    private EditText etDescription, etDate, etHour, etLocation, etDitujukan;
    private ImageView ivImage, ivPreviewImage, ivRemoveImage;
    private Button btnUpload;
    Uri uri;
    File _filePhoto;
    Activities activities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kegiatan_admin);
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);

        etDescription = findViewById(R.id.et_description_activity);
        etDate = findViewById(R.id.et_date_activity);
        etHour = findViewById(R.id.et_hour_activity);
        etLocation = findViewById(R.id.et_location_activity);
        etDitujukan = findViewById(R.id.et_ditujukan);
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

        if (ubahData()) {
            setToolbar(toolbar, getString(R.string.title_menu_ubah_kegiatan));
            btnUpload.setText(R.string.label_update);
            setData();
        } else {
            setToolbar(toolbar, getString(R.string.title_menu_kegiatan));
            btnUpload.setText(R.string.label_upload);
        }

        btnUpload.setOnClickListener(this);
        etDate.setOnClickListener(this);
        etHour.setOnClickListener(this);
        ivImage.setOnClickListener(this);
    }

    private boolean ubahData() {
        if (getIntent().getParcelableExtra("edit_activity") != null) {
            activities = getIntent().getParcelableExtra("edit_activity");
            Toast.makeText(context, activities.getActivityId() +"", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void setData() {
        etDescription.setText(activities.getTitleActivity());
        etDate.setText(activities.getDateActivity());
        etHour.setText(activities.getHour());
        etLocation.setText(activities.getLocation());
        etDitujukan.setText(activities.getDitujukan());
        if (activities.getImageActivity() != null) {
            ivPreviewImage.setVisibility(View.VISIBLE);
            ivRemoveImage.setVisibility(View.VISIBLE);
            ivImage.setVisibility(View.GONE);
            Picasso.get().load(activities.getImageActivity())
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.broken_image)
                    .into(ivPreviewImage);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.et_date_activity) {
            DatePickerFragment newDatePicker = new DatePickerFragment(0, 0);
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
            if (ubahData()) {
                editData();
            } else {
                submitData();
            }
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
//      MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("foto_kegiatan", _filePhoto.getName(), requestFile);
        RequestBody rbDescription = RequestBody.create(MultipartBody.FORM, etDescription.getText().toString());
        RequestBody rbDateKegiatan = RequestBody.create(MultipartBody.FORM, etDate.getText().toString());
        RequestBody rbTimeKegiatan = RequestBody.create(MultipartBody.FORM, etHour.getText().toString());
        RequestBody rbLocation = RequestBody.create(MultipartBody.FORM, etLocation.getText().toString());
        RequestBody rbDitujukan = RequestBody.create(MultipartBody.FORM, etDitujukan.getText().toString());

        showProgressBarUpload(true);
        mApiService.addKegiatan("Bearer " + getUserToken(), rbDescription,
                rbDateKegiatan, rbTimeKegiatan, rbLocation, rbDitujukan, body).enqueue(addKegiatanCallback.build());
    }

    private void editData() {
        MultipartBody.Part body = null;
        if (_filePhoto != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), _filePhoto);
//          MultipartBody.Part is used to send also the actual file name
            body = MultipartBody.Part.createFormData("foto_kegiatan", _filePhoto.getName(), requestFile);
        }
        RequestBody rbActivityId = RequestBody.create(MultipartBody.FORM, String.valueOf(activities.getActivityId()));
        RequestBody rbDescription = RequestBody.create(MultipartBody.FORM, etDescription.getText().toString());
        RequestBody rbDateKegiatan = RequestBody.create(MultipartBody.FORM, etDate.getText().toString());
        RequestBody rbTimeKegiatan = RequestBody.create(MultipartBody.FORM, etHour.getText().toString());
        RequestBody rbLocation = RequestBody.create(MultipartBody.FORM, etLocation.getText().toString());
        RequestBody rbDitujukan = RequestBody.create(MultipartBody.FORM, etDitujukan.getText().toString());

        showProgressBarUpload(true);

            mApiService.updateKegiatan("Bearer " + getUserToken(), rbActivityId, rbDescription,
                    rbDateKegiatan, rbTimeKegiatan, rbLocation, rbDitujukan, body).enqueue(updateKegiatanCallback.build());
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

    ApiCallback updateKegiatanCallback = new ApiCallback() {
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