package com.edu.lx.onedayworkfinal.seeker.recycler_view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.seeker.SeekerMainActivity;
import com.edu.lx.onedayworkfinal.seeker.my_work.JobManageDetailActivity;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseViewHolder;
import com.edu.lx.onedayworkfinal.util.volley.Base;

import com.edu.lx.onedayworkfinal.vo.ManageVO;

public class SeekerManageAcceptJobAdapter extends BaseRecyclerViewAdapter<ManageVO> {

    public SeekerManageAcceptJobAdapter (Context context) {
        super(context);
    }
    @NonNull
    @Override
    public SeekerManageAcceptListViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i) {
        return new SeekerManageAcceptListViewHolder(inflateView(context, R.layout.seeker_accept_job_manage,viewGroup));
    }

    class SeekerManageAcceptListViewHolder extends BaseViewHolder<ManageVO> {
        TextView projectName;
        TextView jobName;
        TextView jobPay;
        TextView targetDate;

        ManageVO item;

        SeekerManageAcceptListViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                int candidateNum = item.getCandidateNumber();
                SeekerMainActivity activity = (SeekerMainActivity) context;
                Intent intent = new Intent(activity,JobManageDetailActivity.class);
                intent.putExtra("candidateNumber",candidateNum);
                activity.startActivityForResult(intent,500);
            });

            projectName = itemView.findViewById(R.id.projectName);
            jobName = itemView.findViewById(R.id.jobName);
            jobPay = itemView.findViewById(R.id.jobPay);
            targetDate = itemView.findViewById(R.id.targetDate);
        }

        @Override
        public void setItem (ManageVO item) {
            this.item = item;

            jobName.setText(item.getJobName());
            //1000 단위 숫자로 콤마를 찍어서 보여준다
            jobPay.setText(Base.decimalFormat(item.getJobPay()));
            targetDate.setText(item.getTargetDate());
            projectName.setText(String.valueOf(item.getProjectName()));

        }

    }

}
