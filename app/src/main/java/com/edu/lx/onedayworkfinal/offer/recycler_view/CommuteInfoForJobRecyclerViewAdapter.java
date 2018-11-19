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

public class CommuteInfoForJobRecyclerViewAdapter extends BaseRecyclerViewAdapter<JobVO> {
    public CommuteInfoForJobRecyclerViewAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(inflateView(context, R.layout.offer_commute_job_item,viewGroup));
    }

    class ViewHolder extends BaseViewHolder<JobVO> {

        TextView jobName;
        TextView jobRecruitmentRate;
        TextView jobAttendanceRate;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            jobName = itemView.findViewById(R.id.jobName);
            jobRecruitmentRate = itemView.findViewById(R.id.jobRecruitmentRate);
            jobAttendanceRate = itemView.findViewById(R.id.jobAttendanceRate);

        }

        @Override
        public void setItem(JobVO jobVO) {
            jobName.setText(jobVO.getJobName());
            //구인된 인원(Recruit) 은 jobPay 에서 꺼내온다
            int recruit = jobVO.getJobPay();
            //출근 인원(Attendance) 을 jobNumber 에서 꺼낸다
            int attendance = jobVO.getJobNumber();
            String jobRecruitmentRateStr = "( " + recruit + " / " + jobVO.getJobLimitCount() + " )";
            jobRecruitmentRate.setText(jobRecruitmentRateStr);

            String jobAttendanceRateStr = "( " + attendance + " / " + recruit + " )";
            jobAttendanceRate.setText(jobAttendanceRateStr);
        }
    }
}
