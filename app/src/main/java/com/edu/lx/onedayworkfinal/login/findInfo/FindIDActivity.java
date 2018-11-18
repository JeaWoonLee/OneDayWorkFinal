package com.edu.lx.onedayworkfinal.login.findInfo;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.util.handler.BackPressCloseHandler;

public class FindIDActivity extends AppCompatActivity {

    OfferIDFindFragment offerIDFindFragment;
    SeekerIDFindFragment seekerIDFindFragment;

    BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        backPressCloseHandler = new BackPressCloseHandler(this);

        offerIDFindFragment = new OfferIDFindFragment();
        seekerIDFindFragment = new SeekerIDFindFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container2, offerIDFindFragment).commit();

        TabLayout findIDTabs = findViewById(R.id.findIDTabs);
        findIDTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeIDTabs(tab);
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

    private void changeIDTabs(TabLayout.Tab tab){
        int positions = tab.getPosition();

        switch (positions){
            case 0:
                getSupportFragmentManager().beginTransaction().replace(R.id.container2,offerIDFindFragment).commit();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction().replace(R.id.container2,seekerIDFindFragment).commit();
                break;
        }
    }

}
