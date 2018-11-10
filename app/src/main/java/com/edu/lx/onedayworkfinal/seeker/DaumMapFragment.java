package com.edu.lx.onedayworkfinal.seeker;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.util.volley.Base;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class DaumMapFragment extends Fragment {

    SeekerMainActivity activity;
    MapView mapView;
    RelativeLayout mapViewContainer;

    MapPOIItem myLocationMarkerOption;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SeekerMainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.daum_map_framgent,container,false);

        //다음이 제공하는 MapView객체 생성 및 API Key 설정
        mapView = new MapView(activity);
        //xml에 선언된 map_view 레이아웃을 찾아온 후, 생성한 MapView객체 추가
        mapViewContainer =  rootView.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showMyLocation();
    }

    private void showMyLocation() {
        Location lastLocation = null;

        //LocationService 로 부터 lastLocation 을 받아옴
        try {
            lastLocation = Base.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        catch (SecurityException e) {
            Toast.makeText(activity,"내 위치 권한이 설정 되어 있지 않습니다",Toast.LENGTH_SHORT).show();
        }

        //플로팅 아이콘을 클릭하여 LocationListener 를 통해 위치를 받았다면 해당 위치를 사용한다

        //카메라를 현제 위치로 이동함
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(lastLocation.getLatitude(),lastLocation.getLongitude()),2,true);

        myLocationMarkerOption = new MapPOIItem();
        myLocationMarkerOption.setItemName("내 위치");
        myLocationMarkerOption.setMapPoint(MapPoint.mapPointWithGeoCoord(lastLocation.getLatitude(),lastLocation.getLongitude()));
        myLocationMarkerOption.setTag(0);
        myLocationMarkerOption.setMarkerType(MapPOIItem.MarkerType.BluePin);
        myLocationMarkerOption.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        mapView.addPOIItem(myLocationMarkerOption);
    }
}
