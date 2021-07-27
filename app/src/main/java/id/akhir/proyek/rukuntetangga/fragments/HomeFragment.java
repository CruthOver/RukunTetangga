package id.akhir.proyek.rukuntetangga.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.akhir.proyek.rukuntetangga.MainActivity;
import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.adapters.MenuGridAdapter;
import id.akhir.proyek.rukuntetangga.listener.AdapterListener;
import id.akhir.proyek.rukuntetangga.models.MenuGrid;

public class HomeFragment extends Fragment {

    private View _viewHome;
    private ViewStub viewStub;
    RecyclerView recyclerView;
    MenuGridAdapter menuGridAdapter;

    Toolbar toolbar;

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
        viewStub = view.findViewById(R.id.home_view);
        viewStub.setLayoutResource(R.layout.home_admin);
        _viewHome = viewStub.inflate();
        recyclerView = view.findViewById(R.id.grid_menu);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));

        menuGridAdapter = new MenuGridAdapter(setDataMenu(false), new AdapterListener<MenuGrid>() {
            @Override
            public void onItemSelected(MenuGrid data) {
//                Intent i = new Intent(getContext(), );
//                startActivity(i);
                Toast.makeText(getContext(), data.getTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongSelected(MenuGrid data) {

            }
        });
        recyclerView.setAdapter(menuGridAdapter);
    }

    public List<MenuGrid> setDataMenu(boolean isAdmin) {
        List<MenuGrid> dataMenu = new ArrayList<MenuGrid>();
        if (isAdmin) {
            dataMenu.add(new MenuGrid(R.string.title_menu_kegiatan, R.drawable.ic_activities));
            dataMenu.add(new MenuGrid(R.string.title_menu_informasi, R.drawable.ic_information));
            dataMenu.add(new MenuGrid(R.string.title_menu_kas, R.drawable.ic_cash));
            dataMenu.add(new MenuGrid(R.string.title_menu_structure, R.drawable.ic_structure));
            dataMenu.add(new MenuGrid(R.string.title_menu_warga, R.drawable.ic_warga));
            dataMenu.add(new MenuGrid(R.string.title_menu_voting, R.drawable.ic_voting));
            dataMenu.add(new MenuGrid(R.string.title_menu_jasa, R.drawable.ic_service));
            dataMenu.add(new MenuGrid(R.string.title_menu_musrenbang, R.drawable.ic_musrenbang));
        } else {
            dataMenu.add(new MenuGrid(R.string.title_menu_kas, R.drawable.ic_cash));
            dataMenu.add(new MenuGrid(R.string.title_menu_kegiatan, R.drawable.ic_activities));
            dataMenu.add(new MenuGrid(R.string.title_menu_complaint, R.drawable.ic_complaint));
            dataMenu.add(new MenuGrid(R.string.title_menu_structure, R.drawable.ic_structure));
            dataMenu.add(new MenuGrid(R.string.title_menu_jasa, R.drawable.ic_service));
            dataMenu.add(new MenuGrid(R.string.title_menu_voting, R.drawable.ic_voting));
            dataMenu.add(new MenuGrid(R.string.title_menu_musrenbang, R.drawable.ic_musrenbang));
            dataMenu.add(new MenuGrid(R.string.title_menu_darurat, R.drawable.ic_emergency));
        }

        return dataMenu;
    }
}