package com.edu.lx.onedayworkfinal.offer.recycler_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.request.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.offer.manage_work.OfferManageCandidateActivity;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseViewHolder;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.JobCandidateVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        //TextView recruitmentRate;
        RecyclerView recyclerView;
        JobCandidateVO jobCandidateVO;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            targetDate = itemView.findViewById(R.id.targetDate);
            //recruitmentRate = itemView.findViewById(R.id.recruitmentRate);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            LinearLayoutManager manager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(manager);

        }

        @Override
        public void setItem(JobCandidateVO jobCandidateVO) {
            this.jobCandidateVO = jobCandidateVO;
            Log.d("setItem",jobCandidateVO.toString());
            targetDate.setText(jobCandidateVO.getTargetDate());
            //String recruit = "구인현황 ( " + jobCandidateVO.getRecruit() + " / " + jobCandidateVO.getJobLimitCount() + " )";
            //불러 온 결과 모집 정원을 추가 한 경우 남은 인원을 강제로 취소시킨다
            Log.d("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!",jobCandidateVO.getRecruit()+ " , " + jobCandidateVO.getTotal());
//            if (jobCandidateVO.getTotal() >= jobCandidateVO.getJobLimitCount()) {
//                requestAllCancel(jobCandidateVO.getTargetDate(),jobCandidateVO.getJobNumber());
//                return;
//            }
            //recruitmentRate.setText(recruit);

            if (context instanceof  OfferManageCandidateActivity){
                OfferManageCandidateActivity activity = (OfferManageCandidateActivity) context;
                List<JobCandidateVO> candidateList = activity.map.get(jobCandidateVO.getTargetDate());
                ArrayList<JobCandidateVO> sortedList = new ArrayList<>();
                ArrayList<String> nameList = new ArrayList<>();
                for (JobCandidateVO item : candidateList) {
                    if (!nameList.contains(item.getSeekerId())){
                        sortedList.add(item);
                        nameList.add(item.getSeekerId());
                    }

                }

                OfferManageCandidateRecyclerViewAdapter adapter = new OfferManageCandidateRecyclerViewAdapter(activity);
                adapter.setItems(sortedList);
                recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(context,"신청관리의 액티비티정보를 가져올 수 없습니다!",Toast.LENGTH_LONG).show();
            }
        }

        private void requestAllCancel(String targetDate, int jobNumber) {
            String url = context.getResources().getString(R.string.url) + "requestAllCancel.do";
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    this::processRequestAllCancelResponse,
                    error -> {

                    }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String,String> params = new HashMap<>();
                    params.put("targetDate",targetDate);
                    params.put("jobNumber",String.valueOf(jobNumber));
                    return params;
                }
            };
            request.setShouldCache(false);
            Base.requestQueue.add(request);
        }

        private void processRequestAllCancelResponse(String response) {
            int cancelResult = Integer.parseInt(response);
            if (context instanceof OfferManageCandidateActivity){
                OfferManageCandidateActivity activity = (OfferManageCandidateActivity) context;
                Snackbar.make(activity.getWindow().getDecorView().getRootView(),"모집 정원이 가득 차서 " + cancelResult + "건의 신청이 자동으로 취소되었습니다",Snackbar.LENGTH_LONG).show();
                activity.requestCandidateListByJobNumber(jobCandidateVO.getJobNumber());
            }

        }

    }


}
