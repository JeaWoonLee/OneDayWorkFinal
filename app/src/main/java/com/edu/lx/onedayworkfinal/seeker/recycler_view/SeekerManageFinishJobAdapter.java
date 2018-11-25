package com.edu.lx.onedayworkfinal.seeker.recycler_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
            TextView candidateStatus;
            LinearLayout layoutHeader;

            SeekerManageFinishListViewHolder(View itemView) {
                super(itemView);
                jobName = itemView.findViewById(R.id.jobName);
                jobPay = itemView.findViewById(R.id.jobPay);
                jobDate = itemView.findViewById(R.id.jobDate);
                candidateStatus = itemView.findViewById(R.id.candidateStatus);
                layoutHeader = itemView.findViewById(R.id.layoutHeader);
            }

            @Override
            public void setItem(ManageVO manageVO) {

                jobName.setText(manageVO.getJobName());
                jobPay.setText(Base.decimalFormat(manageVO.getJobPay()));
                jobDate.setText(manageVO.getTargetDate());

                switch (Integer.parseInt(manageVO.getCandidateStatus())){
                    case 4:
                        layoutHeader.setBackgroundColor(context.getResources().getColor(R.color.blue1,context.getTheme()));
                        candidateStatus.setText("퇴근");
                        candidateStatus.setTextColor(context.getResources().getColor(R.color.blue1,context.getTheme()));
                        break;
                    case 5:
                        layoutHeader.setBackgroundColor(context.getResources().getColor(R.color.danger,context.getTheme()));
                        candidateStatus.setText("무단이탈");
                        candidateStatus.setTextColor(context.getResources().getColor(R.color.danger,context.getTheme()));
                        break;
                    case 6:
                        layoutHeader.setBackgroundColor(context.getResources().getColor(R.color.danger,context.getTheme()));
                        candidateStatus.setText("결근");
                        candidateStatus.setTextColor(context.getResources().getColor(R.color.danger,context.getTheme()));
                        break;
                }


            }
        }

}
