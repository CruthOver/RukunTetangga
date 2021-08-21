package id.akhir.proyek.rukuntetangga;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import id.akhir.proyek.rukuntetangga.adapters.EmergencyAdapter;
import id.akhir.proyek.rukuntetangga.adapters.LetterAdapter;
import id.akhir.proyek.rukuntetangga.apihelper.AppSession;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.models.ApiData;
import id.akhir.proyek.rukuntetangga.models.Letter;
import id.akhir.proyek.rukuntetangga.models.Service;

public class LetterListActivity extends BaseActivity {

    Toolbar toolbar;
    private RecyclerView rvLetter;
    private LetterAdapter adapter;
    private List<Letter> dataLetter = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter_list);
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_fragment_letter));
        rvLetter = findViewById(R.id.rv_letter);
        rvLetter.setLayoutManager(new LinearLayoutManager(context));
        adapter = new LetterAdapter(context, appSession.isAdmin(), dataLetter, new AdapterListener<Letter>() {
            @Override
            public void onItemSelected(Letter data) {
                Toast.makeText(context, data.getLetterType(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongSelected(Letter data) {
                Toast.makeText(context, data.getLetterType(), Toast.LENGTH_SHORT).show();
            }
        });
        showProgressBar(true);
        mApiService.getLetter("Bearer " + appSession.getData(AppSession.TOKEN)).enqueue(letterCallback.build());
        rvLetter.setAdapter(adapter);
    }

    ApiCallback letterCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {

            showProgressBar(false);
            ApiData<Letter> apiService = new Gson().fromJson(result, new TypeToken<ApiData<Letter>>(){}.getType());
            dataLetter = apiService.getData();
            adapter.setData(dataLetter);
        }

        @Override
        public void onApiFailure(String errorMessage) {
            showProgressBar(false);
            showToast(errorMessage);
        }
    };
}