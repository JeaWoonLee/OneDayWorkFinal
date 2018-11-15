package com.edu.lx.onedayworkfinal.seeker.recycler_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.seeker.ManageProjectDetailActivity;
import com.edu.lx.onedayworkfinal.seeker.find.ProjectDetailActivity;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseViewHolder;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.JobVO;
import com.edu.lx.onedayworkfinal.vo.ManageVO;

public class SeekerManageDetailProjectAdapter extends BaseRecyclerViewAdapter<ManageVO> {

    public SeekerManageDetailProjectAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i) {
        return new SeekerProjectListViewHolder(inflateView(super.context, R.layout.activity_project_detail_manage,viewGroup));
    }

    class SeekerProjectListViewHolder extends BaseViewHolder<ManageVO> {
        TextView projectName;
        TextView projectSubject;
        TextView projectDate;

        //Button candidateButton;

        SeekerProjectListViewHolder(@NonNull View itemView) {
            super(itemView);
            projectDate = itemView.findViewById(R.id.projectDate);
            projectName = itemView.findViewById(R.id.projectName);
            projectSubject = itemView.findViewById(R.id.projectSubject);

//            candidateButton = itemView.findViewById(R.id.candidateButton);
        }

        public void setItem (final ManageVO item) {

            projectDate.setText(item.getProjectEnrollDate());
            //1000 단위 숫자로 콤마를 찍어서 보여준다
            projectName.setText(item.getProjectName());
            projectSubject.setText(item.getProjectSubject());

//            candidateButton.setOnClickListener(v -> {
//
//                if (context instanceof ManageProjectDetailFragment){
//                    ManageProjectDetailFragment activity = (ManageProjectDetailFragment) context;
////                    activity.showCandidate(item.getJobNumber());
//                }
//
//            });
        }

    }

}
