package id.akhir.proyek.rukuntetangga.activities.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.apihelper.AppSession;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.listener.DialogListener;
import id.akhir.proyek.rukuntetangga.models.ApiStatus;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddJobActivity extends BaseActivity {
    private static final int REQUEST_CHOOSE_PHOTO = 2;
    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 3;

    Toolbar toolbar;
    EditText etJobName;
    ImageView ivJob, ivPreviewImage, ivRemoveImage;
    Button btnUpload;

    Uri uri;
    File _filePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.add_job));

        etJobName = findViewById(R.id.et_job_name);
        ivJob = findViewById(R.id.iv_job);
        ivPreviewImage = findViewById(R.id.preview_image);
        ivRemoveImage = findViewById(R.id.iv_remove_image);
        btnUpload = findViewById(R.id.btn_upload);

        if (uri == null) {
            ivPreviewImage.setVisibility(View.GONE);
            ivRemoveImage.setVisibility(View.GONE);
        }
        ivRemoveImage.setOnClickListener(v -> {
            ivPreviewImage.setVisibility(View.GONE);
            ivRemoveImage.setVisibility(View.GONE);
            ivJob.setVisibility(View.VISIBLE);
            uri = null;
        });

        ivJob.setOnClickListener(v -> {
            pickImageGallery();
        });

        btnUpload.setOnClickListener(v -> alertSubmitDone(R.string.warning_title, R.string.warning_confirmation, new DialogListener() {
            @Override
            public void onPositiveButton() {
                submitData();
            }

            @Override
            public void onNegativeButton() {

            }
        }));
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
                Toast.makeText(context, R.string.error_permission_denied, Toast.LENGTH_SHORT).show();
            }
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
                ivJob.setVisibility(View.GONE);
                ivPreviewImage.setImageURI(uri);
            }
        }
    }

    private boolean validateData(String jobName) {
        if (jobName.trim().isEmpty()) {
            etJobName.setError(getString(R.string.error_textfield_empty, "Nama Pekerjaan"));
            return false;
        }

        if (uri == null || _filePhoto == null) {
            Toast.makeText(context, "Gambar Belum dipilih", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void submitData() {
        String jobName = etJobName.getText().toString();
        if (!validateData(jobName)) return;

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), _filePhoto);
//        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("foto_pekerjaan", _filePhoto.getName(), requestFile);
        RequestBody rbJobName = RequestBody.create(MultipartBody.FORM, jobName);
        RequestBody rbUserId = RequestBody.create(MultipartBody.FORM, String.valueOf(getUserSession().getUserId()));

        showProgressBarUpload(true);
        mApiService.addJob("Bearer " + getUserToken(), rbUserId,
                rbJobName, body).enqueue(addJobCallback.build());
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    ApiCallback addJobCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBarUpload(false);
            ApiStatus status = new Gson().fromJson(result, ApiStatus.class);
            Toast.makeText(context, status.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onApiFailure(String errorMessage) {

        }
    };
}