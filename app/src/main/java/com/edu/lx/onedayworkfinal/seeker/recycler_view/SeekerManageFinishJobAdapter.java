package com.edu.lx.onedayworkfinal.seeker.recycler_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseViewHolder;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.ManageVO;

public class SeekerManageFinishJobAdapter extends BaseRecyclerViewAdapter<ManageVO> {

    public SeekerManageFinishJobAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public SeekerManageFinishListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SeekerManageFinishListViewHolder(inflateView(context, R.layout.seeker_manage_finish_job_item,viewGroup));
    }

        class SeekerManageFinishListViewHolder extends BaseViewHolder<ManageVO> {
            TextView jobName;
            TextView jobPay;
            TextView jobDate;

            SeekerManageFinishListViewHolder(View itemView) {
                super(itemView);
                jobName = itemView.findViewById(R.id.jobName);
                jobPay = itemView.findViewById(R.id.jobPay);
                jobDate = itemView.findViewById(R.id.jobDate);


            }

            @Override
            public void setItem(ManageVO manageVO) {

                jobName.setText(manageVO.getJobName());
                jobPay.setText(Base.decimalFormat(manageVO.getJobPay()));
                jobDate.setText(String.format("%s - %s", manageVO.getJobStartDate(), manageVO.getJobEndDate()));
            }
        }

}
