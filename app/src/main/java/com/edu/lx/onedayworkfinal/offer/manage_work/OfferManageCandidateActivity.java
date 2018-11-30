package com.edu.lx.onedayworkfinal.offer.manage_work;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.request.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.offer.recycler_view.OfferManageCandidateTargetDateRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.CandidateMapResponseModel;
import com.edu.lx.onedayworkfinal.vo.JobCandidateVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OfferManageCandidateActivity extends AppCompatActivity {

    int jobNumber;
    public HashMap<String,List<JobCandidateVO>> map;

    RecyclerView recyclerView;

    LinearLayout emptyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_manage_candidate_acitivty);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        emptyLayout = findViewById(R.id.emptyLayout);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);

        Intent intent = getIntent();
        jobNumber = intent.getIntExtra("jobNumber",0);
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
        List<JobCandidateVO> headerList = result.getTargetDateList();

        if (headerList.size() > 0) {
            emptyLayout.setVisibility(View.GONE);

            ArrayList<JobCandidateVO> sortedHeaderList = new ArrayList<>();
            ArrayList<String> targetDateList = new ArrayList<>();
            for (JobCandidateVO item : headerList) {
                if (!targetDateList.contains(item.getTargetDate())) {
                    targetDateList.add(item.getTargetDate());
                    sortedHeaderList.add(item);
                }
            }

            OfferManageCandidateTargetDateRecyclerViewAdapter adapter = new OfferManageCandidateTargetDateRecyclerViewAdapter(this);
            adapter.setItems(sortedHeaderList);
            recyclerView.setAdapter(adapter);

        } else {
            emptyLayout.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
