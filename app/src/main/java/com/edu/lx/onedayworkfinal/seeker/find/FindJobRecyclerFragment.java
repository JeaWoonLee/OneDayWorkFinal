package com.edu.lx.onedayworkfinal.seeker.find;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.seeker.SeekerMainActivity;
import com.edu.lx.onedayworkfinal.seeker.recycler_view.SeekerProjectListRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.ProjectVO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.edu.lx.onedayworkfinal.seeker.find.FindJobFrontFragment.items;

//일감 구하기 Fragment
public class FindJobRecyclerFragment extends Fragment {

    SeekerMainActivity activity;

    RecyclerView projectListRecyclerView;
    SeekerProjectListRecyclerViewAdapter adapter;
    @Override
    public void onAttach (Context context) {
        super.onAttach(context);
        activity = (SeekerMainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.seeker_find_job_recycler_fragment,container,false);
        projectListRecyclerView = rootView.findViewById(R.id.projectListRecyclerView);
        return rootView;
    }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //RecyclerView 의 layoutManager 세팅
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity.getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        projectListRecyclerView.setLayoutManager(layoutManager);

        //projectList 요청
        requestProjectList();

    }

    //projectList 요청
    public void requestProjectList() {
        String url = getResources().getString(R.string.url) + "getProjectList.do";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                this::processProjectResponse,
                error -> {

                }
        ){
            @Override
            protected Map<String, String> getParams () {
                Location lastLocation = null;

                //LocationService 로 부터 lastLocation 을 받아옴
                try {
                    lastLocation = Base.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
                catch (SecurityException e) {
                    Toast.makeText(activity,"내 위치 권한이 설정 되어 있지 않습니다",Toast.LENGTH_SHORT).show();
                }
                //필터 정보를 담아서 보내기
                //TODO 위치정보가 null 이라면 위경도 값을 공간정보 아카데미 위치로 설정해 둠
                Map<String,String> params = new HashMap<>();
                if(lastLocation == null) {
                    params.put("myLat",String.valueOf(37.5157941));
                    params.put("myLng",String.valueOf(127.0344488));
                } else {
                    params.put("myLat",String.valueOf(lastLocation.getLatitude()));
                    params.put("myLng",String.valueOf(lastLocation.getLongitude()));
                }

                params.put("projectSubjectFilter", SeekerMainActivity.F_projectSubjectFilter);
                params.put("jobNameFilter", SeekerMainActivity.F_jobNameFilter);
                params.put("jobPayFilter",SeekerMainActivity.F_jobPayFilter);
                params.put("jobRequirementFilter",SeekerMainActivity.F_jobRequirementFilter);
                params.put("maxDistanceFilter",SeekerMainActivity.F_maxDistanceFilter);
                params.put("targetDateFilter",SeekerMainActivity.F_targetDateFilter);
                return params;
            }
        };

        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    //서버로부터 받아온 projectList 를 RecyclerView 에 뿌려줌
    private void processProjectResponse (String response) {
        ProjectVO[] projectArray = Base.gson.fromJson(response,ProjectVO[].class);

        items = new ArrayList<>(Arrays.asList(projectArray));

        //Adapter 할당
        adapter = new SeekerProjectListRecyclerViewAdapter(activity);
        adapter.setItems(items);
        projectListRecyclerView.setAdapter(adapter);
    }

}
