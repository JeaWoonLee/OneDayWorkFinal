package com.edu.lx.onedayworkfinal.seeker;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.seeker.recycler_view.SeekerJobListRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.JobVO;
import com.edu.lx.onedayworkfinal.vo.ProjectVO;
import com.google.android.gms.maps.model.LatLng;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.edu.lx.onedayworkfinal.seeker.FindJobFrontFragment.items;

public class FindJobMapFragment extends Fragment implements LocationListener,MapView.POIItemEventListener {

    SeekerMainActivity activity;

    //맵 뷰 레이아웃
    //다음 맵
    RelativeLayout mapContainer;
    MapView mMapView;

    //내 위치 마커
    MapPOIItem myLocationMarker;
    boolean isAim = false;

    //일감 마커
    ArrayList<MapPOIItem> projectMarkers = new ArrayList<>();

    //내 위치 보기 플로팅 아이콘
    FloatingActionButton myLocationFab;

    //JobList
    public Map<Integer,ArrayList<JobVO>> jobListMap = new HashMap<>();

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

        inflateProjectsLocation();

        mapContainer.addView(mMapView);
        return rootView;
    }

    // CalloutBalloonAdapter 인터페이스 구현
    class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {
        private final View mCalloutBalloon;

        public CustomCalloutBalloonAdapter() {
            mCalloutBalloon = getLayoutInflater().inflate(R.layout.custom_callout_balloon, null);
        }

        @Override
        public View getCalloutBalloon(MapPOIItem poiItem) {
            if (poiItem.getUserObject() instanceof ProjectVO){
                final ProjectVO item = (ProjectVO) poiItem.getUserObject();
                ((TextView) mCalloutBalloon.findViewById(R.id.projectName)).setText(item.getProjectName());
                ((TextView) mCalloutBalloon.findViewById(R.id.projectDate)).setText(item.getProjectStartDate() + " - " + item.getProjectEndDate());
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
        myLocationFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                traceMyLocation();
            }
        });
        showMyLocation(null);

    }

    private  void requestProjectJobList(final int projectNumber) {
        String url = activity.getResources().getString(R.string.url) + "requestProjectJobListByProjectNumber.do";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse (String response) {
                        JobVO[] jobsArray = Base.gson.fromJson(response, JobVO[].class);
                        ArrayList<JobVO> items = new ArrayList<>(Arrays.asList(jobsArray));
                        jobListMap.put(projectNumber,items);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse (VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams () throws AuthFailureError {
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
    private void inflateProjectsLocation() {

        //프로젝트 데이터를 하나씩 가져와서 맵에 뿌려준다

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
        mMapView.addPOIItems(projectMarkers.toArray(new MapPOIItem[projectMarkers.size()]));

    }

    //내 위치보기 플로팅 아이콘을 클릭했을 때
    private void traceMyLocation() {
        if (isAim == false) {
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

        } else if (isAim == true) {
            //아이콘 배경 설정
            myLocationFab.setSupportBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white,activity.getTheme())));
            Base.locationManager.removeUpdates(this);
        }

        //에임 여부가 바뀐다
        isAim = !isAim;
    }

    //내 위치 보여주기
    private void showMyLocation(Location location) {
        Location lastLocation = null;

        //LocationService 로 부터 lastLocation 을 받아옴
        try {
            lastLocation = Base.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        catch (SecurityException e) {
            Toast.makeText(activity,"내 위치 권한이 설정 되어 있지 않습니다",Toast.LENGTH_SHORT).show();
        }

        //lastLocation 을 받아 왔다면 LatLng 타입의 변수에 집어 넣음
        LatLng curPoint = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());

        //카메라를 현제 위치로 이동함
        mMapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(curPoint.latitude,curPoint.longitude),2,true);

        //마커가 만들어 져 있지 않다면 마커를 새로 만듦
        if (myLocationMarker == null){
            myLocationMarker = new MapPOIItem();
            myLocationMarker.setItemName("내 위치");
            myLocationMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(curPoint.latitude,curPoint.longitude));
            myLocationMarker.setMarkerType(MapPOIItem.MarkerType.BluePin);
            myLocationMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
            mMapView.addPOIItem(myLocationMarker);
        }else {
            mMapView.removePOIItem(myLocationMarker);
            myLocationMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(curPoint.latitude,curPoint.longitude));
            mMapView.addPOIItem(myLocationMarker);
        }

    }

    //LocationListener
    @Override
    public void onLocationChanged(Location location) {
        showMyLocation(location);
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
