package id.akhir.proyek.rukuntetangga.activities;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.activities.user.VotingUserActivity;
import id.akhir.proyek.rukuntetangga.adapters.VotingAdapter;
import id.akhir.proyek.rukuntetangga.apihelper.AppSession;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.models.ApiData;
import id.akhir.proyek.rukuntetangga.models.ApiUser;
import id.akhir.proyek.rukuntetangga.models.Service;
import id.akhir.proyek.rukuntetangga.models.Voting;

public class VotingListActivity extends BaseActivity {
    Toolbar toolbar;

    private RecyclerView rvVoting;
    private List<Voting> dataVoting = new ArrayList<>();
    private VotingAdapter adapter;
    boolean isAlreadyVote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting_list);
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_data_voting));

        rvVoting = findViewById(R.id.rv_voting_list);
        rvVoting.setLayoutManager(new LinearLayoutManager(context));
        adapter = new VotingAdapter(dataVoting, context, new AdapterListener<Voting>() {
            @Override
            public void onItemSelected(Voting data) {
                if (getIsAdmin()) {
                    nextActivity(DetailVotingActivity.class);
                } else {
                    mApiService.isAlreadyVote("Bearer " + appSession.getData(AppSession.TOKEN), getUserSession().getUserId(), data.getVoteId()).enqueue(alreadyVoteCallback.build());
                    if (isAlreadyVote) {
                        nextActivity(DetailVotingActivity.class);
                    } else {
                        nextActivity(VotingUserActivity.class);
                    }
                }
            }

            @Override
            public void onItemLongSelected(Voting data) {
            }
        });
        showProgressBar(true);
        mApiService.getVoting("Bearer " + appSession.getData(AppSession.TOKEN)).enqueue(getVotingCallback.build());
        rvVoting.setAdapter(adapter);
    }

//    private void setData() {
//        dataVoting.add(new Voting(1, "Pelaksanaan Gotong Royong", "Jumat, 20.00", "Sabtu, 19 Agustus 2021 08.00", "Sabtu, 19 Agustus 2021 10.00", "Minggu, 20 Agustus 2021 08.00"));
//        dataVoting.add(new Voting(1, "Pelaksanaan Rapat", "Jumat, 20.00", "Sabtu, 19 Agustus 2021 08.00", "Sabtu, 19 Agustus 2021 10.00", "Minggu, 20 Agustus 2021 08.00"));
//        dataVoting.add(new Voting(1, "Perbaikan jalan PBB", "Jumat, 20.00", "Sabtu, 19 Agustus 2021 08.00", "Sabtu, 19 Agustus 2021 10.00", "Minggu, 20 Agustus 2021 08.00"));
//        dataVoting.add(new Voting(1, "Bersih-bersih Masjid PBB", "Jumat, 20.00", "Sabtu, 19 Agustus 2021 08.00", "Sabtu, 19 Agustus 2021 10.00", "Minggu, 20 Agustus 2021 08.00"));
//    }

    ApiCallback getVotingCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {

            showProgressBar(false);
            ApiData<Voting> apiService = new Gson().fromJson(result, new TypeToken<ApiData<Voting>>(){}.getType());
            dataVoting = apiService.getData();

            adapter.setData(dataVoting);
        }

        @Override
        public void onApiFailure(String errorMessage) {

        }
    };

    ApiCallback alreadyVoteCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBar(false);

            ApiUser<Boolean> apiService = new Gson().fromJson(result, new TypeToken<ApiUser<Boolean>>(){}.getType());
            isAlreadyVote = apiService.getData();
        }

        @Override
        public void onApiFailure(String errorMessage) {

        }
    };
}