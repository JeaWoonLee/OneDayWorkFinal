package com.edu.lx.onedayworkfinal.offer.recycler_view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.offer.OfferMainActivity;
import com.edu.lx.onedayworkfinal.offer.manage_work.OfferManageWorkActivity;
import com.edu.lx.onedayworkfinal.seeker.recycler_view.SeekerJobListRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseViewHolder;
import com.edu.lx.onedayworkfinal.vo.ProjectVO;

public class OfferProjectListRecyclerViewAdapter extends BaseRecyclerViewAdapter<ProjectVO> {

    public OfferProjectListRecyclerViewAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i) {
        return new SeekerProjectListViewHolder(inflateView(super.context, R.layout.offer_manage_project_item,viewGroup));
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
                OfferMainActivity activity = (OfferMainActivity)context;

                Intent intent = new Intent(activity,OfferManageWorkActivity.class);
                intent.putExtra("projectNumber",projectNum);
                activity.startActivity(intent);
                //activity.showProjectDetail(Integer.parseInt(projectNum));
            });
            projectNumber = itemView.findViewById(R.id.projectNumber);
            projectName = itemView.findViewById(R.id.projectName);
            projectDate = itemView.findViewById(R.id.projectDate);
            projectEnrollDate = itemView.findViewById(R.id.projectEnrollDate);
            projectSubject = itemView.findViewById(R.id.projectSubject);
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
            } else {

                Log.d(this.getClass().getSimpleName(),"item 이 null 입니다!");

            }
        }
    }



}
