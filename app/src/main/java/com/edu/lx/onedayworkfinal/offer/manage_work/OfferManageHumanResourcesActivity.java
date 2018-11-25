package com.edu.lx.onedayworkfinal.offer.manage_work;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.request.StringRequest;
import com.applandeo.materialcalendarview.CalendarUtils;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.offer.recycler_view.HumanResourceJobRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.HumanResRsponseModel;
import com.edu.lx.onedayworkfinal.vo.JobCandidateVO;
import com.edu.lx.onedayworkfinal.vo.ManageHumanResourceModel;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OfferManageHumanResourcesActivity extends AppCompatActivity {

    public List<JobCandidateVO> jobNumberList;
    public HashMap<Integer,List<ManageHumanResourceModel>> recruitMap;

    String projectNumber;
    String projectStartDate;
    String projectEndDate;

    Toolbar toolbar;

    Button showSelectDayButton;
    LinearLayout selectDayLayout;
    int selectDayLayoutVisible = 1;

    CalendarView calendarView;


    Button showRecyclerViewContainerButton;
    public LinearLayout container;
    public FrameLayout recyclerViewContainerLayout;
    int recyclerViewContainerVisible = 1;
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_manage_human_resources);

        Intent intent = getIntent();
        projectNumber = intent.getStringExtra("projectNumber");
        projectStartDate = intent.getStringExtra("projectStartDate");
        projectEndDate = intent.getStringExtra("projectEndDate");
        Log.d(projectStartDate,projectEndDate);
        initUI();

        calendarSetting();
        requestProjectRecruitInfo(projectNumber);
    }

    private void calendarSetting() {

    }

    /**
     * requestProjectRecruitInfo
     * @param projectNumber 해당 프로젝트의 구인 현황 목록을 가져온다
     *                      1. 프로젝트 targetDate 별 인원수 현황
     *                      2. 프로젝트 기간
     *                      3. 한 명도 구해지지 않은 날짜
     *                      4. 구해지긴 했지만 아직 다 구하진 못한 날짜
     *                      5. 구인이 완료된 날짜
     */
    private void requestProjectRecruitInfo(String projectNumber) {
        String url = getResources().getString(R.string.url) + "requestProjectRecruitInfo.do";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                this::processProjectRecruitInfoResponse,
                error -> {

                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("projectNumber",projectNumber);
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    /**
     * processProjectRecruitInfoResponse
     * @param response 프로젝트 구인현황을 담고있다
     */
    private void processProjectRecruitInfoResponse(String response) {
        JobCandidateVO[] result = Base.gson.fromJson(response,JobCandidateVO[].class);
        ArrayList<JobCandidateVO> items = new ArrayList<>(Arrays.asList(result));

        Calendar curTime = Calendar.getInstance();
        List<EventDay> events = new ArrayList<>();
        for (JobCandidateVO item : items) {

            Calendar calendar = Calendar.getInstance();
            Date date = Date.valueOf(item.getTargetDate());
            calendar.setTime(date);

            int recruit = item.getRecruit();
            int total = item.getTotal();

            int color = R.color.offer;
            if (recruit == total) {
                color = R.color.blue1;
            }

            String eventText = recruit + "/" + total;
            Drawable drawable = CalendarUtils.getDrawableText(this,eventText,Typeface.DEFAULT,color,15);
            events.add(new EventDay(calendar,drawable));
        }
        calendarView.setEvents(events);
        Date startDate = Date.valueOf(projectStartDate);
        Date endDate = Date.valueOf(projectEndDate);

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);

        if (curTime.getTime().getTime() < startCalendar.getTime().getTime()) {
            startCalendar = curTime;
        }

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);

        calendarView.setMinimumDate(startCalendar);
        calendarView.setMaximumDate(endCalendar);

    }

    // 기본적인 UI 세팅
    private void initUI() {
        container = findViewById(R.id.container);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        selectDayLayout = findViewById(R.id.selectDayLayout);
        showSelectDayButton = findViewById(R.id.showSelectDayButton);
        showSelectDayButton.setOnClickListener(v -> showSelectDay(selectDayLayoutVisible));
        calendarView = findViewById(R.id.calendarView);

        recyclerViewContainerLayout = findViewById(R.id.recyclerViewContainerLayout);
        showRecyclerViewContainerButton = findViewById(R.id.showRecyclerViewContainerButton);
        showRecyclerViewContainerButton.setOnClickListener(v -> showRecyclerViewContainer(recyclerViewContainerVisible));
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        calendarView.setOnDayClickListener(eventDay -> requestTargetDateRecruitInfo(projectNumber,eventDay.getCalendar()));
        }

    private void requestTargetDateRecruitInfo(String projectNumber, Calendar calendar) {
        String url = getResources().getString(R.string.url) + "requestTargetDateRecruitInfo.do";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                this::processTargetDateRecruitInfo,
                error -> {

                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("projectNumber",projectNumber);

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH)+1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String targetDate = year + "-" + month + "-" + day;

                params.put("targetDate",targetDate);
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    private void processTargetDateRecruitInfo(String response) {
        HumanResRsponseModel model  = Base.gson.fromJson(response,HumanResRsponseModel.class);
        jobNumberList = model.getJobNumberList();
        recruitMap = model.getRecruitMap();

        int size = 0;
        for (JobCandidateVO item : jobNumberList) {
            List<ManageHumanResourceModel> list = recruitMap.get(item.getJobNumber());
            size += list.size();
        }
        recyclerView.setMinimumHeight(size * 104);
        HumanResourceJobRecyclerViewAdapter adapter = new HumanResourceJobRecyclerViewAdapter(this);
        adapter.setItems((ArrayList<JobCandidateVO>) jobNumberList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void showSelectDay(int selectDayLayoutVisible) {
        switch (selectDayLayoutVisible) {
            case 0 :
                this.selectDayLayoutVisible = 1;
                selectDayLayout.setVisibility(View.VISIBLE);
                showSelectDayButton.setText("-");
                break;
            case 1:
                this.selectDayLayoutVisible = 0;
                selectDayLayout.setVisibility(View.GONE);
                showSelectDayButton.setText("+");
                break;
        }
    }

    private void showRecyclerViewContainer(int recyclerViewContainerVisible) {
        switch (recyclerViewContainerVisible) {
            case 0 :
                this.recyclerViewContainerVisible = 1;
                recyclerViewContainerLayout.setVisibility(View.VISIBLE);
                showRecyclerViewContainerButton.setText("-");
                break;
            case 1:
                this.recyclerViewContainerVisible = 0;
                recyclerViewContainerLayout.setVisibility(View.GONE);
                showRecyclerViewContainerButton.setText("+");
                break;
        }
    }

    //end 기본적인 UI 세팅
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
