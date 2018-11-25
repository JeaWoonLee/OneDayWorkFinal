package com.edu.lx.onedayworkfinal.seeker.today_work;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.request.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.seeker.SeekerMainActivity;
import com.edu.lx.onedayworkfinal.seeker.info.ShowContractActivity;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.WorkVO;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.HashMap;
import java.util.Map;

public class TodayWorkFragment extends Fragment {

    SeekerMainActivity activity;

    TextView projectName;
    TextView projectSubject;
    TextView jobName;
    TextView jobPay;
    TextView candidateStatus;
    TextView projectComment;

    Button commuteButton;
    Button findRouteButton;
    Button refreshButton;

    RelativeLayout map_view;

    MapView daumMap;

    MapPOIItem myLocationMarker;
    MapPOIItem workLocation;

    String seekerId;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SeekerMainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.seeker_today_work_fragment,container,false);
        projectName = rootView.findViewById(R.id.projectName);
        projectSubject = rootView.findViewById(R.id.projectSubject);
        jobName = rootView.findViewById(R.id.jobName);
        jobPay = rootView.findViewById(R.id.jobPay);
        candidateStatus = rootView.findViewById(R.id.candidateStatus);
        projectComment = rootView.findViewById(R.id.projectComment);
        commuteButton = rootView.findViewById(R.id.commuteButton);
        commuteButton.setOnClickListener(v -> showUnsignedContract(activity.todayWorkItem));
        findRouteButton = rootView.findViewById(R.id.findRouteButton);
        findRouteButton.setOnClickListener(v -> showDaumMapFindRoute());
        refreshButton = rootView.findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(v -> activity.requestTodayWorkDetail(Base.sessionManager.getUserDetails().get("id")));
        map_view = rootView.findViewById(R.id.map_view);
        return rootView;
    }

    private void showUnsignedContract(WorkVO todayWorkItem) {
        Intent intent = new Intent(activity,ShowContractActivity.class);
        intent.putExtra("workVO",todayWorkItem.toString());
        activity.startActivityForResult(intent,601);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //다음 맵 보여주기
        showDaumMap();

        //서버에서 정보 가져오기
        seekerId = Base.sessionManager.getUserDetails().get("id");
        processTodayDetail(activity.todayWorkItem);
    }

    private void showDaumMap() {
        daumMap = new MapView(activity);
        map_view.addView(daumMap);

        Location myLocation = null;
        try{
            myLocation = Base.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }catch (SecurityException e) {
            e.printStackTrace();
        }

        if(myLocation!= null){
            myLocationMarker = new MapPOIItem();
            myLocationMarker.setItemName("내 위치");
            myLocationMarker.setTag(1);
            myLocationMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(myLocation.getLatitude(),myLocation.getLongitude()));
            daumMap.addPOIItem(myLocationMarker);
        }


    }

    /**
     * processTodayDetail
     * @param item 아이템으로 오늘의 일감 화면 보여주기
     */
    public void processTodayDetail(WorkVO item) {
        if (item == null) return;
        projectName.setText(item.getProjectName());
        projectSubject.setText(item.getProjectSubject());
        jobName.setText(item.getJobName());
        jobPay.setText(item.getJobPay());
        projectComment.setText(item.getProjectComment());

        switch(item.getCandidateStatus()){
            case 1:
                candidateStatus.setText("미출근");
                candidateStatus.setTextColor(getResources().getColor(R.color.gray,activity.getTheme()));
                commuteButton.setVisibility(View.VISIBLE);
                break;
            case 2:
                candidateStatus.setText("출근");
                candidateStatus.setTextColor(getResources().getColor(R.color.seeker,activity.getTheme()));
                commuteButton.setVisibility(View.GONE);
                break;
            case 3:
                candidateStatus.setText("근무중");
                candidateStatus.setTextColor(getResources().getColor(R.color.offer,activity.getTheme()));
                commuteButton.setVisibility(View.GONE);
                break;
            case 4:
                candidateStatus.setText("퇴근");
                candidateStatus.setTextColor(getResources().getColor(R.color.blue1,activity.getTheme()));
                commuteButton.setVisibility(View.GONE);
                break;
            case 6:
                candidateStatus.setText("결근");
                candidateStatus.setTextColor(getResources().getColor(R.color.danger,activity.getTheme()));
                commuteButton.setVisibility(View.GONE);
                break;
            case 5:
                candidateStatus.setText("무단 이탈");
                candidateStatus.setTextColor(getResources().getColor(R.color.danger,activity.getTheme()));
                commuteButton.setVisibility(View.GONE);
                break;
        }

        workLocation = new MapPOIItem();
        workLocation.setItemName("일감 위치");
        workLocation.setTag(2);
        workLocation.setMapPoint(MapPoint.mapPointWithGeoCoord(item.getProjectLatitude(),item.getProjectLongitude()));
        daumMap.addPOIItem(workLocation);
        daumMap.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(item.getProjectLatitude(),item.getProjectLongitude()),5,true);
    }

    /**
     * findRoute
     * 길 찾기
     */
    private void showDaumMapFindRoute() {
        Location myLocation = null;
        try{
            myLocation = Base.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }catch (SecurityException e) {
            e.printStackTrace();
        }

        if (myLocation != null) {
            double projectLat = workLocation.getMapPoint().getMapPointGeoCoord().latitude;
            double projectLng = workLocation.getMapPoint().getMapPointGeoCoord().longitude;
            double myLat = myLocation.getLatitude();
            double myLng = myLocation.getLongitude();

            try{
                //다음 맵 길찾기 띄워주기
                String url = "daummaps://route?sp="+myLat+","+myLng+"&ep="+projectLat+","+projectLng;
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                startActivity(intent);

                //다음맵이 없을경우 까는 곳 보여주기
            } catch (ActivityNotFoundException e){
                String url = "https://play.google.com/store/apps/details?id=net.daum.android.map";
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                startActivity(intent);
            }

        }
    }

    /**
     * requestCommute
     * 출근 요청
     * @param seekerId seekerId 로 curDate 의 candidateStatus == 1 인 jobCandidate 를 2로 바꿈
     */
    public void requestCommute(String seekerId) {
        String url = getResources().getString(R.string.url) + "requestCommute.do";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                this::processCommuteResponse, error -> {}){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("seekerId",seekerId);
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    /**
     * processCommuteResponse
     * @param response int 형으로 1이면 성공, 0이면 실패이다
     *                 각 상태에 따라 SnackBar 를 띄우고 refresh 한다
     */
    private void processCommuteResponse(String response) {
        int commuteResult = Integer.parseInt(response);

        switch (commuteResult){
            case 0:
                Snackbar.make(activity.getWindow().getDecorView().getRootView(),"출근 처리에 실패했습니다",Snackbar.LENGTH_LONG).show();
                break;
            case 1:
                Snackbar.make(activity.getWindow().getDecorView().getRootView(),"출근 처리되었습니다.",Snackbar.LENGTH_LONG).show();
                break;
        }

        activity.requestTodayWorkDetail(seekerId);
    }


}
