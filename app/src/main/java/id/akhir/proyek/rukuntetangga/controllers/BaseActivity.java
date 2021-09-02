package id.akhir.proyek.rukuntetangga.controllers;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.akhir.proyek.rukuntetangga.MainActivity;
import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.activities.LoginActivity;
import id.akhir.proyek.rukuntetangga.apihelper.AppSession;
import id.akhir.proyek.rukuntetangga.apihelper.BaseApiService;
import id.akhir.proyek.rukuntetangga.apihelper.UtilsApi;
import id.akhir.proyek.rukuntetangga.helpers.DateHelper;
import id.akhir.proyek.rukuntetangga.listener.DialogListener;
import id.akhir.proyek.rukuntetangga.models.ApiStatus;
import id.akhir.proyek.rukuntetangga.models.Month;
import id.akhir.proyek.rukuntetangga.models.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseActivity extends AppCompatActivity {
    public final static String TAG = "RukunTetangga App";

    public String[] permissions = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    public String[] months = {
            "", "Januari", "Februari", "Maret",
            "April", "Mei", "Juni", "Juli", "Agustus",
            "September", "November", "December"
    };

    public Context context;
    public AppSession appSession;
    public BaseApiService mApiService;
    Dialog progressDialog;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initViews();
    }

    protected void initViews(){
        context = this;
        appSession = new AppSession(this);
        mApiService = UtilsApi.getApiService();

        progressDialog = new Dialog(context);
    }

    public void checkSession(){
        if(appSession.isLogin()) {
            Intent i = new Intent(context, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void logout(){
        alertSubmitDone(R.string.warning_title, R.string.warning_logout, new DialogListener() {
            @Override
            public void onPositiveButton() {
                appSession.logout();
                appSession.clearSession();
                nextActivity(LoginActivity.class);
                finish();
            }

            @Override
            public void onNegativeButton() {

            }
        });
    }

    public void alertSubmitDone(int title, int message, DialogListener listener){
        TextView textView = new TextView(context);
        textView.setText(title);
        textView.setPadding(32, 30, 32, 30);
        textView.setTextSize(20F);
        textView.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
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

    public void alertSubmitDone(String title, String message, DialogListener listener){
        TextView textView = new TextView(context);
        textView.setText(title);
        textView.setPadding(32, 30, 32, 30);
        textView.setTextSize(20F);
        textView.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
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

    public void showProgressBar(boolean show){
        if(show){
            showProgress();
        } else {
            progressDialog.dismiss();
        }
    }

    public void showProgressBarUpload(boolean show){
        if(show){
            showProgressUpload();
        } else {
            progressDialog.dismiss();
        }
    }

    public void showProgressUpload(){
        progressDialog.setContentView(R.layout.progressdialog);
        TextView message = progressDialog.findViewById(R.id.tv_process);
        message.setText(R.string.process_upload);
        ProgressBar progressBar = progressDialog.findViewById(R.id.progressBar);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void showProgress(){
        progressDialog.setContentView(R.layout.progressdialog);
        TextView message = progressDialog.findViewById(R.id.tv_process);
        message.setText(R.string.process_api);
        ProgressBar progressBar = progressDialog.findViewById(R.id.progressBar);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void showToast(int message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public User getUserSession(){
        return new Gson().fromJson(appSession.getData(AppSession.USER), User.class);
    }

    public boolean getIsAdmin(){
        return appSession.isAdmin();
    }

    public String getUserToken(){
        return appSession.getData(AppSession.TOKEN);
    }

    public String getUserPhone(){
        //TODO: Get User Phone
        return "";
    }

    public String getStringExtraData(String name){
        return getIntent().getStringExtra(name);
    }

    public int getIntExtraData(String name){
        return getIntent().getIntExtra(name, 0);
    }

    public long getLongExtraData(String name){
        return getIntent().getLongExtra(name, 0);
    }

    public boolean getBooleanExtraData(String name){
        return getIntent().getBooleanExtra(name, false);
    }

    public void nextActivity(Class<?> destination) {
        Intent intent = new Intent(context, destination);
        startActivity(intent);
    }

    public static String getTwoDigitNumber(int number){
        if(number/10 > 0)return ""+number;
        return "0"+number;
    }

    public boolean checkAllPermission(){
        for(int i=0;i<permissions.length;i++){
            if(ContextCompat.checkSelfPermission(context,
                    permissions[i]) != PackageManager.PERMISSION_GRANTED)
                return false;
            else return true;
        }
        Toast.makeText(context, "TRUE", Toast.LENGTH_SHORT).show();
        return true;
    }

    public boolean checkPermission(int requestCode){
        if (!checkAllPermission()) {
            ActivityCompat.requestPermissions((Activity) context,
                    permissions, requestCode);
            return false;
        } else {
            return true;
        }
    }

    public long setDateToday() {
        return new Date().getTime();
    }

    public long setDateOneMonth() {
        return DateHelper.addDate(new Date(), DateHelper.MONTH, 1).getTime();
    }

    public void setToolbar(Toolbar toolbar, String title) {
        setSupportActionBar(toolbar);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public abstract class ApiCallback{
        private Callback<ResponseBody> callback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, response.toString());
                if (response.isSuccessful()) {
                    try {
                        String result = response.body().string();
                        Log.d(TAG, result);
                        ApiStatus status = new Gson().fromJson(result, ApiStatus.class);
                        if(status==null){
                            onApiFailure(getString(R.string.error_parsing));
                            return;
                        }
                        if(status.getStatus()!=null) {
                            if (status.getStatus().compareToIgnoreCase("success") == 0) {
                                onApiSuccess(result);
                            } else {
                                onApiFailure(status.getMessage());
                            }
                        } else {
                            onApiFailure(getString(R.string.error_parsing));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d(TAG, e.toString());
                    }
                } else {
                    Log.d(TAG, response.message());
                    String errorMsg = "";

                    switch (response.code()){
                        case 500:
                            errorMsg = getString(R.string.error_500);
                            break;
                        case 400:
                            errorMsg = getString(R.string.error_400);
                            break;
                        case 403:
                            errorMsg = getString(R.string.error_403);
                            break;
                        case 404:
                            errorMsg = getString(R.string.error_404);
                            break;
                        default:
                            errorMsg = getString(R.string.error_xxx);
                            break;
                    }
                    onApiFailure(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                onApiFailure(t.getMessage());
            }
        };

        public Callback<ResponseBody> build() {
            return callback;
        }

        public abstract void onApiSuccess(String result);
        public abstract void onApiFailure(String errorMessage);
    }

    public String formatRupiah(int price) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(price);
    }

    public List<Month> setDataMonth() {
        List<Month> dataMonth = new ArrayList<>();
        dataMonth.add(new Month(1,"Januari", R.color.colorGray));
        dataMonth.add(new Month(2, "Februari", R.color.colorGray));
        dataMonth.add(new Month(3, "Maret", R.color.colorGray));
        dataMonth.add(new Month(4,"April", R.color.colorGray));
        dataMonth.add(new Month(5,"Mei", R.color.colorGray));
        dataMonth.add(new Month(6,"Juni", R.color.colorGray));
        dataMonth.add(new Month(7,"Juli", R.color.colorGray));
        dataMonth.add(new Month(8,"Agustus", R.color.colorGray));
        dataMonth.add(new Month(9,"September", R.color.colorGray));
        dataMonth.add(new Month(10, "Oktober", R.color.colorGray));
        dataMonth.add(new Month(11,"November", R.color.colorGray));
        dataMonth.add(new Month(12,"Desember", R.color.colorGray));
        return dataMonth;
    }
}
