package id.akhir.proyek.rukuntetangga.models.viewModel;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import id.akhir.proyek.rukuntetangga.apihelper.BaseApiService;
import id.akhir.proyek.rukuntetangga.apihelper.UtilsApi;
import id.akhir.proyek.rukuntetangga.helpers.CallbackApi;
import id.akhir.proyek.rukuntetangga.models.ApiData;
import id.akhir.proyek.rukuntetangga.models.Complaint;
import id.akhir.proyek.rukuntetangga.models.Letter;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class MainViewModelComplaint extends ViewModel {
    private BaseApiService mApiService = UtilsApi.getApiService();

    private MutableLiveData<List<Complaint>> listComplaint = new MutableLiveData<>();
    List<Complaint> complaintsItems = new ArrayList<>();

    public void setData(String authToken, Context context) {
        apiCallback.context = context;
        Call<ResponseBody> callComplaint = mApiService.getComplaint(authToken);
        callComplaint.enqueue(apiCallback.build());
    }

    CallbackApi apiCallback = new CallbackApi() {
        @Override
        public void onApiSuccess(String result) {
            ApiData<Complaint> apiService = new Gson().fromJson(result, new TypeToken<ApiData<Complaint>>(){}.getType());
            listComplaint.postValue(apiService.getData());
        }

        @Override
        public void onApiFailure(String errorMessage) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }
    };

    public MutableLiveData<List<Complaint>> getListComplaint() {
        return listComplaint;
    }
}
