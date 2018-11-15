package com.edu.lx.onedayworkfinal.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.join.JoinActivity;
import com.edu.lx.onedayworkfinal.offer.OfferMainActivity;
import com.edu.lx.onedayworkfinal.seeker.SeekerMainActivity;
import com.edu.lx.onedayworkfinal.util.handler.BackPressCloseHandler;
import com.edu.lx.onedayworkfinal.util.session.SessionManager;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    SeekerLoginFragment seekerLoginFrag;
    OfferLoginFragment offerLoginFrag;

    BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //AutoPermission
        AutoPermissions.Companion.loadAllPermissions(this,101);

        //로그인 세션 체크. 로그인이 되어 있다면 해당 액티비티로 보낸다
        loginCheck();

        //back 키 두 번 누르면 액티비티 종료 해주는 Util 클래스
        backPressCloseHandler = new BackPressCloseHandler(this);

        seekerLoginFrag = new SeekerLoginFragment();
        offerLoginFrag = new OfferLoginFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container,seekerLoginFrag).commit();

        // 로그인 구분탭
        TabLayout loginTabs = findViewById(R.id.loginTabs);

        // 탭이 눌렸을 때 콜백함수
        loginTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeTab(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        // end 탭이 눌렸을 떄 콜백함수

        //회원가입 글씨를 클릭했을 때 회원가입 페이지로 보내기
        TextView joinUserTextView = findViewById(R.id.joinUserTextView);
        joinUserTextView.setOnClickListener(v -> showJoinActivity());

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    //로그인 세션 체크. 로그인이 되어 있다면 해당 액티비티로 보낸다
    private void loginCheck() {
        if (Base.sessionManager.isLoggedIn()) {
            String index = Base.sessionManager.getUserDetails().get("userIndex");

            if (Objects.equals(index, SessionManager.IS_SEEKER)) {
                Intent intent = new Intent(this,SeekerMainActivity.class);
                startActivityForResult(intent,101);
            } else if (Objects.equals(index, SessionManager.IS_OFFER)) {
                //구인자 페이지로 보내기(김동가 - 종료)
                Intent intent = new Intent(this, OfferMainActivity.class);
                startActivityForResult(intent,101);
            } else {
                Toast.makeText(this,"로그인세션은 존재하지만 구분이 되어있지 않은 오류",Toast.LENGTH_LONG).show();
            }

        }
    }

    private void showJoinActivity() {
        Intent intent = new Intent(this,JoinActivity.class);
        startActivityForResult(intent,102);
    }

    // 로그인 구분탭이 눌렸을 때 탭 바꾸기
    private void changeTab(@NonNull TabLayout.Tab tab) {
        //몇 번째 탭이 눌렸는가
        int position = tab.getPosition();

        switch (position) {
            case 0 :
                getSupportFragmentManager().beginTransaction().replace(R.id.container,seekerLoginFrag).commit();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,offerLoginFrag).commit();
                break;
        }
    }
    // end 로그인 구분탭이 눌렸을 때 탭 바꾸기
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //구직자 화면에서 로그아웃을 통해 돌아왔을 경우
        if (requestCode == 101) {

            //뒤로 가기 키를 눌러서 돌아온 경우. 재 로그인을 하지 않고 프로그램을 종료시킨다
            if (resultCode == Activity.RESULT_CANCELED) {
                this.finish();
                return;
            }

            //SeekerLoginFragment 의 EditText 를 지워준다
            seekerLoginFrag.seekerIdInput.setText("");
            seekerLoginFrag.seekerPwInput.setText("");

        }

        //로그인 세션 체크. 로그인이 되어 있다면 해당 액티비티로 보낸다
        loginCheck();
    }

    //TODO 회원가입에 성공하면 해당 정보를 기반으로 바로 로그인을 하도록 구현할까?



}
