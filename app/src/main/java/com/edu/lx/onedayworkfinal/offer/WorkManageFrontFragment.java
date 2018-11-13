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
import android.widget.Button;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.vo.ProjectVO;

import java.util.ArrayList;

public class WorkManageFrontFragment extends Fragment {

    //TODO 일감관리 (김동가 - 진행중)
    OfferMainActivity activity;

    public RecyclerFragment recyclerFragment;
    public TodayWorkFragment todayWorkFragment;

    Button filterButton2;
    Button changeViewButton2;

    public final int RECYLER_FRAGMENT = 0;
    public final int TODAY_WORK_MANAGE = 1;
    private int fragmentIndex2 = 0;

    static ArrayList<ProjectVO> items = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (OfferMainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.work_manager_front_fragment,container,false);

        filterButton2 = rootView.findViewById(R.id.filterButton2);
        changeViewButton2 = rootView.findViewById(R.id.changeViewButton2);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        filterButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilter();
            }
        });

        changeViewButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeView();
            }
        });

        recyclerFragment = new RecyclerFragment();
        todayWorkFragment = new TodayWorkFragment();
        activity.getSupportFragmentManager().beginTransaction().add(R.id.frag_container2,recyclerFragment).commit();
    }

    private void changeView(){
        switch (fragmentIndex2){
            case RECYLER_FRAGMENT :
                fragmentIndex2 = TODAY_WORK_MANAGE;
                changeViewButton2.setText("일감관리");
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frag_container2,recyclerFragment).commit();
                 break;
            case TODAY_WORK_MANAGE:
                fragmentIndex2 = RECYLER_FRAGMENT;
                changeViewButton2.setText("오늘의 일감");
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frag_container2,todayWorkFragment).commit();
                break;
        }
    }

    private void showFilter(){
        Intent intent = new Intent(activity,OfferFilterActivity.class);


        activity.startActivityForResult(intent,301);
    }

}
