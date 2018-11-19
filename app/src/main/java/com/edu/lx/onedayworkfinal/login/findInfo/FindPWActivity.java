package com.edu.lx.onedayworkfinal.login.findInfo;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.util.handler.BackPressCloseHandler;

public class FindPWActivity extends AppCompatActivity {

    OfferPWFindFragment offerPWFindFragment;
    SeekerPWFindFragment seekerPWFindFragment;

    BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        backPressCloseHandler = new BackPressCloseHandler(this);

        offerPWFindFragment = new OfferPWFindFragment();
        seekerPWFindFragment = new SeekerPWFindFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container2,offerPWFindFragment).commit();

        TabLayout findPWTabs = findViewById(R.id.findPWTabs);
        findPWTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changePWTabs(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    private void changePWTabs(TabLayout.Tab tab){
        int positions = tab.getPosition();

        switch (positions) {
            case 0:
                getSupportFragmentManager().beginTransaction().replace(R.id.container2,offerPWFindFragment).commit();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction().replace(R.id.container2,seekerPWFindFragment).commit();
                break;
        }
    }

}
