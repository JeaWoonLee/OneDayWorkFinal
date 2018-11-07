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
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.util.handler.BackPressCloseHandler;
import com.edu.lx.onedayworkfinal.util.volley.Base;

public class OfferMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    AnotherFrontFragment anotherFrontFragment;
    FindLaborFrontFragment findLaborFrontFragment;

    public final static int ANOTHER_FRAGMENT = 0;
    public final static int FIND_LABOR_FRAGMENT = 1;

    NavigationView navigationView2;

    DrawerLayout drawerLayout2;

    private BackPressCloseHandler backPressCloseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_main);

        Base.sessionManager.checkLogin();

        backPressCloseHandler = new BackPressCloseHandler(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout2 = findViewById(R.id.drawer_layout2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout2,toolbar,R.string.navigation_drawer_open1,R.string.navigation_drawer_close1);
        drawerLayout2.addDrawerListener(toggle);
        toggle.syncState();

        navigationView2 = findViewById(R.id.navigationView2);
        navigationView2.setNavigationItemSelectedListener(this);

        TextView offerId = navigationView2.getHeaderView(0).findViewById(R.id.offerId);
        TextView offerName = navigationView2.getHeaderView(0).findViewById(R.id.offerName);
        offerId.setText(Base.sessionManager.getUserDetails().get("id"));
        offerName.setText(Base.sessionManager.getUserDetails().get("name"));

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int resId = menuItem.getItemId();

        switch (resId){
            case R.id.front2:
                getSupportFragmentManager().beginTransaction().replace(R.id.container2,anotherFrontFragment).commit();
                break;

            case R.id.regist_work:
                getSupportFragmentManager().beginTransaction().replace(R.id.container2,anotherFrontFragment).commit();
                break;

            case R.id.req_manage:
                getSupportFragmentManager().beginTransaction().replace(R.id.container2,anotherFrontFragment).commit();
                break;

            case R.id.my_account_info2:
                break;

            case R.id.logout2:
                Toast.makeText(this, "정상적으로 로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                Base.sessionManager.logoutUser();
                break;
        }
        drawerLayout2.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
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

    public void changeFragment(int framentIndex2){
        switch (framentIndex2){
            case ANOTHER_FRAGMENT:
                navigationView2.getMenu().findItem(R.id.front2).setChecked(true);
                break;
            case FIND_LABOR_FRAGMENT:
                navigationView2.getMenu().findItem(R.id.work_manage).setChecked(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.container2,anotherFrontFragment).commit();
                break;
        }
    }

    //TODO 사업자 메인화면 구성 (김동가-진행 중)~
}
