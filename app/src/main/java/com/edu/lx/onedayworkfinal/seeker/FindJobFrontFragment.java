package com.edu.lx.onedayworkfinal.seeker;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.vo.ProjectVO;

import java.util.ArrayList;

public class FindJobFrontFragment extends Fragment {

    SeekerMainActivity activity;
    FindJobRecyclerFragment findJobRecyclerFragment;
    FindJobMapFragment findJobMapFragment;

    Button filterButton;
    Button changeViewButton;

    public final int FIND_JOB_RECYCLER_FRAGMENT = 0;
    public final int FIND_JOB_MAP_FRAGMENT = 1;
    private int fragmentIndex = 0;

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
    private void changeView() {

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

    private void showFilter() {
        Log.d("showFilter","필터 보기 버튼이 눌림");
    }
}
