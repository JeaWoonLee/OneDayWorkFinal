package com.edu.lx.onedayworkfinal.seeker;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.edu.lx.onedayworkfinal.R;

public class FrontFragment extends Fragment {

    SeekerMainActivity activity;

    LinearLayout today_work;
    LinearLayout find_job;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        activity = (SeekerMainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.seeker_front_fragment,container,false);

        today_work = rootView.findViewById(R.id.today_work);
        find_job = rootView.findViewById(R.id.find_job);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        today_work.setOnClickListener(v -> {

        });

        find_job.setOnClickListener(v -> activity.changeFragment(SeekerMainActivity.FIND_JOB_FRAGMENT));
    }
}
