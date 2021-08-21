package id.akhir.proyek.rukuntetangga.activities;

import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;

public class DetailVotingActivity extends BaseActivity {

    PieChart pieChart;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_voting);

        toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.title_menu_musrenbang));

        pieChart = findViewById(R.id.piechart);

        pieChart.addPieSlice(
                new PieModel(
                        "R",
                        Integer.parseInt("8"),
                        Color.parseColor("#FFA726")));
        pieChart.addPieSlice(
                new PieModel(
                        "Python",
                        Integer.parseInt("10"),
                        Color.parseColor("#66BB6A")));
        pieChart.addPieSlice(
                new PieModel(
                        "C++",
                        Integer.parseInt("20"),
                        Color.parseColor("#EF5350")));
        pieChart.startAnimation();
    }

    ApiCallback detailVotingCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {

        }

        @Override
        public void onApiFailure(String errorMessage) {

        }
    };
}