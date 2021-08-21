package id.akhir.proyek.rukuntetangga.helpers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.models.ApiStatus;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class CallbackApi {
    public Context context;
    private Callback<ResponseBody> callback = new Callback<ResponseBody>() {
        @Override
        public void onResponse(@NonNull Call<ResponseBody> call, Response<ResponseBody> response) {
            Log.d("CALLBACK", response.toString());
            if (response.isSuccessful()) {
                try {
                    if (response.body() != null) {
                        String result = response.body().string();
                        Log.d("ADQW", result);
                        ApiStatus status = new Gson().fromJson(result, ApiStatus.class);
                        if(status==null){
                            onApiFailure(context.getString(R.string.error_parsing));
                            return;
                        }
                        if(status.getStatus()!=null) {
                            if (status.getStatus().compareToIgnoreCase("success") == 0) {
                                onApiSuccess(result);
                            } else {
                                onApiFailure(status.getMessage());
                            }
                        } else {
                            onApiFailure(context.getString(R.string.error_parsing));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                String errorMsg = "";

                switch (response.code()){
                    case 500:
                        errorMsg = context.getString(R.string.error_500);
                        break;
                    case 400:
                        errorMsg = context.getString(R.string.error_400);
                        break;
                    case 403:
                        errorMsg = context.getString(R.string.error_403);
                        break;
                    case 404:
                        errorMsg = context.getString(R.string.error_404);
                        break;
                    default:
                        errorMsg = context.getString(R.string.error_xxx);
                        break;
                }
                onApiFailure(errorMsg);
            }
        }

        @Override
        public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
            onApiFailure(t.getMessage());
        }
    };

    public Callback<ResponseBody> build() {
        return callback;
    }

    public abstract void onApiSuccess(String result);
    public abstract void onApiFailure(String errorMessage);
}
