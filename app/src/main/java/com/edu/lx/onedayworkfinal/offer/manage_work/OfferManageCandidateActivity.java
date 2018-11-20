package com.edu.lx.onedayworkfinal.offer.manage_work;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.offer.recycler_view.OfferManageCandidateTargetDateRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.CandidateMapResponseModel;
import com.edu.lx.onedayworkfinal.vo.JobCandidateVO;
import com.edu.lx.onedayworkfinal.vo.JobVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class OfferManageCandidateActivity extends AppCompatActivity {

    int jobNumber;
    public HashMap<String,List<JobCandidateVO>> map;

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_manage_candidate_acitivty);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);

        Intent intent = getIntent();
        jobNumber = intent.getIntExtra("jobNumber",0);
        Toast.makeText(getApplicationContext(),"jobNumber : " + jobNumber,Toast.LENGTH_LONG).show();
        requestCandidateListByJobNumber(jobNumber);
    }

    /**
     * requestCandidateListByJobNumber
     * @param jobNumber 해당 jobNumber 로 targetDate 로 정렬된 신청자 정보를 가져온다
     */
    public void requestCandidateListByJobNumber(int jobNumber) {
        String url = getResources().getString(R.string.url) + "requestCandidateListByJobNumber.do";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                this::processCandidateList,
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
     * processCandidateList
     * @param response targetDate 를 key 값으로 한 List<JobCandidateVO> value 값을 갖고있는 HashMap
     */
    private void processCandidateList(String response) {
        CandidateMapResponseModel result = Base.gson.fromJson(response,CandidateMapResponseModel.class);
        map = result.getResult();
        Set key = map.keySet();
        List<JobCandidateVO> headerList = result.getTargetDateList();
        for (JobCandidateVO header : headerList) {
            List<JobCandidateVO> list = map.get(header.getTargetDate());
            if (list.get(0) != null) {
                header.setJobLimitCount(list.get(0).getJobLimitCount());
                header.setRecruit(list.get(0).getRecruit());
                header.setTargetDate(list.get(0).getTargetDate());
            }

        }

        OfferManageCandidateTargetDateRecyclerViewAdapter adapter = new OfferManageCandidateTargetDateRecyclerViewAdapter(this);
        adapter.setItems((ArrayList<JobCandidateVO>) headerList);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
