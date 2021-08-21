package id.akhir.proyek.rukuntetangga.fragments.admin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.adapters.EmergencyAdapter;
import id.akhir.proyek.rukuntetangga.apihelper.AppSession;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.models.Service;
import id.akhir.proyek.rukuntetangga.models.viewModel.MainViewModelEmergency;

public class EmergencyFragment extends Fragment {

    private RecyclerView rvEmergency;
    private EmergencyAdapter adapter;
    private List<Service> dataService = new ArrayList<>();
    AppSession appSession;

    Toolbar toolbar;
    Dialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_emergency, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar);

        if (getActivity() != null)
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_menu_darurat);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        appSession = new AppSession(getActivity());
        progressDialog = new Dialog(getContext());
        showProgress();

//        dataService.add(new Service(1, "Polisi", R.drawable.ic_police_station, R.drawable.ic_home));
//        dataService.add(new Service(2, "Ambulance", R.drawable.ic_ambulance, R.drawable.ic_home));
//        dataService.add(new Service(3, "Pemadam Kebakaran", R.drawable.ic_firefighter, R.drawable.ic_home));
//        dataService.add(new Service(4, "Satpam", R.drawable.ic_security, R.drawable.ic_home));
//        dataService.add(new Service(5, "Puskesmas", R.drawable.ic_hospital, R.drawable.ic_home));
        initData(view);
    }

    private final Observer<List<Service>> getEmergency = new Observer<List<Service>>() {
        @Override
        public void onChanged(List<Service> serviceData) {
//            if (serviceData != null) {
//
//            } else {
//                layoutNoConnection(getView());
//            }
            progressDialog.dismiss();
            adapter.setData(serviceData);
        }
    };

    private void initData(View view) {
        rvEmergency = view.findViewById(R.id.rv_emergency);
        rvEmergency.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EmergencyAdapter(dataService, true, new AdapterListener<Service>() {
            @Override
            public void onItemSelected(Service data) {
                Toast.makeText(view.getContext(), data.getServiceName(), Toast.LENGTH_SHORT).show();
                Uri number = Uri.parse("tel:"+data.getPhoneNumber());
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
            }

            @Override
            public void onItemLongSelected(Service data) {
                Toast.makeText(view.getContext(), data.getServiceName(), Toast.LENGTH_SHORT).show();
            }
        });

        progressDialog.show();
        MainViewModelEmergency mainViewModel = ViewModelProviders.of(this).get(MainViewModelEmergency.class);
        mainViewModel.getListEmergency().observe(getViewLifecycleOwner(), getEmergency);
        mainViewModel.setData("Bearer " + appSession.getData(AppSession.TOKEN), getContext());
        rvEmergency.setAdapter(adapter);
    }

    private void showProgress(){
        progressDialog.setContentView(R.layout.progressdialog);
        TextView message = progressDialog.findViewById(R.id.tv_process);
        message.setText(R.string.process_api);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(true);
    }
}