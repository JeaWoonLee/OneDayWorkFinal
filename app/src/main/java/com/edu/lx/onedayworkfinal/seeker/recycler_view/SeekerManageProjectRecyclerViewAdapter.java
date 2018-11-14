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
import android.widget.Toast;

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
import com.edu.lx.onedayworkfinal.vo.ManageVO;
import com.edu.lx.onedayworkfinal.vo.ProjectVO;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

//recycler view 해제 
public class SeekerManageProjectRecyclerViewAdapter extends BaseRecyclerViewAdapter<ManageVO> {

    public SeekerManageProjectRecyclerViewAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SeekerManageProjectViewHolder(inflateView(super.context, R.layout.seeker_manage_project_item, viewGroup));
    }

    class SeekerManageProjectViewHolder extends BaseViewHolder<ManageVO> {

        TextView projectName;
        TextView projectDate;
        TextView projectSubject;
        TextView targetDate;
        TextView projectNumber;
        TextView candidateNumber;
        //RecyclerView ManageListRecyclerView;

        SeekerManageProjectRecyclerViewAdapter adapter;

        public SeekerManageProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String candidateNum = candidateNumber.getText().toString();
                    SeekerMainActivity activity = (SeekerMainActivity) context;
                    Toast.makeText(activity.getApplicationContext(),"candidateNum" + candidateNum,Toast.LENGTH_LONG).show();
                    activity.showProjectDetailManage(Integer.parseInt(candidateNum));
                }
            });
            projectNumber = itemView.findViewById(R.id.projectNumber);
            projectName = itemView.findViewById(R.id.projectName);
            projectDate = itemView.findViewById(R.id.projectDate);
            targetDate = itemView.findViewById(R.id.targetDate);
            projectSubject = itemView.findViewById(R.id.projectSubject);
            candidateNumber = itemView.findViewById(R.id.candidateNumber);

            //ManageListRecyclerView = itemView.findViewById(R.id.ManageListRecyclerView);

            LinearLayoutManager layoutManager = new LinearLayoutManager(context.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
            //ManageListRecyclerView.setLayoutManager(layoutManager);

            adapter = new SeekerManageProjectRecyclerViewAdapter(context);


        }


        @Override
        public void setItem(ManageVO manageVO) {

            if (manageVO != null) {
                Log.d("manageVO",manageVO.toString());
                projectName.setText(manageVO.getProjectName());
                projectSubject.setText(manageVO.getProjectSubject());
                projectDate.setText(manageVO.getProjectStartDate() + "~" +manageVO.getProjectEndDate());
                targetDate.setText(manageVO.getTargetDate());
                projectNumber.setText(String.valueOf(manageVO.getProjectNumber()));
                candidateNumber.setText(String.valueOf(manageVO.getCandidateNumber()));
                //requestProjectJobListCanNum(manageVO.getCandidateNumber());

            } else {

                Log.d(this.getClass().getSimpleName(), "item 이 null 입니다.");
            }
        }

        //날짜로 구분 진행해야함 . 181113. new .do 생성, jobVO를 포함한 새로운 VO 생성
//        private void requestProjectJobListCanNum(final int candidateNumber) {
//            String url = context.getResources().getString(R.string.url) + "requestProjectJobListCanNum.do";
//
//            StringRequest request = new StringRequest(
//                    Request.Method.POST,
//                    url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            processJobListResponse(response);
//                        }
//                    },
//
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//
//                        }
//                    }
//
//
//            ) {
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<>();
//                    params.put("candidateNumber", String.valueOf(candidateNumber));
//                    return params;
//                }
//            };
//
//            request.setShouldCache(false);
//            Base.requestQueue.add(request);
//
//        }
//
//        private void processJobListResponse(String response) {
//            ManageVO[] ManageArray = Base.gson.fromJson(response, ManageVO[].class);
//            ArrayList<ManageVO> items = new ArrayList<>(Arrays.asList(ManageArray));
//
//            adapter.setItems(items);
//            ManageListRecyclerView.setAdapter(adapter);
//        }
    }


}

