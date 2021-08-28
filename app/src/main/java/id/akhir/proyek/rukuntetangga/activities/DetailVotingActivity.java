package id.akhir.proyek.rukuntetangga.activities;

import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.List;
import java.util.Random;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.models.ApiData;
import id.akhir.proyek.rukuntetangga.models.ApiUser;
import id.akhir.proyek.rukuntetangga.models.DetailVoting;
import id.akhir.proyek.rukuntetangga.models.Voting;

public class DetailVotingActivity extends BaseActivity {

    PieChart pieChart;
    Toolbar toolbar;
    TextView tvChoiceOne, tvChoiceTwo, tvChoiceThree;
    View viewOne, viewTwo, viewThree;
    List<DetailVoting> voting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_voting);

        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_menu_musrenbang));

        tvChoiceOne = findViewById(R.id.tv_choice_one);
        tvChoiceTwo = findViewById(R.id.tv_choice_two);
        tvChoiceThree = findViewById(R.id.tv_choice_three);
        viewOne = findViewById(R.id.view_choice_one);
        viewTwo = findViewById(R.id.view_choice_two);
        viewThree = findViewById(R.id.view_choice_three);
        pieChart = findViewById(R.id.piechart);

//        pieChart.addPieSlice(
//                new PieModel(
//                        "R",
//                        Integer.parseInt("8"),
//                        Color.parseColor("#FFA726")));
//        pieChart.addPieSlice(
//                new PieModel(
//                        "Python",
//                        Integer.parseInt("10"),
//                        Color.parseColor("#66BB6A")));
//        pieChart.addPieSlice(
//                new PieModel(
//                        "C++",
//                        Integer.parseInt("20"),
//                        Color.parseColor("#EF5350")));
        mApiService.getResultVoting("Bearer "+ getUserToken(), getIntExtraData("vote_id")).enqueue(detailVotingCallback.build());
    }

    ApiCallback detailVotingCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBar(false);
            ApiData<DetailVoting> apiDetailVoting = new Gson().fromJson(result, new TypeToken<ApiData<DetailVoting>>(){}.getType());
            voting = apiDetailVoting.getData();

            Random rnd = new Random();
            for (int i=0; i<voting.size(); i++) {
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                pieChart.addPieSlice(
                        new PieModel(
                                voting.get(i).getPilihan(),
                                voting.get(i).getJumlah(),
                                color));
                if (i == 0) {
                    viewOne.setBackgroundColor(color);
                } else if (i == 1) {
                    viewTwo.setBackgroundColor(color);
                } else {
                    viewThree.setBackgroundColor(color);
                }
            }

            tvChoiceOne.setText(voting.get(0).getPilihan());
            tvChoiceTwo.setText(voting.get(1).getPilihan());
            if (voting.size() == 3) {
                tvChoiceThree.setText(voting.get(2).getPilihan());
            } else {
                tvChoiceThree.setText("");
            }
            pieChart.startAnimation();
        }

        @Override
        public void onApiFailure(String errorMessage) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }
    };
}