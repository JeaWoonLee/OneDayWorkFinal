package com.edu.lx.onedayworkfinal.seeker.recycler_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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

public class SeekerManageProjectRecyclerViewAdapter extends BaseRecyclerViewAdapter<ProjectVO> {

    public SeekerManageProjectRecyclerViewAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SeekerManageProjectViewHolder(inflateView(super.context, R.layout.seeker_find_project_item, viewGroup));
    }

    class SeekerManageProjectViewHolder extends BaseViewHolder<ProjectVO> {

        TextView projectName;
        TextView projectDate;
        TextView projectSubject;
        TextView projectEnrollDate;
        TextView projectNumber;
        RecyclerView jobListRecyclerView;

        SeekerManageProjectRecyclerViewAdapter adapter;

        public SeekerManageProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView projectNumber = v.findViewById(R.id.projectNumber);
                    String projectNum = projectNumber.getText().toString();
                    SeekerMainActivity activity = (SeekerMainActivity) context;
                    activity.showProjectDetail(Integer.parseInt(projectNum));
                }
            });
            projectNumber = itemView.findViewById(R.id.projectNumber);
            projectName = itemView.findViewById(R.id.projectName);
            projectDate = itemView.findViewById(R.id.projectDate);
            projectEnrollDate = itemView.findViewById(R.id.projectEnrollDate);
            projectSubject = itemView.findViewById(R.id.projectSubject);

            jobListRecyclerView = itemView.findViewById(R.id.jobListRecyclerView);

            LinearLayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            jobListRecyclerView.setLayoutManager(layoutManager);

            adapter = new SeekerManageProjectRecyclerViewAdapter(context);


        }


        @Override
        public void setItem(ProjectVO projectVO) {

            if (projectVO != null) {

                projectName.setText(projectVO.getProjectName());
                projectSubject.setText(projectVO.getProjectSubject());
                projectDate.setText(projectVO.getProjectStartDate() + projectVO.getProjectEndDate());
                projectEnrollDate.setText(projectVO.getProjectEnrollDate());
                projectNumber.setText(String.valueOf(projectVO.getProjectNumber()));
                //requestProjectJobList(projectVO.getProjectNumber());

            } else {

                Log.d(this.getClass().getSimpleName(), "item 이 null 입니다.");
            }
        }


        private void requestProjectJobList(final int projectNumber) {
            String url = context.getResources().getString(R.string.url) + "manageJobList.do";

            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            processJobListResponse(response);
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }


            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("projectNumber", String.valueOf(projectNumber));
                    return params;
                }
            };

            request.setShouldCache(false);
            Base.requestQueue.add(request);

        }

        private void processJobListResponse(String response) {
            ProjectVO[] jobsArray = Base.gson.fromJson(response, ProjectVO[].class);
            ArrayList<ProjectVO> items = new ArrayList<>(Arrays.asList(jobsArray));

            adapter.setItems(items);
            jobListRecyclerView.setAdapter(adapter);
        }
    }


}

