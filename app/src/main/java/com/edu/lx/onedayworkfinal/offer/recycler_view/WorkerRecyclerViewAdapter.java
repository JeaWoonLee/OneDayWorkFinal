package com.edu.lx.onedayworkfinal.offer.recycler_view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.offer.SeekerInfoPopupActivity;
import com.edu.lx.onedayworkfinal.offer.manage_commute.OfferManageCommuteDetailActivity;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseViewHolder;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.JobCandidateVO;

import java.util.HashMap;
import java.util.Map;

public class WorkerRecyclerViewAdapter extends BaseRecyclerViewAdapter<JobCandidateVO> {

    public WorkerRecyclerViewAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(inflateView(context, R.layout.worker_recycler_item,viewGroup));
    }

    class ViewHolder extends BaseViewHolder<JobCandidateVO>{
        TextView jobName;
        TextView seekerName;
        Button checkButton;
        Button showInfoButton;
        CardView cardView;
        JobCandidateVO vo;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            jobName = itemView.findViewById(R.id.jobName);
            seekerName = itemView.findViewById(R.id.seekerName);
            checkButton = itemView.findViewById(R.id.checkButton);
            showInfoButton = itemView.findViewById(R.id.showInfoButton);
            cardView = itemView.findViewById(R.id.cardView);
        }

        @Override
        public void setItem(JobCandidateVO jobCandidateVO) {
            //근무 상태에 따라 색 변경
            vo = jobCandidateVO;
            int candidateStatus = jobCandidateVO.getCandidateStatus();
            setItemUI(candidateStatus);

            jobName.setText(jobCandidateVO.getJobName());
            seekerName.setText(jobCandidateVO.getSeekerName());
            checkButton.setOnClickListener(v -> changeCandidateStatus(jobCandidateVO.getCandidateNumber(),candidateStatus));

            showInfoButton.setOnClickListener(v -> showSeekerInfo(jobCandidateVO.getSeekerId()));


        }

        /**
         * showSeekerInfo
         * @param seekerId 해당 아이템의 구직자 정보
         *                 구직자의 정보를 팝업으로 보여준다
         */
        private void showSeekerInfo(String seekerId) {
            Intent intent = new Intent(context,SeekerInfoPopupActivity.class);
            intent.putExtra("seekerId",seekerId);
            context.startActivity(intent);
        }

        /**
         * changeCandidateStatus
         * @param candidateNumber 해당 키값의 상태를 변경한다
         * @param candidateStatus 상태에 따라 다른 요청을 보낸다
         *                        1. 미출근 -> 결근 / 2. 출근 -> 근무중 / 3. 근무중 -> 퇴근 / 4. 퇴근 -> 근무자 평가
         *                        무단이탈은 다른 버튼에서 처리한다
         */
        private void changeCandidateStatus(int candidateNumber, int candidateStatus) {
            String requestStr = "";
            switch (candidateStatus){
                case 1:
                    requestStr =  "requestAbsentByCandidateNumber.do";
                    break;
                case 2:
                    requestStr = "requestWorkingByCandidateNumber.do";
                    break;
                case 3:
                    requestStr = "requestOffWorkByCandidateNumber.do";
                    break;
                case 4:
                    evaluationSeeker(candidateNumber);
                    return;
            }

            String url = context.getResources().getString(R.string.url) + requestStr;
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    this::processChangeStatusResponse,
                    error -> {

                    }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String,String> params = new HashMap<>();
                    params.put("candidateNumber",String.valueOf(candidateNumber));
                    params.put("candidateStatus",String.valueOf(candidateStatus));
                    return params;
                }
            };
            request.setShouldCache(false);
            Base.requestQueue.add(request);

        }

        private void evaluationSeeker(int candidateNumber) {
            OfferManageCommuteDetailActivity activity = (OfferManageCommuteDetailActivity) context;
            CardView cardView = (CardView) View.inflate(activity,R.layout.offer_evaluate_seeker_layout,null);
            RatingBar ratingBar = cardView.findViewById(R.id.ratingBar1);
            final float[] rate = new float[1];
            ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> rate[0] = rating);
            new AlertDialog.Builder(activity)
                    .setTitle("근로자 평가")
                    .setIcon(R.drawable.evaluate)
                    .setView(cardView)
                    .setPositiveButton("평가하기", (dialog, which) -> {
                        EditText editText = cardView.findViewById(R.id.editText);
                        String score = Float.toString(rate[0]);
                        String evaluate = editText.getText().toString();
                        requestEvaluate(candidateNumber,score,evaluate);
                    })
                    .setNegativeButton("취소", (dialog, which) -> Snackbar.make(activity.getWindow().getDecorView().getRootView(),"평가가 취소되었습니다",Snackbar.LENGTH_LONG).show())
                    .show();
        }

        /**
         * requestEvaluate
         * 구직자의 별점 평가입력
         * @param candidateNumber 평가 대상 구직자
         * @param score 평가 점수
         * @param evaluate 평가 내용
         */
        private void requestEvaluate(int candidateNumber, String score, String evaluate) {
            String url = context.getResources().getString(R.string.url) + "requestEvaluate.do";
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    this::onEvaluateResponse,
                    error -> {

                    }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String,String> params = new HashMap<>();
                    params.put("candidateNumber",String.valueOf(candidateNumber));
                    params.put("score",score);
                    params.put("evaluate",evaluate);
                    return  params;
                }
            };
            request.setShouldCache(false);
            Base.requestQueue.add(request);
        }

        /**
         * onEvaluateResponse
         * 평과 결과 처리
         * @param response update 결과 0 또는 1
         */
        private void onEvaluateResponse(String response) {
            OfferManageCommuteDetailActivity activity = (OfferManageCommuteDetailActivity) context;
            int updateResponse = Integer.parseInt(response);
            switch (updateResponse){
                case 0:
                    Snackbar.make(activity.getWindow().getDecorView().getRootView(),"근무자 평가에 실패하였습니다",Snackbar.LENGTH_LONG).show();
                    break;
                case 1:
                    Snackbar.make(activity.getWindow().getDecorView().getRootView(),"근무자 평가가 완료되었습니다",Snackbar.LENGTH_LONG).show();
                    activity.requestProjectCommuteInfo(activity.projectNumber);
                    break;
            }
        }

        /**
         * processChangeStatusResponse
         * @param response update 결과(0 또는 1) 을 리턴하므로 그에 맞는 스낵바를 띄운다
         */
        private void processChangeStatusResponse(String response) {
            int updateResult = Integer.parseInt(response);
            OfferManageCommuteDetailActivity activity = (OfferManageCommuteDetailActivity) context;
            switch (updateResult){
                case 0:
                    Snackbar.make(activity.getWindow().getDecorView().getRootView(),"서버에서 상태정보를 변경하는데 실패했습니다.",Snackbar.LENGTH_LONG).show();
                    break;
                case 1:
                    Snackbar.make(activity.getWindow().getDecorView().getRootView(),"해당 요청이 처리되었습니다.",Snackbar.LENGTH_LONG).show();
                    activity.requestProjectCommuteInfo(activity.projectNumber);
                    break;
            }
        }

        /**
         * setItemUI
         * @param candidateStatus 근무 상태에 따라 색깔을 변경한다
         */
        private void setItemUI(int candidateStatus) {
            Resources res = context.getResources();
            switch (candidateStatus) {
                case 1:
                    //미출근 : 배경 회색 / 글자 검은색 / 버튼 danger
                    cardView.setCardBackgroundColor(res.getColor(R.color.dark_gray,context.getTheme()));
                    checkButton.setBackgroundColor(res.getColor(R.color.danger,context.getTheme()));
                    checkButton.setText("결근");
//                    jobName.setTextColor(res.getColor(R.color.black,context.getTheme()));
//                    seekerName.setTextColor(res.getColor(R.color.black,context.getTheme()));
                    break;
                case 2:
                    //출근 : 배경 seeker / 글자 검은색 / 버튼 offer
                    cardView.setCardBackgroundColor(res.getColor(R.color.seeker,context.getTheme()));
                    checkButton.setBackgroundColor(res.getColor(R.color.offer,context.getTheme()));
                    checkButton.setText("출근확인");
//                    jobName.setTextColor(res.getColor(R.color.black,context.getTheme()));
//                    seekerName.setTextColor(res.getColor(R.color.black,context.getTheme()));
                    break;
                case 3:
                    //근무중 : 배경 offer / 글자 하얀색 / 버튼 blue1 / 버튼 2 danger
                    cardView.setCardBackgroundColor(res.getColor(R.color.offer,context.getTheme()));
                    checkButton.setBackgroundColor(res.getColor(R.color.blue1,context.getTheme()));
                    checkButton.setText("퇴근");
//                    jobName.setTextColor(res.getColor(R.color.black,context.getTheme()));
//                    seekerName.setTextColor(res.getColor(R.color.black,context.getTheme()));
                    break;
                case 4:
                    //퇴근 : 배경 blue1 / 글자 black / 버튼 seeker
                    cardView.setCardBackgroundColor(res.getColor(R.color.blue1,context.getTheme()));
                    checkButton.setBackgroundColor(res.getColor(R.color.seeker,context.getTheme()));
                    checkButton.setText("평가");
//                    jobName.setTextColor(res.getColor(R.color.black,context.getTheme()));
//                    seekerName.setTextColor(res.getColor(R.color.black,context.getTheme()));
                    if (vo.getEvaluate() != null) {
                        checkButton.setVisibility(View.INVISIBLE);
                    }
                    break;
                case 5:
                    //무단이탈 : 배경 danger / 글자 하얀색 / 버튼없음
                    cardView.setCardBackgroundColor(res.getColor(R.color.danger,context.getTheme()));
                    checkButton.setVisibility(View.GONE);
//                    jobName.setTextColor(res.getColor(R.color.black,context.getTheme()));
//                    seekerName.setTextColor(res.getColor(R.color.black,context.getTheme()));
                    break;
                case 6:
                    //결근 : 배경 danger / 글자 하얀색 / 버튼없음
                    cardView.setCardBackgroundColor(res.getColor(R.color.danger,context.getTheme()));
                    checkButton.setVisibility(View.GONE);
//                    jobName.setTextColor(res.getColor(R.color.black,context.getTheme()));
//                    seekerName.setTextColor(res.getColor(R.color.black,context.getTheme()));
                    break;
            }

        }


    }



}
