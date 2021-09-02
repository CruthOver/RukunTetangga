package id.akhir.proyek.rukuntetangga.fragments;

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

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.activities.user.AddNiagaActivity;
import id.akhir.proyek.rukuntetangga.adapters.NiagaAdapter;
import id.akhir.proyek.rukuntetangga.apihelper.AppSession;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.models.Niaga;
import id.akhir.proyek.rukuntetangga.models.viewModel.MainViewModelNiaga;

public class NiagaFragment extends Fragment {

    Toolbar toolbar;

    private List<Niaga> _dataNiaga = new ArrayList<>();
    private NiagaAdapter adapter;
    private RecyclerView rvNiaga;
    AppSession appSession;
    Dialog progressDialog;
    MainViewModelNiaga mainViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_niaga, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        if (getActivity() != null)
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.label_menu_niaga);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        progressDialog = new Dialog(getContext());
        showProgress();
        initData(view);
    }

    private final Observer<List<Niaga>> getNiaga = new Observer<List<Niaga>>() {
        @Override
        public void onChanged(List<Niaga> niagaData) {
            progressDialog.dismiss();
            adapter.setData(niagaData);
        }
    };

    private void initData(View view) {
        rvNiaga = view.findViewById(R.id.rv_niaga);
        rvNiaga.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NiagaAdapter(_dataNiaga, getContext(), new AdapterListener<Niaga>() {
            @Override
            public void onItemSelected(Niaga data) {
                openWhatsApp(data.getPhoneNumber());
            }

            @Override
            public void onItemLongSelected(Niaga data) {
                Toast.makeText(view.getContext(), data.getNiagaName(), Toast.LENGTH_SHORT).show();
            }
        });
        progressDialog.show();
        mainViewModel = ViewModelProviders.of(this).get(MainViewModelNiaga.class);
        mainViewModel.getListNiaga().observe(getViewLifecycleOwner(), getNiaga);
        mainViewModel.setData("Bearer " + appSession.getData(AppSession.TOKEN), getContext(), progressDialog);
        rvNiaga.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mainViewModel = ViewModelProviders.of(this).get(MainViewModelNiaga.class);
        mainViewModel.getListNiaga().observe(getViewLifecycleOwner(), getNiaga);
        mainViewModel.setData("Bearer " + appSession.getData(AppSession.TOKEN), getContext(), progressDialog);
        rvNiaga.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void openWhatsApp(String toNumber){
        try {
            String text = "This is a test";// Replace with your message.

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appSession = new AppSession(getActivity());
        if (!appSession.isAdmin())
            setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_toolbar) {
            Intent intent = new Intent(getActivity(), AddNiagaActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}