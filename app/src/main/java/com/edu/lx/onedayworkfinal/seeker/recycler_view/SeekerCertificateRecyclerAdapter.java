package com.edu.lx.onedayworkfinal.seeker.recycler_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.recycler_view.BaseViewHolder;
import com.edu.lx.onedayworkfinal.vo.CertificationVO;

public class SeekerCertificateRecyclerAdapter extends BaseRecyclerViewAdapter<CertificationVO> {

    public SeekerCertificateRecyclerAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(inflateView(context, R.layout.seeker_certificate_item,viewGroup));
    }

    public class ViewHolder extends BaseViewHolder<CertificationVO>{
        TextView certificateName;
        TextView certificateNumber;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            certificateName = itemView.findViewById(R.id.certificateName);
            certificateNumber = itemView.findViewById(R.id.certificateNumber);
        }

        @Override
        public void setItem(CertificationVO certificationVO) {
            certificateName.setText(certificationVO.getCertificateName());
            certificateNumber.setText(certificationVO.getSeekerCertificateNumber());
        }
    }
}
