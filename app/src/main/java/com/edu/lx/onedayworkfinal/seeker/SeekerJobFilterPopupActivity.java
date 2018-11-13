package com.edu.lx.onedayworkfinal.seeker;

import android.app.Activity;
import android.app.DatePickerDialog;
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

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
        confirmFilterButton.setOnClickListener(v -> confirmFilter());

        Button selectTargetDateButton =findViewById(R.id.selectTargetDateButton);
        selectTargetDateButton.setOnClickListener(v -> selectTargetDate());
        setFilterSelected();
    }

    private void selectTargetDate() {
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String date = year + "-" + (month+1) + "-" + dayOfMonth;
            targetDate.setText(date);
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(new Date().getTime());
        dialog.show();
    }

    private void setSubjectSpinner(Spinner projectSubjectSpinner, String[] projectSubjectFilter) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,projectSubjectFilter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        projectSubjectSpinner.setAdapter(adapter);
        projectSubjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0 :
                        setSpinner(jobNameSpinner,noneFilter);
                        break;
                    case 1:
                        setSpinner(jobNameSpinner,constructionSiteFilter);
                        break;
                    case 2:
                        setSpinner(jobNameSpinner,civilEngineeringFilter);
                        break;
                    case 3:
                        setSpinner(jobNameSpinner,shipbuildingFilter);
                        break;
                    case 4:
                        setSpinner(jobNameSpinner,factoryFilter);
                        break;
                    case 5:
                        setSpinner(jobNameSpinner,transitFilter);
                        break;
                    case 6:
                        setSpinner(jobNameSpinner,cateringFilter);
                        break;
                    case 7:
                        setSpinner(jobNameSpinner,eventFilter);
                        break;
                    case 8:
                        setSpinner(jobNameSpinner,cleaningFilter);
                        break;
                    case 9:
                        setSpinner(jobNameSpinner,otherFilter);
                        break;
                }
                F_jobNameFilter = "없음";
            }

            @Override
            public void onNothingSelected (AdapterView<?> parent) {

            }
        });

    }

    private void setSpinner(Spinner spinner, String[] arrays) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,arrays);
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
        intent.putExtra("projectSubjectFilter",(String) projectSubjectSpinner.getSelectedItem());
        intent.putExtra("maxDistanceFilter",(String) maxDistanceSpinner.getSelectedItem());
        intent.putExtra("jobNameFilter",(String) jobNameSpinner.getSelectedItem());
        intent.putExtra("jobPayFilter",(String) jobPaySpinner.getSelectedItem());
        intent.putExtra("jobRequirementFilter",(String) jobRequirementSpinner.getSelectedItem());
        intent.putExtra("targetDateFilter",targetDate.getText().toString());

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
        for (int i = 0 ; i < projectSubjectFilter.length ; i ++ ) {
            if (TextUtils.equals(projectSubjectFilter[i],F_projectSubjectFilter)) {
                projectSubjectSpinner.setSelection(i);
                break;
            }
        }

        for (int i = 0 ; i < maxDistanceFilter.length ; i ++ ) {
            if (TextUtils.equals(maxDistanceFilter[i],F_maxDistanceFilter)) {
                maxDistanceSpinner.setSelection(i);
                break;
            }
        }
        for (int i = 0 ; i < jobPayFilter.length ; i ++ ) {
            if (TextUtils.equals(jobPayFilter[i],F_jobPayFilter)) {
                jobPaySpinner.setSelection(i);
                break;
            }
        }
        for (int i = 0 ; i < jobRequirementFilter.length ; i ++ ) {
            if (TextUtils.equals(jobRequirementFilter[i],F_jobRequirementFilter)) {
                jobRequirementSpinner.setSelection(i);
                break;
            }
        }
        targetDate.setText(F_targetDateFilter);

    }

    //res/values/arrays 에 설정해 둔 필터 배열 설정
    private void initFilterArrays() {
        projectSubjectFilter = getResources().getStringArray(R.array.projectSubjectFilter);
        maxDistanceFilter = getResources().getStringArray(R.array.maxDistanceFilter);
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
