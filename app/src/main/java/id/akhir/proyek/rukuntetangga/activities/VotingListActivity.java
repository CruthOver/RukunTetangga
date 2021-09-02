package id.akhir.proyek.rukuntetangga.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.activities.admin.KegiatanAdminActivity;
import id.akhir.proyek.rukuntetangga.activities.admin.VotingAdminActivity;
import id.akhir.proyek.rukuntetangga.activities.user.VotingUserActivity;
import id.akhir.proyek.rukuntetangga.adapters.VotingAdapter;
import id.akhir.proyek.rukuntetangga.apihelper.AppSession;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.listener.DialogListener;
import id.akhir.proyek.rukuntetangga.listener.MenuListener;
import id.akhir.proyek.rukuntetangga.models.ApiData;
import id.akhir.proyek.rukuntetangga.models.ApiStatus;
import id.akhir.proyek.rukuntetangga.models.ApiUser;
import id.akhir.proyek.rukuntetangga.models.Service;
import id.akhir.proyek.rukuntetangga.models.Voting;

public class VotingListActivity extends BaseActivity {
    Toolbar toolbar;

    private RecyclerView rvVoting;
    private List<Voting> dataVoting = new ArrayList<>();
    private VotingAdapter adapter;
    boolean isAlreadyVote;
    Voting votingSelected;

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
        adapter = new VotingAdapter(getIsAdmin(), dataVoting, context, new AdapterListener<Voting>() {
            @Override
            public void onItemSelected(Voting data) {
                votingSelected = data;
                if (getIsAdmin()) {
                    Intent intent = new Intent(context, DetailVotingActivity.class);
                    intent.putExtra("vote_id", data.getVoteId());
                    startActivity(intent);
                } else {
                    mApiService.isAlreadyVote("Bearer " + appSession.getData(AppSession.TOKEN), getUserSession().getUserId(), data.getVoteId()).enqueue(alreadyVoteCallback.build());
                }
            }

            @Override
            public void onItemLongSelected(Voting data) {
            }
        }, new MenuListener<Voting>() {
            @Override
            public void onEdit(Voting data) {
                Intent intent = new Intent(context, VotingAdminActivity.class);
                intent.putExtra("edit_voting", data);
                startActivity(intent);
            }

            @Override
            public void onDelete(Voting data) {
                alertSubmitDone(getString(R.string.warning_confirmation), getString(R.string.delete_confirmation, "Voting"),
                        new DialogListener() {
                            @Override
                            public void onPositiveButton() {
                                votingSelected = data;
                                showProgressBar(true);
                                mApiService.deleteVoting("Bearer " + getUserToken(), data.getVoteId()).enqueue(deleteCallback.build());
                            }

                            @Override
                            public void onNegativeButton() {

                            }
                        });
            }
        });
        showProgressBar(true);
        mApiService.getVoting("Bearer " + appSession.getData(AppSession.TOKEN)).enqueue(getVotingCallback.build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgressBar(true);
        mApiService.getVoting("Bearer " + appSession.getData(AppSession.TOKEN)).enqueue(getVotingCallback.build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (appSession.isAdmin()) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_add, menu);
            return true;
        } else
            return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_toolbar) {
            nextActivity(VotingAdminActivity.class);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    ApiCallback getVotingCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBar(false);
            ApiData<Voting> apiService = new Gson().fromJson(result, new TypeToken<ApiData<Voting>>(){}.getType());
            dataVoting = apiService.getData();

            adapter.setData(dataVoting);
            rvVoting.setAdapter(adapter);
        }

        @Override
        public void onApiFailure(String errorMessage) {
            showProgressBar(false);
            showToast(errorMessage);
        }
    };

    ApiCallback alreadyVoteCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBar(false);
            ApiUser<Boolean> apiService = new Gson().fromJson(result, new TypeToken<ApiUser<Boolean>>(){}.getType());
            isAlreadyVote = apiService.getData();
            Intent intent;
            if (!isAlreadyVote) {
                intent = new Intent(context, VotingUserActivity.class);
            } else {
                intent = new Intent(context, DetailVotingActivity.class);
            }
            intent.putExtra("vote_id", votingSelected.getVoteId());
            startActivity(intent);
        }

        @Override
        public void onApiFailure(String errorMessage) {

        }
    };

    ApiCallback deleteCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBar(false);
            ApiStatus status = new Gson().fromJson(result, ApiStatus.class);
            showToast(status.getMessage());
            dataVoting.remove(votingSelected);
            adapter.setData(dataVoting);
            rvVoting.setAdapter(adapter);
        }

        @Override
        public void onApiFailure(String errorMessage) {
            showProgressBar(false);
            showToast(errorMessage);
        }
    };
}