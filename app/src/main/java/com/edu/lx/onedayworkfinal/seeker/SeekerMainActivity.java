package com.edu.lx.onedayworkfinal.seeker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.util.handler.BackPressCloseHandler;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;


public class SeekerMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Fragment
    FrontFragment frontFragment;
    public FindJobFrontFragment findJobFrontFragment;
    public ManageJobFrontFragment manageJobFrontFragment;
    MyInfoFragment myInfoFragment;


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

    //FindJobFragment(일 찾기) 에서 사용되는 필터 설정
    //프로젝트 대분류
    static String F_projectSubjectFilter;
    //프로젝트 거리
    static String F_maxDistanceFilter;
    //직군 중분류
    static String F_jobNameFilter;
    //일당
    static String F_jobPayFilter;
    //요구 조건
    static String F_jobRequirementFilter;
    //대상 날짜
    static String F_targetDateFilter;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_main);

        //AutoPermission
        AutoPermissions.Companion.loadAllPermissions(this,101);

        try{
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String key = new String(Base64.encode(md.digest(), 0));
                Log.d("Hash key:", "!!!!!!!"+key+"!!!!!!");
            }
        } catch (Exception e){
            Log.e("name not found", e.toString());
        }

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

        //프래그먼트 초기 설정
        frontFragment = new FrontFragment();
        findJobFrontFragment = new FindJobFrontFragment();
        myInfoFragment = new MyInfoFragment();
        manageJobFrontFragment = new ManageJobFrontFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container,frontFragment).commit();

        //필터 설정 init
        filterInit();

        //TODO 신청 일감 관리 구현하기
        // 수정 181107 yjm

        //TODO 일감 관리 구현하기

        //TODO 오늘의 일감 구현하기

        //TODO 이력 관리 구현하기

        //TODO 일감 초대 구현하기

    }

    // AutoPermission CallBack
    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, new AutoPermissionsListener() {
            @Override
            public void onGranted (int i, @NotNull String[] permissions) {
            }
            @Override
            public void onDenied (int i, @NotNull String[] permissions) {
            }
        });
    }
    // end AutoPermission CallBack


    //필터 초기 설정
    private void filterInit() {
        //초기설정(없음) 으로 설정하기
        F_projectSubjectFilter = getResources().getStringArray(R.array.projectSubjectFilter)[0];
        F_maxDistanceFilter = getResources().getStringArray(R.array.maxDistanceFilter)[0];
        F_jobNameFilter = getResources().getStringArray(R.array.noneFilter)[0];
        F_jobPayFilter = getResources().getStringArray(R.array.jobPayFilter)[0];
        F_jobRequirementFilter = getResources().getStringArray(R.array.jobRequirementFilter)[0];
        F_targetDateFilter = "없음";
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

                //일 관리
            case R.id.manage_job :
                //일 관리 프래그먼트로 이동
                getSupportFragmentManager().beginTransaction().replace(R.id.container, manageJobFrontFragment).commit();
                break;

                //내 계정 정보 프래그먼트로 이동
            case R.id.my_account_info :
                getSupportFragmentManager().beginTransaction().replace(R.id.container, myInfoFragment).commit();
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
            case MANAGE_JOB_FRAGMENT:
                navigationView.getMenu().findItem(R.id.manage_job).setChecked(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.container,manageJobFrontFragment).commit();
                break;

        }
    }

    //프로젝트 디테일 보여주기

    /**
     * showProjectDetail
     * FindJobMapFragment, SeekerProjectListRecyclerView, SeekerManagerFragmentRecyclerView 에서 호출됨
     * @param projectNumber 인텐트에 실어보내서 projectDetailActivity 화면을 띄워준다
     */
    public void showProjectDetail(int projectNumber) {
        Intent intent = new Intent(this,ProjectDetailActivity.class);
        intent.putExtra("projectNumber",projectNumber);
        startActivityForResult(intent,201);
    }

    public void showProjectDetailManage(int candidateNumber) {
        Intent intent = new Intent(this,ManageProjectDetailFragment.class);
        intent.putExtra("candidateNumber",candidateNumber);
        startActivityForResult(intent,202);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (intent != null) {
            switch (requestCode) {
                //프로젝트 디테일 액티비티
                case  201 :
                    break;
                //필터 팝업 액티비티
                case 301 :
                    //인텐트로부터 필터 데이터 설정
                    F_projectSubjectFilter = intent.getStringExtra("projectSubjectFilter");
                    F_maxDistanceFilter = intent.getStringExtra("maxDistanceFilter");
                    F_jobNameFilter = intent.getStringExtra("jobNameFilter");
                    F_jobPayFilter = intent.getStringExtra("jobPayFilter");
                    F_jobRequirementFilter = intent.getStringExtra("jobRequirementFilter");
                    F_targetDateFilter = intent.getStringExtra("targetDateFilter");

                    if (findJobFrontFragment.fragmentIndex == findJobFrontFragment.FIND_JOB_RECYCLER_FRAGMENT){
                        findJobFrontFragment.findJobRecyclerFragment.requestProjectList();
                    }
                    else if (findJobFrontFragment.fragmentIndex == findJobFrontFragment.FIND_JOB_MAP_FRAGMENT) {
                        //findJobFrontFragment.findJobMapFragment.mMapView.removePOIItems(findJobFrontFragment.findJobMapFragment.projectMarkers.toArray(new MapPOIItem[findJobFrontFragment.findJobMapFragment.projectMarkers.size()]));
                        findJobFrontFragment.findJobMapFragment.mMapView.removeAllPOIItems();
                        findJobFrontFragment.findJobMapFragment.showMyLocation();
                        findJobFrontFragment.findJobMapFragment.requestProjectList();
                    }
                    break;
                    case 401 :
                        manageJobFrontFragment.manageJobListFragment.requestManageList(Base.sessionManager.getUserDetails().get("id"));
                        break;
            }
        }

    }
}
