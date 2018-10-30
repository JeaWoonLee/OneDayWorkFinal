package com.edu.lx.onedayworkfinal.join;

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

//회원가입 종류 선택 프래그먼트
public class FrontJoinFragment extends Fragment {

    JoinActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (JoinActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.join_front_fragment,container,false);

        //일용직 노동자 회원가입
        Button joinSeekerButton = rootView.findViewById(R.id.joinSeekerButton);
        joinSeekerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.changeTab(activity.SEEKER_JOIN_FRAGMENT);
            }
        });

        //사업자 회원가입
        Button joinOfferButton = rootView.findViewById(R.id.joinOfferButton);
        joinOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.changeTab(activity.OFFER_JOIN_FRAGMENT);
            }
        });

        return rootView;
    }
}
