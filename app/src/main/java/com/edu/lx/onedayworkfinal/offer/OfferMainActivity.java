package com.edu.lx.onedayworkfinal.offer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.offer.manage_commute.OfferManageCommuteFragment;
import com.edu.lx.onedayworkfinal.offer.manage_work.OfferManageWorkFragment;
import com.edu.lx.onedayworkfinal.offer.my_info.OfferMyInfoFragment;
import com.edu.lx.onedayworkfinal.util.handler.BackPressCloseHandler;
import com.edu.lx.onedayworkfinal.util.volley.Base;

import java.util.Objects;

public class OfferMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    NavigationView navigationView;
    DrawerLayout drawerLayout;

    //선택화면
    OfferFrontFragment offerFrontFragment;
    public static final int FRONT_FRAGMENT = 0;
    //일감 관리
    OfferManageWorkFragment offerManageWorkFragment;
    public static final int MANAGE_WORK_FRAGMENT = 1;
    //출퇴근 관리
    OfferManageCommuteFragment offerManageCommuteFragment;
    public static final int MANAGE_COMMUTE_FRAGMENT = 2;
    //내 정보
    OfferMyInfoFragment offerMyInfoFragment;

    public static final int MY_INFO_FRAGMENT = 3;

    public static int FRAGMENT_INDEX = FRONT_FRAGMENT;

    private BackPressCloseHandler backPressCloseHandler;

    public static String OFFER_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_main);

        //세션 로그인 체크
        Base.sessionManager.checkLogin();
        //백키 핸들러
        backPressCloseHandler = new BackPressCloseHandler(this);
        //툴바 설정
        Toolbar toolbar2 = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);

        //드로어 레이아웃과 햄버거 메뉴
        drawerLayout = findViewById(R.id.drawer_layout2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar2,R.string.navigation_drawer_open1,R.string.navigation_drawer_close1);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //네비게이션 뷰
        navigationView = findViewById(R.id.navigationView2);
        navigationView.setNavigationItemSelectedListener(this);

        //네비게이션 뷰 헤더에 아이디 설정
        TextView offerId = navigationView.getHeaderView(0).findViewById(R.id.offerId);
        TextView offerName = navigationView.getHeaderView(0).findViewById(R.id.offerName);
        offerId.setText(Base.sessionManager.getUserDetails().get("id"));
        offerName.setText(Base.sessionManager.getUserDetails().get("name"));

        OFFER_ID = Base.sessionManager.getUserDetails().get("id");

        //FragmentInit
        offerFrontFragment = new OfferFrontFragment();
        offerManageWorkFragment = new OfferManageWorkFragment();
        offerManageCommuteFragment = new OfferManageCommuteFragment();
        offerMyInfoFragment = new OfferMyInfoFragment();
        //기본 FrontFragment 설정
        getSupportFragmentManager().beginTransaction().add(R.id.container2, offerFrontFragment).commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int resId = menuItem.getItemId();

        switch (resId){
            case R.id.front:
                changeFragment(FRONT_FRAGMENT);
                break;
            case R.id.manage_work:
                changeFragment(MANAGE_WORK_FRAGMENT);
                break;
            case R.id.manage_commute:
                changeFragment(MANAGE_COMMUTE_FRAGMENT);
                break;
            case R.id.my_info:
                changeFragment(MY_INFO_FRAGMENT);
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
                navigationView.getMenu().findItem(R.id.front).setChecked(true);
                FRAGMENT_INDEX = FRONT_FRAGMENT;
                Objects.requireNonNull(getSupportActionBar()).setTitle("사업자 메인화면");
                getSupportFragmentManager().beginTransaction().replace(R.id.container2, offerFrontFragment).commit();
                break;
            case MANAGE_WORK_FRAGMENT:
                navigationView.getMenu().findItem(R.id.manage_work).setChecked(true);
                FRAGMENT_INDEX = MANAGE_WORK_FRAGMENT;
                Objects.requireNonNull(getSupportActionBar()).setTitle("일감 관리");
                getSupportFragmentManager().beginTransaction().replace(R.id.container2, offerManageWorkFragment).commit();
                break;
            case MANAGE_COMMUTE_FRAGMENT:
                FRAGMENT_INDEX = MANAGE_COMMUTE_FRAGMENT;
                navigationView.getMenu().findItem(R.id.manage_commute).setChecked(true);
                Objects.requireNonNull(getSupportActionBar()).setTitle("출퇴근 관리");
                getSupportFragmentManager().beginTransaction().replace(R.id.container2, offerManageCommuteFragment).commit();
                break;
            case MY_INFO_FRAGMENT:
                FRAGMENT_INDEX = MY_INFO_FRAGMENT;
                Objects.requireNonNull(getSupportActionBar()).setTitle("내 정보");
                getSupportFragmentManager().beginTransaction().replace(R.id.container2, offerMyInfoFragment).commit();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (FRAGMENT_INDEX == FRONT_FRAGMENT) {
            backPressCloseHandler.onBackPressed();
        }  else {

            changeFragment(FRONT_FRAGMENT);
        }


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
