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
import com.edu.lx.onedayworkfinal.vo.ProjectVO;

import java.util.ArrayList;

public class FindJobFrontFragment extends Fragment {

    SeekerMainActivity activity;

    public FindJobRecyclerFragment findJobRecyclerFragment;
    public FindJobMapFragment findJobMapFragment;

    //필터 버튼
    Button filterButton;
    //보기 전환 버튼
    Button changeViewButton;

    //리사이클러 뷰 / 맵 뷰 인덱스
    public final int FIND_JOB_RECYCLER_FRAGMENT = 0;
    public final int FIND_JOB_MAP_FRAGMENT = 1;
    public int fragmentIndex = 0;

    //리사이클러 뷰 / 맵 뷰 에서 사용되는 프로젝트 배열
    static ArrayList<ProjectVO> items = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SeekerMainActivity) getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.seeker_find_job_main_fragment,container,false);

        filterButton = rootView.findViewById(R.id.filterButton);
        changeViewButton = rootView.findViewById(R.id.changeViewButton);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //필터 버튼 클릭
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilter();
            }
        });

        changeViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeView();
            }
        });

        findJobRecyclerFragment = new FindJobRecyclerFragment();
        findJobMapFragment = new FindJobMapFragment();
        activity.getSupportFragmentManager().beginTransaction().add(R.id.frag_container,findJobRecyclerFragment).commit();
    }

    //지도화면 / 리사이클러 뷰 화면 보기전환 버튼
    public void changeView() {

        switch (fragmentIndex) {
            case FIND_JOB_RECYCLER_FRAGMENT :
                fragmentIndex = FIND_JOB_MAP_FRAGMENT;
                changeViewButton.setText("리스트로 보기");
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,findJobMapFragment).commit();
                break;
            case FIND_JOB_MAP_FRAGMENT :
                fragmentIndex = FIND_JOB_RECYCLER_FRAGMENT;
                changeViewButton.setText("맵으로 보기");
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,findJobRecyclerFragment).commit();
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
