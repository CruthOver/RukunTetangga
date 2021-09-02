package id.akhir.proyek.rukuntetangga.models.viewModel;

import android.app.Dialog;
import android.content.Context;
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
import id.akhir.proyek.rukuntetangga.models.Niaga;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class MainViewModelNiaga extends ViewModel {
    private BaseApiService mApiService = UtilsApi.getApiService();

    private MutableLiveData<List<Niaga>> listNiaga= new MutableLiveData<>();
    private Dialog progressDialog;

    public void setData(String authToken, Context context, Dialog progressDialog) {
        this.progressDialog = progressDialog;
        apiCallback.context = context;
        Call<ResponseBody> callInformation = mApiService.getNiaga(authToken);
        callInformation.enqueue(apiCallback.build());
    }

    CallbackApi apiCallback = new CallbackApi() {
        @Override
        public void onApiSuccess(String result) {
            progressDialog.dismiss();
            ApiData<Niaga> apiService = new Gson().fromJson(result, new TypeToken<ApiData<Niaga>>(){}.getType());
            listNiaga.postValue(apiService.getData());
        }

        @Override
        public void onApiFailure(String errorMessage) {
            progressDialog.dismiss();
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }
    };

    public MutableLiveData<List<Niaga>> getListNiaga() {
        return listNiaga;
    }
}
