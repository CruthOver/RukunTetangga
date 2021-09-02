package id.akhir.proyek.rukuntetangga.fragments.admin;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.activities.admin.AddComplaintActionActivity;
import id.akhir.proyek.rukuntetangga.activities.user.ComplaintActionActivity;
import id.akhir.proyek.rukuntetangga.adapters.ComplaintAdapter;
import id.akhir.proyek.rukuntetangga.apihelper.AppSession;
import id.akhir.proyek.rukuntetangga.apihelper.BaseApiService;
import id.akhir.proyek.rukuntetangga.apihelper.UtilsApi;
import id.akhir.proyek.rukuntetangga.helpers.CallbackApi;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.listener.DialogListener;
import id.akhir.proyek.rukuntetangga.listener.MenuListener;
import id.akhir.proyek.rukuntetangga.models.ApiStatus;
import id.akhir.proyek.rukuntetangga.models.ApiUser;
import id.akhir.proyek.rukuntetangga.models.Complaint;
import id.akhir.proyek.rukuntetangga.models.viewModel.MainViewModelComplaint;

public class LaporanFragment extends Fragment {
    Toolbar toolbar;

    private List<Complaint> _dataComplaint = new ArrayList<>();
    private ComplaintAdapter adapter;
    private RecyclerView rvComplaint;
    AppSession appSession;
    BaseApiService mApiService;
    Dialog progressDialog;
    MainViewModelComplaint mainViewModel;
    Complaint complaintSelected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_laporan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar);
        if (getActivity() != null)
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.label_menu_laporan);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));

        mApiService = UtilsApi.getApiService();
        appSession = new AppSession(getActivity());
        progressDialog = new Dialog(getContext());
        showProgress();
        initData(view);
    }

    private final Observer<List<Complaint>> getComplaint = new Observer<List<Complaint>>() {
        @Override
        public void onChanged(List<Complaint> complaintData) {
            progressDialog.dismiss();
            _dataComplaint = complaintData;
            adapter.setData(complaintData);
        }
    };

    private void initData(View view) {
        rvComplaint = view.findViewById(R.id.rv_complaint);
        rvComplaint.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ComplaintAdapter(appSession.isAdmin(), _dataComplaint, getContext(), new AdapterListener<Complaint>() {
            @Override
            public void onItemSelected(Complaint data) {
                if (data.getStatusComplaint() == 1) {
                    Intent intent = new Intent(getContext(), AddComplaintActionActivity.class);
                    intent.putExtra("complaint_id", data.getComplaintId());
                    startActivity(intent);
                } else if (data.getStatusComplaint() == 0) {
                    alertSubmitDone(R.string.warning_title, R.string.warning_confirmation_update, new DialogListener() {
                        @Override
                        public void onPositiveButton() {
                            int status = data.getStatusComplaint();
                            progressDialog.show();
                            complaintUpdate.context = getContext();
                            if (status == 0) {
                                status = 1;
                                mApiService.updateStatusLaporan("Bearer " + appSession.getData(AppSession.TOKEN), data.getComplaintId(), status)
                                        .enqueue(complaintUpdate.build());
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(view.getContext(), "Laporan Sudah Selesai", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onNegativeButton() {
                            Toast.makeText(getActivity(), R.string.label_cancel, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (data.getStatusComplaint() == 2) {
                    Intent intent = new Intent(getContext(), ComplaintActionActivity.class);
                    intent.putExtra("complaint_id", data.getComplaintId());
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongSelected(Complaint data) {
                Toast.makeText(view.getContext(), data.getTitleComplaint(), Toast.LENGTH_SHORT).show();
            }
        }, new MenuListener<Complaint>() {
            @Override
            public void onEdit(Complaint data) {

            }

            @Override
            public void onDelete(Complaint data) {
                alertSubmitDone(getString(R.string.warning_title), getString(R.string.delete_confirmation, "Surat"), new DialogListener() {
                    @Override
                    public void onPositiveButton() {
                        deleteComplaintCallback.context = getContext();
                        complaintSelected = data;
                        progressDialog.show();
                        mApiService.deleteComplaint("Bearer " + appSession.getData(AppSession.TOKEN), data.getComplaintId())
                                .enqueue(deleteComplaintCallback.build());
                    }

                    @Override
                    public void onNegativeButton() {

                    }
                });
            }
        });
        progressDialog.show();
        mainViewModel = ViewModelProviders.of(this).get(MainViewModelComplaint.class);
        mainViewModel.getListComplaint().observe(getViewLifecycleOwner(), getComplaint);
        mainViewModel.setData("Bearer " + appSession.getData(AppSession.TOKEN), getContext(), progressDialog);
        rvComplaint.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mainViewModel.getListComplaint().observe(getViewLifecycleOwner(), getComplaint);
        mainViewModel.setData("Bearer " + appSession.getData(AppSession.TOKEN), getContext(), progressDialog);
        rvComplaint.setAdapter(adapter);
    }

    private void showProgress(){
        progressDialog.setContentView(R.layout.progressdialog);
        TextView message = progressDialog.findViewById(R.id.tv_process);
        message.setText(R.string.process_api);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(true);
    }

    CallbackApi complaintUpdate = new CallbackApi() {
        @Override
        public void onApiSuccess(String result) {
            progressDialog.dismiss();
            ApiUser<Complaint> apiComplaint = new Gson().fromJson(result, new TypeToken<ApiUser<Complaint>>(){}.getType());
            Complaint complaint = apiComplaint.getData();
            for (int i=0; i<_dataComplaint.size(); i++) {
                if (_dataComplaint.get(i).getComplaintId() == complaint.getComplaintId()) {
                    _dataComplaint.get(i).setStatusComplaint(complaint.getStatusComplaint());
                    break;
                }
            }
            adapter.setData(_dataComplaint);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onApiFailure(String errorMessage) {
            progressDialog.dismiss();
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }
    };

    CallbackApi deleteComplaintCallback = new CallbackApi() {
        @Override
        public void onApiSuccess(String result) {
            progressDialog.dismiss();
            ApiStatus status = new Gson().fromJson(result, ApiStatus.class);
            Toast.makeText(context, status.getMessage(), Toast.LENGTH_SHORT).show();
            _dataComplaint.remove(complaintSelected);
            adapter.setData(_dataComplaint);
            rvComplaint.setAdapter(adapter);
        }

        @Override
        public void onApiFailure(String errorMessage) {
            progressDialog.dismiss();
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }
    };

    public void alertSubmitDone(int title, int message, DialogListener listener){

        TextView textView = new TextView(getActivity());
        textView.setText(title);
        textView.setPadding(32, 30, 32, 30);
        textView.setTextSize(20F);
        textView.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setCustomTitle(textView)
                .setMessage(message);
        builder.setPositiveButton(R.string.warning_ok, (dialog, id) -> {
            if (listener != null)
                listener.onPositiveButton();
        });
        builder.setNegativeButton(R.string.warning_cancel, (dialog, which) -> {
            if (listener != null)
                listener.onNegativeButton();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void alertSubmitDone(String title, String message, DialogListener listener){
        TextView textView = new TextView(getActivity());
        textView.setText(title);
        textView.setPadding(32, 30, 32, 30);
        textView.setTextSize(20F);
        textView.setBackgroundColor(getResources().getColor(R.color.colorSecondary));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setCustomTitle(textView)
                .setMessage(message);
        builder.setPositiveButton(R.string.warning_ok, (dialog, id) -> {
            if (listener != null)
                listener.onPositiveButton();
        });
        builder.setNegativeButton(R.string.warning_cancel, (dialog, which) -> {
            if (listener != null)
                listener.onNegativeButton();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}