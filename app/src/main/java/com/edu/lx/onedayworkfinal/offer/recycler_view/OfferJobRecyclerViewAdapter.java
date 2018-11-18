package com.edu.lx.onedayworkfinal.offer.recycler_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseViewHolder;
import com.edu.lx.onedayworkfinal.vo.JobVO;

public class OfferJobRecyclerViewAdapter extends BaseRecyclerViewAdapter<JobVO> {

    public OfferJobRecyclerViewAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(inflateView(context, R.layout.offer_job_item,viewGroup));
    }

    class ViewHolder extends BaseViewHolder<JobVO> {
        TextView jobName;
        TextView jobLimitCount;
        TextView jobDate;
        TextView jobRequirement;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            jobName = itemView.findViewById(R.id.jobName);
            jobLimitCount = itemView.findViewById(R.id.jobLimitCount);
            jobDate = itemView.findViewById(R.id.jobDate);
            jobRequirement = itemView.findViewById(R.id.jobRequirement);
        }

        @Override
        public void setItem(JobVO jobVO) {
            jobName.setText(jobVO.getJobName());
            jobLimitCount.setText(String.valueOf(jobVO.getJobLimitCount()));
            String date = jobVO.getJobStartDate() + " ~ " + jobVO.getJobEndDate();
            jobDate.setText(date);
            jobRequirement.setText(jobVO.getJobRequirement());
        }
    }
}
