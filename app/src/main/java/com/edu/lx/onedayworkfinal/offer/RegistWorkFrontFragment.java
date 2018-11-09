package com.edu.lx.onedayworkfinal.offer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RegistWorkFrontFragment extends Fragment {

    //TODO 일감 등록 (김동가 - 진행중)
    OfferMainActivity activity;

    //RegistWORK 일감 등록에서 사용되는 필터 설정
    //대분류
    static String Offer_registSubjectFilter;
    //산업군 분류
    static String Offer_registNameFilter;
    //직무 분류
    static String offer_registOpFilter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
