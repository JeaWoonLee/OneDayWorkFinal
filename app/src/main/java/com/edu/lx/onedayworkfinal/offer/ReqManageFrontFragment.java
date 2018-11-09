package com.edu.lx.onedayworkfinal.offer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edu.lx.onedayworkfinal.R;

public class ReqManageFrontFragment extends Fragment {

    //TODO 신청관리 (김동가 - 진행중)
    OfferMainActivity activity;

    FindLaborFrontFragment findLaborFrontFragment;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.req_manager_front_fragment,container,false);

        return rootView;
    }
}
