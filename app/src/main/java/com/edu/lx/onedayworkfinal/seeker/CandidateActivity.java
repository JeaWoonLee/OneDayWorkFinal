package com.edu.lx.onedayworkfinal.seeker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.ProjectJobListVO;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CandidateActivity extends AppCompatActivity {

    //이전 액티비티로부터 넘어온 값
    int jobNumber;

    //일감의 상세정보
    ProjectJobListVO item;

    //툴바
    Toolbar toolbar;

    //일감 상세정보 TextView
    TextView jobName;
    TextView jobPay;
    TextView jobRequirement;

    //달력
    CalendarView calendar;
    TextView selectDay;

    //정원
    TextView currentCount;
    TextView limitCount;

    //신청 버튼
    Button candidateButton;

    //신청 가능여부(지원 가능한 날짜를 선택해야 true 가 됨)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //뒤로가기 버튼 설정
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //인텐트로부터 검색할 jobNumber 를 가져옴
        Intent intent = getIntent();
        jobNumber = intent.getIntExtra("jobNumber",0);
        requestJobDetail(jobNumber);

        jobName = findViewById(R.id.jobName);
        jobPay = findViewById(R.id.jobPay);
        jobRequirement = findViewById(R.id.jobRequirement);
        currentCount = findViewById(R.id.currentCount);
        limitCount = findViewById(R.id.limitCount);

        selectDay = findViewById(R.id.selectDay);

        calendar = findViewById(R.id.calendar);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year1, int month1, int dayOfMonth1) {
                String date = year1+"-"+(month1+1)+"-"+dayOfMonth1;
                selectDay.setText(date);
                requestTargetDateCount(date,jobNumber);
            }
        });

        candidateButton = findViewById(R.id.candidateButton);
        candidateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                candidate();
            }
        });

    }

    private void candidate() {
        final String targetDate = selectDay.getText().toString();
        final String seekerId = Base.sessionManager.getUserDetails().get("id");

        String url = getResources().getString(R.string.url) + "candidateJob.do";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processCandidateRequest(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("targetDate",targetDate);
                params.put("seekerId",seekerId);
                params.put("jobNumber",String.valueOf(jobNumber));
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

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

    private void requestTargetDateCount(final String date, final int jobNumber) {
        String url = getResources().getString(R.string.url)+"requestTargetDateCount.do";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        currentCount.setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("targetDate",date);
                params.put("jobNumber",String.valueOf(jobNumber));
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    private void requestJobDetail(final int jobNumber) {
        String url = getResources().getString(R.string.url) + "requestJobDetail.do";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processRequestJobDetail(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("jobNumber",String.valueOf(jobNumber));
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    private void processRequestJobDetail(String response) {
        item = Base.gson.fromJson(response,ProjectJobListVO.class);

        //setText 하기
        jobName.setText(item.getJobName());
        jobPay.setText(Base.decimalFormat(item.getJobPay()));
        jobRequirement.setText(item.getJobRequirement());
        //TODO 현제 Count 를 체크하기
        currentCount.setText("0");
        limitCount.setText(String.valueOf(item.getJobLimitCount()));

        Date minDate = Date.valueOf(item.getJobStartDate());
        Date maxDate = Date.valueOf(item.getJobEndDate());

        calendar.setMinDate(minDate.getTime());
        calendar.setMaxDate(maxDate.getTime());
    }
}

//TODO job 의 limitCount 와 candidateQueue 의 targetDate 지원자 숫자를 계산하여 disable 할 날짜를 가져올 수 있는 쿼리를 만들자

//        Calendar now = Calendar.getInstance();
//        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
//                your_on_date_set_listner,
//                now.get(Calendar.YEAR),
//                now.get(Calendar.MONTH),
//                now.get(Calendar.DAY_OF_MONTH)
//        );
//        dpd.setVersion(com.wdullaer.materialdatetimepicker.date.DatePickerDialog.Version.VERSION_1);
//        Calendar[] days;
//        List<Calendar> blockedDays = new ArrayList<>();
//
//// Code to disable particular date
//        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
//        Date date = formatter.parse("06/25/2020");
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        blockedDays.add(cal);
//        days = blockedDays.toArray(new Calendar[blockedDays.size()]);
//        dpd.setDisabledDays(days);
//    }