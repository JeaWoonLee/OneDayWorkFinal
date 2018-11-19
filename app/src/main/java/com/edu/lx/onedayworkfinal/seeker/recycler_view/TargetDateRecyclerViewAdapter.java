package com.edu.lx.onedayworkfinal.seeker.recycler_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.seeker.SeekerMainActivity;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseViewHolder;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.JobCandidateVO;
import com.edu.lx.onedayworkfinal.vo.ManageVO;
import com.edu.lx.onedayworkfinal.vo.SeekerVO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.edu.lx.onedayworkfinal.seeker.SeekerMainActivity.items;

public class TargetDateRecyclerViewAdapter extends BaseRecyclerViewAdapter<JobCandidateVO> {

    public TargetDateRecyclerViewAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(inflateView(context, R.layout.target_date_recycler_item,viewGroup));
    }

    class ViewHolder extends BaseViewHolder<JobCandidateVO>{

        TextView targetDate;

        RecyclerView recyclerView;
        SeekerManageProjectRecyclerViewAdapter adapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            targetDate = itemView.findViewById(R.id.targetDate);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(layoutManager);
        }

        @Override
        public void setItem(JobCandidateVO jobCandidateVO) {
            String seekerId = Base.sessionManager.getUserDetails().get("id");
            targetDate.setText(jobCandidateVO.getTargetDate());
            requestManageList(seekerId,jobCandidateVO.getTargetDate());
        }

        /**
         * requestManageList
         * @param seekerId 세션에서 가져온 값
         * @param targetDate recyclerView 의 setItem 에서 가져올 값
         * //TODO requestCandidateDateList 의 onResponse 의 setAdapter 해주는 클래스 내부에서 호출
         */
        public void requestManageList (final String seekerId, String targetDate) {
            String url = context.getResources().getString(R.string.url) + "manageJobList.do";
            StringRequest request = new StringRequest(
                    Request.Method.POST,
                    url,
                    this::processProjectResponse,
                    error -> {

                    }
            ){
                @Override
                protected Map<String, String> getParams () {
                    Map<String, String> params = new HashMap<>();
                    params.put("seekerId", String.valueOf(seekerId));
                    params.put("targetDate",targetDate);

                    return params;
                }
            };

            request.setShouldCache(false);
            Base.requestQueue.add(request);
        }

        //서버로부터 받아온 projectList 를 RecyclerView 에 뿌려줌
        private void processProjectResponse (String response) {
            ManageVO[] manageArray = Base.gson.fromJson(response,ManageVO[].class);

            items = new ArrayList<>(Arrays.asList(manageArray));
            adapter = new SeekerManageProjectRecyclerViewAdapter(context);
            //Adapter 할당
            adapter.setItems(items);
            recyclerView.setAdapter(adapter);
        }
    }
}
