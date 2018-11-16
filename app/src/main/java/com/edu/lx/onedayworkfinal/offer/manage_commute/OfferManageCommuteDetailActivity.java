package com.edu.lx.onedayworkfinal.offer.manage_commute;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.edu.lx.onedayworkfinal.R;

public class OfferManageCommuteDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_manage_commute_detail);

        TextView projectNumber = findViewById(R.id.projectNumber);
        Intent intent =getIntent();
        int number = intent.getIntExtra("projectNumber",0);
        String projectName = intent.getStringExtra("projectName");
        String str = String.valueOf(number);
        projectNumber.setText(str + "," + projectName);
    }
}
