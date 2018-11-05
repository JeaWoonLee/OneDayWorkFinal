package com.edu.lx.onedayworkfinal.seeker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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

    FindJobFragment findJobFragment;

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
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //TODO 네비게이션 바 추가하기
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        //네비게이션 바 Id / name 세팅
        TextView seekerId = navigationView.getHeaderView(0).findViewById(R.id.seekerId);
        TextView seekerName = navigationView.getHeaderView(0).findViewById(R.id.seekerName);
        seekerId.setText(Base.sessionManager.getUserDetails().get("id"));
        seekerName.setText(Base.sessionManager.getUserDetails().get("name"));

        //TODO 일감 구하기 구현하기
        findJobFragment = new FindJobFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container,findJobFragment).commit();

        //TODO 신청 일감 관리 구현하기
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
                //일 찾기
            case R.id.find_job :
                //일 찾기 프래그먼트로 이동
                break;
                //일 관리 프래그먼트로 이동
            case R.id.manage_job :
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
}
