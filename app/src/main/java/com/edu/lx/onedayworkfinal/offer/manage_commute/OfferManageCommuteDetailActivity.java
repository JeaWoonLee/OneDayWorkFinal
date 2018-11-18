package com.edu.lx.onedayworkfinal.offer.manage_commute;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.offer.recycler_view.CommuteInfoForJobRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.offer.recycler_view.WorkerRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.CommuteInfoVO;
import com.edu.lx.onedayworkfinal.vo.JobCandidateVO;
import com.edu.lx.onedayworkfinal.vo.JobVO;
import com.edu.lx.onedayworkfinal.vo.OfferWorkVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OfferManageCommuteDetailActivity extends AppCompatActivity {

    //intent 로 받아오는 정보
    public int projectNumber;

    //서버에서 받아오는 응답 정보들
    OfferWorkVO offerWorkVO;
    List<JobVO> jobList;
    Map<Integer,List<JobCandidateVO>> candidateMap;

    //각 근무 상태별 근로자
    ArrayList<JobCandidateVO> notCommuteList;
    ArrayList<JobCandidateVO> commuteList;
    ArrayList<JobCandidateVO> workingList;
    ArrayList<JobCandidateVO> offWorkList;
    ArrayList<JobCandidateVO> runList;
    ArrayList<JobCandidateVO> absentList;

    WorkerRecyclerViewAdapter offWorkAdapter;
    WorkerRecyclerViewAdapter workingAdapter;
    WorkerRecyclerViewAdapter commuteAdapter;
    WorkerRecyclerViewAdapter notCommuteAdapter;
    WorkerRecyclerViewAdapter absentAdapter;
    WorkerRecyclerViewAdapter runAdapter;

    //Toolbar
    Toolbar toolbar;
    //모집/총원
    TextView recruitmentRate;
    //츨석/모집
    TextView attendanceRate;
    //근무시간
    TextView workTime;
    //직종별 출석 현황
    RecyclerView commuteInfoForJobRecyclerView;
    //퇴근
    int offWorkVisible = 0;
    LinearLayout offWorkLayout;
    Button showOffWorkButton;
    RecyclerView offWorkRecyclerView;
    //근무중
    int workingVisible = 0;
    LinearLayout workingLayout;
    Button showWorkingButton;
    RecyclerView workingRecyclerView;
    Button allOffWorkButton;
    //출근 인원
    int commuteVisible = 0;
    LinearLayout commuteLayout;
    Button showCommuteButton;
    RecyclerView commuteRecyclerView;
    Button allWorkingButton;
    //미출근
    int notCommuteVisible = 0;
    LinearLayout notCommuteLayout;
    Button showNotCommuteButton;
    RecyclerView notCommuteRecyclerView;
    Button allAbsentButton;
    //결근
    int absentVisible = 0;
    LinearLayout absentLayout;
    Button absentButton;
    RecyclerView absentRecyclerView;
    //무단 이탈
    int runVisible = 0;
    LinearLayout runLayout;
    Button runButton;
    RecyclerView runRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_manage_commute_detail);

        initUI();
        requestProjectCommuteInfo(projectNumber);
    }

    /**
     * findViewById 부터 기초적인 UI 세팅
     */
    private void initUI() {
        Intent intent =getIntent();
        projectNumber = intent.getIntExtra("projectNumber",0);
        String projectName = intent.getStringExtra("projectName");
        //툴바 설정
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(projectName);

        //모집 총원
        recruitmentRate = findViewById(R.id.recruitmentRate);
        //출석 / 모집
        attendanceRate = findViewById(R.id.attendanceRate);
        //근무시간
        workTime = findViewById(R.id.workTime);
        //직종별 출석 현황
        commuteInfoForJobRecyclerView = findViewById(R.id.commuteInfoForJobRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        commuteInfoForJobRecyclerView.setLayoutManager(layoutManager);
        //퇴근
        offWorkLayout = findViewById(R.id.offWorkLayout);
        showOffWorkButton = findViewById(R.id.showOffWorkButton);
        showOffWorkButton.setOnClickListener(v -> showOffWork(offWorkVisible));
        offWorkRecyclerView = findViewById(R.id.offWorkRecyclerView);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        offWorkRecyclerView.setLayoutManager(layoutManager1);
        //근무중
        workingLayout = findViewById(R.id.workingLayout);
        showWorkingButton = findViewById(R.id.showWorkingButton);
        showWorkingButton.setOnClickListener(v -> showWorking(workingVisible));
        workingRecyclerView = findViewById(R.id.workingRecyclerView);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        workingRecyclerView.setLayoutManager(layoutManager2);
        allOffWorkButton = findViewById(R.id.allOffWorkButton);
        allOffWorkButton.setOnClickListener(v -> allOffWork(projectNumber));
        //출근 인원
        commuteLayout = findViewById(R.id.commuteLayout);
        showCommuteButton = findViewById(R.id.showCommuteButton);
        showCommuteButton.setOnClickListener(v -> showCommute(commuteVisible));
        commuteRecyclerView = findViewById(R.id.commuteRecyclerView);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        commuteRecyclerView.setLayoutManager(layoutManager3);
        allWorkingButton = findViewById(R.id.allWorkingButton);
        allWorkingButton.setOnClickListener(v -> allWorking(projectNumber));
        //미출근
        notCommuteLayout = findViewById(R.id.notCommuteLayout);
        showNotCommuteButton = findViewById(R.id.showNotCommuteButton);
        showNotCommuteButton.setOnClickListener(v -> showNotCommute(notCommuteVisible));
        notCommuteRecyclerView = findViewById(R.id.notCommuteRecyclerView);
        LinearLayoutManager layoutManager4 = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        notCommuteRecyclerView.setLayoutManager(layoutManager4);
        allAbsentButton = findViewById(R.id.allAbsentButton);
        allAbsentButton.setOnClickListener(v -> allAbsent(projectNumber));
        //결근
        absentLayout = findViewById(R.id.absentLayout);
        absentButton = findViewById(R.id.absentButton);
        absentButton.setOnClickListener(v -> showAbsent(absentVisible));
        absentRecyclerView = findViewById(R.id.absentRecyclerView);
        LinearLayoutManager layoutManager5 = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        absentRecyclerView.setLayoutManager(layoutManager5);
        //무단 이탈
        runLayout = findViewById(R.id.runLayout);
        runButton = findViewById(R.id.runButton);
        runButton.setOnClickListener(v -> showRun(runVisible));
        runRecyclerView = findViewById(R.id.runRecyclerView);
        LinearLayoutManager layoutManager6 = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        runRecyclerView.setLayoutManager(layoutManager6);

    }

    // 일괄처리 요청 버튼 처리
    /**
     * allAbsent
     * @param projectNumber 오늘 해당 일감의 미출근(1) 인 상태의 인원들을 전부 결근(6) 으로 만든다
     */
    private void allAbsent(int projectNumber) {
        String url = getResources().getString(R.string.url) + "allAbsent.do";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                this::processAllAbsentResponse,
                error -> {

                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("projectNumber",String.valueOf(projectNumber));
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    /**
     * processAllAbsentResponse
     * @param response 전체 결근처리한 결과
     *                 0보다 크다면 성공적으로 수행되었다는 메세지를 띄우고 아니라면 실패 메세지를 띄운다
     */
    private void processAllAbsentResponse(String response) {
        int allAbsentResult = Integer.parseInt(response);
        if (allAbsentResult > 0) {
            Snackbar.make(getWindow().getDecorView().getRootView(),"성공적으로 일괄 결근처리 되었습니다.",Snackbar.LENGTH_LONG).show();
            requestProjectCommuteInfo(projectNumber);
        } else {
            Toast.makeText(getApplicationContext(),"일괄 결근처리에 실패했습니다.",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * allWorking
     * @param projectNumber 오늘 해당 일감의 출근(2) 인 상태의 인원들을 전부 근무중(3) 으로 만든다
     */
    private void allWorking(int projectNumber) {
        String url = getResources().getString(R.string.url) + "allWorking.do";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                this::processAllWorkingResponse,
                error -> {

                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("projectNumber",String.valueOf(projectNumber));
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    /**
     * processAllWorkingResponse
     * @param response 전체 출근확인 처리한 결과
     *                 0보다 크면 성공적으로 수행되었다는 메세지를 띄우고 아니라면 실패 메세지를 띄운다
     */
    private void processAllWorkingResponse(String response) {
        int allWorkingResult = Integer.parseInt(response);
        if (allWorkingResult > 0) {
            Snackbar.make(getWindow().getDecorView().getRootView(),"성공적으로 일괄 출근처리 되었습니다.",Snackbar.LENGTH_LONG).show();
            requestProjectCommuteInfo(projectNumber);
        } else {
            Toast.makeText(getApplicationContext(),"일괄 출근처리에 실패했습니다.",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * allOffWork
     * @param projectNumber 오늘 해당 일감의 근무중(3) 인 상태의 인원들을 전부 퇴근(4) 으로 만든다
     */
    private void allOffWork(int projectNumber) {
        String url = getResources().getString(R.string.url) + "allOffWork.do";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                this::processAllOffWorkResponse,
                error -> {

                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("projectNumber",String.valueOf(projectNumber));
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    /**
     * processAllOffWorkResponse
     * @param response 전체 퇴근처리한 결과
     *                 0보다 크면 성공적으로 수행되었다는 메세지를 띄우고 아니라면 실패 메세지를 띄운다
     */
    private void processAllOffWorkResponse(String response) {
        int allOffWorkResult = Integer.parseInt(response);
        if (allOffWorkResult > 0) {
            Snackbar.make(getWindow().getDecorView().getRootView(),"성공적으로 퇴근처리 되었습니다.",Snackbar.LENGTH_LONG).show();
            requestProjectCommuteInfo(projectNumber);
        } else {
            Toast.makeText(getApplicationContext(),"일괄 퇴근처리에 실패했습니다.",Toast.LENGTH_LONG).show();
        }
    }
    //end 일괄처리 요청 버튼 처리

    /**
     * requestProjectCommuteInfo
     * @param projectNumber 해당 키값으로 해당 날짜의 출근인원을 불러온다
     *                      다른 근무 상태 처리 함수를 실행한 후에 Refresh 해주기 위해 이 함수를 다시 실행한다
     */
    public void requestProjectCommuteInfo(int projectNumber) {
        String url = getResources().getString(R.string.url) + "requestProjectCommuteInfo.do";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                this::processCommuteInfo,
                error -> {

                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("projectNumber",String.valueOf(projectNumber));
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    private void processCommuteInfo(String response) {
        CommuteInfoVO item = Base.gson.fromJson(response,CommuteInfoVO.class);
        //기본 정보 출력
        offerWorkVO = item.getOfferWorkVO();
        inflateOfferWorkInfo(offerWorkVO);

        //직군 정보
        jobList = item.getJobList();
        //노동자 정보
        candidateMap = item.getCandidateMap();

        notCommuteList = new ArrayList<>();
        commuteList = new ArrayList<>();
        workingList = new ArrayList<>();
        offWorkList = new ArrayList<>();
        runList = new ArrayList<>();
        absentList = new ArrayList<>();

        //하나의 job 마다 jobName, jobLimitCount, attendance, recruit 를 구하자
        for (JobVO jobVO : jobList){
            List<JobCandidateVO> candidateList = candidateMap.get(jobVO.getJobNumber());
            //구인 된 인원을 jobPay 에 임시로 넣어둠
            assert candidateList != null;
            jobVO.setJobPay(candidateList.size());
            int attendanceCount = 0;
            for (JobCandidateVO jobCandidateVO : candidateList) {
                //1 수락(미출근) / 2 출근 / 3 근무중 / 4 퇴근 / 5 무단이탈 / 6 결근
                //수락과 결근을 제외한 인원들의 숫자를 센다
                int candidateStatus = jobCandidateVO.getCandidateStatus();

                if (candidateStatus>=1 && candidateStatus <= 6) {
                    attendanceCount ++;
                }
                //각 근무 상태의 인원들을 분류/ 정렬한다
                if (candidateStatus == 1) notCommuteList.add(jobCandidateVO);
                else if (candidateStatus == 2) commuteList.add(jobCandidateVO);
                else if (candidateStatus == 3) workingList.add(jobCandidateVO);
                else if (candidateStatus == 4) offWorkList.add(jobCandidateVO);
                else if (candidateStatus == 5) runList.add(jobCandidateVO);
                else if (candidateStatus == 6) absentList.add(jobCandidateVO);
            }
            //출근 인원을 jobNumber 에 임시로 담는다
            jobVO.setJobNumber(attendanceCount);
        }
        //근무중 / 출근 / 미출근에 해당하는 인원이 0 이라면 버튼을 숨김
        if (notCommuteList.size() == 0) allAbsentButton.setVisibility(View.GONE);
        else allAbsentButton.setVisibility(View.VISIBLE);
        if (commuteList.size() == 0) allWorkingButton.setVisibility(View.GONE);
        else allWorkingButton.setVisibility(View.VISIBLE);
        if (workingList.size() == 0) allOffWorkButton.setVisibility(View.GONE);
        else allOffWorkButton.setVisibility(View.VISIBLE);

        //출근정보 리사이클러 뷰 설정
        CommuteInfoForJobRecyclerViewAdapter adapter1 = new CommuteInfoForJobRecyclerViewAdapter(this);
        adapter1.setItems((ArrayList<JobVO>) jobList);
        commuteInfoForJobRecyclerView.setAdapter(adapter1);

        //퇴근 리사이클러 뷰 설정
        offWorkLayout.setMinimumHeight(offWorkList.size()*50);
        offWorkAdapter = new WorkerRecyclerViewAdapter(this);
        offWorkAdapter.setItems(offWorkList);
        offWorkRecyclerView.setMinimumHeight(offWorkList.size() * 104);
        offWorkRecyclerView.setAdapter(offWorkAdapter);

        //근무중 리사이클러 뷰 설정
        workingAdapter = new WorkerRecyclerViewAdapter(this);
        workingAdapter.setItems(workingList);
        workingRecyclerView.setMinimumHeight(workingList.size() * 104);
        workingRecyclerView.setAdapter(workingAdapter);

        //출근중 리사이클러 뷰 설정
        commuteAdapter = new WorkerRecyclerViewAdapter(this);
        commuteAdapter.setItems(commuteList);
        commuteRecyclerView.setMinimumHeight(commuteList.size() * 104);
        commuteRecyclerView.setAdapter(commuteAdapter);

        //미출근 리사이클러 뷰 설정
        notCommuteAdapter = new WorkerRecyclerViewAdapter(this);
        notCommuteAdapter.setItems(notCommuteList);
        notCommuteRecyclerView.setMinimumHeight(notCommuteList.size() * 104);
        notCommuteRecyclerView.setAdapter(notCommuteAdapter);

        //결근 리사이클러 뷰 설정
        absentAdapter = new WorkerRecyclerViewAdapter(this);
        absentAdapter.setItems(absentList);
        absentRecyclerView.setMinimumHeight(absentList.size() * 104);
        absentRecyclerView.setAdapter(absentAdapter);

        //무단이탈 리사이클러 뷰 설정
        runAdapter = new WorkerRecyclerViewAdapter(this);
        runAdapter.setItems(runList);
        runRecyclerView.setMinimumHeight(runList.size() * 104);
        runRecyclerView.setAdapter(runAdapter);

    }

    /**
     * inflateOfferWorkInfo
     * @param offerWorkVO 출퇴근관리의 출석현황 및 근무 시간이 담겨있는 VO
     *                    해당 정보를 기본 정보 구역에서 출력한다
     */
    private void inflateOfferWorkInfo(OfferWorkVO offerWorkVO) {
        String recruitmentRateStr = "( "+offerWorkVO.getRecruit() + " / " + offerWorkVO.getTotal()+" )";
        recruitmentRate.setText(recruitmentRateStr);

        String attendanceRateStr = "( "+offerWorkVO.getCommute() + " / " +offerWorkVO.getRecruit() +" )";
        attendanceRate.setText(attendanceRateStr);

        String workTimeStr= offerWorkVO.getWorkStartTime() + " - " + offerWorkVO.getWorkEndTime();
        workTime.setText(workTimeStr);
    }

    //각 레이아웃 보이기 숨기기
    private void showRun(int runVisible) {
        switch (runVisible){
            case 0:
                this.runVisible = 1;
                runButton.setText("-");
                if (runList.size() >0) runLayout.setVisibility(View.VISIBLE);
                break;
            case 1:
                this.runVisible = 0;
                runLayout.setVisibility(View.GONE);
                runButton.setText("+");
                break;
        }
    }

    private void showAbsent(int absentVisible) {
        switch (absentVisible){
            case 0:
                this.absentVisible = 1;
                absentButton.setText("-");
                if (absentList.size()>0) absentLayout.setVisibility(View.VISIBLE);
                break;
            case 1:
                this.absentVisible = 0;
                absentLayout.setVisibility(View.GONE);
                absentButton.setText("+");
                break;
        }
    }

    private void showNotCommute(int notCommuteVisible) {
        switch (notCommuteVisible){
            case 0:
                this.notCommuteVisible = 1;
                showNotCommuteButton.setText("-");
                if (notCommuteList.size()>0) notCommuteLayout.setVisibility(View.VISIBLE);
                break;
            case 1:
                this.notCommuteVisible = 0;
                notCommuteLayout.setVisibility(View.GONE);
                showNotCommuteButton.setText("+");
                break;
        }
    }

    private void showCommute(int commuteVisible) {
        switch (commuteVisible){
            case 0:
                this.commuteVisible = 1;
                showCommuteButton.setText("-");
                if (commuteList.size()>0) commuteLayout.setVisibility(View.VISIBLE);
                break;
            case 1:
                this.commuteVisible = 0;
                commuteLayout.setVisibility(View.GONE);
                showCommuteButton.setText("+");
                break;
        }
    }

    private void showWorking(int workingVisible) {
        switch (workingVisible){
            case 0:
                this.workingVisible = 1;
                showWorkingButton.setText("-");
                if (workingList.size()>0) workingLayout.setVisibility(View.VISIBLE);
                break;
            case 1:
                this.workingVisible = 0;
                workingLayout.setVisibility(View.GONE);
                showWorkingButton.setText("+");
                break;
        }
    }

    private void showOffWork(int offWorkVisible) {
        switch (offWorkVisible){
            case 0:
                this.offWorkVisible = 1;
                showOffWorkButton.setText("-");
                if (offWorkList.size() > 0) offWorkLayout.setVisibility(View.VISIBLE);
                break;
            case 1:
                this.offWorkVisible = 0;
                offWorkLayout.setVisibility(View.GONE);
                showOffWorkButton.setText("+");
                break;
        }
    }
    //end 각 레이아웃 보이기 숨기기

}
