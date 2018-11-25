package com.edu.lx.onedayworkfinal.seeker.my_work;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.error.AuthFailureError;
import com.android.volley.request.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.seeker.SeekerMainActivity;
import com.edu.lx.onedayworkfinal.seeker.recycler_view.SeekerManageAcceptJobAdapter;
import com.edu.lx.onedayworkfinal.seeker.recycler_view.SeekerManageFinishJobAdapter;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.ManageVO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MyWorkFragment extends Fragment {

    SeekerMainActivity activity;

    String seekerId;
    ManageVO manageVO;
    TextView receivedPayView;
    TextView predictPayView;

    //수락한 일감, 종료된 일감 목록
    ArrayList<ManageVO> acceptedItems;
    ArrayList<ManageVO> finishJobList;
    int nowPayData;

    SeekerManageAcceptJobAdapter adapter;
    SeekerManageFinishJobAdapter FinishAdapter;

    //현재까지 수령한 총액
    TextView nowPay;
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
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SeekerMainActivity) getActivity();
        seekerId = Base.sessionManager.getUserDetails().get("id");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.my_work_fragment,container,false);
        rootView = initUI(rootView);


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestAcceptJobList(seekerId);
    }

    //UI 세팅
    private ViewGroup initUI(ViewGroup rootView) {
        //현재까지 수령 총액
        receivedPayView = rootView.findViewById(R.id.receivedPayView);
        //수령할 총액
        predictPayView = rootView.findViewById(R.id.predictPayView);


        //수락한 일감
        acceptJobView = rootView.findViewById(R.id.acceptJobView);
        showAcceptButton = rootView.findViewById(R.id.showAcceptButton);
        showAcceptButton.setOnClickListener(v -> showAcceptJob(acceptJobVisible));
        acceptJobRecyclerView = rootView.findViewById(R.id.acceptJobRecyclerView);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(activity.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        acceptJobRecyclerView.setLayoutManager(layoutManager1);

        //근무결과목록 확인
        finishJobView = rootView.findViewById(R.id.finishJobView);
        showFinishButton = rootView.findViewById(R.id.showFinishButton);
        showFinishButton.setOnClickListener(v -> showFinishJob(finishJobVisible));
        finishJobViewRecyclerView = rootView.findViewById(R.id.finishJobViewRecyclerView);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(activity.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        finishJobViewRecyclerView.setLayoutManager(layoutManager2);

        return rootView;
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
        acceptedItems = new ArrayList<>(Arrays.asList(vo));
        adapter = new SeekerManageAcceptJobAdapter(activity);
        adapter.setItems(acceptedItems);
        acceptJobRecyclerView.setAdapter(adapter);

        requestFinishJobList(seekerId);
    }

    //수락한 일감 보여주기, 숨기기
    public void showAcceptJob(int acceptJobVisible) {
        switch (acceptJobVisible) {
            case 0:
                if (acceptedItems.size() == 0) {
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
    //finishjob List 요청
    public void requestFinishJobList(String seekerId) {
        String url = getResources().getString(R.string.url) + "requestFinishJobList.do";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                this::processFinishJobJobList, error -> {


        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("seekerId", seekerId);
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);

    }
    //finishjob List
    public void processFinishJobJobList(String response) {
        ManageVO[] vo = Base.gson.fromJson(response,ManageVO[].class);
        finishJobList = new ArrayList<>(Arrays.asList(vo));
        FinishAdapter = new SeekerManageFinishJobAdapter(activity);
        FinishAdapter.setItems(finishJobList);
        finishJobViewRecyclerView.setAdapter(FinishAdapter);

        processPayInfo();
    }

    //FinishJob 닫기 ,펼치리
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

    //이번 달 수령액 / 이번달 예상 수령액
    private void processPayInfo () {
        //예상 수령액
        int predictPay = 0;
        int receivedPay = 0;

        for (ManageVO item : acceptedItems) predictPay += item.getJobPay();
        for (ManageVO item : finishJobList) if (TextUtils.equals(item.getCandidateStatus(),"4")) receivedPay += item.getJobPay();

        receivedPayView.setText(String.valueOf(Base.decimalFormat(receivedPay))+" 원");
        predictPayView.setText(String.valueOf(Base.decimalFormat(predictPay))+" 원");
    }


}
