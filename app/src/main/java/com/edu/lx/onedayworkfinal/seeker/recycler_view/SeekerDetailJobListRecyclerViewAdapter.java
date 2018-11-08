package com.edu.lx.onedayworkfinal.seeker.recycler_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.seeker.ProjectDetailActivity;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseViewHolder;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.JobVO;

public class SeekerDetailJobListRecyclerViewAdapter extends BaseRecyclerViewAdapter<JobVO> {

    public SeekerDetailJobListRecyclerViewAdapter(Context context) {
        super(context);
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i) {
        return new SeekerProjectListViewHolder(inflateView(super.context, R.layout.seeker_detail_job_item,viewGroup));
    }

    class SeekerProjectListViewHolder extends BaseViewHolder<JobVO> {
        TextView jobName;
        TextView jobPay;
        TextView jobDate;

        Button candidateButton;

        public SeekerProjectListViewHolder (@NonNull View itemView) {
            super(itemView);
            jobName = itemView.findViewById(R.id.jobName);
            jobPay = itemView.findViewById(R.id.jobPay);
            jobDate = itemView.findViewById(R.id.jobDate);

            candidateButton = itemView.findViewById(R.id.candidateButton);
        }

        @Override
        public void setItem (final JobVO item) {

            jobName.setText(item.getJobName());
            //1000 단위 숫자로 콤마를 찍어서 보여준다
            jobPay.setText(Base.decimalFormat(item.getJobPay()));
            jobDate.setText(item.getJobStartDate() + " - " + item.getJobEndDate());

            candidateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (context instanceof  ProjectDetailActivity){
                        ProjectDetailActivity activity = (ProjectDetailActivity) context;
                        activity.showCandidate(item.getJobNumber());
                    }

                }
            });
        }

    }

}
