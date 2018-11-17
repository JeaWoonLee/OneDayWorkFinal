package com.edu.lx.onedayworkfinal.login.findInfo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

import com.edu.lx.onedayworkfinal.R;

import java.util.Objects;

public class FindInfoActivity extends AppCompatActivity {

    final int FRONT_FIND_FRAGMENT = 0;
    final int FIND_ID_FRAGMENT =1;
    final int FIND_PW_FRAGNENT= 2;

    int fragdex = FRONT_FIND_FRAGMENT;

    FrontFindFragment frontFindFragment;
    FindIDFragment findIDFragment;
    FindPWFragment findPWFragment;

    Toolbar toolbar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_info);

        toolbar2 = findViewById(R.id.toolbar2);
        toolbar2.setTitle("하루일감 아이디/비밀번호 찾기");
        setSupportActionBar(toolbar2);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        frontFindFragment = new FrontFindFragment();
        findIDFragment = new FindIDFragment();
        findPWFragment = new FindPWFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container2,frontFindFragment).commit();
    }

    public void changeTabs(int tabs){
        switch (tabs){
            case FRONT_FIND_FRAGMENT:
                getSupportFragmentManager().beginTransaction().replace(R.id.container2,frontFindFragment).commit();
                fragdex = FRONT_FIND_FRAGMENT;
                toolbar2.setBackgroundColor(getResources().getColor(R.color.dark_gray,this.getTheme()));
                toolbar2.setTitle("회원 정보 찾기");
                break;

            case FIND_ID_FRAGMENT:
                getSupportFragmentManager().beginTransaction().replace(R.id.container2,findIDFragment).commit();
                fragdex = FIND_ID_FRAGMENT;
                toolbar2.setBackgroundColor(getResources().getColor(R.color.seeker,this.getTheme()));
                toolbar2.setTitle("아이디 찾기");
                break;

            case FIND_PW_FRAGNENT:
                getSupportFragmentManager().beginTransaction().replace(R.id.container2,findPWFragment).commit();
                fragdex = FIND_PW_FRAGNENT;
                toolbar2.setBackgroundColor(getResources().getColor(R.color.offer,this.getTheme()));
                toolbar2.setTitle("비밀번호 찾기");
                break;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (fragdex){
            case FRONT_FIND_FRAGMENT:
                onBackPressed();
                break;

            case FIND_ID_FRAGMENT:
                changeTabs(FRONT_FIND_FRAGMENT);
                break;

            case FIND_PW_FRAGNENT:
                changeTabs(FRONT_FIND_FRAGMENT);
                break;
        }

        return true;
    }
}
