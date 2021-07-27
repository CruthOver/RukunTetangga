package id.akhir.proyek.rukuntetangga;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import id.akhir.proyek.rukuntetangga.controllers.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadBottomNavigation("Admin");
    }

    private void loadBottomNavigation(String user) {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.getMenu().clear();
//        AppBarConfiguration appBarConfiguration;
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        NavController navController = navHostFragment.getNavController();
        navView.setItemIconTintList(null);
        if (user.equals("Admin")) {
            navController.setGraph(R.navigation.navigation_admin);
            navView.inflateMenu(R.menu.bottom_nav_admin);
//            appBarConfiguration = new AppBarConfiguration.Builder(
//                    R.id.navigation_home, R.id.navigation_letter, R.id.navigation_laporan, R.id.navigation_emergency, R.id.navigation_niaga)
//                    .build();
        } else {
            navController.setGraph(R.navigation.navigation_user);
            navView.inflateMenu(R.menu.bottom_nav_user);
//            appBarConfiguration = new AppBarConfiguration.Builder(
//                    R.id.navigation_home, R.id.navigation_letter, R.id.navigation_laporan, R.id.navigation_job, R.id.navigation_niaga)
//                    .build();
        }
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }
}