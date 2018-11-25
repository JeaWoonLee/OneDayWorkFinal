package com.edu.lx.onedayworkfinal.offer.recycler_view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.offer.SeekerInfoPopupActivity;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseViewHolder;
import com.edu.lx.onedayworkfinal.vo.ManageHumanResourceModel;

import java.sql.Date;
import java.util.Calendar;

class HumanResourceRecyclerViewAdapter extends BaseRecyclerViewAdapter<ManageHumanResourceModel> {

    public HumanResourceRecyclerViewAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(inflateView(context, R.layout.offer_manage_human_recource_item,viewGroup));
    }

    class ViewHolder extends BaseViewHolder<ManageHumanResourceModel>{
        TextView seekerId;
        TextView seekerAge;
        TextView seekerSex;
        TextView reliabilityView;
        LinearLayout showSeekerDetailButtonLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            seekerId = itemView.findViewById(R.id.seekerId);
            seekerAge = itemView.findViewById(R.id.seekerAge);
            seekerSex = itemView.findViewById(R.id.seekerSex);
            reliabilityView = itemView.findViewById(R.id.reliability);
            showSeekerDetailButtonLayout = itemView.findViewById(R.id.showSeekerDetailButtonLayout);
        }

        @Override
        public void setItem(ManageHumanResourceModel manageHumanResourceModel) {
            seekerId.setText(manageHumanResourceModel.getSeekerId());
            seekerSex.setText(manageHumanResourceModel.getSeekerSex());

            //나이 설정
            Date date = Date.valueOf(manageHumanResourceModel.getSeekerBirth());
            Calendar today = Calendar.getInstance();
            Calendar seekerCalendar = Calendar.getInstance();
            seekerCalendar.setTime(date);
            int todayYear = today.get(Calendar.YEAR);
            int seekerYear = seekerCalendar.get(Calendar.YEAR);
            int old = todayYear - seekerYear + 1;
            seekerAge.setText(String.valueOf(old));

            //신뢰도 설정
            double workOffCount = manageHumanResourceModel.getOffWork();
            double total = manageHumanResourceModel.getTotal();
            if (total != 0) {
                int reliability = (int) ((workOffCount/total) * 100);
                String reliabilityStr = reliability + "% ("+(int)workOffCount + "/" + (int)total + ")";
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
            showSeekerDetailButtonLayout.setOnClickListener(v -> showSeekerInfo(manageHumanResourceModel.getSeekerId()));
            //end 구직자 상세정보 보기 설정
        }

        private void showSeekerInfo(String seekerId) {
            Intent intent = new Intent(context,SeekerInfoPopupActivity.class);
            intent.putExtra("seekerId",seekerId);
            context.startActivity(intent);
        }
    }
}
