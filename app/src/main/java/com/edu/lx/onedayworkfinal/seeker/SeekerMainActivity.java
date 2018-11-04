package com.edu.lx.onedayworkfinal.seeker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.edu.lx.onedayworkfinal.R;

public class SeekerMainActivity extends AppCompatActivity {

    FindJobFragment findJobFragment;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_main);

        //툴바 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //TODO 툴바 햄버거 버튼 추가하기
        //TODO 네비게이션 바 추가하기

        //TODO 일감 구하기 구현하기
        findJobFragment = new FindJobFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container,findJobFragment).commit();

        //TODO 신청 일감 관리 구현하기
        //TODO 일감 관리 구현하기
        //TODO 오늘의 일감 구현하기
        //TODO 오늘의 일감 관리하기
        //TODO 일감 초대 구현하기

    }
}
