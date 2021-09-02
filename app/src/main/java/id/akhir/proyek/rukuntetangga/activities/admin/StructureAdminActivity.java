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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.apihelper.AppSession;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.models.ApiData;
import id.akhir.proyek.rukuntetangga.models.ApiStatus;
import id.akhir.proyek.rukuntetangga.models.Position;
import id.akhir.proyek.rukuntetangga.models.Service;
import id.akhir.proyek.rukuntetangga.models.User;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class StructureAdminActivity extends BaseActivity {
    private static final int REQUEST_CHOOSE_PHOTO = 2;
    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 100;

    Toolbar toolbar;

    Spinner spinner;
    EditText etName;
    Button btnUpload;
    ImageView ivPosition, ivPreviewImage, ivRemoveImage;

    List<Position> _dataPosition = new ArrayList<>();
    List<User> _dataUser = new ArrayList<>();
    Position _position;
    User user;
    File _filePhoto;
    Uri uri;

    int _selectedPosition, _selectedWarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_structure_admin);
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_menu_structure));

        etName = findViewById(R.id.et_name);
        spinner = findViewById(R.id.spinner_position);
        ivPosition = findViewById(R.id.iv_position_structure);
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
            ivPosition.setVisibility(View.VISIBLE);
            uri = null;
        });

        setData();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                _position = (Position) adapterView.getSelectedItem();
                _selectedPosition = _position.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        spName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                user = (User) parent.getSelectedItem();
//                _selectedWarga = user.getUserId();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        ivPosition.setOnClickListener(v -> {
            pickImageGallery();
        });

        btnUpload.setOnClickListener(v -> {
            String name = etName.getText().toString();
            if (!validateData(name)) return;

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), _filePhoto);
//        // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body = MultipartBody.Part.createFormData("user_photo", _filePhoto.getName(), requestFile);
//            RequestBody rbUserId = RequestBody.create(MultipartBody.FORM, String.valueOf(_selectedWarga));
            RequestBody rbName = RequestBody.create(MultipartBody.FORM, name);
            RequestBody rbPositionId = RequestBody.create(MultipartBody.FORM, String.valueOf(_selectedPosition));
            showProgressBarUpload(true);
            mApiService.addStructure("Bearer " + getUserToken(), rbName, rbPositionId, body).enqueue(addStructureCallback.build());
        });
    }

    private boolean validateData(String name) {
        if (name.trim().isEmpty()) {
            etName.setError(getString(R.string.error_textfield_empty, "Nama"));
            etName.requestFocus();
            return false;
        }
        return true;
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
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.title_choose_image)),
                REQUEST_CHOOSE_PHOTO);
    }

    private void setData() {
        showProgressBar(true);
        mApiService.getPosition("Bearer "+ appSession.getData(AppSession.TOKEN)).enqueue(getPositionCallback.build());

//        mApiService.getWarga("Bearer "+ appSession.getData(AppSession.TOKEN)).enqueue(getUserCallback.build());
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
                ivPosition.setVisibility(View.GONE);
                ivPreviewImage.setImageURI(uri);
            }
        }
    }

    ApiCallback getPositionCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBar(false);
            ApiData<Position> apiPosition = new Gson().fromJson(result, new TypeToken<ApiData<Position>>(){}.getType());
            _dataPosition = apiPosition.getData();
            final ArrayAdapter<Position> adapter = new ArrayAdapter<Position>(context,
                    R.layout.spinner_position, _dataPosition);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            spinner.setAdapter(adapter);
        }

        @Override
        public void onApiFailure(String errorMessage) {
            Toast.makeText(context, "Gagal Mengambil Posisi Karena " + errorMessage, Toast.LENGTH_SHORT).show();
        }
    };

//    ApiCallback getUserCallback = new ApiCallback() {
//        @Override
//        public void onApiSuccess(String result) {
//            showProgressBar(false);
//            ApiData<User> apiPosition = new Gson().fromJson(result, new TypeToken<ApiData<User>>(){}.getType());
//            _dataUser = apiPosition.getData();
//            final ArrayAdapter<User> adapter = new ArrayAdapter<User>(context,
//                    R.layout.spinner_position, _dataUser);
//            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//            spName.setAdapter(adapter);
//        }
//
//        @Override
//        public void onApiFailure(String errorMessage) {
//            Toast.makeText(context, "Gagal Mengambil User Karena " + errorMessage, Toast.LENGTH_SHORT).show();
//        }
//    };

    ApiCallback addStructureCallback = new ApiCallback() {
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