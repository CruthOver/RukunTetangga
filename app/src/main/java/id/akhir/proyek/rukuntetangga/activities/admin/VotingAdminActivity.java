package id.akhir.proyek.rukuntetangga.activities.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.activities.VotingListActivity;
import id.akhir.proyek.rukuntetangga.apihelper.AppSession;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.helpers.DatePickerFragment;
import id.akhir.proyek.rukuntetangga.helpers.TimePicker;
import id.akhir.proyek.rukuntetangga.listener.UniversalListener;
import id.akhir.proyek.rukuntetangga.models.ApiStatus;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class VotingAdminActivity extends BaseActivity implements View.OnClickListener{
    Toolbar toolbar;

    EditText etQuestion, etTimeVote, etDateVote, etChoiceOne, etChoiceTwo, etChoiceThree;
    Button btnUpload;
    String date, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting_admin);
        initData();
    }

    private void initData() {
        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_menu_voting));
        
        etQuestion = findViewById(R.id.et_question);
        etDateVote = findViewById(R.id.et_date_vote);
        etTimeVote = findViewById(R.id.et_time_vote);
        etChoiceOne = findViewById(R.id.et_choice_one);
        etChoiceTwo = findViewById(R.id.et_choice_two);
        etChoiceThree = findViewById(R.id.et_choice_three);
        btnUpload = findViewById(R.id.btn_upload);

        etTimeVote.setOnClickListener(this);
        etDateVote.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        menu.findItem(R.id.menu_toolbar).setIcon(R.drawable.ic_baseline_menu_24);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_toolbar) {
            nextActivity(VotingListActivity.class);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.et_time_vote) {
            TimePicker timePicker = new TimePicker();
            timePicker.holder = etTimeVote;
            timePicker.listener = new UniversalListener() {
                @Override
                public void onSetData() {
                    date = etTimeVote.getText().toString();
                }

                @Override
                public void onResetData() {
                    date = "";
                }
            };
            timePicker.show(getSupportFragmentManager(), "timePicker");
        } else if (v.getId() == R.id.et_date_vote) {
            DatePickerFragment newDatePicker = new DatePickerFragment(setDateOneMonth(), setDateToday());
            newDatePicker.holder = etDateVote;
            newDatePicker.listener = new UniversalListener() {
                @Override
                public void onSetData() {
                    time = etDateVote.getText().toString();
                }

                @Override
                public void onResetData() {
                    time = "";
                }
            };
            newDatePicker.show(getSupportFragmentManager(), "datePicker");
        } else if (v.getId() == R.id.btn_upload) {
            submitData();
        }
    }

    private void submitData() {
        String question = etQuestion.getText().toString();
        String dueDate = etDateVote.getText().toString() + " " + etTimeVote.getText().toString();
        ArrayList<String> choices = new ArrayList<>();

        choices.add(etChoiceOne.getText().toString());
        choices.add(etChoiceTwo.getText().toString());
        choices.add(etChoiceThree.getText().toString());

//        String data = "[" +etChoiceOne.getText().toString()+ "," + etChoiceTwo.getText().toString() + "," +
//                etChoiceThree.getText().toString()+"]"
        Log.d(TAG, choices.toString());

        showProgressBarUpload(true);
        mApiService.addVoting("Bearer " + getUserToken(), question, dueDate, choices).enqueue(addVotingCallback.build());
    }

    ApiCallback addVotingCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBarUpload(false);
            ApiStatus status = new Gson().fromJson(result, ApiStatus.class);
            Toast.makeText(context, status.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onApiFailure(String errorMessage) {
            showProgressBar(false);
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }
    };
}