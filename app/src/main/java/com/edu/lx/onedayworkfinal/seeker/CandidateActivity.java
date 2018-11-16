package com.edu.lx.onedayworkfinal.seeker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.JobCandidateVO;
import com.edu.lx.onedayworkfinal.vo.JobVO;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CandidateActivity extends AppCompatActivity{

    //이전 액티비티로부터 넘어온 값
    int jobNumber;

    //일감의 상세정보
    JobVO item;

    //툴바
    Toolbar toolbar;

    //일감 상세정보 TextView
    TextView jobName;
    TextView jobPay;
    TextView jobRequirement;

    //달력
    CalendarView calendar;
    TextView selectDay;

    ArrayList<Calendar> disableDays;

    //정원
    TextView currentCount;
    TextView limitCount;

    //신청 버튼
    Button candidateButton;

    //신청 가능여부(지원 가능한 날짜를 선택해야 true 가 됨)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //뒤로가기 버튼 설정
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //인텐트로부터 검색할 jobNumber 를 가져옴
        Intent intent = getIntent();
        jobNumber = intent.getIntExtra("jobNumber",0);

        jobName = findViewById(R.id.jobName);
        jobPay = findViewById(R.id.jobPay);
        jobRequirement = findViewById(R.id.jobRequirement);
        currentCount = findViewById(R.id.currentCount);
        limitCount = findViewById(R.id.limitCount);

        selectDay = findViewById(R.id.selectDay);

        calendar = findViewById(R.id.calendar);
        calendar.setHeaderColor(R.color.seeker);
        calendar.setOnDayClickListener(this::setSelectDay);

        candidateButton = findViewById(R.id.candidateButton);
        candidateButton.setOnClickListener(v -> candidate());

        requestJobDetail(jobNumber);
    }

    /**
     * requestJobDetaiㅣ
     * @param jobNumber 인텐트로 넘어온 jobNumber 를 이용하여 상세정보를 가져옴
     */
    private void requestJobDetail(final int jobNumber) {
        String url = getResources().getString(R.string.url) + "requestJobDetail.do";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                this::processRequestJobDetail,
                error -> {

                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("jobNumber",String.valueOf(jobNumber));
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    /**
     * processRequestJobDetail
     * @param response 서버로부터 받은 직업의 상세정보를 Gson 으로 파싱하고 View 에 뿌려줌
     * CalendarView 에 jobStartDate 와 jobEndDate 를 이용하여 시작 및 끝 날짜 설정
     *  해당 설정이 끝나면, 정원이 가득 차서 disable 해야 할 날짜 목록을 가져온다
     */
    private void processRequestJobDetail(String response) {
        item = Base.gson.fromJson(response, JobVO.class);

        //setText 하기
        jobName.setText(item.getJobName());
        jobPay.setText(Base.decimalFormat(item.getJobPay()));
        jobRequirement.setText(item.getJobRequirement());
        currentCount.setText("0");
        limitCount.setText(String.valueOf(item.getJobLimitCount()));

        Calendar now = Calendar.getInstance();
        java.util.Date date = new java.util.Date();
        now.setTime(date);
        String cutTime = now.get(Calendar.YEAR)+ "-" + (now.get(Calendar.MONTH)+1) + "-" + (now.get(Calendar.DAY_OF_MONTH));
        Date date1 = Date.valueOf(cutTime);
        now.setTime(date1);
        long curTime = now.getTime().getTime();

        Calendar minDate = Calendar.getInstance();
        minDate.setTime(Date.valueOf(item.getJobStartDate()));
        Calendar maxDate = Calendar.getInstance();
        maxDate.setTime(Date.valueOf(item.getJobEndDate()));

        long minTime = minDate.getTime().getTime();

        if ((curTime-minTime)>0) {
            calendar.setMinimumDate(now);
            try {
                calendar.setDate(Date.valueOf(cutTime));
            } catch (OutOfDateRangeException e) {
                e.printStackTrace();
            }
        }else {
            calendar.setMinimumDate(minDate);
        }
        calendar.setMaximumDate(maxDate);


        requestDisableDays(jobNumber);
    }

    /**
     * requestDisableDays
     * @param jobNumber 정원이 가득 차서 disable 해야 할 날짜를 서버로부터 요청하는 메서드
     */
    private void requestDisableDays(final int jobNumber) {
        String url = getResources().getString(R.string.url) + "requestDisableDaysByJobNumber.do";
        StringRequest request = new StringRequest(Request.Method.POST,
                url,
                this::processDisableDaysResponse,
                error -> {

                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("jobNumber",String.valueOf(jobNumber));
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    /**
     * processDisableDaysResponse
     *  @param response 서버로 부터 받아온 날짜들을 이용하여 CalendarView 를 disable 한다.
     *  초기 선택 날짜를 선택한다. disable 된 날짜를 선택한다면 다음 날짜를 기본적으로 선택하도록 한다.
     */
    private void processDisableDaysResponse(String response) {
        JobCandidateVO[] targetDateArrays = Base.gson.fromJson(response,JobCandidateVO[].class);

        if (targetDateArrays != null){
            Calendar[] days = new Calendar[targetDateArrays.length];
            for (int i = 0 ; i < targetDateArrays.length ; i++){
                days[i] = Calendar.getInstance();
                days[i].setTime(Date.valueOf(targetDateArrays[i].getTargetDate()));
            }
            disableDays = new ArrayList<>(Arrays.asList(days));
            calendar.setDisabledDays(disableDays);
            Calendar setDate = Calendar.getInstance();
            setDate.setTime(Date.valueOf(item.getJobStartDate()));

            while (disableDays.contains(setDate)) {
                setDate.set(setDate.get(Calendar.YEAR),setDate.get(Calendar.MONTH),setDate.get(Calendar.DAY_OF_MONTH)+1);
            }

            try {
                calendar.setDate(setDate);
            } catch (OutOfDateRangeException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * setSelectDay
     * @param eventDay CalendarView 에서 날짜를 선택하면 실행되는 콜백함수
     *                  disable 된 날짜를 선택하면 Toast 메세지와 함께 return 시킨다
     *                  정상적인 날짜를 선택했다면 선택 날짜를 표시하고, 해당 날짜에 몇 명의 인원이 수락되어 있는지를 요청한다
     */
    private void setSelectDay(EventDay eventDay) {
        Calendar clickedDayCalendar = eventDay.getCalendar();
        if (disableDays.contains(clickedDayCalendar)){
            Toast.makeText(getApplicationContext(),"해당 날짜는 이미 신청이 마감되었습니다",Toast.LENGTH_SHORT).show();
            return;
        }
        String date = clickedDayCalendar.get(Calendar.YEAR)+"-"+(clickedDayCalendar.get(Calendar.MONTH)+1)+"-"+clickedDayCalendar.get(Calendar.DAY_OF_MONTH);
        try {
            calendar.setDate(clickedDayCalendar);
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }
        selectDay.setText(date);
        date = clickedDayCalendar.get(Calendar.YEAR)+"-"+(clickedDayCalendar.get(Calendar.MONTH)+2)+"-"+clickedDayCalendar.get(Calendar.DAY_OF_MONTH);
        requestTargetDateCount(date,jobNumber);
    }

    /**
     * requestTargetDateCount
     * @param date 선택한 날짜(targetDate) 와
     * @param jobNumber jobNumber 를 이용하여 선택한 날짜의 수락된 인원(candidate_status == 1) 수를 가져온다.
     */
    private void requestTargetDateCount(final String date, final int jobNumber) {
        String url = getResources().getString(R.string.url)+"requestTargetDateCount.do";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> currentCount.setText(response),
                error -> {

                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("targetDate",date);
                params.put("jobNumber",String.valueOf(jobNumber));
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    /**
     * candidate()
     * 신청하기 버튼을 눌렀을 때 실행되는 함수
     * targetDate, seekerId, jobNumber 로 job_candidate 에 insert 한다
     */
    private void candidate() {
        if (TextUtils.equals(selectDay.getText().toString(),"날짜를 선택하세요")){
            Toast.makeText(getApplicationContext(),"먼저 신청할 날짜를 선택해 주세요!",Toast.LENGTH_LONG).show();
            return;
        }
        final String targetDate = selectDay.getText().toString();
        final String seekerId = Base.sessionManager.getUserDetails().get("id");

        String url = getResources().getString(R.string.url) + "candidateJob.do";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                this::processCandidateRequest,
                error -> {

                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("targetDate",targetDate);
                params.put("seekerId",Objects.requireNonNull(seekerId));
                params.put("jobNumber",String.valueOf(jobNumber));
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    /**
     * processCandidateRequest
     * @param response 서버로부터 받아온 insert 응답 코드
     * 0 : insert 실패
     * 1 : insert 성공
     * 2 : 같은 targetDate 에 candidate_status == 1  인 row 가 있음
     * 3 : 같은 targetDate, job_number 에 candidate_status == 1 으로 중복으로 신청되어 있음
     * 4 : 같은 targetDate, job_number 에 candodate_status == 1 으로 이미 수락되어 있음
     */
    private void processCandidateRequest(String response) {
        int candidateResult = Integer.parseInt(response);

        //인서트 결과가 0
        if (candidateResult == 0) {
            Toast.makeText(getApplicationContext(),"신청이 실패했습니다! + errorCode : 0",Toast.LENGTH_LONG).show();
        //인서트 성공
        }else if (candidateResult == 1) {
            Toast.makeText(getApplicationContext(),"성공적으로 신청되었습니다!",Toast.LENGTH_LONG).show();
        //같은 날짜에 수락된 다른 일감이 있는 경우
        }else if (candidateResult == 2) {
            Toast.makeText(getApplicationContext(),"같은 날짜에 수락된 다른 일감이 있습니다!",Toast.LENGTH_LONG).show();
        //중복으로 신청된 경우
        }else if (candidateResult == 3) {
            Toast.makeText(getApplicationContext(),"이미 같은 날짜에 같은 일감으로 신청되어 있습니다!",Toast.LENGTH_LONG).show();
        //같은 날짜에 같은 일감으로 신청 수락된 경우
        }else if (candidateResult == 4) {
            Toast.makeText(getApplicationContext(),"해당 날짜의 해당 일감이 이미 수락되어 있습니다!",Toast.LENGTH_LONG).show();
        }

    }

    /**
     * onOptionsItemSelected
     * @param item 상단 툴바 뒤로가기 버튼을 눌렀을 때
     *             이전 액티비티로 보낸다
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }


}