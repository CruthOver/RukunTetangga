package id.akhir.proyek.rukuntetangga.fragments.user;

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

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.akhir.proyek.rukuntetangga.LetterListActivity;
import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.apihelper.AppSession;
import id.akhir.proyek.rukuntetangga.apihelper.BaseApiService;
import id.akhir.proyek.rukuntetangga.apihelper.UtilsApi;
import id.akhir.proyek.rukuntetangga.helpers.CallbackApi;
import id.akhir.proyek.rukuntetangga.helpers.DateHelper;
import id.akhir.proyek.rukuntetangga.helpers.DatePickerFragment;
import id.akhir.proyek.rukuntetangga.listener.DialogListener;
import id.akhir.proyek.rukuntetangga.listener.UniversalListener;
import id.akhir.proyek.rukuntetangga.models.ApiData;
import id.akhir.proyek.rukuntetangga.models.ApiStatus;
import id.akhir.proyek.rukuntetangga.models.ApiUser;
import id.akhir.proyek.rukuntetangga.models.Complaint;
import id.akhir.proyek.rukuntetangga.models.LetterType;
import id.akhir.proyek.rukuntetangga.models.Position;
import id.akhir.proyek.rukuntetangga.models.User;
import id.akhir.proyek.rukuntetangga.models.viewModel.MainViewModelLetterType;

public class AddLetterFragment extends Fragment {
    Toolbar toolbar;

    Spinner spinner;
    Button btnUpload;
    EditText etApplicantName, etDateNeed, etDescription,
        etDateBirth, etPlaceBirth, etAddress, etEmail;
    List<LetterType> _dataLetterType = new ArrayList<>();
    LetterType _letterSelected;

    BaseApiService mApiService;
    AppSession appSession;
    User user;
    Dialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_letter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar);
        if (getActivity() != null)
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_add_letter);
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        mApiService = UtilsApi.getApiService();
        appSession = new AppSession(getActivity());
        user = new Gson().fromJson(appSession.getData(AppSession.USER), User.class);
        progressDialog = new Dialog(getContext());
        showProgress();
        setData();
        initData(view);
    }

    private void initData(View view) {
        spinner = view.findViewById(R.id.spinner_letter_type);
        etApplicantName = view.findViewById(R.id.et_applicant_name);
        etDateNeed = view.findViewById(R.id.et_date_need);
        etEmail = view.findViewById(R.id.et_email);
        etAddress = view.findViewById(R.id.et_address);
        etPlaceBirth = view.findViewById(R.id.et_birth_place);
        etDateBirth = view.findViewById(R.id.et_birth_date);
        etDescription = view.findViewById(R.id.et_description);
        btnUpload = view.findViewById(R.id.btn_upload);

        etApplicantName.setText(user.getFullName());
        etDateBirth.setText(user.getDateBirth());
        etPlaceBirth.setText(user.getBirthPlace());
        etAddress.setText(user.getAddress());
        etEmail.setText(user.getEmail());

        setData();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                _letterSelected = (LetterType) adapterView.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        etDateNeed.setOnClickListener(v -> {
            DatePickerFragment newDatePicker = new DatePickerFragment(DateHelper.addDate(new Date(), DateHelper.MONTH, 1).getTime(), new Date().getTime());
            newDatePicker.holder = etDateNeed;
            newDatePicker.listener = new UniversalListener() {
                @Override
                public void onSetData() {
                    Toast.makeText(getActivity(), etDateNeed.getText().toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResetData() {
                    Toast.makeText(getActivity(), "Pilih Tanggal", Toast.LENGTH_SHORT).show();
                }
            };
            newDatePicker.show(getActivity().getSupportFragmentManager(), "datePicker");
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertSubmitDone(R.string.warning_title, R.string.warning_confirmation, new DialogListener() {
                    @Override
                    public void onPositiveButton() {
                        addLetterCallback.context = getContext();
                        submitData();
                    }

                    @Override
                    public void onNegativeButton() {

                    }
                });
            }
        });
    }

    private final Observer<List<LetterType>> getLetterType = new Observer<List<LetterType>>() {
        @Override
        public void onChanged(List<LetterType> letterData) {
            progressDialog.dismiss();
            final ArrayAdapter<LetterType> adapter = new ArrayAdapter<LetterType>(getContext(),
                    R.layout.spinner_position, letterData);
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            spinner.setAdapter(adapter);
        }
    };

    private void setData() {
        progressDialog.show();
        MainViewModelLetterType mainViewModel = ViewModelProviders.of(this).get(MainViewModelLetterType.class);
        mainViewModel.getListLetterType().observe(getViewLifecycleOwner(), getLetterType);
        mainViewModel.setData("Bearer " + appSession.getData(AppSession.TOKEN), getContext());
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
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_add, menu);
        menu.findItem(R.id.menu_toolbar).setIcon(R.drawable.ic_baseline_menu_24);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_toolbar) {
            Intent intent = new Intent(getActivity(), LetterListActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private boolean validateData(String description, String dateNeed) {
        if (description.trim().isEmpty()) {
            etDescription.setError(getString(R.string.error_textfield_empty, "Keterangan"));
            etDescription.requestFocus();
            return false;
        }

        if (dateNeed.trim().isEmpty()) {
            etDateNeed.setError(getString(R.string.error_textfield_empty, "Keterangan"));
            etDateNeed.requestFocus();
            return false;
        }

        return true;
    }

    private void submitData() {
        String _description = etDescription.getText().toString();
        String _dateNeed = etDateNeed.getText().toString();
        if (!validateData(_description, _dateNeed)) return;

        progressDialog.show();
        mApiService.addSurat("Bearer " + appSession.getData(AppSession.TOKEN),
                user.getUserId(), _dateNeed, _letterSelected.getId(), _description).enqueue(addLetterCallback.build());
    }

    CallbackApi addLetterCallback = new CallbackApi() {
        @Override
        public void onApiSuccess(String result) {
            progressDialog.dismiss();
            ApiStatus status = new Gson().fromJson(result, new TypeToken<ApiStatus>(){}.getType());
            Toast.makeText(context, status.getMessage(), Toast.LENGTH_SHORT).show();
            spinner.setSelection(0);
            etDescription.setText("");
            etDateNeed.setText("");
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
}