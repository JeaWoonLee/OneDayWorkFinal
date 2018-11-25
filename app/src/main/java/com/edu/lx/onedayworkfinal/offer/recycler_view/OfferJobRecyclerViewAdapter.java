package com.edu.lx.onedayworkfinal.offer.recycler_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.offer.manage_work.OfferManageWorkActivity;
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

        LinearLayout manageCandidateButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            jobName = itemView.findViewById(R.id.jobName);
            jobLimitCount = itemView.findViewById(R.id.jobLimitCount);
            jobDate = itemView.findViewById(R.id.jobDate);
            jobRequirement = itemView.findViewById(R.id.jobRequirement);
            manageCandidateButton = itemView.findViewById(R.id.manageCandidateButton);
        }

        @Override
        public void setItem(JobVO jobVO) {
            jobName.setText(jobVO.getJobName());
            jobLimitCount.setText(String.valueOf(jobVO.getJobLimitCount()));
            String date = jobVO.getJobStartDate() + " ~ " + jobVO.getJobEndDate();
            jobDate.setText(date);
            String requirement = jobVO.getJobRequirement();
            if (requirement != null) {
                if (requirement.length() == 0) {
                    requirement = "없음";
                }
            }
            jobRequirement.setText(requirement);
            manageCandidateButton.setOnClickListener(v -> {
                if (context instanceof  OfferManageWorkActivity) {
                    OfferManageWorkActivity activity = (OfferManageWorkActivity) context;
                    activity.showManageCandidateActivity(jobVO.getJobNumber());

                } else {
                    Toast.makeText(context,"해당 일감관리 액티비티 정보를 불러올 수 없습니다!",Toast.LENGTH_LONG).show();
                }

            });

        }
    }
}
