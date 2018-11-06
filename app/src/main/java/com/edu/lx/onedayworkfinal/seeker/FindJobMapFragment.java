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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static com.edu.lx.onedayworkfinal.seeker.FindJobFrontFragment.items;

public class FindJobMapFragment extends Fragment implements LocationListener {

    SeekerMainActivity activity;

    //맵 뷰 레이아웃
    MapView mapView;
    //구글 맵
    GoogleMap map;

    //내 위치 마커
    MarkerOptions myLocationMarkerOption;
    Marker myLocationMarker;
    boolean isAim = false;

    //일감 마커
    MarkerOptions projectOptions;
    Marker projectMarker;
    ArrayList<Marker> projectMarkers = new ArrayList<>();

    //내 위치 보기 플로팅 아이콘
    FloatingActionButton myLocationFab;

    //JobList
    Map<Integer,ArrayList<ProjectJobListVO>> jobListMap = new HashMap<>();
    SeekerJobListRecyclerViewAdapter adapter;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SeekerMainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.seeker_find_job_map_framgent,container,false);

        mapView = rootView.findViewById(R.id.googleMap);
        myLocationFab = rootView.findViewById(R.id.myLocationFab);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //구글 맵이 로드 되었을 떄
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                //인포윈도우 설정
                map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    //마커를 클릭했을 때 나타나는 인포윈도우
                    @Override
                    public View getInfoContents(Marker marker) {
                        //내 위치 인포윈도우는 기본 뷰를 보여준다
                        if (TextUtils.equals(marker.getTitle(),"내 위치")){
                            return null;

                            //일감들의 인포윈도우는 리스트 뷰 형식에 따라 만든다
                        } else {
                            final ProjectVO item = (ProjectVO) marker.getTag();
                            View v = getLayoutInflater().inflate(R.layout.seeker_find_project_item,null);
                            TextView projectName = v.findViewById(R.id.projectName);
                            TextView projectDate = v.findViewById(R.id.projectDate);
                            TextView projectSubject = v.findViewById(R.id.projectSubject);
                            TextView projectEnrollDate = v.findViewById(R.id.projectEnrollDate);
                            projectName.setText(item.getProjectName());
                            projectDate.setText(item.getProjectStartDate() + " - " + item.getProjectEndDate());
                            projectSubject.setText(item.getProjectSubject());
                            projectEnrollDate.setText(item.getProjectEnrollDate());
                            RecyclerView jobListRecyclerView = v.findViewById(R.id.jobListRecyclerView);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(activity.getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                            jobListRecyclerView.setLayoutManager(layoutManager);
                            adapter = new SeekerJobListRecyclerViewAdapter(activity);
                            //Adapter 에서 setItems 를 해줄 때, 맵에 담아두었던 ArrayList 를 가져온다
                            adapter.setItems(jobListMap.get(item.getProjectNumber()));
                            jobListRecyclerView.setAdapter(adapter);
                            return v;
                        } //end 일감 인포 윈도우 설정

                    } //end getInfoContents

                }); //end 인포윈도우 설정

                //인포윈도우 클릭 리스너
                map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        ProjectVO projectVO = (ProjectVO) marker.getTag();
                        activity.showProjectDetail(projectVO.getProjectNumber());
                    }
                });

                showMyLocation(null);

                inflateProjectsLocation();
            }

        });

        //내 위치보기 플로팅 아이콘을 클릭했을 때
        myLocationFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                traceMyLocation();
            }
        });

    }

    private  void requestProjectJobList(final int projectNumber) {
        String url = activity.getResources().getString(R.string.url) + "requestProjectJobListByProjectNumber.do";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse (String response) {
                        ProjectJobListVO[] jobsArray = Base.gson.fromJson(response,ProjectJobListVO[].class);
                        ArrayList<ProjectJobListVO> items = new ArrayList<>(Arrays.asList(jobsArray));
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
        if (items != null) {

            //프로젝트 데이터를 하나씩 가져와서 맵에 뿌려준다
            projectOptions = new MarkerOptions();
            for (ProjectVO item : items) {
                projectOptions.position(new LatLng(item.getProjectLat(),item.getProjectLng()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.select_loctaion))
                        .title(item.getProjectName());
                projectMarker = map.addMarker(projectOptions);
                //각 마커를 생성할 때 직업 목록을 조회해서 map 에 담아둔다
                requestProjectJobList(item.getProjectNumber());
                projectMarker.setTag(item);
                projectMarkers.add(projectMarker);
            }
        } else {
            Toast.makeText(activity,"지도에 일감 목록을 불러오는 데 실패했습니다",Toast.LENGTH_SHORT).show();
        }
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

        //플로팅 아이콘을 클릭하여 LocationListener 를 통해 위치를 받았다면 해당 위치를 사용한다
        if (location != null) {
            lastLocation = location;
        }

        //lastLocation 을 받아 왔다면 LatLng 타입의 변수에 집어 넣음
        if (lastLocation != null){
            LatLng curPoint = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());

            //카메라를 현제 위치로 이동함
            if (map != null){
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(curPoint,15));

                //마커가 만들어 져 있지 않다면 마커를 새로 만듦
                if (myLocationMarkerOption == null) {
                    myLocationMarkerOption = new MarkerOptions();
                    myLocationMarkerOption.position(curPoint)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocation))
                            .title("내 위치");
                    myLocationMarker = map.addMarker(myLocationMarkerOption);
                } else {
                   //마커가 null 이라면 마커를 remove 한 뒤에 새로 찍어준다. addMarker 로 인해 마커가 늘어나는 것을 방지
                    myLocationMarker.remove();
                    myLocationMarkerOption.position(curPoint);
                    myLocationMarker = map.addMarker(myLocationMarkerOption);
                }
            }

        } else {
            Toast.makeText(activity,"lastLocation 이 null 입니다!",Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //액티비티가 처음 생성될 때 실행되는 함수
        if(mapView != null) mapView.onCreate(savedInstanceState);
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
}
