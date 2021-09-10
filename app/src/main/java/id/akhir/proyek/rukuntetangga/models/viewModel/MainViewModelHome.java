package id.akhir.proyek.rukuntetangga.models.viewModel;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import id.akhir.proyek.rukuntetangga.apihelper.BaseApiService;
import id.akhir.proyek.rukuntetangga.apihelper.UtilsApi;
import id.akhir.proyek.rukuntetangga.helpers.CallbackApi;
import id.akhir.proyek.rukuntetangga.models.ApiData;
import id.akhir.proyek.rukuntetangga.models.ApiUser;
import id.akhir.proyek.rukuntetangga.models.Information;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class MainViewModelHome extends ViewModel {
    private BaseApiService mApiService = UtilsApi.getApiService();

    private MutableLiveData<List<Information>> listInformation= new MutableLiveData<>();
    private MutableLiveData<Integer> totalNotification = new MutableLiveData<>();
    private Dialog progressDialog;

    public void setData(String authToken, Context context, Dialog progressDialog) {
        this.progressDialog = progressDialog;
        apiCallback.context = context;
        Call<ResponseBody> callInformation = mApiService.getInformation(authToken);
        callInformation.enqueue(apiCallback.build());
    }

    public void setData(String authToken, int userId, Context context) {
        apiCallback.context = context;
        Call<ResponseBody> callInformation = mApiService.getTotalNotificationLog(authToken, userId);
        callInformation.enqueue(getTotalNotification.build());

    }

    CallbackApi apiCallback = new CallbackApi() {
        @Override
        public void onApiSuccess(String result) {
            progressDialog.dismiss();
            ApiData<Information> apiService = new Gson().fromJson(result, new TypeToken<ApiData<Information>>(){}.getType());
            listInformation.postValue(apiService.getData());
        }

        @Override
        public void onApiFailure(String errorMessage) {
            progressDialog.dismiss();
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }
    };

    CallbackApi getTotalNotification = new CallbackApi() {
        @Override
        public void onApiSuccess(String result) {
            progressDialog.dismiss();
            ApiUser<Integer> apiTotalNotification = new Gson().fromJson(result, new TypeToken<ApiUser<Integer>>(){}.getType());
            totalNotification.postValue(apiTotalNotification.getData());
        }

        @Override
        public void onApiFailure(String errorMessage) {
            progressDialog.dismiss();
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }
    };

    public MutableLiveData<List<Information>> getListInformation() {
        return listInformation;
    }

    public MutableLiveData<Integer> getTotalNotification() {
        return totalNotification;
    }
}
