package id.akhir.proyek.rukuntetangga.fragments.user;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.Date;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.apihelper.AppSession;
import id.akhir.proyek.rukuntetangga.apihelper.BaseApiService;
import id.akhir.proyek.rukuntetangga.apihelper.UtilsApi;
import id.akhir.proyek.rukuntetangga.helpers.CallbackApi;
import id.akhir.proyek.rukuntetangga.helpers.DateHelper;
import id.akhir.proyek.rukuntetangga.helpers.DatePickerFragment;
import id.akhir.proyek.rukuntetangga.listener.DialogListener;
import id.akhir.proyek.rukuntetangga.listener.UniversalListener;
import id.akhir.proyek.rukuntetangga.models.ApiStatus;
import id.akhir.proyek.rukuntetangga.models.LetterType;
import id.akhir.proyek.rukuntetangga.models.User;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;


public class AddLaporanFragment extends Fragment {
    private static final int REQUEST_CHOOSE_PHOTO = 2;
    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 3;
    public String[] permissions = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    Toolbar toolbar;
    BaseApiService mApiService;
    AppSession appSession;
    User user;
    Dialog progressDialog;
    Uri uri;
    File _filePhoto;

    private EditText etDescription;
    private ImageView ivImage, ivPreviewImage, ivRemoveImage;
    private Button btnUpload;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_laporan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar);
        if (getActivity() != null)
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.label_menu_laporan);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        mApiService = UtilsApi.getApiService();
        appSession = new AppSession(getActivity());
        user = new Gson().fromJson(appSession.getData(AppSession.USER), User.class);
        progressDialog = new Dialog(getContext());
        showProgress();
        initData(view);
    }

    private void initData(View view) {
        etDescription = view.findViewById(R.id.et_complaint);
        ivImage = view.findViewById(R.id.iv_complaint_photo);
        ivPreviewImage = view.findViewById(R.id.preview_image);
        ivRemoveImage = view.findViewById(R.id.iv_remove_image);

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
        btnUpload = view.findViewById(R.id.btn_upload);
        ivImage.setOnClickListener(v -> {
            pickImageGallery();
        });

        btnUpload.setOnClickListener(v -> alertSubmitDone(R.string.warning_title, R.string.warning_confirmation, new DialogListener() {
            @Override
            public void onPositiveButton() {
                addLaporanCallback.context = getContext();
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
//        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        // There are no request codes
//                        Intent data = result.getData();
//                    }
//                });
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
                Toast.makeText(getActivity(), R.string.error_permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean checkPermission(int requestCode){
        if (!checkAllPermission()) {
            ActivityCompat.requestPermissions((Activity) getActivity(),
                    permissions, requestCode);
            return false;
        } else {
            return true;
        }
    }

    private boolean checkAllPermission(){
        for (String permission : permissions) {
            return ContextCompat.checkSelfPermission(getActivity(),
                    permission) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getActivity(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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

    private void showProgress(){
        progressDialog.setContentView(R.layout.progressdialog);
        TextView message = progressDialog.findViewById(R.id.tv_process);
        message.setText(R.string.process_api);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(true);
    }

    private boolean validateData(String description, File file) {
        if (description.trim().isEmpty()) {
            etDescription.setError(getString(R.string.error_textfield_empty, "Keterangan"));
            etDescription.requestFocus();
            return false;
        }

        if (file.length() == 0) {
            Toast.makeText(getActivity(), "Gambar belum dipilih", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void submitData() {
        String _description = etDescription.getText().toString();
        if (!validateData(_description, _filePhoto)) return;

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), _filePhoto);
//
//        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("gambar_laporan", _filePhoto.getName(), requestFile);
//        RequestBody rb_foto = RequestBody.create(okhttp3.MultipartBody.FORM, _filePhoto);
        RequestBody rbDescription = RequestBody.create(MultipartBody.FORM, _description);
        RequestBody rbUserId = RequestBody.create(MultipartBody.FORM, String.valueOf(user.getUserId()));

        progressDialog.show();
        mApiService.addLaporan("Bearer " + appSession.getData(AppSession.TOKEN), rbDescription, rbUserId, body).enqueue(addLaporanCallback.build());
    }

    CallbackApi addLaporanCallback = new CallbackApi() {
        @Override
        public void onApiSuccess(String result) {
            progressDialog.dismiss();
            ApiStatus status = new Gson().fromJson(result, new TypeToken<ApiStatus>(){}.getType());
            Toast.makeText(context, status.getMessage(), Toast.LENGTH_SHORT).show();
            etDescription.setText("");
            ivPreviewImage.setVisibility(View.GONE);
            ivRemoveImage.setVisibility(View.GONE);
            ivImage.setVisibility(View.VISIBLE);
        }

        @Override
        public void onApiFailure(String errorMessage) {
            progressDialog.dismiss();
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }
    };

    public void alertSubmitDone(int title, int message, DialogListener listener){
        TextView textView = new TextView(getActivity());
        textView.setText(title);
        textView.setPadding(32, 30, 32, 30);
        textView.setTextSize(20F);
        textView.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setCustomTitle(textView)
                .setMessage(message);
        builder.setPositiveButton(R.string.warning_ok, (dialog, id) -> {
            if (listener != null)
                listener.onPositiveButton();
        });
        builder.setNegativeButton(R.string.warning_cancel, (dialog, which) -> {
            if (listener != null)
                listener.onNegativeButton();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}