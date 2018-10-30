package com.edu.lx.onedayworkfinal.join;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.edu.lx.onedayworkfinal.R;

public class OfferJoinFragment extends Fragment {

    JoinActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (JoinActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.join_offer_fragment,container,false);

        return rootView;
    }
}
