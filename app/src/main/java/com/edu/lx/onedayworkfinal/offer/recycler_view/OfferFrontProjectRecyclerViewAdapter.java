package com.edu.lx.onedayworkfinal.offer.recycler_view;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.request.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.offer.OfferMainActivity;
import com.edu.lx.onedayworkfinal.offer.manage_commute.OfferManageCommuteDetailActivity;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseViewHolder;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.OfferWorkVO;

import java.util.HashMap;
import java.util.Map;

public class OfferFrontProjectRecyclerViewAdapter extends BaseRecyclerViewAdapter<OfferWorkVO> {


    public OfferFrontProjectRecyclerViewAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(inflateView(context, R.layout.offer_project_front_item,viewGroup));
    }

    class ViewHolder extends BaseViewHolder<OfferWorkVO> {

        //프로젝트 이름
        TextView projectName;
        //모집률
        TextView recruitmentRate;
        //근무 시간
        TextView workTime;
        //출석률
        TextView attendanceRate;
        //바로가기 버튼
        LinearLayout showCommuteButton;

        OfferWorkVO offerWorkVO;

        View view;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            projectName = itemView.findViewById(R.id.projectName);
            recruitmentRate = itemView.findViewById(R.id.recruitmentRate);
            workTime = itemView.findViewById(R.id.workTime);
            attendanceRate = itemView.findViewById(R.id.attendanceRate);
            showCommuteButton = itemView.findViewById(R.id.showCommuteButton);
            showCommuteButton.setOnClickListener(v -> {
                OfferMainActivity activity = (OfferMainActivity) context;
                Intent intent = new Intent(activity,OfferManageCommuteDetailActivity.class);
                intent.putExtra("projectNumber",offerWorkVO.getProjectNumber());
                intent.putExtra("projectName",offerWorkVO.getProjectName());
                activity.startActivityForResult(intent,101);
            });
        }

        @Override
        public void setItem(OfferWorkVO offerWorkVO) {
            getProjectCommuteInfo(offerWorkVO.getProjectNumber());
        }

        private void getProjectCommuteInfo(int projectNumber) {
            String url = context.getResources().getString(R.string.url) + "getProjectCommuteInfo.do";
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    this::processProjectCommuteInfo,
                    error -> {

                    }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String,String> params = new HashMap<>();
                    params.put("projectNumber",String.valueOf(projectNumber));
                    return params;
                }
            };
            request.setShouldCache(false);
            Base.requestQueue.add(request);
        }

        private void processProjectCommuteInfo(String response) {
            OfferWorkVO vo = Base.gson.fromJson(response,OfferWorkVO.class);
            this.offerWorkVO = vo;
            if (vo != null) {
                projectName.setText(vo.getProjectName());
                String workTimeStr = vo.getWorkStartTime() + " ~ " + vo.getWorkEndTime();
                workTime.setText(workTimeStr);

                String recruitmentRateStr = "모집률 : ( "+vo.getRecruit() + " / " + vo.getTotal() +" )";
                recruitmentRate.setText(recruitmentRateStr);

                String attendanceRateStr = "출석률 : ( "+ vo.getCommute()+" / " + vo.getRecruit() + " )";
                attendanceRate.setText(attendanceRateStr);
            } else {
                view.setVisibility(View.GONE);
            }

        }

    }

}
