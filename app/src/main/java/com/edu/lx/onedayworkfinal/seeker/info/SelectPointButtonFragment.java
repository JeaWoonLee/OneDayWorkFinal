package com.edu.lx.onedayworkfinal.seeker.info;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.seeker.SeekerMainActivity;

public class SelectPointButtonFragment extends Fragment {

    SeekerMainActivity activity;
    public MyInfoFragment myInfoFragment;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SeekerMainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.seeker_select_point_fragment,container,false);

        myInfoFragment = activity.myInfoFragment;
        Button selectLocationButton = rootView.findViewById(R.id.selectLocationButton);
        selectLocationButton.setOnClickListener(v -> myInfoFragment.showSelectLocationActivity());
        return rootView;
    }
}
