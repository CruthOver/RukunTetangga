package id.akhir.proyek.rukuntetangga.activities.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;
import id.akhir.proyek.rukuntetangga.models.ApiData;
import id.akhir.proyek.rukuntetangga.models.ApiStatus;
import id.akhir.proyek.rukuntetangga.models.ApiUser;
import id.akhir.proyek.rukuntetangga.models.Complaint;
import id.akhir.proyek.rukuntetangga.models.ComplaintAction;

public class ComplaintActionActivity extends BaseActivity {

    private ScrollView scrollView;
    private TextView tvDescription, tvEmptyAction;
    private ImageView ivAction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_action);
        initData();
    }

    private void initData() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setToolbar(toolbar, getString(R.string.complaint_action));

        int complaintId = getIntExtraData("complaint_id");
        scrollView = findViewById(R.id.scrollView);
        tvEmptyAction = findViewById(R.id.tv_empty_action);
        tvDescription = findViewById(R.id.tv_description_action);
        ivAction = findViewById(R.id.iv_action);

        showProgressBar(true);
        mApiService.getComplaintAction("Bearer " + getUserToken(), complaintId).enqueue(getActionCallback.build());
    }

    ApiCallback getActionCallback = new ApiCallback() {
        @Override
        public void onApiSuccess(String result) {
            showProgressBar(false);
            ApiUser<ComplaintAction> actionComplaint = new Gson().fromJson(result, new TypeToken<ApiUser<ComplaintAction>>(){}.getType());
            if (actionComplaint.getData() != null) {
                ComplaintAction action = actionComplaint.getData();
                scrollView.setVisibility(View.VISIBLE);
                tvEmptyAction.setVisibility(View.GONE);
                tvDescription.setText(action.getDescription());
                Picasso.get().load(action.getImageAction())
                        .placeholder(R.drawable.image_placeholder)
                        .error(R.drawable.broken_image)
                        .into(ivAction);
            } else {
                scrollView.setVisibility(View.GONE);
                tvEmptyAction.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onApiFailure(String errorMessage) {
            showProgressBar(false);
            showToast(errorMessage);
        }
    };
}