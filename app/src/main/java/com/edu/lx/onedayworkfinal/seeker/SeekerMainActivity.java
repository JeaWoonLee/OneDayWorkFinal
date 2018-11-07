package com.edu.lx.onedayworkfinal.seeker;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Toast;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.util.handler.BackPressCloseHandler;
import com.edu.lx.onedayworkfinal.util.volley.Base;

public class SeekerMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Fragment
    FrontFragment frontFragment;
    FindJobFrontFragment findJobFrontFragment;

    //TODO 프래그먼트 추가될 때마다 index 추가하기
    //Fragment Index
    public final static int FRONT_FRAGMENT = 0;
    public final static int FIND_JOB_FRAGMENT = 1;
    public final static int MANAGE_JOB_FRAGMENT = 2;
    //네비게이션 뷰
    NavigationView navigationView;

    //드로어 레이아웃
    DrawerLayout drawerLayout;

    //백키 핸들러
    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_main);

        //로그인 체크
        Base.sessionManager.checkLogin();

        //back 키 두 번 누르면 액티비티 종료 해주는 Util 클래스
        backPressCloseHandler = new BackPressCloseHandler(this);

        //툴바 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //툴바 햄버거 버튼 추가하기
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //TODO 네비게이션 바 추가하기
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        //네비게이션 바 Id / name 세팅
        TextView seekerId = navigationView.getHeaderView(0).findViewById(R.id.seekerId);
        TextView seekerName = navigationView.getHeaderView(0).findViewById(R.id.seekerName);
        seekerId.setText(Base.sessionManager.getUserDetails().get("id"));
        seekerName.setText(Base.sessionManager.getUserDetails().get("name"));

        //TODO 일감 구하기 구현하기
        frontFragment = new FrontFragment();
        findJobFrontFragment = new FindJobFrontFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container,frontFragment).commit();

        //TODO 신청 일감 관리 구현하기
        // 수정 181107 yjm

        getSupportFragmentManager().beginTransaction().add(R.id.container,frontFragment).commit();

        //TODO 일감 관리 구현하기

        //TODO 오늘의 일감 구현하기

        //TODO 이력 관리 구현하기

        //TODO 일감 초대 구현하기

    }

    //네비게이션 뷰 아이템 클릭 리스너
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int resId = menuItem.getItemId();

        switch (resId) {

                //프론트 페이지
            case R.id.front :
                getSupportFragmentManager().beginTransaction().replace(R.id.container,frontFragment).commit();

                break;

                //일 찾기
            case R.id.find_job :

                //일 찾기 프래그먼트로 이동
                getSupportFragmentManager().beginTransaction().replace(R.id.container,findJobFrontFragment).commit();
                break;

                //내 계정 정보 프래그먼트로 이동
            case R.id.my_account_info :
                break;

                //로그 아웃
            case R.id.logout :
                //세션 정보를 null 로 삭제하고 finish 해준다
                Toast.makeText(this,"정상적으로 로그아웃 되었습니다",Toast.LENGTH_LONG).show();
                Base.sessionManager.logoutUser();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
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

    //프래그먼트 바꾸기
    public void changeFragment(int fragmentIndex1) {

        switch (fragmentIndex1) {
            case FRONT_FRAGMENT :
                navigationView.getMenu().findItem(R.id.front).setChecked(true);
                break;
            case FIND_JOB_FRAGMENT :
                navigationView.getMenu().findItem(R.id.find_job).setChecked(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.container,findJobFrontFragment).commit();
                break;

        }
    }

    //프로젝트 디테일 보여주기
    public void showProjectDetail(int projectNumber) {
        Intent intent = new Intent(this,ProjectDetailActivity.class);
        intent.putExtra("projectNumber",projectNumber);
        startActivityForResult(intent,201);
    }
}
