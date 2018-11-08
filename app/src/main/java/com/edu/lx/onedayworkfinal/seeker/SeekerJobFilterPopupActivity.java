package com.edu.lx.onedayworkfinal.seeker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.edu.lx.onedayworkfinal.R;

public class SeekerJobFilterPopupActivity extends AppCompatActivity {

    //스피너 설정
    Spinner projectSubjectSpinner;
    Spinner jobNameSpinner;
    Spinner jobPaySpinner;
    Spinner maxDistanceSpinner;
    Spinner jobRequirementSpinner;
    TextView targetDate;

    //프로젝트 대분류
    String[] projectSubjectFilter;
    //프로젝트 거리
    String[] maxDistanceFilter;
    //직군 중분류
    String[] constructionSiteFilter;
    String[] civilEngineeringFilter;
    String[] shipbuildingFilter;
    String[] transitFilter;
    String[] cateringFilter;
    String[] eventFilter;
    String[] cleaningFilter;
    String[] otherFilter;
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
        setContentView(R.layout.activity_filter_popup);

        Intent intent = getIntent();
        initFilterSetting(intent);
        initFilterArrays();

        targetDate = findViewById(R.id.targetDate);

        projectSubjectSpinner = findViewById(R.id.projectSubjectSpinner);
        jobNameSpinner = findViewById(R.id.jobNameSpinner);
        jobPaySpinner = findViewById(R.id.jobPaySpinner);
        maxDistanceSpinner = findViewById(R.id.maxDistanceSpinner);
        jobRequirementSpinner = findViewById(R.id.jobRequirementSpinner);
        setSubjectSpinner(projectSubjectSpinner,projectSubjectFilter);
        setSpinner(jobPaySpinner,jobPayFilter);
        setSpinner(maxDistanceSpinner,maxDistanceFilter);
        setSpinner(jobRequirementSpinner,jobRequirementFilter);

        Button confirmFilterButton = findViewById(R.id.confirmFilterButton);
        confirmFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void setSubjectSpinner(Spinner projectSubjectSpinner, String[] projectSubjectFilter) {
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,projectSubjectFilter);
    }

    private void setSpinner(Spinner spinner, String[] arrays) {

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

    //res/values/arrays 에 설정해 둔 필터 배열 설정
    private void initFilterArrays() {
        projectSubjectFilter = getResources().getStringArray(R.array.projectSubjectFilter);
        maxDistanceFilter = getResources().getStringArray(R.array.maxDistanceFilter);
        constructionSiteFilter = getResources().getStringArray(R.array.constructionSiteFilter);
        civilEngineeringFilter = getResources().getStringArray(R.array.civilEngineeringFilter);
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
