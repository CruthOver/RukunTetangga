package id.akhir.proyek.rukuntetangga.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.activities.MusrenbangActivity;
import id.akhir.proyek.rukuntetangga.activities.ProfileActivity;
import id.akhir.proyek.rukuntetangga.activities.ServiceActivity;
import id.akhir.proyek.rukuntetangga.activities.VotingListActivity;
import id.akhir.proyek.rukuntetangga.activities.admin.DataWargaActivity;
import id.akhir.proyek.rukuntetangga.activities.admin.InformationActivity;
import id.akhir.proyek.rukuntetangga.activities.admin.KasAdminActivity;
import id.akhir.proyek.rukuntetangga.activities.admin.KegiatanAdminActivity;
import id.akhir.proyek.rukuntetangga.activities.admin.StructureAdminActivity;
import id.akhir.proyek.rukuntetangga.activities.admin.VotingAdminActivity;
import id.akhir.proyek.rukuntetangga.activities.user.ComplaintActivity;
import id.akhir.proyek.rukuntetangga.activities.user.EmergencyActivity;
import id.akhir.proyek.rukuntetangga.activities.user.KasUserActivity;
import id.akhir.proyek.rukuntetangga.activities.user.KegiatanUserActivity;
import id.akhir.proyek.rukuntetangga.activities.user.StructureUserActivity;
import id.akhir.proyek.rukuntetangga.activities.user.VotingUserActivity;
import id.akhir.proyek.rukuntetangga.adapters.BannerAdapter;
import id.akhir.proyek.rukuntetangga.adapters.ComplaintAdapter;
import id.akhir.proyek.rukuntetangga.adapters.MenuGridAdapter;
import id.akhir.proyek.rukuntetangga.apihelper.AppSession;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.models.Complaint;
import id.akhir.proyek.rukuntetangga.models.Information;
import id.akhir.proyek.rukuntetangga.models.MenuGrid;
import id.akhir.proyek.rukuntetangga.models.User;
import id.akhir.proyek.rukuntetangga.models.viewModel.MainViewModelComplaint;
import id.akhir.proyek.rukuntetangga.models.viewModel.MainViewModelHome;

public class HomeFragment extends Fragment {

    private View _viewHome;
    private ViewStub viewStub;
    RecyclerView recyclerView;
    MenuGridAdapter menuGridAdapter;
    ViewPager2 viewPager2;
    TextView tvWelcome, tvFullName;

    BannerAdapter adapter;
    Toolbar toolbar;
    AppSession appSession;
    Dialog progressDialog;
    List<MenuGrid> dataMenu = new ArrayList<MenuGrid>();
    List<Information> dataInformation = new ArrayList<Information>();

    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appSession = new AppSession(getContext());
        setDataMenu(appSession.isAdmin());

        viewStub = view.findViewById(R.id.home_view);
        if (appSession.isAdmin()) {
            viewStub.setLayoutResource(R.layout.home_admin);
            _viewHome = viewStub.inflate();
        } else {
            progressDialog = new Dialog(getContext());
            initHomeUser();
        }
        recyclerView = view.findViewById(R.id.grid_menu);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));

        menuGridAdapter = new MenuGridAdapter(dataMenu, new AdapterListener<MenuGrid>() {
            @Override
            public void onItemSelected(MenuGrid data) {
                Intent intent = new Intent(getContext(), data.getClassDestination());
                startActivity(intent);
            }

            @Override
            public void onItemLongSelected(MenuGrid data) {

            }
        });
        recyclerView.setAdapter(menuGridAdapter);
    }

    private final Observer<List<Information>> getInformation = new Observer<List<Information>>() {
        @Override
        public void onChanged(List<Information> informationData) {
//            if (serviceData != null) {
//
//            } else {
//                layoutNoConnection(getView());
//            }
            progressDialog.dismiss();
            adapter.setData(informationData);
        }
    };

    private void initHomeUser() {
        User user = new Gson().fromJson(appSession.getData(AppSession.USER), User.class);
        viewStub.setLayoutResource(R.layout.home_user);
        _viewHome = viewStub.inflate();
        viewPager2 = _viewHome.findViewById(R.id.viewPagerBanner);
        tvWelcome = _viewHome.findViewById(R.id.tv_welcome);
        tvFullName = _viewHome.findViewById(R.id.tv_full_name);
        tvWelcome.setText(R.string.label_welcome);
        tvFullName.setText(user.getFullName());
        dataInformation.add(new Information(1, "Informasi 1"));
        dataInformation.add(new Information(2, "Informasi 2"));
        dataInformation.add(new Information(3, "Informasi 3"));

        adapter = new BannerAdapter(getActivity(), dataInformation, viewPager2);

        progressDialog.show();
        MainViewModelHome mainViewModel = ViewModelProviders.of(this).get(MainViewModelHome.class);
        mainViewModel.getListInformation().observe(getViewLifecycleOwner(), getInformation);
        mainViewModel.setData("Bearer " + appSession.getData(AppSession.TOKEN), getContext());

        viewPager2.setAdapter(adapter);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(2);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 2 - Math.abs(position);
            page.setScaleY(0.75f + r * 0.15f);
        });

        viewPager2.setPageTransformer(compositePageTransformer);
    }

    public void setDataMenu(boolean isAdmin) {
        if (isAdmin) {
            dataMenu.add(new MenuGrid(R.string.title_menu_kegiatan, R.drawable.ic_activities, KegiatanAdminActivity.class));
            dataMenu.add(new MenuGrid(R.string.title_menu_informasi, R.drawable.ic_information, InformationActivity.class));
            dataMenu.add(new MenuGrid(R.string.title_menu_kas, R.drawable.ic_cash, KasAdminActivity.class));
            dataMenu.add(new MenuGrid(R.string.title_menu_structure, R.drawable.ic_structure, StructureAdminActivity.class));
            dataMenu.add(new MenuGrid(R.string.title_menu_warga, R.drawable.ic_warga, DataWargaActivity.class));
            dataMenu.add(new MenuGrid(R.string.title_menu_voting, R.drawable.ic_voting, VotingAdminActivity.class));
            dataMenu.add(new MenuGrid(R.string.title_menu_jasa, R.drawable.ic_service, ServiceActivity.class));
            dataMenu.add(new MenuGrid(R.string.title_menu_musrenbang, R.drawable.ic_musrenbang, MusrenbangActivity.class));
        } else {
            dataMenu.add(new MenuGrid(R.string.title_menu_kas, R.drawable.ic_cash, KasUserActivity.class));
            dataMenu.add(new MenuGrid(R.string.title_menu_kegiatan, R.drawable.ic_activities, KegiatanUserActivity.class));
            dataMenu.add(new MenuGrid(R.string.title_menu_complaint, R.drawable.ic_complaint, ComplaintActivity.class));
            dataMenu.add(new MenuGrid(R.string.title_menu_structure, R.drawable.ic_structure, StructureUserActivity.class));
            dataMenu.add(new MenuGrid(R.string.title_menu_jasa, R.drawable.ic_service, ServiceActivity.class));
            dataMenu.add(new MenuGrid(R.string.title_menu_voting, R.drawable.ic_voting, VotingListActivity.class));
            dataMenu.add(new MenuGrid(R.string.title_menu_musrenbang, R.drawable.ic_musrenbang, MusrenbangActivity.class));
            dataMenu.add(new MenuGrid(R.string.title_menu_darurat, R.drawable.ic_emergency, EmergencyActivity.class));
        }
    }
}