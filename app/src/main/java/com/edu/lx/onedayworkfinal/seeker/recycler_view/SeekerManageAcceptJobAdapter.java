package com.edu.lx.onedayworkfinal.seeker.recycler_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseViewHolder;
import com.edu.lx.onedayworkfinal.util.volley.Base;

import com.edu.lx.onedayworkfinal.vo.JobVO;
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
        TextView jobName;
        TextView jobPay;
        TextView jobDate;


        SeekerManageAcceptListViewHolder(@NonNull View itemView) {
            super(itemView);
            jobName = itemView.findViewById(R.id.jobName);
            jobPay = itemView.findViewById(R.id.jobPay);
            jobDate = itemView.findViewById(R.id.jobDate);
        }

        @Override
        public void setItem (ManageVO item) {

            jobName.setText(item.getJobName());
            //1000 단위 숫자로 콤마를 찍어서 보여준다
            jobPay.setText(Base.decimalFormat(item.getJobPay()));
            jobDate.setText(String.format("%s - %s", item.getJobStartDate(), item.getJobEndDate()));

        }

    }

}
