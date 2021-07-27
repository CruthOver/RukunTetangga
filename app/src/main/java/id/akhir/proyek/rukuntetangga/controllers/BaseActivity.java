package id.akhir.proyek.rukuntetangga.controllers;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import id.akhir.proyek.rukuntetangga.R;
import id.akhir.proyek.rukuntetangga.models.MenuGrid;

public class BaseActivity extends AppCompatActivity {
    public final static String TAG = "RukunTetangga App";

    public Context context;

    private final int[] back_layouts = {R.layout.activity_main};

    private final int[] no_action_bars = {R.layout.activity_main};

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initViews(layoutResID);
    }

    protected void initViews(int layout){
        context = this;
//        appSession = new AppSession(this);
//        mApiService = UtilsApi.getApiService();

//        if(isNoActionbar(layout)){
//            getWindow().setBackgroundDrawableResource(R.drawable.background);
//        }
//
//        if(isBack(layout)){
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
//
//        progressDialog = new Dialog(context);
    }

    private boolean isBack(int layout){
        for (int back_layout : back_layouts) {
            if (layout == back_layout)
                return true;
        }
        return false;
    }

    private boolean isNoActionbar(int layout){
        for (int no_action_bar : no_action_bars) {
            if (layout == no_action_bar)
                return true;
        }
        return false;
    }

    public void checkSession2(){
        //TODO: CheckSession Login
    }

    public void logout(){
        //TODO: Logout
    }

    public void alertSubmitDone(String title, String message, int ok){
        //TODO: Alert Confirmation
    }

    public void showProgressBar(boolean show){
        //TODO: Show Progress Bar
    }

    public void showSnackbar(String message){
        //TODO: Show Snackbar
//        if(findViewById(R.id.coordinator_layout)==null)return;
//        Snackbar.make(findViewById(R.id.coordinator_layout), message, Snackbar.LENGTH_SHORT).show();
    }

    //TODO: Get User Session
//    public User getUserSession(){
//        return new Gson().fromJson(appSession.getData(AppSession.USER), User.class);
//    }

    public String getUserToken(){
        //TODO: Get User Token
        return "";
    }

    public String getUserPhone(){
        //TODO: Get User Phone
        return "";
    }

    public String getStringExtraData(String name){
        return getIntent().getStringExtra(name);
    }

    public long getLongExtraData(String name){
        return getIntent().getLongExtra(name, 0);
    }

    public boolean getBooleanExtraData(String name){
        return getIntent().getBooleanExtra(name, false);
    }
}
