package com.edu.lx.onedayworkfinal.login;

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
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {

    SeekerLoginFragment seekerLoginFrag;
    OfferLoginFragment offerLoginFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //AutoPermission
        AutoPermissions.Companion.loadAllPermissions(this,101);

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
        joinUserTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showJoinActivity();
            }
        });
    }

    private void showJoinActivity() {
        Intent intent = new Intent(this,JoinActivity.class);
        startActivityForResult(intent,101);
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
                Toast.makeText(getApplicationContext(),"거절한 권한 : " + permissions.length,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onDenied (int i, @NotNull String[] permissions) {
                Toast.makeText(getApplicationContext(),"거절한 권한 : " + permissions.length,Toast.LENGTH_LONG).show();
            }
        });
    }
    // end AutoPermission CallBack

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //구직자 화면에서 로그아웃을 통해 돌아왔을 경우
        if (requestCode == 101) {
            //SeekerLoginFragment 의 EditText 를 지워준다
            seekerLoginFrag.seekerIdInput.setText("");
            seekerLoginFrag.seekerPwInput.setText("");
        }
    }


    //TODO 회원가입에 성공하면 해당 정보를 기반으로 바로 로그인을 하도록 구현할까?
}
