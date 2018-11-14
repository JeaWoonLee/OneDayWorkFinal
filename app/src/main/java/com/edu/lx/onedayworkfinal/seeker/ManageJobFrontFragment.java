package com.edu.lx.onedayworkfinal.seeker;

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
import com.edu.lx.onedayworkfinal.seeker.find.SeekerJobFilterPopupActivity;
import com.edu.lx.onedayworkfinal.vo.ProjectVO;

import java.util.ArrayList;

//신청 일감 관리 FrontFragment 윤정민(진행중)
public class ManageJobFrontFragment extends Fragment {


    SeekerMainActivity activity;

    ManageJobListFragment manageJobListFragment;
    ManageJobMapFragment manageJobMapFragment;

    //필터 버튼
    Button filterButton;
    //보기 전환 버튼
    Button changeViewButton;

    //리사이클러 뷰 / 맵 뷰 인덱스
    public final int MANAGE_JOB_LIST_FRAGMENT = 0;
    public final int MANAGE_JOB_MAP_FRAGMENT = 1;
    private int fragmentIndex = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SeekerMainActivity) getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_manage_job_front,container,false);

        filterButton = rootView.findViewById(R.id.filterButton);
        changeViewButton = rootView.findViewById(R.id.changeViewButton);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //필터 버튼 클릭
        filterButton.setOnClickListener(v -> showFilter());

        changeViewButton.setOnClickListener(v -> changeView());

        manageJobListFragment = new ManageJobListFragment();
        manageJobMapFragment = new ManageJobMapFragment();
        activity.getSupportFragmentManager().beginTransaction().add(R.id.frag_container,manageJobListFragment).commit();
    }

    //지도화면 / 리사이클러 뷰 화면 보기전환 버튼
    private void changeView() {

        switch (fragmentIndex) {
            case MANAGE_JOB_LIST_FRAGMENT :
                this.fragmentIndex = MANAGE_JOB_MAP_FRAGMENT;
                changeViewButton.setText("리스트로 보기");
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,manageJobMapFragment).commit();
                break;
            case MANAGE_JOB_MAP_FRAGMENT :
                this.fragmentIndex = MANAGE_JOB_LIST_FRAGMENT;
                changeViewButton.setText("맵으로 보기");
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,manageJobListFragment).commit();
                break;
        }
    }


    //필터 팝업
    private void showFilter() {
        Intent intent = new Intent(activity,SeekerJobFilterPopupActivity.class);
        intent.putExtra("projectSubjectFilter",SeekerMainActivity.F_projectSubjectFilter);
        intent.putExtra("maxDistanceFilter",SeekerMainActivity.F_maxDistanceFilter);
        intent.putExtra("jobNameFilter",SeekerMainActivity.F_jobNameFilter);
        intent.putExtra("jobPayFilter",SeekerMainActivity.F_jobPayFilter);
        intent.putExtra("jobRequirementFilter",SeekerMainActivity.F_jobRequirementFilter);
        intent.putExtra("targetDateFilter",SeekerMainActivity.F_targetDateFilter);

        activity.startActivityForResult(intent,301);
    }
}
