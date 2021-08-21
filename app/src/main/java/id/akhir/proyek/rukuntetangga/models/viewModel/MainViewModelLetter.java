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
import id.akhir.proyek.rukuntetangga.models.Letter;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class MainViewModelLetter extends ViewModel {
    private BaseApiService mApiService = UtilsApi.getApiService();

    private MutableLiveData<List<Letter>> listLetter = new MutableLiveData<>();
    List<Letter> letterItems = new ArrayList<>();

    public void setData(String authToken, Context context) {
        apiCallback.context = context;
        Call<ResponseBody> callEmergency = mApiService.getLetter(authToken);
        callEmergency.enqueue(apiCallback.build());
    }

    CallbackApi apiCallback = new CallbackApi() {
        @Override
        public void onApiSuccess(String result) {
            ApiData<Letter> apiService = new Gson().fromJson(result, new TypeToken<ApiData<Letter>>(){}.getType());
            listLetter.postValue(apiService.getData());
        }

        @Override
        public void onApiFailure(String errorMessage) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }
    };

    public MutableLiveData<List<Letter>> getListLetter() {
        return listLetter;
    }
}
