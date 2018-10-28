package com.edu.lx.onedayworkfinal.login;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.edu.lx.onedayworkfinal.R;
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
}
