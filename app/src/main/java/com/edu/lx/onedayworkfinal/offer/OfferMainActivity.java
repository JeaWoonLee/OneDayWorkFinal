package com.edu.lx.onedayworkfinal.offer;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.util.handler.BackPressCloseHandler;
import com.edu.lx.onedayworkfinal.util.volley.Base;

public class OfferMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    NavigationView navigationView;
    DrawerLayout drawerLayout;

    OfferFrontFragment offerFrontFragment;

    public static final int FRONT_FRAGMENT = 0;

    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_main);

        Base.sessionManager.checkLogin();
        backPressCloseHandler = new BackPressCloseHandler(this);

        Toolbar toolbar2 = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);

        drawerLayout = findViewById(R.id.drawer_layout2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar2,R.string.navigation_drawer_open1,R.string.navigation_drawer_close1);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.navigationView2);
        navigationView.setNavigationItemSelectedListener(this);

        TextView offerId = navigationView.getHeaderView(0).findViewById(R.id.offerId);
        TextView offerName = navigationView.getHeaderView(0).findViewById(R.id.offerName);
        offerId.setText(Base.sessionManager.getUserDetails().get("id"));
        offerName.setText(Base.sessionManager.getUserDetails().get("name"));

        offerFrontFragment = new OfferFrontFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container2, offerFrontFragment).commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int resId = menuItem.getItemId();

        switch (resId){
            case R.id.front2:
                getSupportFragmentManager().beginTransaction().replace(R.id.container2, offerFrontFragment).commit();
                break;

            case R.id.req_manage:
                break;

            case R.id.rec_labor:
                break;

            case R.id.regist_work:
                break;

            case R.id.my_account_info2:
                break;

            case R.id.logout2:
                Base.sessionManager.logoutUser();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * changeFragment
     * @param fragmentIndex 프래그먼트 인덱스. OfferMainActivity 안에 static 하게 존재한다
     *                      입력 Index 에 따라 해당 프래그먼트로 바꿔준다
     */
    public void changeFragment(int fragmentIndex){
        switch (fragmentIndex){
            case FRONT_FRAGMENT:
                navigationView.getMenu().findItem(R.id.front2).setChecked(true);
                break;

        }
    }
    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Base.sessionManager.checkLogin();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Base.sessionManager.checkLogin();
    }


}
