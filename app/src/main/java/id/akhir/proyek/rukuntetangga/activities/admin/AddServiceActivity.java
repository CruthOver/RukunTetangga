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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.io.File;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.models.ApiStatus;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddServiceActivity extends BaseActivity {
    private static final int REQUEST_CHOOSE_PHOTO = 2;
    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 3;

    Toolbar toolbar;
    EditText etNameService, etPhoneNumber;
    ImageView ivServiceImage, ivPreviewImage, ivRemoveImage;
    Button btnUpload;

    Uri uri;
    File _filePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_input_service));

        etNameService = findViewById(R.id.et_name_service);
        etPhoneNumber = findViewById(R.id.et_service_phone);
        ivPreviewImage = findViewById(R.id.preview_image);
        ivRemoveImage = findViewById(R.id.iv_remove_image);
        ivServiceImage = findViewById(R.id.iv_service);
        btnUpload = findViewById(R.id.btn_upload);

        if (uri == null) {
            ivPreviewImage.setVisibility(View.GONE);
            ivRemoveImage.setVisibility(View.GONE);
        }

        ivRemoveImage.setOnClickListener(v -> {
            ivPreviewImage.setVisibility(View.GONE);
            ivRemoveImage.setVisibility(View.GONE);
            ivServiceImage.setVisibility(View.VISIBLE);
            uri = null;
        });

        ivServiceImage.setOnClickListener(v -> pickImageGallery());

        btnUpload.setOnClickListener(v -> submitData());
    }

    private boolean validate(String name, String phoneNumber) {
        if (name.trim().isEmpty() || name.trim().equals("")) {
            etNameService.setError(getString(R.string.error_textfield_empty, "Nama jasa"));
            etNameService.requestFocus();
            return false;
        }

        if (phoneNumber.trim().isEmpty() || phoneNumber.trim().equals("")) {
            etPhoneNumber.setError(getString(R.string.error_textfield_empty, "Nomor telepon"));
            etPhoneNumber.requestFocus();
            return false;
        }

        return true;
    }

    private void submitData() {
        String name = etNameService.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();
        if (!validate(name, phoneNumber)) return;

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), _filePhoto);
//      MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("gambar_jasa", _filePhoto.getName(), requestFile);
        RequestBody rbServiceName = RequestBody.create(MultipartBody.FORM, name);
        RequestBody rbPhoneNumber = RequestBody.create(MultipartBody.FORM, phoneNumber);

        showProgressBarUpload(true);
        mApiService.addService("Bearer " + getUserToken(), rbServiceName,
                rbPhoneNumber, body).enqueue(addServiceCallback.build());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHOOSE_PHOTO && resultCode == RESULT_OK && data != null){
            uri = data.getData();
            if (uri != null) {
                _filePhoto = new File(getRealPathFromURI(uri));
                ivPreviewImage.setVisibility(View.VISIBLE);
                ivRemoveImage.setVisibility(View.VISIBLE);
                ivServiceImage.setVisibility(View.GONE);
                ivPreviewImage.setImageURI(uri);
            }
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

    ApiCallback addServiceCallback = new ApiCallback() {
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