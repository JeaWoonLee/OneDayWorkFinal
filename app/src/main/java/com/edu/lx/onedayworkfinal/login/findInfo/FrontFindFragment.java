package com.edu.lx.onedayworkfinal.login.findInfo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.edu.lx.onedayworkfinal.R;

public class FrontFindFragment extends Fragment {

    FindInfoActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    activity = (FindInfoActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_front_find,container,false);
        Button findIDButton = rootView.findViewById(R.id.findIDButton);
        Button findPWButton = rootView.findViewById(R.id.findPWButton);

        findIDButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                activity.changeTabs(activity.FIND_ID_ACT);
                Intent intent = new Intent(activity,FindIDActivity.class);
                startActivityForResult(intent,407);
            }
        });

        findPWButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                activity.changeTabs(activity.FIND_PW_ACT);
                Intent intent = new Intent(activity,FindPWActivity.class);
                startActivityForResult(intent,408);
            }
        });

       return rootView;
    }
}
