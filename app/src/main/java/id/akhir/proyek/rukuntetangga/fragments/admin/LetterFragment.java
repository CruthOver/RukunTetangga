package id.akhir.proyek.rukuntetangga.fragments.admin;

import android.app.Dialog;
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
import id.akhir.proyek.rukuntetangga.adapters.ComplaintAdapter;
import id.akhir.proyek.rukuntetangga.adapters.LetterAdapter;
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
import id.akhir.proyek.rukuntetangga.models.Letter;
import id.akhir.proyek.rukuntetangga.models.Service;
import id.akhir.proyek.rukuntetangga.models.User;
import id.akhir.proyek.rukuntetangga.models.viewModel.MainViewModelComplaint;
import id.akhir.proyek.rukuntetangga.models.viewModel.MainViewModelEmergency;
import id.akhir.proyek.rukuntetangga.models.viewModel.MainViewModelLetter;

public class LetterFragment extends Fragment {

    Toolbar toolbar;

    private List<Letter> _dataLetter = new ArrayList<>();
    private LetterAdapter adapter;
    private RecyclerView rvLetter;
    Letter letterSelected;
    AppSession appSession;
    BaseApiService mApiService;
    Dialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_letter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar);
        if (getActivity() != null)
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_fragment_letter);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        mApiService = UtilsApi.getApiService();
        appSession = new AppSession(getActivity());
        progressDialog = new Dialog(getContext());
        showProgress();
        initData(view);
    }

    private final Observer<List<Letter>> getLetter = new Observer<List<Letter>>() {
        @Override
        public void onChanged(List<Letter> letterData) {
//            if (serviceData != null) {
//
//            } else {
//                layoutNoConnection(getView());
//            }
            progressDialog.dismiss();
            _dataLetter = letterData;
            adapter.setData(letterData);
        }
    };

    private void initData(View view) {
        rvLetter = view.findViewById(R.id.rv_letter);
        rvLetter.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LetterAdapter(getActivity(), appSession.isAdmin(), _dataLetter, new AdapterListener<Letter>() {
            @Override
            public void onItemSelected(Letter data) {
                alertSubmitDone(R.string.warning_title, R.string.warning_confirmation_update, new DialogListener() {
                    @Override
                    public void onPositiveButton() {
                        int status = data.getStatusLetter();
                        progressDialog.show();
                        letterUpdate.context = getContext();
                        if (status == 0) {
                            status = 1;
                            mApiService.updateStatusLetter("Bearer " + appSession.getData(AppSession.TOKEN), data.getLetterId(), status)
                                    .enqueue(letterUpdate.build());
                        } else if (status == 1) {
                            status = 2;
                            mApiService.updateStatusLetter("Bearer " + appSession.getData(AppSession.TOKEN), data.getLetterId(), status)
                                    .enqueue(letterUpdate.build());
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(view.getContext(), "Pengajuran Surat Sudah Selesai", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onNegativeButton() {
                        Toast.makeText(getActivity(), R.string.label_cancel, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onItemLongSelected(Letter data) {
                Toast.makeText(getContext(), data.getLetterType(), Toast.LENGTH_SHORT).show();
            }
        }, new MenuListener<Letter>() {
            @Override
            public void onEdit(Letter data) {

            }

            @Override
            public void onDelete(Letter data) {
                alertSubmitDone(getString(R.string.warning_title), getString(R.string.delete_confirmation, "Surat"), new DialogListener() {
                    @Override
                    public void onPositiveButton() {
                        deleteLetterCallback.context = getContext();
                        letterSelected = data;
                        progressDialog.show();
                        mApiService.deleteSurat("Bearer " + appSession.getData(AppSession.TOKEN), data.getLetterId())
                                .enqueue(deleteLetterCallback.build());
                    }

                    @Override
                    public void onNegativeButton() {

                    }
                });
            }
        });

        progressDialog.show();
        MainViewModelLetter mainViewModel = ViewModelProviders.of(this).get(MainViewModelLetter.class);
        mainViewModel.getListLetter().observe(getViewLifecycleOwner(), getLetter);
        mainViewModel.setData("Bearer " + appSession.getData(AppSession.TOKEN), getContext());
        rvLetter.setAdapter(adapter);
    }

    private void showProgress(){
        progressDialog.setContentView(R.layout.progressdialog);
        TextView message = progressDialog.findViewById(R.id.tv_process);
        message.setText(R.string.process_api);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(true);
    }

    CallbackApi letterUpdate = new CallbackApi() {
        @Override
        public void onApiSuccess(String result) {
            progressDialog.dismiss();
            ApiUser<Letter> apiLetter = new Gson().fromJson(result, new TypeToken<ApiUser<Letter>>(){}.getType());
            Letter complaint = apiLetter.getData();
            for (int i=0; i<_dataLetter.size(); i++) {
                if (_dataLetter.get(i).getLetterId() == complaint.getLetterId()) {
                    _dataLetter.get(i).setStatusLetter(complaint.getStatusLetter());
                    break;
                }
            }
            adapter.setData(_dataLetter);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onApiFailure(String errorMessage) {
            progressDialog.dismiss();
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }
    };

    CallbackApi deleteLetterCallback = new CallbackApi() {
        @Override
        public void onApiSuccess(String result) {
            progressDialog.dismiss();
            ApiStatus status = new Gson().fromJson(result, ApiStatus.class);
            Toast.makeText(context, status.getMessage(), Toast.LENGTH_SHORT).show();
            _dataLetter.remove(letterSelected);
            adapter.setData(_dataLetter);
            rvLetter.setAdapter(adapter);
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