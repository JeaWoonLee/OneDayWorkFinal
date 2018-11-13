package com.edu.lx.onedayworkfinal.offer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.seeker.SeekerMainActivity;

public class AnotherFrontFragment extends Fragment {

    OfferMainActivity activity;

    LinearLayout work_manage;
    LinearLayout rec_labor;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        activity = (OfferMainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.offer_front_fragment,container,false);

        work_manage = rootView.findViewById(R.id.work_manage);
        rec_labor = rootView.findViewById(R.id.rec_labor);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        work_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.changeFragment(OfferMainActivity.WORK_MANAGE_FRAGMENT);
            }
        });

        rec_labor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.changeFragment(OfferMainActivity.REC_LABOR_FRAGMENT);
            }
        });
    }
}
