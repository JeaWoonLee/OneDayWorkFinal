package com.edu.lx.onedayworkfinal.seeker;

import android.content.Context;
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
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.seeker.recycler_view.SeekerManageProjectRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.ManageVO;
import com.edu.lx.onedayworkfinal.vo.ProjectVO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.edu.lx.onedayworkfinal.seeker.ManageJobFrontFragment.items;

//신청 일감 관리 RecyclerViewFragment 윤정민
public class ManageJobListFragment extends Fragment {

    SeekerMainActivity activity;
    String seekerId;
    RecyclerView ManageListRecyclerView;
    SeekerManageProjectRecyclerViewAdapter adapter;

    @Override
    public void onAttach (Context context) {
        super.onAttach(context);
        activity = (SeekerMainActivity) getActivity();

    }

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.seeker_manage_project_item,container,false);
        ManageListRecyclerView = rootView.findViewById(R.id.ManageListRecyclerView);

        return rootView;
    }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //RecyclerView 의 layoutManager 세팅
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity.getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        ManageListRecyclerView.setLayoutManager(layoutManager);
        seekerId = Base.sessionManager.getUserDetails().get("id");
        //신청 일감 요청

        requestManageList(seekerId);

    }

    //신청 일감 요청
    public void requestManageList (final String seekerId) {
        String url = getResources().getString(R.string.url) + "manageJobList.do";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                this::processProjectResponse,
                error -> {

                }
        ){
            @Override
            protected Map<String, String> getParams () {
                //TODO ID로 결과값 가져오기
                Map<String, String> params = new HashMap<>();
                params.put("seekerId", String.valueOf(seekerId));
                return params;
            }
        };

        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    //서버로부터 받아온 projectList 를 RecyclerView 에 뿌려줌
    private void processProjectResponse (String response) {
        ManageVO[] manageArray = Base.gson.fromJson(response,ManageVO[].class);

        items = new ArrayList<>(Arrays.asList(manageArray));
        Log.d("ManageVO[]",items.toString());
        //Adapter 할당
        adapter = new SeekerManageProjectRecyclerViewAdapter(activity);
        adapter.setItems(items);
        ManageListRecyclerView.setAdapter(adapter);
    }



}
