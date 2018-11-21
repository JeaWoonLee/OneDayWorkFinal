package com.edu.lx.onedayworkfinal.offer.manage_work;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.request.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.offer.recycler_view.OfferJobRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.JobVO;
import com.edu.lx.onedayworkfinal.vo.ProjectDetailVO;
import com.edu.lx.onedayworkfinal.vo.ProjectVO;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OfferManageWorkActivity extends AppCompatActivity {

    Toolbar toolbar;

    Button showOfferBasicInfoButton;
    int offerBasicInfoVisible = 1;
    LinearLayout offerBasicInfoLayout;
    ProjectVO projectVO;
    TextView projectName;
    TextView projectSubject;
    TextView projectDate;
    TextView projectWorkTime;
    TextView projectRequirement;
    TextView projectOffering;

    RecyclerView jobListRecyclerView;

    Button showOfferProjectLocationButton;
    FrameLayout mapViewLayout;
    int offerMapViewVisible = 0;
    FrameLayout mapView;
    MapView daumMap;
    Button manageHumanResourcesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_manage_work);

        initUI();

        Intent intent = getIntent();
        String projectNumber = intent.getStringExtra("projectNumber");
        requestManageProjectDetailInfo(projectNumber);
    }

    /**
     * requestManageProjectDetailInfo
     * @param projectNumber 해당 프로젝트의 상세정보 및 직군정보 갖고오기
     */
    private void requestManageProjectDetailInfo(String projectNumber) {
        String url = getResources().getString(R.string.url) + "requestManageProjectDetailInfo.do";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                this::processManageProjectDetailInfoResponse,
                error -> {

                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("projectNumber",projectNumber);
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    /**
     * processManageProjectDetailInfoResponse
     * @param response 해당 프로젝트의 상세정보를 풀어재끼는 곳
     */
    private void processManageProjectDetailInfoResponse(String response) {
        ProjectDetailVO vo = Base.gson.fromJson(response,ProjectDetailVO.class);
        //프로젝트 상세정보 출력
        ProjectVO projectInfo = vo.getProjectVO();
        projectVO = vo.getProjectVO();

        projectName.setText(projectInfo.getProjectName());
        projectSubject.setText(projectInfo.getProjectSubject());
        String prjDate = projectInfo.getProjectStartDate() + " ~ " + projectInfo.getProjectEndDate();
        projectDate.setText(prjDate);
        String workTime = projectInfo.getWorkStartTime() + " ~ " + projectInfo.getWorkEndTime();
        projectWorkTime.setText(workTime);

        //TODO 프로젝트 요구사항 늘어날 때마다 추가할 것
        String requestPicture = "";
        if (TextUtils.equals(projectInfo.getRequestPicture(),"필요")) {
            requestPicture = " 사진";
        }
        String constCertificate = "";
        if (TextUtils.equals(projectInfo.getConstCertificate(),"필요")) {
            constCertificate = " 건설안전교육이수증 ";
        }
        String requirement = requestPicture + constCertificate;
        projectRequirement.setText(requirement);

        String morning = "";
        if (TextUtils.equals(projectInfo.getMorning(),"제공")){
            morning = " 아침식사 ";
        }
        String launch = "";
        if (TextUtils.equals(projectInfo.getLaunch(),"제공")) {
            launch = " 점심식사 ";
        }
        String evening = "";
        if (TextUtils.equals(projectInfo.getEvening(),"제공")){
            evening = " 저녁식사 ";
        }
        String commute ="";
        if (TextUtils.equals(projectInfo.getCommute(),"제공")){
            commute = " 통근차량 ";
        }
        String offWork = "";
        if (TextUtils.equals(projectInfo.getOffWork(),"제공")){
            offWork = " 퇴근차량 ";
        }
        String offering = morning + launch + evening + commute + offWork;
        projectOffering.setText(offering);

        //모집 직군 리스트 출력
        List<JobVO> jobList = vo.getJobList();
        OfferJobRecyclerViewAdapter adapter = new OfferJobRecyclerViewAdapter(this);
        adapter.setItems((ArrayList<JobVO>) jobList);
        jobListRecyclerView.setAdapter(adapter);

    }

    private void initUI() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        showOfferBasicInfoButton = findViewById(R.id.showOfferBasicInfoButton);
        showOfferBasicInfoButton.setOnClickListener(v -> showOfferBasicInfo(offerBasicInfoVisible));
        offerBasicInfoLayout = findViewById(R.id.offerBasicInfoLayout);

        projectName = findViewById(R.id.projectName);
        projectSubject = findViewById(R.id.projectSubject);
        projectDate = findViewById(R.id.projectDate);
        projectWorkTime = findViewById(R.id.projectWorkTime);
        jobListRecyclerView = findViewById(R.id.jobListRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        jobListRecyclerView.setLayoutManager(manager);
        projectRequirement = findViewById(R.id.projectRequirement);
        projectOffering = findViewById(R.id.projectOffering);

        showOfferProjectLocationButton = findViewById(R.id.showOfferProjectLocation);
        showOfferProjectLocationButton.setOnClickListener(v -> showOfferProjectLocation(offerMapViewVisible));
        mapViewLayout = findViewById(R.id.mapViewLayout);

        mapView = findViewById(R.id.mapView);

        manageHumanResourcesButton = findViewById(R.id.manageHumanResourcesButton);
        manageHumanResourcesButton.setOnClickListener(v -> showManageHumanResourcesActivity(projectVO.getProjectNumber()));
    }

    /**
     * showManageCandidateActivity
     * @param jobNumber 신청 관리 액티비티 보여주기
     */
    public void showManageCandidateActivity(int jobNumber) {
        Intent intent = new Intent(this,OfferManageCandidateActivity.class);
        intent.putExtra("jobNumber",jobNumber);
        startActivity(intent);
    }

    /**
     * showManageHumanResourcesActivity
     * @param projectNumber 인력 관리 액티비티 보여주기
     */
    private void showManageHumanResourcesActivity(int projectNumber) {
        Intent intent = new Intent(this,OfferManageHumanResourcesActivity.class);
        intent.putExtra("projectNumber",String.valueOf(projectNumber));
        intent.putExtra("projectStartDate",projectVO.getProjectStartDate());
        intent.putExtra("projectEndDate",projectVO.getProjectEndDate());
        startActivity(intent);
    }



    //레이아웃 열고 닫기
    private void showOfferBasicInfo(int offerBasicInfoVisible) {
        switch (offerBasicInfoVisible) {
            case 0 :
                this.offerBasicInfoVisible = 1;
                offerBasicInfoLayout.setVisibility(View.VISIBLE);
                showOfferBasicInfoButton.setText("-");
                break;
            case 1:
                this.offerBasicInfoVisible = 0;
                offerBasicInfoLayout.setVisibility(View.GONE);
                showOfferBasicInfoButton.setText("+");
                break;
        }

    }

    private void showOfferProjectLocation(int offerMapViewVisible) {
        switch (offerMapViewVisible) {
            case 0 :
                this.offerMapViewVisible = 1;


                mapViewLayout.setVisibility(View.VISIBLE);
                showOfferProjectLocationButton.setText("-");
                daumMap = new MapView(this);
                daumMap.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(projectVO.getProjectLat(),projectVO.getProjectLng()),3,false);
                MapPOIItem item = new MapPOIItem();
                item.setItemName("일감 위치");
                item.setTag(1);
                item.setMapPoint(MapPoint.mapPointWithGeoCoord(projectVO.getProjectLat(),projectVO.getProjectLng()));
                daumMap.addPOIItem(item);

                mapView.addView(daumMap);
                break;
            case 1:
                this.offerMapViewVisible = 0;
                mapViewLayout.setVisibility(View.GONE);
                mapView.removeAllViews();
                showOfferProjectLocationButton.setText("+");
                break;
        }

    }
    //end 레이아웃 열고 닫기

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
