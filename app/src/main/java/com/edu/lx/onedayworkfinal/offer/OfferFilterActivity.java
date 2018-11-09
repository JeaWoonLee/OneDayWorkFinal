package com.edu.lx.onedayworkfinal.offer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.edu.lx.onedayworkfinal.R;

public class OfferFilterActivity extends AppCompatActivity {

    //스피너 설정
    Spinner projectSubjectSpinner2;
    Spinner jobNameSpinner2;
    Spinner jobPaySpinner2;
    Spinner maxDistanceSpinner2;
    Spinner jobRequirementSpinner2;
    TextView targetDate2;

    //프로젝트 대분류
    String[] projectSubjectFilter2;
    //프로젝트 거리
    String[] maxDistanceFilter2;

    //직군 중분류

    //건설현장
    String[] constructionSiteFilter;
    //토목
    String[] civilEngineeringFilter;
    //조선
    String[] shipbuildingFilter;
    //공장
    String[] factoryFilter;
    //운송
    String[] transitFilter;
    //요식업
    String[] cateringFilter;
    //이벤트
    String[] eventFilter;
    //청소
    String[] cleaningFilter;
    //기타
    String[] otherFilter;
    //없음
    String[] noneFilter;
    //일당
    String[] jobPayFilter;
    //요구 조건
    String[] jobRequirementFilter;

    //FindJobFragment(일 찾기) 에서 사용되는 필터 설정
    //프로젝트 대분류
    String F_projectSubjectFilter;
    //프로젝트 거리
    String F_maxDistanceFilter;
    //직군 중분류
    String F_jobNameFilter;
    //일당
    String F_jobPayFilter;
    //요구 조건
    String F_jobRequirementFilter;
    //대상 날짜
    String F_targetDateFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_filter_popup);

        Intent intent = getIntent();
        initFilterSetting(intent);
        initFilterArrays();

        targetDate2 = findViewById(R.id.targetDate);

        projectSubjectSpinner2 = findViewById(R.id.projectSubjectSpinner2);
        jobNameSpinner2 = findViewById(R.id.jobNameSpinner2);
        jobPaySpinner2 = findViewById(R.id.jobPaySpinner2);
        maxDistanceSpinner2 = findViewById(R.id.maxDistanceSpinner2);
        jobRequirementSpinner2 = findViewById(R.id.jobRequirementSpinner2);
        setSubjectSpinner2(projectSubjectSpinner2,projectSubjectFilter2);
        setSpinner(jobPaySpinner2,jobPayFilter);
        setSpinner(maxDistanceSpinner2,maxDistanceFilter2);
        setSpinner(jobRequirementSpinner2,jobRequirementFilter);
        Button confirmFilterButton = findViewById(R.id.confirmFilterButton2);
        confirmFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmFilter();
            }
        });

        setFilterSelected();
    }

    private void setSubjectSpinner2(Spinner projectSubjectSpinner, String[] projectSubjectFilter) {
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,projectSubjectFilter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        projectSubjectSpinner.setAdapter(adapter);
        projectSubjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0 :
                        setSpinner(jobNameSpinner2,noneFilter);
                        break;
                    case 1:
                        setSpinner(jobNameSpinner2,constructionSiteFilter);
                        break;
                    case 2:
                        setSpinner(jobNameSpinner2,civilEngineeringFilter);
                        break;
                    case 3:
                        setSpinner(jobNameSpinner2,shipbuildingFilter);
                        break;
                    case 4:
                        setSpinner(jobNameSpinner2,factoryFilter);
                        break;
                    case 5:
                        setSpinner(jobNameSpinner2,transitFilter);
                        break;
                    case 6:
                        setSpinner(jobNameSpinner2,cateringFilter);
                        break;
                    case 7:
                        setSpinner(jobNameSpinner2,eventFilter);
                        break;
                    case 8:
                        setSpinner(jobNameSpinner2,cleaningFilter);
                        break;
                    case 9:
                        setSpinner(jobNameSpinner2,otherFilter);
                        break;
                }
            }

            @Override
            public void onNothingSelected (AdapterView<?> parent) {

            }
        });

    }

    private void setSpinner(Spinner spinner, String[] arrays) {
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,arrays);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected (AdapterView<?> parent) {

            }
        });
    }

    private void confirmFilter () {
        Intent intent = new Intent();
        intent.putExtra("projectSubjectFilter",(String) projectSubjectSpinner2.getSelectedItem());
        intent.putExtra("maxDistanceFilter",(String) maxDistanceSpinner2.getSelectedItem());
        intent.putExtra("jobNameFilter",(String) jobNameSpinner2.getSelectedItem());
        intent.putExtra("jobPayFilter",(String) jobPaySpinner2.getSelectedItem());
        intent.putExtra("jobRequirementFilter",(String) jobRequirementSpinner2.getSelectedItem());
        intent.putExtra("targetDateFilter",targetDate2.getText().toString());

        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    //intent 로 부터 넘어온 필터 설정
    private void initFilterSetting(Intent intent) {
        F_projectSubjectFilter = intent.getStringExtra("projectSubjectFilter");
        F_maxDistanceFilter = intent.getStringExtra("maxDistanceFilter");
        F_jobNameFilter = intent.getStringExtra("jobNameFilter");
        F_jobPayFilter = intent.getStringExtra("jobPayFilter");
        F_jobRequirementFilter = intent.getStringExtra("jobRequirementFilter");
        F_targetDateFilter = intent.getStringExtra("targetDateFilter");
    }

    private void setFilterSelected () {
        for (int i = 0 ; i < projectSubjectFilter2.length ; i ++ ) {
            if (TextUtils.equals(projectSubjectFilter2[i],F_projectSubjectFilter)) {
                projectSubjectSpinner2.setSelection(i);
                break;
            }
        }

        for (int i = 0 ; i < maxDistanceFilter2.length ; i ++ ) {
            if (TextUtils.equals(maxDistanceFilter2[i],F_maxDistanceFilter)) {
                maxDistanceSpinner2.setSelection(i);
                break;
            }
        }
        for (int i = 0 ; i < jobPayFilter.length ; i ++ ) {
            if (TextUtils.equals(jobPayFilter[i],F_jobPayFilter)) {
                jobPaySpinner2.setSelection(i);
                break;
            }
        }
        for (int i = 0 ; i < jobRequirementFilter.length ; i ++ ) {
            if (TextUtils.equals(jobRequirementFilter[i],F_jobRequirementFilter)) {
                jobRequirementSpinner2.setSelection(i);
                break;
            }
        }
        targetDate2.setText(F_targetDateFilter);

    }

    //res/values/arrays 에 설정해 둔 필터 배열 설정
    private void initFilterArrays() {
        projectSubjectFilter2 = getResources().getStringArray(R.array.projectSubjectFilter);
        maxDistanceFilter2 = getResources().getStringArray(R.array.maxDistanceFilter);
        constructionSiteFilter = getResources().getStringArray(R.array.constructionSiteFilter);
        civilEngineeringFilter = getResources().getStringArray(R.array.civilEngineeringFilter);
        factoryFilter = getResources().getStringArray(R.array.factoryFilter);
        shipbuildingFilter = getResources().getStringArray(R.array.shipbuildingFilter);
        transitFilter = getResources().getStringArray(R.array.transitFilter);
        cateringFilter = getResources().getStringArray(R.array.cateringFilter);
        eventFilter = getResources().getStringArray(R.array.eventFilter);
        cleaningFilter = getResources().getStringArray(R.array.cleaningFilter);
        otherFilter = getResources().getStringArray(R.array.otherFilter);
        noneFilter = getResources().getStringArray(R.array.noneFilter);
        jobPayFilter = getResources().getStringArray(R.array.jobPayFilter);
        jobRequirementFilter = getResources().getStringArray(R.array.jobRequirementFilter);
    }
}
