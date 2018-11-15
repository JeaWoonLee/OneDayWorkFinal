package com.edu.lx.onedayworkfinal.seeker.recycler_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.seeker.SeekerMainActivity;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseViewHolder;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.ManageVO;

//recycler view 해제
public class SeekerManageProjectRecyclerViewAdapter extends BaseRecyclerViewAdapter<ManageVO> {

    public SeekerManageProjectRecyclerViewAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SeekerManageProjectViewHolder(inflateView(super.context, R.layout.seeker_manage_project_item, viewGroup));
    }

    class SeekerManageProjectViewHolder extends BaseViewHolder<ManageVO> {

        TextView projectName;
        TextView projectSubject;
        TextView targetDate;
        TextView projectNumber;
        TextView candidateNumber;
        TextView job_name;
        TextView job_pay;

        SeekerManageProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                String candidateNum = candidateNumber.getText().toString();
                SeekerMainActivity activity = (SeekerMainActivity) context;
                activity.showProjectDetailManage(Integer.parseInt(candidateNum));
            });
            projectNumber = itemView.findViewById(R.id.projectNumber);
            projectName = itemView.findViewById(R.id.projectName);
            job_name = itemView.findViewById(R.id.job_name);
            job_pay = itemView.findViewById(R.id.job_pay);
            targetDate = itemView.findViewById(R.id.targetDate);
            projectSubject = itemView.findViewById(R.id.projectSubject);
            candidateNumber = itemView.findViewById(R.id.candidateNumber);

        }

        @Override
        public void setItem(ManageVO manageVO) {

            if (manageVO != null) {
                Log.d("manageVO", manageVO.toString());
                projectName.setText(manageVO.getProjectName());
                projectSubject.setText(manageVO.getProjectSubject());
                targetDate.setText(manageVO.getTargetDate());
                projectNumber.setText(String.valueOf(manageVO.getProjectNumber()));
                candidateNumber.setText(String.valueOf(manageVO.getCandidateNumber()));

                String pay = Base.decimalFormat(manageVO.getJobPay()) + "원";
                job_pay.setText(pay);
                job_name.setText(String.valueOf(manageVO.getJobName()));

            } else {
                Log.d(this.getClass().getSimpleName(), "item 이 null 입니다.");
            }
        }

    }

}

