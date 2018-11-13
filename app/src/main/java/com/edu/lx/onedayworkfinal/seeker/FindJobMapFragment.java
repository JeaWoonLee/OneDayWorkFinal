package com.edu.lx.onedayworkfinal.seeker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.seeker.recycler_view.SeekerJobListRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.JobVO;
import com.edu.lx.onedayworkfinal.vo.ProjectVO;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FindJobMapFragment extends Fragment implements LocationListener,MapView.POIItemEventListener {

    SeekerMainActivity activity;

    //맵 뷰 레이아웃
    //다음 맵
    public RelativeLayout mapContainer;
    public MapView mMapView;

    //내 위치 마커
    public MapPOIItem myLocationMarker;
    boolean isAim = false;

    //필터 버퍼
    MapCircle distanceBuffer;
    //일감 마커
    public ArrayList<MapPOIItem> projectMarkers;

    //내 위치 보기 플로팅 아이콘
    FloatingActionButton myLocationFab;

    //ProjectList
    public ArrayList<ProjectVO> items;
    //JobList
    public SparseArray<ArrayList<JobVO>> jobListMap = new SparseArray<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SeekerMainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.seeker_find_job_map_framgent,container,false);

        myLocationFab = rootView.findViewById(R.id.myLocationFab);
        mapContainer = rootView.findViewById(R.id.map_view);
        mMapView = new MapView(activity);
        mMapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
        mMapView.setDaumMapApiKey(getResources().getString(R.string.kakao_app_key));
        mMapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());

        //다른 액티비티에서 사용할 것
        //mMapView.setMapViewEventListener(this);
        mMapView.setPOIItemEventListener(this);

        requestProjectList();

        mapContainer.addView(mMapView);
        return rootView;
    }

    // CalloutBalloonAdapter 인터페이스 구현
    class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {
        private final View mCalloutBalloon;

        @SuppressLint("InflateParams")
        CustomCalloutBalloonAdapter() {
            mCalloutBalloon = getLayoutInflater().inflate(R.layout.custom_callout_balloon, null);
        }

        @Override
        public View getCalloutBalloon(MapPOIItem poiItem) {
            if (poiItem.getUserObject() instanceof ProjectVO){
                final ProjectVO item = (ProjectVO) poiItem.getUserObject();
                ((TextView) mCalloutBalloon.findViewById(R.id.projectName)).setText(item.getProjectName());
                ((TextView) mCalloutBalloon.findViewById(R.id.projectDate)).setText(String.format("%s - %s", item.getProjectStartDate(), item.getProjectEndDate()));
                ((TextView) mCalloutBalloon.findViewById(R.id.projectSubject)).setText(item.getProjectSubject());
                ((TextView) mCalloutBalloon.findViewById(R.id.projectEnrollDate)).setText(item.getProjectEnrollDate());
                LinearLayoutManager layoutManager = new LinearLayoutManager(activity.getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                SeekerJobListRecyclerViewAdapter adapter = new SeekerJobListRecyclerViewAdapter(activity);
                //Adapter 에서 setItems 를 해줄 때, 맵에 담아두었던 ArrayList 를 가져온다
                adapter.setItems(jobListMap.get(item.getProjectNumber()));
                RecyclerView recyclerView =  mCalloutBalloon.findViewById(R.id.jobListRecyclerView);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                return mCalloutBalloon;
            }else {
                return null;
            }

        }

        @Override
        public View getPressedCalloutBalloon(MapPOIItem poiItem) {
            return null;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //내 위치보기 플로팅 아이콘을 클릭했을 때
        myLocationFab.setOnClickListener(v -> traceMyLocation());
        showMyLocation();

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

                //거리를 검색할 때, 버퍼 써클 추가
                if(!TextUtils.equals(SeekerMainActivity.F_maxDistanceFilter,"없음")){
                    int distance = Integer.valueOf(SeekerMainActivity.F_maxDistanceFilter);
                    if (distanceBuffer != null) {
                        mMapView.removeCircle(distanceBuffer);
                    }
                    distanceBuffer = new MapCircle(
                            MapPoint.mapPointWithGeoCoord(Objects.requireNonNull(lastLocation).getLatitude(),lastLocation.getLongitude()),
                            distance,
                            getResources().getColor(R.color.black,activity.getTheme()),
                            getResources().getColor(R.color.seeker50,activity.getTheme())
                    );
                    distanceBuffer.setTag(distance);
                    mMapView.addCircle(distanceBuffer);
                }
                //필터 정보를 담아서 보내기
                Map<String,String> params = new HashMap<>();
                params.put("myLat",String.valueOf(Objects.requireNonNull(lastLocation).getLatitude()));
                params.put("myLng",String.valueOf(lastLocation.getLongitude()));
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

    private void processProjectResponse (String response) {
        ProjectVO[] projectArray = Base.gson.fromJson(response,ProjectVO[].class);
        items = new ArrayList<>(Arrays.asList(projectArray));
        inflateProjectsLocation();
    }

    private  void requestProjectJobList(final int projectNumber) {
        String url = activity.getResources().getString(R.string.url) + "requestProjectJobListByProjectNumber.do";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    JobVO[] jobsArray = Base.gson.fromJson(response, JobVO[].class);
                    ArrayList<JobVO> items = new ArrayList<>(Arrays.asList(jobsArray));
                    jobListMap.put(projectNumber,items);
                },
                error -> {

                }
        ){
            @Override
            protected Map<String, String> getParams () {
                Map<String,String> params = new HashMap<>();
                params.put("projectNumber",String.valueOf(projectNumber));
                return params;
            }
        };

        request.setShouldCache(false);
        Base.requestQueue.add(request);
        Base.requestQueue.start();

    }

    //프로젝트 리스트를 화면에 뿌려주기
    public void inflateProjectsLocation() {

        //프로젝트 데이터를 하나씩 가져와서 맵에 뿌려준다
        projectMarkers = new ArrayList<>();
        int i = 0;
        for (ProjectVO item : items) {
            MapPOIItem projectMarker = new MapPOIItem();
            projectMarker.setItemName(item.getProjectName());
            projectMarker.setTag(i);
            projectMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(item.getProjectLat(),item.getProjectLng()));
            projectMarker.setMarkerType(MapPOIItem.MarkerType.BluePin);
            projectMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

            //각 마커를 생성할 때 직업 목록을 조회해서 map 에 담아둔다
            requestProjectJobList(item.getProjectNumber());
            projectMarker.setUserObject(item);
            projectMarkers.add(projectMarker);
        }
        mMapView.addPOIItems(projectMarkers.toArray(new MapPOIItem[0]));

    }

    //내 위치보기 플로팅 아이콘을 클릭했을 때
    private void traceMyLocation() {
        if (!isAim) {
            //아이콘 배경 설정
            myLocationFab.setSupportBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.seeker,activity.getTheme())));
            //검색 최소 시간
            long minTime = 1000;
            //변경 감지 최소거리
            float minDistance = 0;

            try{
                Base.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,minTime,minDistance,this);
            } catch (SecurityException e) {
                e.printStackTrace();
            }

        } else {
            //아이콘 배경 설정
            myLocationFab.setSupportBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white,activity.getTheme())));
            Base.locationManager.removeUpdates(this);
        }

        //에임 여부가 바뀐다
        isAim = !isAim;
    }

    //내 위치 보여주기
    public void showMyLocation() {
        Location lastLocation = null;
        //LocationService 로 부터 lastLocation 을 받아옴
        try {
            lastLocation = Base.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        catch (SecurityException e) {
            Toast.makeText(activity,"내 위치 권한이 설정 되어 있지 않습니다",Toast.LENGTH_SHORT).show();
        }
        //DistanceFilter 에 따라 zoomLevel 이 바뀜
        int zoomLevel = 0;
        if (TextUtils.equals(SeekerMainActivity.F_maxDistanceFilter,"없음")){
            zoomLevel = 7;
        } else if (Integer.valueOf(SeekerMainActivity.F_maxDistanceFilter) == 500){
            zoomLevel = 4;
        } else if (Integer.valueOf(SeekerMainActivity.F_maxDistanceFilter) == 1000){
            zoomLevel = 5;
        } else if (Integer.valueOf(SeekerMainActivity.F_maxDistanceFilter) == 5000){
            zoomLevel = 6;
        } else if (Integer.valueOf(SeekerMainActivity.F_maxDistanceFilter) == 10000){
            zoomLevel = 7;
        }

        //카메라를 현제 위치로 이동함
        if (lastLocation != null){
            mMapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(lastLocation.getLatitude(),lastLocation.getLongitude()),zoomLevel,true);

            //마커가 만들어 져 있지 않다면 마커를 새로 만듦
            if (myLocationMarker == null){
                myLocationMarker = new MapPOIItem();
                myLocationMarker.setItemName("내 위치");
                myLocationMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(lastLocation.getLatitude(),lastLocation.getLongitude()));
                myLocationMarker.setMarkerType(MapPOIItem.MarkerType.BluePin);
                myLocationMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
                mMapView.addPOIItem(myLocationMarker);
            }else {
                mMapView.removePOIItem(myLocationMarker);
                myLocationMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(lastLocation.getLatitude(),lastLocation.getLongitude()));
                mMapView.addPOIItem(myLocationMarker);
            }
        }


    }

    //LocationListener
    @Override
    public void onLocationChanged(Location location) {
        showMyLocation();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    //end LocationListener

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
        if (mapPOIItem.getUserObject() instanceof ProjectVO) {
            activity.findJobFrontFragment.changeView();
            ProjectVO item =(ProjectVO) mapPOIItem.getUserObject();
            activity.showProjectDetail(item.getProjectNumber());
        }
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}
