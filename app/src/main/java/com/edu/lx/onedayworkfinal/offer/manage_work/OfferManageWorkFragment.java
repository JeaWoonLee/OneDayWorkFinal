package com.edu.lx.onedayworkfinal.offer.manage_work;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.offer.OfferMainActivity;
import com.edu.lx.onedayworkfinal.offer.recycler_view.OfferProjectListRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.ProjectVO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class OfferManageWorkFragment extends Fragment {
    OfferMainActivity activity;
    RecyclerView projectListRecyclerView;
    OfferProjectListRecyclerViewAdapter adapter;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (OfferMainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.offer_manage_work_fragment,container,false);
        projectListRecyclerView = rootView.findViewById(R.id.projectListRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity.getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        projectListRecyclerView.setLayoutManager(layoutManager);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requestOfferProjectList(OfferMainActivity.OFFER_ID);
    }

    /**
     * requestOfferProjectLis
     * @param offerId 해당 사업자의 프로젝트 목록을 가져온다
     */
    private void requestOfferProjectList(String offerId) {
        String url = getResources().getString(R.string.url) + "requestOfferManageProjectList.do";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                this::processOfferProjectListResponse,
                error -> {

                }){
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

    private void processOfferProjectListResponse(String response) {
        ProjectVO[] result = Base.gson.fromJson(response,ProjectVO[].class);
        ArrayList<ProjectVO> items = new ArrayList<>(Arrays.asList(result));
        adapter = new OfferProjectListRecyclerViewAdapter(activity);
        adapter.setItems(items);
        projectListRecyclerView.setAdapter(adapter);
    }
}
