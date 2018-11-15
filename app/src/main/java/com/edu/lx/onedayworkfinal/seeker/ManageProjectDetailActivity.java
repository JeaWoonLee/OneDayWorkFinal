package com.edu.lx.onedayworkfinal.seeker;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.seeker.recycler_view.SeekerDetailJobListRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.seeker.recycler_view.SeekerManageDetailProjectAdapter;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.JobVO;
import com.edu.lx.onedayworkfinal.vo.ManageVO;
import com.edu.lx.onedayworkfinal.vo.ProjectVO;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class ManageProjectDetailActivity extends AppCompatActivity {
    //decimalFormat
    public DecimalFormat decimalFormat;
    //이전 액티비티로 부터 받아온 엑스트라 데이터
    int candidateNumber;
    //candidateNumber 로 서버로 부터 받아온 프로젝트의 상세정보
    ManageVO manageVO;

    Toolbar toolbar;

    //프로젝트 상세정보 TextView
    TextView projectCommnet;
    TextView projectName;
    TextView jobPay;
    TextView jobName;
    TextView targetDate;
    TextView jobRequirement;
//    TextView requirement;

    //프로젝트 위치 MapView
    RelativeLayout mapContainer;
    MapView mapView;


    //프로젝트 위치 마커
    MapPOIItem projectMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail_manage);

        decimalFormat = new DecimalFormat("###,###,###");
        //로그인 체크
        Base.sessionManager.checkLogin();

        //이전 액티비티로 부터 받아온 인텐트 처리
        Intent intent = getIntent();
        candidateNumber = intent.getIntExtra("candidateNumber",0);
        Log.d("나와라", String.valueOf(candidateNumber));
        requestManageProjectDetail();

        //툴바 설정
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //뒤로가기 버튼 설정
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //프로젝트 상세정보 TextView
        projectName = findViewById(R.id.projectName);
        projectCommnet = findViewById(R.id.projectComment);
        targetDate = findViewById(R.id.projectTargetDate);
        jobPay = findViewById(R.id.job_pay);
        jobName = findViewById(R.id.job_name);
        jobRequirement = findViewById(R.id.job_Requirement);


        //프로젝트 위치 MapView
        mapContainer = findViewById(R.id.map_view);
        mapView = new MapView(this);
        mapContainer.addView(mapView);

        //길찾기 버튼
        Button findRouteButton = findViewById(R.id.findRouteButton1);
        findRouteButton.setOnClickListener(v -> showDaumMapFindRoute1());


        //일감 취소 버튼
        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });

    }

    //cancel Button 클릭 시 AlertDialog 호출
    private void show() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("일감 취소");
        builder.setMessage("정말로 일을 취소하시겠습니까 ? ");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"일감을 취소하였습니다..",Toast.LENGTH_LONG).show();
                        // 일감 취소 실행
                        cancelProject(candidateNumber);
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
     }
    private void showDaumMapFindRoute1() {
        Location myLocation = null;

        try{
            myLocation = Base.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        }catch (SecurityException e) {

            e.printStackTrace();

        }


        if (myLocation != null) {

            double projectLat = projectMarker.getMapPoint().getMapPointGeoCoord().latitude;
            double projectLng = projectMarker.getMapPoint().getMapPointGeoCoord().longitude;
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
            Toast.makeText(this,"오류오류오루오루",Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this,"살려줘어 ㅓㅓ어ㅓ어",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }


    //프로젝트 상세정보 요청
    private void requestManageProjectDetail() {

        String url = getResources().getString(R.string.url) + "requestManageProjectDetail.do";
        Log.d("this", String.valueOf(candidateNumber));
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                this::processProjectDetailResponse,
                error -> {

                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("candidateNumber",String.valueOf(candidateNumber));
                return params;
            }
        };

        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    //프로젝트 상세정보 처리
    private void processProjectDetailResponse(String response) {
        manageVO = Base.gson.fromJson(response,ManageVO.class);

        if (manageVO == null) {
            Toast.makeText(this,"manageVO가 null 입니다!",Toast.LENGTH_LONG).show();
            return;
        }
        //Toolbar Title 입력
        toolbar.setTitle(manageVO.getProjectName());

        //TextView 에 값 입력
        projectName.setText(manageVO.getProjectName());
        jobName.setText(manageVO.getJobName());
        targetDate.setText(String.valueOf(manageVO.getTargetDate()));
        jobPay.setText(decimalFormat.format(manageVO.getJobPay()) + "원");
        jobRequirement.setText(manageVO.getJobRequirement());
        projectCommnet.setText(String.valueOf(manageVO.getProjectComment()));


        mapView.setDaumMapApiKey(getResources().getString(R.string.kakao_app_key));
        showProjectLocation(manageVO.getProjectLat(),manageVO.getProjectLng());

    }

    //프로젝트 위치로 이동하기
    private void showProjectLocation(double projectLat, double projectLng) {
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(projectLat,projectLng),3,false);
        projectMarker = new MapPOIItem();
        projectMarker.setItemName("일감 위치");
        projectMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(projectLat,projectLng));
        projectMarker.setMarkerType(MapPOIItem.MarkerType.BluePin);
        projectMarker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);
        mapView.addPOIItem(projectMarker);
    }

    //일감 취소
    private void cancelProject(final int candidateNumber) {
        String url = getResources().getString(R.string.url) + "cancelProject.do";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                this::processCancelProjectResponse,
                error -> {

            }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("candidateNumber", String.valueOf(candidateNumber));

                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }
    // projectCancel response process
    private void processCancelProjectResponse(String response) {
        int cancelResult = Integer.parseInt(response);

        if (cancelResult == 0) {
            Toast.makeText(this,"신청 취소에 실패했습니다",Toast.LENGTH_LONG).show();
        }else if (cancelResult == 1) {
            Intent intent = new Intent();
            intent.putExtra("result","ok");
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
    }




}
