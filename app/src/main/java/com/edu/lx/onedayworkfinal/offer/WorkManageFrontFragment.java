package com.edu.lx.onedayworkfinal.offer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.seeker.manage.TodayWorkFragment;

public class WorkManageFrontFragment extends Fragment {

    //TODO 일감관리 (김동가 - 진행중)
    OfferMainActivity activity;

    TodayWorkFragment todayWorkFragment;
    OfferFilterActivity offerFilterActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.work_manager_front_fragment,container,false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

}
