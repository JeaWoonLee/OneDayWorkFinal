package com.edu.lx.onedayworkfinal.join;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.edu.lx.onedayworkfinal.R;

import java.util.Objects;

public class JoinActivity extends AppCompatActivity {

    //Fragment 구분값
    final int FRONT_JOIN_FRAGMENT = 0;
    final int SEEKER_JOIN_FRAGMENT = 1;
    final int OFFER_JOIN_FRAGMENT = 2;

    //현제 프래그먼트 - 기본값 : 0 (FRONT_JOIN_FRAGMENT)
    int fragIndex = FRONT_JOIN_FRAGMENT;

    //Fragment
    FrontJoinFragment frontJoinFragment;
    SeekerJoinFragment seekerJoinFragment;
    OfferJoinFragment offerJoinFragment;

    //툴바
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        //Toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("하루일감 회원가입");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //Fragment Init
        frontJoinFragment = new FrontJoinFragment();
        seekerJoinFragment = new SeekerJoinFragment();
        offerJoinFragment = new OfferJoinFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container,frontJoinFragment).commit();

    }

    /*
     * 프래그먼트를 바꿔주는 메소드
     * changeTab
     * @param tab
     * 0 : front
     * 1 : seeker
     * 2 : offer
     */
    public void changeTab(int tab) {
        switch (tab) {

            case FRONT_JOIN_FRAGMENT :
                getSupportFragmentManager().beginTransaction().replace(R.id.container,frontJoinFragment).commit();
                fragIndex = FRONT_JOIN_FRAGMENT;
                toolbar.setBackgroundColor(getResources().getColor(R.color.seeker,this.getTheme()));
                toolbar.setTitle("하루일감 회원가입");
                break;

            case SEEKER_JOIN_FRAGMENT :
                getSupportFragmentManager().beginTransaction().replace(R.id.container,seekerJoinFragment).commit();
                fragIndex = SEEKER_JOIN_FRAGMENT;
                toolbar.setBackgroundColor(getResources().getColor(R.color.seeker,this.getTheme()));
                toolbar.setTitle("구직자 회원가입");
                break;

            case OFFER_JOIN_FRAGMENT :
                getSupportFragmentManager().beginTransaction().replace(R.id.container,offerJoinFragment).commit();
                fragIndex = OFFER_JOIN_FRAGMENT;
                toolbar.setBackgroundColor(getResources().getColor(R.color.offer,this.getTheme()));
                toolbar.setTitle("구인자 회원가입");
                break;
        }
    }

    /*
     * 뒤로가기 버튼이 눌렸을 때 실행되는 메소드
     * fragIndex 를 FRONT_JOIN_FRAGMENT 로 변경해주고 이미 frontFrag 라면 activity 를 종료시킴
     * @param : item - 어차피 눌릴 메뉴아이템이 뒤로가기 버튼 뿐이라서 상관이 없다.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (fragIndex) {

            case FRONT_JOIN_FRAGMENT :
                onBackPressed();
                break;

            case SEEKER_JOIN_FRAGMENT :
                changeTab(FRONT_JOIN_FRAGMENT);
                break;

            case OFFER_JOIN_FRAGMENT :
                changeTab(FRONT_JOIN_FRAGMENT);
                break;
        }
        return true;
    }
}
