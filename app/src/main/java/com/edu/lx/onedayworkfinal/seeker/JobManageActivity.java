package com.edu.lx.onedayworkfinal.seeker;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;

import java.util.Date;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.offer.recycler_view.WorkerRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.seeker.recycler_view.SeekerManageAcceptJobAdapter;
import com.edu.lx.onedayworkfinal.seeker.recycler_view.SeekerManageFinishJobAdapter;
import com.edu.lx.onedayworkfinal.seeker.recycler_view.SeekerManageProjectRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.seeker.recycler_view.TargetDateRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.JobCandidateVO;
import com.edu.lx.onedayworkfinal.vo.JobVO;
import com.edu.lx.onedayworkfinal.vo.ManageVO;
import com.edu.lx.onedayworkfinal.vo.OfferWorkVO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class JobManageActivity extends Activity {


    String seekerId;
    ManageVO manageVO;
    //intent 로 받아오는 정보
    public int projectNumber;

    //서버에서 받아오는 응답 정보들


    //수락한 일감, 종료된 일감 목록
    ArrayList<ManageVO> items;
    ArrayList<ManageVO> finishJobList;

    SeekerManageAcceptJobAdapter adapter;
    SeekerManageFinishJobAdapter FinishAdapter;

    //Toolbar
    Toolbar toolbar;

    //수령총액
    TextView expectaionReceipt;

    //수락 일감
    int acceptJobVisible = 1;
    LinearLayout acceptJobView;
    Button showAcceptButton;
    RecyclerView acceptJobRecyclerView;
    // 근무 결과 목록 확인
    int finishJobVisible = 0;
    LinearLayout finishJobView;
    Button showFinishButton;
    RecyclerView finishJobViewRecyclerView;
    //오늘의일감 버튼
    int todayJobVisible = 0;
    Button todayJobButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_job_manage);
        seekerId = Base.sessionManager.getUserDetails().get("id");
        initUI();

        requestAcceptJobList(seekerId);
        requestFinishJobList(seekerId);
    }

    //UI 세팅
    private void initUI() {
        Intent intent = getIntent();
        projectNumber = intent.getIntExtra("projectNumber", 0);
        String projectName = intent.getStringExtra("projectName");
        //툴바 설정
        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        Objects.requireNonNull(getSupportActionBar()).setTitle(projectName);

        //수령할 총액
        expectaionReceipt = findViewById(R.id.expectaionReceipt);
        //수락한 일감
        acceptJobView = findViewById(R.id.acceptJobView);
        showAcceptButton = findViewById(R.id.showAcceptButton);
        showAcceptButton.setOnClickListener(v -> showAcceptJob(acceptJobVisible));
        acceptJobRecyclerView = findViewById(R.id.acceptJobRecyclerView);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        acceptJobRecyclerView.setLayoutManager(layoutManager1);
        //근무결과목록 확인
        finishJobView = findViewById(R.id.finishJobView);
        showFinishButton = findViewById(R.id.showFinishButton);
        showFinishButton.setOnClickListener(v -> showFinishJob(finishJobVisible));
        finishJobViewRecyclerView = findViewById(R.id.finishJobViewRecyclerView);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        finishJobViewRecyclerView.setLayoutManager(layoutManager2);


    }

    //수락한 일감 조회
    public void requestAcceptJobList(String seekerId){
        String url = getResources().getString(R.string.url) + "requestAcceptJobList.do";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                this::processAcceptJobList, error -> {

        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("seekerId",seekerId);
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    /**
     * processAcceptJobList
     * @param response 서버로부터  id를 참조해 수락된 일감을 나타낸 결과
     * */
    private void processAcceptJobList(String response) {
        ManageVO[] vo = Base.gson.fromJson(response,ManageVO[].class);
        items = new ArrayList<>(Arrays.asList(vo));
        adapter = new SeekerManageAcceptJobAdapter(this);
        adapter.setItems(items);
        acceptJobRecyclerView.setAdapter(adapter);

    }

    //수락한 일감 보여주기, 숨기기
    public void showAcceptJob(int acceptJobVisible) {
        switch (acceptJobVisible) {
            case 0:
                if (items.size() == 0) {
                    return;
                }
                this.acceptJobVisible = 1;
                showAcceptButton.setText("-");
                acceptJobView.setVisibility(View.VISIBLE);
                break;
            case 1:
                this.acceptJobVisible = 0;
                acceptJobView.setVisibility(View.GONE);
                showAcceptButton.setText("+");
                break;
        }
    }

    public void requestFinishJobList(String seekerId) {
        String url = getResources().getString(R.string.url) + "requestFinishJobList.do";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                    this::processFinishJobJobList, error -> {


        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("seekerId", seekerId);
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);

    }

    public void processFinishJobJobList(String response) {
        ManageVO[] vo = Base.gson.fromJson(response,ManageVO[].class);
        finishJobList = new ArrayList<>(Arrays.asList(vo));
        FinishAdapter = new SeekerManageFinishJobAdapter(this);
        FinishAdapter.setItems(finishJobList);
        finishJobViewRecyclerView.setAdapter(FinishAdapter);

    }
    public void showFinishJob(int finishJobVisible) {
        switch (finishJobVisible) {
            case 0:
                this.finishJobVisible = 1;
                showFinishButton.setText("-");
                finishJobView.setVisibility(View.VISIBLE);
                break;
            case 1:
                this.finishJobVisible = 0;
                finishJobView.setVisibility(View.GONE);
                showFinishButton.setText("+");
                break;
        }
    }

//    public String getDateString() {
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
//        String str_date = df.format(new Date());
//
//        return str_date;
//    }
//
//
//    public void showTodayjobButton(int todayJobVisible) {
//
//        if(manageVO.getJobStartDate().equals(getDateString())) {
//            switch(todayJobVisible) {
//                case 0:
//
//                    this.todayJobVisible = 1;
//
//                    todayJobButton.setVisibility(View.VISIBLE);
//                    break;
//            }
//
//        }
//
//    }

}
