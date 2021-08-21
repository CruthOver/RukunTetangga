package id.akhir.proyek.rukuntetangga.activities.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.adapters.MusrenbangAdapter;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.helpers.PathUtils;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.models.ApiStatus;
import id.akhir.proyek.rukuntetangga.models.Musrenbang;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static id.akhir.proyek.rukuntetangga.helpers.PathUtils.getDataColumn;

public class AddMusrenbangActivity extends BaseActivity {
    private static final int REQUEST_CHOOSE_PHOTO = 2;
    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 100;
    Toolbar toolbar;

    Button btnUpload;
    ImageView ivMusrenbang, ivPreviewImage, ivRemoveImage;
    TextView tvFileName;

    File _filePhoto;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_musrenbang);
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.label_input_musrenbang));

        ivMusrenbang = findViewById(R.id.iv_musrenbang);
        ivPreviewImage = findViewById(R.id.preview_image);
        ivRemoveImage = findViewById(R.id.iv_remove_image);
        tvFileName = findViewById(R.id.tv_filename);
        btnUpload = findViewById(R.id.btn_upload);
        if (uri == null) {
            ivPreviewImage.setVisibility(View.GONE);
            ivRemoveImage.setVisibility(View.GONE);
            tvFileName.setVisibility(View.GONE);
        }
        ivRemoveImage.setOnClickListener(v -> {
            ivPreviewImage.setVisibility(View.GONE);
            ivRemoveImage.setVisibility(View.GONE);
            tvFileName.setVisibility(View.GONE);
            ivMusrenbang.setVisibility(View.VISIBLE);
            uri = null;
        });

        ivMusrenbang.setOnClickListener(v -> {
            pickImageGallery();
        });

        btnUpload.setOnClickListener(v -> {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), _filePhoto);
//        // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body = MultipartBody.Part.createFormData("user_photo", _filePhoto.getName(), requestFile);
            RequestBody rbName = RequestBody.create(MultipartBody.FORM, _filePhoto.getName());
            showProgressBarUpload(true);
            mApiService.addMusrenbang("Bearer " + getUserToken(), rbName, body).enqueue(addMusrenbangCallback.build());
        });
    }

    private void pickImageGallery() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkPermission(2))
                dispatchChoosePictureIntent();
            else Toast.makeText(context, "Keluarin Toast", Toast.LENGTH_SHORT).show();
        } else
            dispatchChoosePictureIntent();
    }

    private void dispatchChoosePictureIntent(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "application/pdf");
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

    public String getRealPathFromURI(Uri contentUri) {
        String docId;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT  &&
                DocumentsContract.isDocumentUri(context, contentUri)){
            if("com.android.externalstorage.documents".equals(contentUri.getAuthority())) {
                docId = DocumentsContract.getDocumentId(contentUri);
                final String split[] = docId.split(":");
                if("primary".equalsIgnoreCase(split[0])) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            else if("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                docId = DocumentsContract.getDocumentId(uri);
                contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/my_downloads"), Long.valueOf(docId));
                return getDataColumn(context, contentUri, null, null);
            }
        }
        else if(contentUri.getScheme().equalsIgnoreCase("content")) {
            return  getDataColumn(context, contentUri, null, null);
        }
        else if(contentUri.getScheme().equalsIgnoreCase("file")){
            return contentUri.getPath();
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHOOSE_PHOTO && resultCode == RESULT_OK && data != null){
            uri = data.getData();
            if (uri != null) {
                Log.d("Uri", uri.getAuthority());
                Log.d("Uri", uri.getPath());
                _filePhoto = new File(getRealPathFromURI(uri));
                ivPreviewImage.setVisibility(View.VISIBLE);
                ivRemoveImage.setVisibility(View.VISIBLE);
                tvFileName.setVisibility(View.VISIBLE);
                ivMusrenbang.setVisibility(View.GONE);
//                tvFileName.setText(_filePhoto.getName());
                ivPreviewImage.setImageResource(R.drawable.ic_pdf);
            }
        }
    }

    ApiCallback addMusrenbangCallback = new ApiCallback() {
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