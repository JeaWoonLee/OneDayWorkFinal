package com.edu.lx.onedayworkfinal.seeker.recycler_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.request.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.seeker.SeekerMainActivity;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseViewHolder;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.JobVO;
import com.edu.lx.onedayworkfinal.vo.ProjectVO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SeekerProjectListRecyclerViewAdapter extends BaseRecyclerViewAdapter<ProjectVO> {

    public SeekerProjectListRecyclerViewAdapter (Context context) {
        super(context);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i) {
        return new SeekerProjectListViewHolder(inflateView(super.context, R.layout.seeker_find_project_item,viewGroup));
    }

    class SeekerProjectListViewHolder extends BaseViewHolder<ProjectVO> {
        TextView projectName;
        TextView projectDate;
        TextView projectSubject;
        TextView projectEnrollDate;
        TextView projectNumber;
        RecyclerView jobListRecyclerView;

        SeekerJobListRecyclerViewAdapter adapter;

        //SeekerProjectListViewHolder
        SeekerProjectListViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                TextView projectNumber = v.findViewById(R.id.projectNumber);
                String projectNum = projectNumber.getText().toString();
                SeekerMainActivity activity = (SeekerMainActivity)context;
                activity.showProjectDetail(Integer.parseInt(projectNum));
            });
            projectNumber = itemView.findViewById(R.id.projectNumber);
            projectName = itemView.findViewById(R.id.projectName);
            projectDate = itemView.findViewById(R.id.projectDate);
            projectEnrollDate = itemView.findViewById(R.id.projectEnrollDate);
            projectSubject = itemView.findViewById(R.id.projectSubject);

            //내부 RecyclerView
            jobListRecyclerView = itemView.findViewById(R.id.jobListRecyclerView);
            //RecyclerView 의 layoutManager 세팅
            LinearLayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext(),LinearLayoutManager.VERTICAL,false);
            jobListRecyclerView.setLayoutManager(layoutManager);
            //Adapter Init
            adapter = new SeekerJobListRecyclerViewAdapter(context);

        }
        //end SeekerProjectListViewHolder

        //setItem
        @Override
        public void setItem (ProjectVO projectVO) {

            if (projectVO != null) {

                projectName.setText(projectVO.getProjectName());
                projectSubject.setText(projectVO.getProjectSubject());
                projectDate.setText(String.format("%s - %s", projectVO.getProjectStartDate(), projectVO.getProjectEndDate()));
                projectEnrollDate.setText(projectVO.getProjectEnrollDate());
                projectNumber.setText(String.valueOf(projectVO.getProjectNumber()));
                requestProjectJobList(projectVO.getProjectNumber());
            } else {

                Log.d(this.getClass().getSimpleName(),"item 이 null 입니다!");

            }

        }
        //end setItem

        //projectNumber 로 projectJobList 를 서버에 요청하는 메서드
        private void requestProjectJobList (final int projectNumber) {
            String url = context.getResources().getString(R.string.url) + "requestProjectJobListByProjectNumber.do";

            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    url,
                    this::processJobListResponse,
                    error -> {

                    }
            ){
                @Override
                protected Map<String, String> getParams () {
                    Map<String,String> params = new HashMap<>();
                    params.put("projectNumber",String.valueOf(projectNumber));
                    return params;
                }
            };

            request.setShouldCache(false);
            Base.requestQueue.add(request);
        }
        //end requestProjectJobList

        //서버에서 받아온 projectJobList 를 RecyclerView 에 뿌려줌
        private void processJobListResponse (String response) {
            JobVO[] jobsArray = Base.gson.fromJson(response, JobVO[].class);
            ArrayList<JobVO> items = new ArrayList<>(Arrays.asList(jobsArray));

            adapter.setItems(items);
            jobListRecyclerView.setAdapter(adapter);

        }
        //end processJobListResponse

    }
    //end SeekerProjectListViewHolder


}
