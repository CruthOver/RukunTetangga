package id.akhir.proyek.rukuntetangga.activities;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.File;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.apihelper.AppSession;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.models.ApiData;
import id.akhir.proyek.rukuntetangga.models.ApiStatus;
import id.akhir.proyek.rukuntetangga.models.ApiUser;
import id.akhir.proyek.rukuntetangga.models.DetailVoting;
import id.akhir.proyek.rukuntetangga.models.User;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfileActivity extends BaseActivity {
    private static final int REQUEST_CHOOSE_PHOTO_KTP = 2;
    private static final int REQUEST_CHOOSE_PHOTO_KK = 1;
    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 3;

    Toolbar toolbar;
    private ImageView ivUser, ivKTP, ivKK;
    private TextView tvUsername, tvAddress, tvGender, tvAddressKtp
            , tvDateBirth, tvEmail, tvPhoneNumber;
    private TextView tvEmptyImageKTP, tvEmptyImageKK;
    private Button btnUploadKtp, btnUploadKK;

    User user;
    Uri uriKtp, uriKk;
    File _filePhotoKtp, _filePhotoKk;

    String buttonType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.label_profile));

        ivUser = findViewById(R.id.iv_user);
        ivKK = findViewById(R.id.iv_foto_kartu_keluarga);
        ivKTP = findViewById(R.id.iv_foto_ktp);
        tvUsername = findViewById(R.id.tv_username);
        tvAddress = findViewById(R.id.tv_address_user);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);
        tvGender = findViewById(R.id.tv_gender);
        tvAddressKtp = findViewById(R.id.tv_address_ktp);
        tvDateBirth = findViewById(R.id.tv_date_of_birth);
        tvEmail = findViewById(R.id.tv_email);
        tvEmptyImageKK = findViewById(R.id.tv_empty_image_kk);
        tvEmptyImageKTP = findViewById(R.id.tv_empty_image_ktp);
        btnUploadKK = findViewById(R.id.btn_upload_kk);
        btnUploadKtp = findViewById(R.id.btn_upload_ktp);

        setData();
    }
    
    private void setData() {
        user = getUserSession();
        String gender = user.getGender().equals("0") ? "Laki-laki" : "Perempuan";
        String birthDate = user.getBirthPlace() + "," + user.getDateBirth();
        tvUsername.setText(user.getFullName());
        tvAddress.setText(user.getCurrentAddress());
        tvPhoneNumber.setText(user.getPhoneNumber());
        tvAddressKtp.setText(user.getAddress());
        tvEmail.setText(user.getEmail());
        tvGender.setText(gender);
        tvDateBirth.setText(birthDate);

        if (user.getImageKTP() == null || user.getImageKTP().equals("")) {
            tvEmptyImageKTP.setVisibility(View.VISIBLE);
            ivKTP.setVisibility(View.GONE);
        } else {
            tvEmptyImageKTP.setVisibility(View.GONE);
            ivKTP.setVisibility(View.VISIBLE);
            Picasso.get().load(user.getImageKTP())
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.broken_image)
                    .into(ivKTP);
        }

        if (user.getImageKK() == null || user.getImageKK().equals("")) {
            tvEmptyImageKK.setVisibility(View.VISIBLE);
            ivKK.setVisibility(View.GONE);
        } else {
            tvEmptyImageKK.setVisibility(View.GONE);
            ivKK.setVisibility(View.VISIBLE);
            Picasso.get().load(user.getImageKK())
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.broken_image)
                    .into(ivKK);
        }

        btnUploadKtp.setOnClickListener(v -> {
            buttonType = "KTP";
            pickImageGallery();
        });

        btnUploadKK.setOnClickListener(v -> {
            buttonType = "KK";
            pickImageGallery();
        });
    }

    private void pickImageGallery() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkPermission(2))
                dispatchChoosePictureIntent();
        } else
            dispatchChoosePictureIntent();
    }

    private void dispatchChoosePictureIntent(){
        if (buttonType.equals("KTP")) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(Intent.createChooser(intent, getString(R.string.title_choose_image)),
                    REQUEST_CHOOSE_PHOTO_KTP);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(Intent.createChooser(intent, getString(R.string.title_choose_image)),
                    REQUEST_CHOOSE_PHOTO_KK);
        }
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

        if (buttonType.equals("KTP")) {
            if (requestCode == REQUEST_CHOOSE_PHOTO_KTP && resultCode == RESULT_OK && data != null){
                uriKtp = data.getData();
                if (uriKtp != null) {
                    _filePhotoKtp = new File(getRealPathFromURI(uriKtp));

                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), _filePhotoKtp);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("foto_ktp", _filePhotoKtp.getName(), requestFile);
                    RequestBody rbUserId = RequestBody.create(MultipartBody.FORM, String.valueOf(user.getUserId()));
                    RequestBody rbType = RequestBody.create(MultipartBody.FORM, "KTP");
                    showProgressBar(true);
                    mApiService.uploadFotoKtp("Bearer " + getUserToken(), rbUserId, rbType, body).enqueue(uploadDataCallback.build());
                }
            }
        } else {
            if (requestCode == REQUEST_CHOOSE_PHOTO_KK && resultCode == RESULT_OK && data != null){
                uriKk = data.getData();
                if (uriKk != null) {
                    _filePhotoKk = new File(getRealPathFromURI(uriKk));
                    tvEmptyImageKK.setVisibility(View.GONE);
                    ivKK.setVisibility(View.VISIBLE);
                    ivKK.setImageURI(uriKk);
                    Log.d("Data User : ", user.getUserId() + "");
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), _filePhotoKk);
                    RequestBody rbUserId = RequestBody.create(MultipartBody.FORM, String.valueOf(user.getUserId()));
                    RequestBody rbType = RequestBody.create(MultipartBody.FORM, "KK");
                    MultipartBody.Part body = MultipartBody.Part.createFormData("foto_kk", _filePhotoKk.getName(), requestFile);
                    showProgressBar(true);
                    mApiService.uploadFotoKk("Bearer " + getUserToken(), rbUserId, rbType, body).enqueue(uploadDataCallback.build());
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        menu.findItem(R.id.menu_toolbar).setIcon(R.drawable.ic_edit);
        menu.findItem(R.id.logout).setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_toolbar) {
            Intent intent = new Intent(context, EditProfileActivity.class);
            intent.putExtra("edit_user", user);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.logout) {
            logout();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    ApiCallback uploadDataCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBar(false);
            ApiStatus status = new Gson().fromJson(result, ApiStatus.class);
            Toast.makeText(context, status.getMessage(), Toast.LENGTH_SHORT).show();
            ApiUser<User> dataUser = new Gson().fromJson(result, new TypeToken<ApiUser<User>>(){}.getType());
            User userData = dataUser.getData();
            appSession.setData(AppSession.USER, new Gson().toJson(userData));
            user.setImageKTP(userData.getImageKTP());
            user.setImageKK(userData.getImageKK());
            if (buttonType.equals("KTP")) {
                tvEmptyImageKTP.setVisibility(View.GONE);
                ivKTP.setVisibility(View.VISIBLE);
                Picasso.get().load(user.getImageKTP())
                        .placeholder(R.drawable.image_placeholder)
                        .error(R.drawable.broken_image)
                        .into(ivKTP);
            } else {
                tvEmptyImageKK.setVisibility(View.GONE);
                ivKK.setVisibility(View.VISIBLE);
                Picasso.get().load(user.getImageKK())
                        .placeholder(R.drawable.image_placeholder)
                        .error(R.drawable.broken_image)
                        .into(ivKK);
            }
        }

        @Override
        public void onApiFailure(String errorMessage) {
            showProgressBar(false);
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }
    };
}