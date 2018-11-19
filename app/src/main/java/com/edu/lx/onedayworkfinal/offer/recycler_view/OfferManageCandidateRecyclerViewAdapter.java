package com.edu.lx.onedayworkfinal.offer.recycler_view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.offer.SeekerInfoPopupActivity;
import com.edu.lx.onedayworkfinal.offer.manage_work.OfferManageCandidateActivity;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseViewHolder;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.JobCandidateVO;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class OfferManageCandidateRecyclerViewAdapter extends BaseRecyclerViewAdapter<JobCandidateVO> {

    OfferManageCandidateRecyclerViewAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(inflateView(context,R.layout.offer_manage_candidate_item,viewGroup));
    }

    class ViewHolder extends BaseViewHolder<JobCandidateVO> {
        TextView seekerId;
        TextView seekerSex;
        TextView seekerAge;
        TextView reliabilityView;

        LinearLayout seekerInfoLayout;
        LinearLayout acceptCandidateButton;

        JobCandidateVO vo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            seekerId = itemView.findViewById(R.id.seekerId);
            seekerSex = itemView.findViewById(R.id.seekerSex);
            seekerAge = itemView.findViewById(R.id.seekerAge);
            reliabilityView = itemView.findViewById(R.id.reliability);

            seekerInfoLayout = itemView.findViewById(R.id.seekerInfoLayout);
            acceptCandidateButton = itemView.findViewById(R.id.acceptCandidateButton);
        }

        @Override
        public void setItem(JobCandidateVO jobCandidateVO) {
            vo = jobCandidateVO;
            if (jobCandidateVO != null){
                //ID 설정
                seekerId.setText(jobCandidateVO.getSeekerId());
                //성별 성정
                seekerSex.setText(jobCandidateVO.getSeekerSex());
                //나이 설정
                Date date = Date.valueOf(jobCandidateVO.getSeekerBirth());
                Calendar today = Calendar.getInstance();
                Calendar seekerCalendar = Calendar.getInstance();
                seekerCalendar.setTime(date);
                int todayYear = today.get(Calendar.YEAR);
                int seekerYear = seekerCalendar.get(Calendar.YEAR);
                int old = todayYear - seekerYear + 1;
                seekerAge.setText(String.valueOf(old));

                //신뢰도 설정
                int workOffCount = jobCandidateVO.getOffWork();
                int total = jobCandidateVO.getTotal();
                if (total != 0) {
                    int reliability = (workOffCount/total) * 100;
                    String reliabilityStr = reliability + "% ("+workOffCount + "/" + total + ")";
                    if (reliability < 80) {
                        reliabilityView.setTextColor(context.getResources().getColor(R.color.danger,context.getTheme()));
                    } else if (reliability < 90){
                        reliabilityView.setTextColor(context.getResources().getColor(R.color.black,context.getTheme()));
                    } else  {
                        reliabilityView.setTextColor(context.getResources().getColor(R.color.blue1,context.getTheme()));
                    }
                    reliabilityView.setText(reliabilityStr);
                } else {
                    reliabilityView.setTextColor(context.getResources().getColor(R.color.dark_gray,context.getTheme()));
                    reliabilityView.setText("이력없음");
                }
                //end 신뢰도 설정

                //구직자 상세정보 보기 설정
                seekerInfoLayout.setOnClickListener(v -> showSeekerInfo(vo.getSeekerId()));
                //end 구직자 상세정보 보기 설정

                //신청 수락 설정
                acceptCandidateButton.setOnClickListener(v -> requestAcceptCandidateByCandidateNumber(vo.getCandidateNumber()));
            }
        }

        private void requestAcceptCandidateByCandidateNumber(int candidateNumber) {
            String url = context.getResources().getString(R.string.url) + "requestAcceptCandidateByCandidateNumber.do";
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    this::processAcceptCandidateResponse,
                    error -> {

                    }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String,String> params = new HashMap<>();
                    params.put("candidateNumber",String.valueOf(candidateNumber));
                    return params;
                }
            };
            request.setShouldCache(false);
            Base.requestQueue.add(request);
        }

        private void processAcceptCandidateResponse(String response) {
            int updateResult = Integer.parseInt(response);
            switch (updateResult) {
                case 0:
                    Toast.makeText(context,"신청 수락 요청에 실패하였습니다.",Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    if (context instanceof OfferManageCandidateActivity){
                        OfferManageCandidateActivity activity = (OfferManageCandidateActivity) context;
                        Snackbar.make(activity.getWindow().getDecorView().getRootView(),"성공적으로 수락되었습니다.",Snackbar.LENGTH_LONG).show();
                        activity.requestCandidateListByJobNumber(vo.getJobNumber());
                    } else {
                        Toast.makeText(context,"신청관리 액티비티 정보를 불러오는데 실패하였습니다",Toast.LENGTH_LONG).show();
                    }
                    break;
            }
        }

        private void showSeekerInfo(String seekerId) {
            Intent intent = new Intent(context,SeekerInfoPopupActivity.class);
            intent.putExtra("seekerId",seekerId);
            context.startActivity(intent);
        }
    }
}
