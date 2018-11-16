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

public class OfferFrontFragment extends Fragment {

    OfferMainActivity activity;

    LinearLayout manage_work;
    LinearLayout manage_commute;
    LinearLayout my_info;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (OfferMainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.offer_front_fragment,container,false);

        manage_work = rootView.findViewById(R.id.work_manage);
        manage_commute = rootView.findViewById(R.id.manage_commute);
        my_info = rootView.findViewById(R.id.my_info);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        manage_work.setOnClickListener(v -> activity.changeFragment(OfferMainActivity.MANAGE_WORK_FRAGMENT));
        manage_commute.setOnClickListener(v -> activity.changeFragment(OfferMainActivity.MANAGE_COMMUTE_FRAGMENT));
        my_info.setOnClickListener(v -> activity.changeFragment(OfferMainActivity.MY_INFO_FRAGMENT));
    }

}
