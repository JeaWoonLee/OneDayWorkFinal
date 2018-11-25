package com.edu.lx.onedayworkfinal.offer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.request.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.offer.recycler_view.OfferFrontProjectRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.offer.recycler_view.OfferProjectRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.seeker.SeekerMainActivity;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.OfferWorkVO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class OfferFrontFragment extends Fragment {

    OfferMainActivity activity;

    //오늘의 일감
    LinearLayout today_work_empty_layout;
    LinearLayout today_work_layout;
    RecyclerView today_work_recycler_view;
    OfferFrontProjectRecyclerViewAdapter adapter;

    //하루일감 메뉴
    LinearLayout manage_work;
    LinearLayout manage_commute;
    LinearLayout my_info;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (OfferMainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.offer_front_fragment,container,false);

        today_work_empty_layout = rootView.findViewById(R.id.today_work_empty_layout);
        today_work_layout = rootView.findViewById(R.id.today_work_layout);
        today_work_recycler_view = rootView.findViewById(R.id.today_work_recycler_view);

        manage_work = rootView.findViewById(R.id.work_manage);
        manage_commute = rootView.findViewById(R.id.manage_commute);
        my_info = rootView.findViewById(R.id.my_info);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        manage_work.setOnClickListener(v -> activity.changeFragment(OfferMainActivity.MANAGE_WORK_FRAGMENT));
        manage_commute.setOnClickListener(v -> activity.changeFragment(OfferMainActivity.MANAGE_COMMUTE_FRAGMENT));
        my_info.setOnClickListener(v -> activity.changeFragment(OfferMainActivity.MY_INFO_FRAGMENT));

        requestOfferProjectList(OfferMainActivity.OFFER_ID);
    }

    /**
     * requestOfferProjectList
     * @param offerId 해당 사업자의 프로젝트 중에 오늘 진행되는 프로젝트 목록을 가져온다
     */
    private void requestOfferProjectList(String offerId) {
        String url = getResources().getString(R.string.url) + "requestOfferProjectList.do";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                this::processRequestOfferProjectListResponse,
                error -> { })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("offerId",offerId);
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    /**
     * processRequestOfferProjectListResponse
     * @param response 오늘 진행되는 프로젝트 리스트
     */
    private void processRequestOfferProjectListResponse(String response) {
        OfferWorkVO[] projects = Base.gson.fromJson(response,OfferWorkVO[].class);
        ArrayList<OfferWorkVO> items = new ArrayList<>(Arrays.asList(projects));
        if (items.size() > 0) {
            today_work_empty_layout.setVisibility(View.GONE);
            today_work_layout.setVisibility(View.VISIBLE);

            LinearLayoutManager manager = new LinearLayoutManager(activity.getApplicationContext(),LinearLayoutManager.VERTICAL,false);
            today_work_recycler_view.setLayoutManager(manager);

            Log.d("items",items.toString());
            adapter = new OfferFrontProjectRecyclerViewAdapter(activity);
            adapter.setItems(items);
            today_work_recycler_view.setAdapter(adapter);
        } else {
            today_work_empty_layout.setVisibility(View.VISIBLE);
            today_work_layout.setVisibility(View.GONE);
        }

    }

}
