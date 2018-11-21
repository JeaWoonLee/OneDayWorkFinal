package com.edu.lx.onedayworkfinal.offer.recycler_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.offer.manage_work.OfferManageHumanResourcesActivity;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseViewHolder;
import com.edu.lx.onedayworkfinal.vo.JobCandidateVO;
import com.edu.lx.onedayworkfinal.vo.ManageHumanResourceModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HumanResourceJobRecyclerViewAdapter extends BaseRecyclerViewAdapter<JobCandidateVO> {

    public HumanResourceJobRecyclerViewAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(inflateView(context,R.layout.offer_manage_job_item,viewGroup));
    }

    class ViewHolder extends BaseViewHolder<JobCandidateVO>{
        TextView jobName;
        TextView recruitmentRate;
        RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            jobName= itemView.findViewById(R.id.jobName);
            recruitmentRate = itemView.findViewById(R.id.recruitmentRate);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            LinearLayoutManager manager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(manager);
        }

        @Override
        public void setItem(JobCandidateVO jobCandidateVO) {
            jobName.setText(jobCandidateVO.getJobName());
            int recruit = jobCandidateVO.getRecruit();
            int getJobLimitCount = jobCandidateVO.getJobLimitCount();
            String recruitRate = "("+recruit+"/"+getJobLimitCount+")";
            recruitmentRate.setText(recruitRate);

            if (context instanceof OfferManageHumanResourcesActivity) {
                OfferManageHumanResourcesActivity activity = (OfferManageHumanResourcesActivity) context;
                HashMap<Integer,List<ManageHumanResourceModel>> recruitMap = activity.recruitMap;

                List<ManageHumanResourceModel> list = recruitMap.get(jobCandidateVO.getJobNumber());
                HumanResourceRecyclerViewAdapter adapter = new HumanResourceRecyclerViewAdapter(activity);
                adapter.setItems((ArrayList<ManageHumanResourceModel>) list);
//                activity.container.setMinimumHeight(list.size() * 104);
//                activity.recyclerViewContainerLayout.setMinimumHeight(list.size() * 104);
//                activity.recyclerView.setMinimumHeight(list.size() * 104);
                recyclerView.setMinimumHeight(list.size() * 104);
                recyclerView.setAdapter(adapter);
            }

        }
    }
}
