package com.edu.lx.onedayworkfinal.login;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.edu.lx.onedayworkfinal.R;

public class LoginActivity extends AppCompatActivity {

    SeekerLoginFragment seekerLoginFrag;
    OfferLoginFragment offerLoginFrag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //TODO 권한체크 한번에 하기

        seekerLoginFrag = new SeekerLoginFragment();
        offerLoginFrag = new OfferLoginFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container,seekerLoginFrag).commit();

        //로그인 구분탭
        TabLayout loginTabs = findViewById(R.id.loginTabs);

        //탭이 눌렸을 때 콜백함수
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
    }

    //탭이 눌렸을 때 탭 바꾸기
    private void changeTab(@NonNull TabLayout.Tab tab) {
        //몇 번째 탭이 눌렸는가
        int position = tab.getPosition();

        //TODO 몇 번째 탭이 눌렸는지에 따라 프래그먼트 바꾸기
        switch (position) {
            case 0 :
                getSupportFragmentManager().beginTransaction().replace(R.id.container,seekerLoginFrag).commit();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction().replace(R.id.container,offerLoginFrag).commit();
                break;
        }
    }
}
