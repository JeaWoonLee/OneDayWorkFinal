package com.edu.lx.onedayworkfinal.offer.recycler_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseViewHolder;
import com.edu.lx.onedayworkfinal.vo.JobCandidateVO;

public class SeekerRecordRecyclerViewAdapter extends BaseRecyclerViewAdapter<JobCandidateVO> {

    public SeekerRecordRecyclerViewAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(inflateView(context, R.layout.seeker_record_item,viewGroup));
    }

    class ViewHolder extends BaseViewHolder<JobCandidateVO> {
        TextView jobName;
        TextView targetDate;
        TextView score;
        TextView candidateStatusView;
        CardView cardView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            jobName = itemView.findViewById(R.id.jobName);
            targetDate = itemView.findViewById(R.id.targetDate);
            score = itemView.findViewById(R.id.score);
            candidateStatusView = itemView.findViewById(R.id.candidateStatus);
            cardView = itemView.findViewById(R.id.cardView);
        }

        @Override
        public void setItem(JobCandidateVO jobCandidateVO) {
            jobName.setText(jobCandidateVO.getJobName());
            targetDate.setText(jobCandidateVO.getTargetDate());
            score.setText(jobCandidateVO.getScore());

            int candidateStatus = jobCandidateVO.getCandidateStatus();
            switch (candidateStatus) {
                case 4:
                    //퇴근인 경우
                    cardView.setCardBackgroundColor(context.getResources().getColor(R.color.blue1,context.getTheme()));
                    candidateStatusView.setText("퇴근");
                    break;
                case 5:
                    //무단 이탈인 경우
                    cardView.setCardBackgroundColor(context.getResources().getColor(R.color.danger,context.getTheme()));
                    candidateStatusView.setText("무단 이탈");
                    break;
                case 6:
                    //결근인 경우
                    cardView.setCardBackgroundColor(context.getResources().getColor(R.color.danger,context.getTheme()));
                    candidateStatusView.setText("결근");
                    break;
            }
        }
    }
}
