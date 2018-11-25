package com.edu.lx.onedayworkfinal.offer.recycler_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.offer.manage_work.OfferManageCandidateActivity;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseViewHolder;
import com.edu.lx.onedayworkfinal.vo.JobCandidateVO;

import java.util.ArrayList;
import java.util.List;

public class OfferManageCandidateTargetDateRecyclerViewAdapter extends BaseRecyclerViewAdapter<JobCandidateVO> {

    public OfferManageCandidateTargetDateRecyclerViewAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(inflateView(context,R.layout.offer_manage_candidate_date_item,viewGroup));
    }

    class ViewHolder extends BaseViewHolder<JobCandidateVO>{
        TextView targetDate;
        TextView recruitmentRate;
        RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            targetDate = itemView.findViewById(R.id.targetDate);
            recruitmentRate = itemView.findViewById(R.id.recruitmentRate);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            LinearLayoutManager manager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(manager);

        }

        @Override
        public void setItem(JobCandidateVO jobCandidateVO) {
            Log.d("setItem",jobCandidateVO.toString());
            targetDate.setText(jobCandidateVO.getTargetDate());
            String recruit = "구인현황 ( " + jobCandidateVO.getRecruit() + " / " + jobCandidateVO.getJobLimitCount() + " )";
            //불러 온 결과 모집 정원을 추가 한 경우 남은 인원을 강제로 취소시킨다
            if (jobCandidateVO.getRecruit() >= jobCandidateVO.getJobLimitCount()) {

            }
            recruitmentRate.setText(recruit);

            if (context instanceof  OfferManageCandidateActivity){
                OfferManageCandidateActivity activity = (OfferManageCandidateActivity) context;
                List<JobCandidateVO> candidateList = activity.map.get(jobCandidateVO.getTargetDate());

                OfferManageCandidateRecyclerViewAdapter adapter = new OfferManageCandidateRecyclerViewAdapter(activity);
                adapter.setItems((ArrayList<JobCandidateVO>) candidateList);
                recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(context,"신청관리의 액티비티정보를 가져올 수 없습니다!",Toast.LENGTH_LONG).show();
            }
        }
    }
}
