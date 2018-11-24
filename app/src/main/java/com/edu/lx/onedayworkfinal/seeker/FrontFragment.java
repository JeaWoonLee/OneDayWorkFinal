package com.edu.lx.onedayworkfinal.seeker;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.request.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.JobCandidateVO;
import com.edu.lx.onedayworkfinal.vo.WorkVO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.edu.lx.onedayworkfinal.seeker.SeekerMainActivity.FIND_JOB_FRAGMENT;
import static com.edu.lx.onedayworkfinal.seeker.SeekerMainActivity.FRONT_FRAGMENT;
import static com.edu.lx.onedayworkfinal.seeker.SeekerMainActivity.MANAGE_JOB_FRAGMENT;

public class FrontFragment extends Fragment {

    SeekerMainActivity activity;


    //오늘의 일감
    LinearLayout today_work_empty_layout;
    LinearLayout today_work_layout;

    TextView projectName;
    TextView jobName;
    TextView jobPay;
    TextView workTime;
    TextView candidateStatus;
    LinearLayout today_work;

    //하루일감 메뉴
    LinearLayout find_job;
    LinearLayout manage_work;
    LinearLayout my_work;

    //이력 정보 메뉴
    LinearLayout record_empty_layout;
    LinearLayout record_layout;
    TextView total;
    TextView reliability;
    TextView offWorkCount;
    TextView absentCount;
    TextView runCount;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        activity = (SeekerMainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.seeker_front_fragment,container,false);
        //오늘의 일감
        today_work_empty_layout = rootView.findViewById(R.id.today_work_empty_layout);
        today_work_layout = rootView.findViewById(R.id.today_work_layout);
        projectName = rootView.findViewById(R.id.projectName);
        jobName = rootView.findViewById(R.id.jobName);
        jobPay = rootView.findViewById(R.id.jobPay);
        workTime = rootView.findViewById(R.id.workTime);
        candidateStatus = rootView.findViewById(R.id.candidateStatus);
        today_work = rootView.findViewById(R.id.today_work);

        //하루일감 메뉴
        find_job = rootView.findViewById(R.id.find_job);
        manage_work = rootView.findViewById(R.id.manage_work);
        my_work = rootView.findViewById(R.id.my_work);

        //이력 정보 메뉴
        record_empty_layout = rootView.findViewById(R.id.record_empty_layout);
        record_layout = rootView.findViewById(R.id.record_layout);
        total = rootView.findViewById(R.id.total);
        reliability = rootView.findViewById(R.id.reliability);
        offWorkCount = rootView.findViewById(R.id.offWorkCount);
        absentCount = rootView.findViewById(R.id.absentCount);
        runCount = rootView.findViewById(R.id.runCount);


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //오늘의 일감 정보를 가지고 온다
        requestTodayWorkDetail(Base.sessionManager.getUserDetails().get("id"));

        //하루일감 메뉴 리스너
        today_work.setOnClickListener(v -> activity.requestTodayWorkDetailCheck(Base.sessionManager.getUserDetails().get("id")));
        find_job.setOnClickListener(v -> activity.changeFragment(FIND_JOB_FRAGMENT));
        manage_work.setOnClickListener(v -> activity.changeFragment(MANAGE_JOB_FRAGMENT));
        my_work.setOnClickListener(v -> {
            //TODO 일관리 메뉴 추가하기
            activity.changeFragment(MANAGE_JOB_FRAGMENT);
        });

        //이력정보 메뉴
        requestSeekerRecord(Base.sessionManager.getUserDetails().get("id"));
    }

    public void requestTodayWorkDetail(String id) {
        String url = getResources().getString(R.string.url) + "requestTodayWorkDetail.do";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                this::processTodayDetail, error -> {}){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("seekerId",id);
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    private void processTodayDetail(String response) {
        activity.todayWorkItem = Base.gson.fromJson(response,WorkVO.class);
        //오늘의 일감 처리
        showTodayWorkMenu(activity.todayWorkItem);
    }

    private void showTodayWorkMenu(WorkVO todayWorkItem) {
        if (todayWorkItem == null) {
            today_work_empty_layout.setVisibility(View.VISIBLE);
            today_work_layout.setVisibility(View.GONE);
        }else {
            today_work_empty_layout.setVisibility(View.GONE);
            today_work_layout.setVisibility(View.VISIBLE);

            projectName.setText(todayWorkItem.getProjectName());
            jobName.setText(todayWorkItem.getJobName());
            String jobPayStr = Base.decimalFormat(Integer.parseInt(todayWorkItem.getJobPay())) + " 원";
            jobPay.setText(jobPayStr);
            String workTimeStr = todayWorkItem.getWorkStartTime() + " ~ " + todayWorkItem.getWorkEndTime();
            workTime.setText(workTimeStr);
            int status = todayWorkItem.getCandidateStatus();
            switch (status) {
                case 1:
                    candidateStatus.setText("미출근");
                    candidateStatus.setTextColor(activity.getResources().getColor(R.color.dark_gray,activity.getTheme()));
                    break;
                case 2:
                    candidateStatus.setText("출근");
                    candidateStatus.setTextColor(activity.getResources().getColor(R.color.seeker,activity.getTheme()));
                    break;
                case 3:
                    candidateStatus.setText("근무중");
                    candidateStatus.setTextColor(activity.getResources().getColor(R.color.offer,activity.getTheme()));
                    break;
                case 4:
                    candidateStatus.setText("퇴근");
                    candidateStatus.setTextColor(activity.getResources().getColor(R.color.blue1,activity.getTheme()));
                    break;
                case 5:
                    candidateStatus.setText("무단이탈");
                    candidateStatus.setTextColor(activity.getResources().getColor(R.color.danger,activity.getTheme()));
                    break;
                case 6:
                    candidateStatus.setText("결근");
                    candidateStatus.setTextColor(activity.getResources().getColor(R.color.danger,activity.getTheme()));
                    break;
            }

        }

    }

    private void requestSeekerRecord(String seekerId) {
        String url = getResources().getString(R.string.url) + "requestSeekerRecord.do";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                this::processSeekerRecordResponse,
                error -> {

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

    private void processSeekerRecordResponse(String response) {
        JobCandidateVO[] result = Base.gson.fromJson(response,JobCandidateVO[].class);
        ArrayList<JobCandidateVO> items = new ArrayList<>(Arrays.asList(result));
        if (items.size() == 0) {
            record_empty_layout.setVisibility(View.VISIBLE);
            record_layout.setVisibility(View.GONE);
        } else  {
            record_empty_layout.setVisibility(View.GONE);
            record_layout.setVisibility(View.VISIBLE);

            int totalCnt = items.size();
            int offCnt = 0;
            int absentCnt = 0;
            int runCnt = 0;

            for (JobCandidateVO item : items) {
                int status = item.getCandidateStatus();
                switch (status) {
                    case 4:
                        offCnt ++;
                        break;
                    case 5:
                        runCnt ++;
                        break;
                    case 6:
                        absentCnt ++;
                        break;
                }
            }

            total.setText(String.valueOf(totalCnt));
            offWorkCount.setText(String.valueOf(offCnt));
            absentCount.setText(String.valueOf(absentCnt));
            runCount.setText(String.valueOf(runCnt));
            int reliable = ((int)(((double) offCnt / (double) totalCnt)*100));

            if (reliable < 80) {
                reliability.setTextColor(getResources().getColor(R.color.danger,activity.getTheme()));
            } else if (reliable < 90){
                reliability.setTextColor(getResources().getColor(R.color.black,activity.getTheme()));
            } else  {
                reliability.setTextColor(getResources().getColor(R.color.blue1,activity.getTheme()));
            }
            String reliableStr = String.valueOf(reliable);
            reliability.setText(reliableStr + "%");
        }

    }
}
