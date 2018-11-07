package com.edu.lx.onedayworkfinal.seeker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.seeker.recycler_view.SeekerDetailJobListRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.ProjectJobListVO;
import com.edu.lx.onedayworkfinal.vo.ProjectVO;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProjectDetailActivity extends AppCompatActivity {

    //이전 액티비티로 부터 받아온 엑스트라 데이터
    int projectNumber;
    //projectNumber 로 서버로 부터 받아온 프로젝트의 상세정보
    ProjectVO projectVO;

    Toolbar toolbar;

    //프로젝트 상세정보 TextView
    TextView projectName;
    TextView projectSubject;
    TextView projectDate;
    TextView projectEnrollDate;
    TextView projectRequirement;
    TextView projectComment;

    //프로젝트 위치 MapView
    MapView mapView;

    //구글 맵
    GoogleMap map;

    //프로젝트 위치 마커
    MarkerOptions projectMarkerOption;
    Marker projectMarker;

    //모집 직군 RecyclerView
    RecyclerView jobListRecyclerView;

    //모집 직군 ArrayList
    ArrayList<ProjectJobListVO> jobList;

    //리사이클러 뷰 어뎁터
    SeekerDetailJobListRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        //로그인 체크
        Base.sessionManager.checkLogin();

        //이전 액티비티로 부터 받아온 인텐트 처리
        Intent intent = getIntent();
        projectNumber = intent.getIntExtra("projectNumber",0);
        requestProjectDetail();
        requestProjectJobList();

        //툴바 설정
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //뒤로가기 버튼 설정
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //프로젝트 상세정보 TextView
        projectName = findViewById(R.id.projectName);
        projectSubject = findViewById(R.id.projectSubject);
        projectDate = findViewById(R.id.projectDate);
        projectEnrollDate = findViewById(R.id.projectEnrollDate);
        projectRequirement = findViewById(R.id.projectRequirement);
        projectComment = findViewById(R.id.projectComment);

        //모집 직군 RecyclerView
        jobListRecyclerView = findViewById(R.id.jobListRecyclerView);
        //RecyclerView 의 layoutManager 세팅
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        jobListRecyclerView.setLayoutManager(layoutManager);

        //프로젝트 위치 MapView
        mapView = findViewById(R.id.mapView);
        //액티비티가 처음 생성될 때 실행되는 함수
        if(mapView != null) mapView.onCreate(savedInstanceState);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }


    //프로젝트 상세정보 요청
    private void requestProjectDetail() {
        String url = getResources().getString(R.string.url) + "requestProjectDetail.do";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processProjectDetailResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("projectNumber",String.valueOf(projectNumber));
                return params;
            }
        };

        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    //프로젝트 상세정보 처리
    private void processProjectDetailResponse(String response) {
        projectVO = Base.gson.fromJson(response,ProjectVO.class);

        //Toolbar Title 입력
        toolbar.setTitle(projectVO.getProjectName());

        //TextView 에 값 입력
        projectName.setText(projectVO.getProjectName());
        projectSubject.setText(projectVO.getProjectSubject());
        projectDate.setText(projectVO.getProjectStartDate() + " - " + projectVO.getProjectEndDate());
        projectEnrollDate.setText(projectVO.getProjectEnrollDate());
        projectRequirement.setText(projectVO.getProjectRequirement());
        projectComment.setText(projectVO.getProjectComment());

        //MapView 띄우기
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                //프로젝트 위치로 이동하기
                showProjectLocation(projectVO.getProjectLat(),projectVO.getProjectLng());
            }
        });

    }

    //프로젝트 위치로 이동하기
    private void showProjectLocation(double projectLat, double projectLng) {

        //위치 변수 만들기
        LatLng projectPoint = new LatLng(projectLat,projectLng);

        //해당 위치로 카메라 이동하기
        if (map != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(projectPoint,15));

            //마커 만들기
            if (projectMarkerOption == null){
                projectMarkerOption = new MarkerOptions();
                projectMarkerOption.position(projectPoint)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.select_loctaion))
                        .title(projectVO.getProjectName());
                projectMarker = map.addMarker(projectMarkerOption);
            } else {
                projectMarker.remove();
                projectMarkerOption.position(projectPoint);
                projectMarker = map.addMarker(projectMarkerOption);
            }
        }

    }

    //직군 상세정보 요청
    private void requestProjectJobList() {
        String url = getResources().getString(R.string.url) + "requestProjectJobListByProjectNumber.do";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processProjectJobLIstResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("projectNumber",String.valueOf(projectNumber));
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    //직군 상세정보 처리
    private void processProjectJobLIstResponse(String response) {
        ProjectJobListVO[] projectJobListVOS = Base.gson.fromJson(response,ProjectJobListVO[].class);
        jobList = new ArrayList<>(Arrays.asList(projectJobListVOS));
        adapter = new SeekerDetailJobListRecyclerViewAdapter(this);
        adapter.setItems(jobList);
        jobListRecyclerView.setAdapter(adapter);
    }

    //지원하기 창 띄우기
    public void showCandidate(int jobNumber) {
        Intent intent = new Intent(this,CandidateActivity.class);
        intent.putExtra("jobNumber",jobNumber);
        startActivityForResult(intent,301);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();
    }
}
