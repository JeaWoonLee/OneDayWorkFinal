package com.edu.lx.onedayworkfinal.seeker.info;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.util.volley.Base;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.Objects;

public class SelectLocationActivity extends AppCompatActivity implements MapView.POIItemEventListener, MapView.MapViewEventListener {

    MapView mapView;
    FrameLayout mapContainer;

    double myLat;
    double myLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        mapContainer = findViewById(R.id.mapContainer);
        mapView = new MapView(this);
        mapView.setPOIItemEventListener(this);
        mapView.setMapViewEventListener(this);
        mapContainer.addView(mapView);
        Intent intent = getIntent();
        myLat = intent.getDoubleExtra("lat",0);
        myLng = intent.getDoubleExtra("lng",0);

        showMyLocation(myLat,myLng);
    }

    /**
     * showMyLocation
     * 위치 정보를 받아오고, 해당 값이 없다면(0,0) GPS 를 통해 받아온 좌표를 보여준다
     * @param myLat latitude
     * @param myLng longitude
     */
    private void showMyLocation(double myLat, double myLng) {

        if (myLat == 0 && myLng == 0) {
            Location lastLocation = null;
            try{
                lastLocation = Base.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }catch (SecurityException e) {
                e.printStackTrace();
            }
            myLat = Objects.requireNonNull(lastLocation).getLatitude();
            myLng = lastLocation.getLongitude();
        }else {
            addPoint(MapPoint.mapPointWithGeoCoord(myLat,myLng));
        }
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(myLat,myLng),3,true);
    }

    //MapViewEventListener

    private void addPoint(MapPoint mapPoint) {
        mapView.removeAllPOIItems();

        MapPOIItem item = new MapPOIItem();
        item.setItemName("선택한 위치");
        item.setTag(0);
        item.setMapPoint(mapPoint);
        mapView.addPOIItem(item);
    }
    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        addPoint(mapPoint);


    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }
    //end MapViewEventListener

    //POIItemEventListener
    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        MapPoint point = mapPOIItem.getMapPoint();
        Intent intent = new Intent();
        intent.putExtra("lat",point.getMapPointGeoCoord().latitude);
        intent.putExtra("lng",point.getMapPointGeoCoord().longitude);
        setResult(Activity.RESULT_OK,intent);
        mapContainer.removeAllViews();
        finish();
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
    //end POIItemEventListener
}
