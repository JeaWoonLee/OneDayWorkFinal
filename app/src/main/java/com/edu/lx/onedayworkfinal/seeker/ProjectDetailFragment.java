package com.edu.lx.onedayworkfinal.seeker;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edu.lx.onedayworkfinal.R;

public class ProjectDetailFragment extends Fragment {

    //이전 프래그먼트에서 넘어온 인덱스값들
    public int projectNumber;
    public int fragIndex;

    SeekerMainActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SeekerMainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.seeker_project_detail,container,false);

        return rootView;
    }
}
