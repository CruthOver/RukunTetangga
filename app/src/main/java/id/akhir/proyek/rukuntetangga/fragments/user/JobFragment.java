package id.akhir.proyek.rukuntetangga.fragments.user;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import id.akhir.proyek.rukuntetangga.LetterListActivity;
import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.activities.user.AddJobActivity;
import id.akhir.proyek.rukuntetangga.adapters.EmergencyAdapter;
import id.akhir.proyek.rukuntetangga.adapters.JobAdapter;
import id.akhir.proyek.rukuntetangga.apihelper.AppSession;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.models.Jobs;
import id.akhir.proyek.rukuntetangga.models.Service;
import id.akhir.proyek.rukuntetangga.models.User;
import id.akhir.proyek.rukuntetangga.models.viewModel.MainViewModelEmergency;
import id.akhir.proyek.rukuntetangga.models.viewModel.MainViewModelJobs;

public class JobFragment extends Fragment {

    private RecyclerView rvJobs;
    private JobAdapter adapter;
    private List<Jobs> dataJobs = new ArrayList<>();
    AppSession appSession;
    User user;

    Toolbar toolbar;
    Dialog progressDialog;
    MainViewModelJobs mainViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_job, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar);

        if (getActivity() != null)
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.label_menu_job);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        appSession = new AppSession(getActivity());
        user = new Gson().fromJson(appSession.getData(AppSession.USER), User.class);
        progressDialog = new Dialog(getContext());
        showProgress();

        initData(view);
    }

    private void initData(View view) {
        rvJobs = view.findViewById(R.id.rv_jobs);
        rvJobs.setLayoutManager(new LinearLayoutManager(getContext()));
        rvJobs.setHasFixedSize(true);
        ((SimpleItemAnimator) rvJobs.getItemAnimator()).setSupportsChangeAnimations(false);
        adapter = new JobAdapter(dataJobs, getContext());

        progressDialog.show();
        mainViewModel = ViewModelProviders.of(this).get(MainViewModelJobs.class);
        mainViewModel.getListJob().observe(getViewLifecycleOwner(), getJobs);
        mainViewModel.setData("Bearer " + appSession.getData(AppSession.TOKEN), getContext(), progressDialog);
        rvJobs.setAdapter(adapter);
    }

    private final Observer<List<Jobs>> getJobs = new Observer<List<Jobs>>() {
        @Override
        public void onChanged(List<Jobs> jobsData) {
            progressDialog.dismiss();
            dataJobs = jobsData;
            adapter.setData(jobsData);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mainViewModel = ViewModelProviders.of(this).get(MainViewModelJobs.class);
        mainViewModel.getListJob().observe(getViewLifecycleOwner(), getJobs);
        mainViewModel.setData("Bearer " + appSession.getData(AppSession.TOKEN), getContext(), progressDialog);
        rvJobs.setAdapter(adapter);
    }

    private void showProgress(){
        progressDialog.setContentView(R.layout.progressdialog);
        TextView message = progressDialog.findViewById(R.id.tv_process);
        message.setText(R.string.process_api);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_toolbar) {
            Intent intent = new Intent(getActivity(), AddJobActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}