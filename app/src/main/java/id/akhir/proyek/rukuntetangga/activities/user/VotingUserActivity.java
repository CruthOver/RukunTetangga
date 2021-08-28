package id.akhir.proyek.rukuntetangga.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Date;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.activities.DetailVotingActivity;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.models.ApiStatus;
import id.akhir.proyek.rukuntetangga.models.ApiUser;
import id.akhir.proyek.rukuntetangga.models.Voting;

public class VotingUserActivity extends BaseActivity {

    Toolbar toolbar;
    RadioGroup radioGroup;
    TextView tvLabelVoting, tvDate, tvTime, tvQuestion;
    Button btnVote;
    boolean dueDate;

    Voting voting;
    int _choiceSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting_user);
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_menu_voting));

        radioGroup = findViewById(R.id.choice_voting);
        tvLabelVoting = findViewById(R.id.tv_label_vote);
        tvDate = findViewById(R.id.tv_date_voting);
        tvTime = findViewById(R.id.tv_time_voting);
        tvQuestion = findViewById(R.id.tv_question);
        btnVote = findViewById(R.id.btnVote);

        showProgressBar(true);
        mApiService.getDetailVoting("Bearer " + getUserToken(), getIntExtraData("vote_id")).enqueue(getDetailVote.build());
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            group.check(checkedId);
            _choiceSelected = checkedId;
        });
        btnVote.setOnClickListener(v -> {
            if (dueDate) {
                Intent intent = new Intent(context, DetailVotingActivity.class);
                intent.putExtra("vote_id", getIntExtraData("vote_id"));
                startActivity(intent);
            } else {
                int voteId = getIntExtraData("vote_id");
                mApiService.addVoted("Bearer " + getUserToken(), voteId,
                        _choiceSelected, getUserSession().getUserId()).enqueue(votingCallback.build());
            }
        });
    }

    private void addRadioButton(int length, boolean isEnabled) {
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        //
        for (int i = 0; i < length; i++) {
            RadioButton rdbtn = new RadioButton(context);
            rdbtn.setId(voting.getChoice().get(i).getId());
            rdbtn.setText(voting.getChoice().get(i).getPilihan());
            rdbtn.setEnabled(isEnabled);
            rdbtn.setOnClickListener(v -> {
                Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show();
            });
            radioGroup.addView(rdbtn);
        }
    }

    private boolean dueDateVote(long date) {
        if (date > (long) (new Date().getTime() / 1000)) {
            return false;
        }
        return true;
    }

    ApiCallback getDetailVote = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBar(false);
            ApiUser<Voting> apiVoting = new Gson().fromJson(result, new TypeToken<ApiUser<Voting>>() {
            }.getType());
            voting = apiVoting.getData();
            tvQuestion.setText(voting.getQuestion());
            tvDate.setText("Tanggal " + voting.getDateVote());
            tvTime.setText("Batas Waktu Voting " + voting.getTimeVote() + " WIB");
            if (dueDateVote(voting.getDateTimeVote())) {
                btnVote.setText(getString(R.string.title_hasil_voting));
                dueDate = true;
                addRadioButton(voting.getChoice().size(), false);
                tvLabelVoting.setText("Voting Ditutup");
            } else {
                btnVote.setText(getString(R.string.title_menu_voting));
                dueDate = false;
                addRadioButton(voting.getChoice().size(), true);
                tvLabelVoting.setText("Voting Dibuka");
            }
        }

        @Override
        public void onApiFailure(String errorMessage) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }
    };

    ApiCallback votingCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBarUpload(false);
            ApiStatus status = new Gson().fromJson(result, ApiStatus.class);
            Toast.makeText(context, status.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onApiFailure(String errorMessage) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }
    };
}