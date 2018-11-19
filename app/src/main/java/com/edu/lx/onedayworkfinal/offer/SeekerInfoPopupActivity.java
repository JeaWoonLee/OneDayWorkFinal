package com.edu.lx.onedayworkfinal.offer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.offer.recycler_view.SeekerRecordRecyclerViewAdapter;
import com.edu.lx.onedayworkfinal.seeker.recycler_view.SeekerCertificateRecyclerAdapter;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.CertificationVO;
import com.edu.lx.onedayworkfinal.vo.JobCandidateVO;
import com.edu.lx.onedayworkfinal.vo.SeekerDetailVO;
import com.edu.lx.onedayworkfinal.vo.SeekerVO;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeekerInfoPopupActivity extends AppCompatActivity {

    //인텐트로 받아오는 seeker 정보
    String seekerId;
    SeekerVO seekerVO;

    //닫기 X 버튼
    Button dismissButton;

    //기본 정보
    Button showCommonInfoButton;
    LinearLayout commonInfoLayout;
    int commonInfoVisible = 0;
    TextView seekerIdView;
    TextView seekerName;
    TextView seekerSex;
    TextView seekerBirth;
    TextView seekerEmail;

    //소개글
    Button showSeekerInfoButton;
    int seekerInfoVisible = 0;
    LinearLayout seekerInfoLayout;
    TextView seekerInfo;

    //이력정보
    TextView seekerReliability; // 이력 브리핑
    Button showSeekerRecordButton;
    int seekerRecordVisible = 0;
    LinearLayout seekerRecordLayout;
    RecyclerView seekerRecordRecyclerView;

    //자격 정보
    TextView seekerQualification;
    Button showSeekerQualificationButton;
    int seekerQualificationVisible = 0;
    LinearLayout seekerQualificationLayout;
    RecyclerView seekerQualificationRecyclerView;

    //활동 위치
    TextView seekerLocation; // 활동 위치 브리핑
    Button showSeekerLocationButton;
    int seekerLocationVisible = 0;
    FrameLayout seekerLocationLayout;
    FrameLayout mapView;

    //사진정보
    TextView seekerIsPicture; // 사진 정보 브리핑
    Button showSeekerPictureButton;
    int seekerPictureVisible = 0;
    FrameLayout seekerPictureLayout;
    ImageView seekerPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_info_popup);

        //FindViewById 및 리사이클러 뷰 레이아웃 매니저 설정
        initUI();
        //각 버튼별 클릭리스너 설정
        initClickListener();

        //SeekerInfo 가져오기
        Intent intent = getIntent();
        seekerId = intent.getStringExtra("seekerId");
        requestSeekerDetail(seekerId);
    }

    /**
     * requestSeekerDetail
     * @param seekerId 해당 아이디로 구직자의 상세정보를 가져온다
     */
    private void requestSeekerDetail(String seekerId) {
        String url = getResources().getString(R.string.url) + "requestSeekerDetailPopup.do";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                this::processSeekerDetail,
                error -> {

                }){
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
     * processSeekerDetail
     * @param response 구직자의 상세정보가 담겨있다
     *                 1. 기본정보(seeker)
     *                 2. 소개글(seeker)
     *                 3. 이력정보(job_candidate)
     *                 4. 자격정보(seeker_certificate)
     *                 5. 활동위치(seeker)
     *                 6. 사진정보(seeker)
     */
    private void processSeekerDetail(String response) {
        SeekerDetailVO item = Base.gson.fromJson(response,SeekerDetailVO.class);
        Log.d("SeekerDetailVO",item.toString());
        SeekerVO seekervo = item.getSeekerVO();
        setSeekerDetailInfo(seekervo);
        List<JobCandidateVO> record = item.getRecord();
        setSeekerRecordList(record);
        List<CertificationVO> certificate = item.getCertificate();
        setCertificateList(certificate);
    }

    /**
     * setSeekerRecordList
     * @param record 해당 seeker 의 이력정보
     *               recyclerView 형태로 출력한다
     */
    private void setSeekerRecordList(List<JobCandidateVO> record) {
        //신뢰도 설정
        double offWorkCount = 0;
        double runCount = 0;
        double absentCount = 0;

        for (JobCandidateVO item : record) {
            int candidateStatus = item.getCandidateStatus();
            switch (candidateStatus) {
                case 4:
                    offWorkCount ++;
                    break;
                case 5:
                    runCount ++;
                    break;
                case 6:
                    absentCount ++;
                    break;
            }
            double reliability = ( offWorkCount / record.size() ) * 100;
            String recordStr = "신뢰도 : " + (int) reliability + "% (" + (int) offWorkCount + " / " + (record.size()) + " )";
            seekerReliability.setText(recordStr);
            if (record.size() == 0) {
                showSeekerRecordButton.setVisibility(View.GONE);
            }
        }


        //리사이클러 뷰 설정
        SeekerRecordRecyclerViewAdapter recordAdapter = new SeekerRecordRecyclerViewAdapter(this);
        recordAdapter.setItems((ArrayList<JobCandidateVO>) record);
        seekerRecordRecyclerView.setMinimumHeight(record.size()*104);
        seekerRecordRecyclerView.setAdapter(recordAdapter);
    }

    /**
     * setCertificateList
     * @param certificate 해당 seeker 의 자격정보
     *                    recyclerView 형태로 출력된다
     */
    private void setCertificateList(List<CertificationVO> certificate) {
        //자격 정보 설정
        String qualification = certificate.size() + " 건";
        seekerQualification.setText(qualification);
        //리사이클러 뷰 설정
        SeekerCertificateRecyclerAdapter certificateAdapter = new SeekerCertificateRecyclerAdapter(this);
        certificateAdapter.setItems((ArrayList<CertificationVO>) certificate);
        seekerQualificationRecyclerView.setMinimumHeight(certificate.size()*104);
        seekerQualificationRecyclerView.setAdapter(certificateAdapter);

        if (certificate.size() == 0) {
            showSeekerQualificationButton.setVisibility(View.GONE);
        }
    }

    /**
     * setSeekerDetailInfo
     * @param seekerVO 정보를 화면에 뿌려준다
     */
    private void setSeekerDetailInfo(SeekerVO seekerVO) {
        this.seekerVO = seekerVO;
        seekerIdView.setText(seekerVO.getSeekerId());
        seekerName.setText(seekerVO.getSeekerName());
        seekerSex.setText(seekerVO.getSeekerSex());
        seekerBirth.setText(seekerVO.getSeekerBirth());
        seekerEmail.setText(seekerVO.getSeekerEmail());

        seekerInfo.setText(seekerVO.getSeekerInfo());

        //활동 위치
        if (seekerVO.getSeekerLongitude() == 0 && seekerVO.getSeekerLatitude() == 0) {
            seekerLocation.setText("없음");
            showSeekerLocationButton.setVisibility(View.GONE);
        } else if (!TextUtils.equals(seekerVO.getOpenLocationInfo(),"공개")) {
            seekerLocation.setText("비공개");
            showSeekerLocationButton.setVisibility(View.GONE);
        } else if (TextUtils.equals(seekerVO.getOpenLocationInfo(),"공개")) {
            seekerLocation.setText("공개");
        }

        //사진 정보
        if (seekerVO.getSeekerPicture() == null) {
            seekerIsPicture.setText("없음");
            showSeekerPictureButton.setVisibility(View.GONE);
        } else if (!TextUtils.equals(seekerVO.getOpenPictureInfo(),"공개")) {
            seekerIsPicture.setText("비공개");
            showSeekerPictureButton.setVisibility(View.GONE);
        } else if (TextUtils.equals(seekerVO.getOpenPictureInfo(),"공개")) {
            seekerIsPicture.setText("공개");
            showSeekerPictureImage(seekerVO.getSeekerPicture());
        }

    }

    /**
     * showSeekerLocationMap
     * @param seekerLatitude 위도
     * @param seekerLongitude 경도
     *                        해당 정보를 이용하여 다음 지도를 띄워준다
     */
    private void showSeekerLocationMap(double seekerLatitude, double seekerLongitude) {
        MapView daumMap = new MapView(this);
        mapView.addView(daumMap);
        //활동위치
        MapPOIItem seekerLocationMarker = new MapPOIItem();
        seekerLocationMarker.setItemName("노동자 활동위치");
        seekerLocationMarker.setTag(0);
        seekerLocationMarker.setMapPoint(MapPoint.mapPointWithGeoCoord(seekerLatitude,seekerLongitude));
        daumMap.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(seekerLatitude,seekerLongitude),3,true);

        daumMap.addPOIItem(seekerLocationMarker);
    }

    private void showSeekerPictureImage(String seekerPictureURL) {
        String url = getResources().getString(R.string.url) + seekerPictureURL;
        ImageRequest request = new ImageRequest(url,
                response -> seekerPicture.setImageBitmap(response),
                100, 100, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,
                error -> {
                    Toast.makeText(getApplicationContext(),"사진정보를 불러오는데 실패했습니다",Toast.LENGTH_LONG).show();
                });
        Base.requestQueue.add(request);
    }

    //findViewById
    private void initUI() {
        dismissButton = findViewById(R.id.dismissButton);
        dismissButton.setOnClickListener(v -> finish());

        showCommonInfoButton = findViewById(R.id.showCommonInfoButton);
        commonInfoLayout = findViewById(R.id.commonInfoLayout);
        seekerIdView = findViewById(R.id.seekerId);
        seekerName = findViewById(R.id.seekerName);
        seekerSex = findViewById(R.id.seekerSex);
        seekerBirth = findViewById(R.id.seekerBirth);
        seekerEmail = findViewById(R.id.seekerEmail);

        //소개글
        showSeekerInfoButton = findViewById(R.id.showSeekerInfoButton);
        seekerInfoLayout = findViewById(R.id.seekerInfoLayout);
        seekerInfo = findViewById(R.id.seekerInfo);

        //이력정보
        seekerReliability = findViewById(R.id.seekerReliability); // 이력 브리핑
        showSeekerRecordButton = findViewById(R.id.showSeekerRecordButton);
        seekerRecordLayout = findViewById(R.id.seekerRecordLayout);
        seekerRecordRecyclerView = findViewById(R.id.seekerRecordRecyclerView);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        seekerRecordRecyclerView.setLayoutManager(layoutManager1);

        //자격 정보
        seekerQualification = findViewById(R.id.seekerQualification); //자격 정보 브리핑
        showSeekerQualificationButton = findViewById(R.id.showSeekerQualificationButton);
        seekerQualificationLayout = findViewById(R.id.seekerQualificationLayout);
        seekerQualificationRecyclerView = findViewById(R.id.seekerQualificationRecyclerView);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        seekerQualificationRecyclerView.setLayoutManager(layoutManager2);

        //활동 위치
        seekerLocation = findViewById(R.id.seekerLocation); // 활동 위치 브리핑
        showSeekerLocationButton = findViewById(R.id.showSeekerLocationButton);
        seekerLocationLayout = findViewById(R.id.seekerLocationLayout);
        mapView = findViewById(R.id.mapView);

        //사진정보
        seekerIsPicture = findViewById(R.id.seekerIsPicture); // 사진 정보 브리핑
        seekerPictureLayout = findViewById(R.id.seekerPictureLayout);
        seekerPicture = findViewById(R.id.seekerPicture);
        showSeekerPictureButton = findViewById(R.id.showSeekerPictureButton);
    }

    //setOnClickListener
    private void initClickListener() {
        showCommonInfoButton.setOnClickListener(v -> showCommonInfo(commonInfoVisible));
        showSeekerInfoButton.setOnClickListener(v -> showSeekerInfo(seekerInfoVisible));
        showSeekerRecordButton.setOnClickListener(v -> showSeekerRecord(seekerRecordVisible));
        showSeekerQualificationButton.setOnClickListener(v -> showSeekerQualification(seekerQualificationVisible));
        showSeekerLocationButton.setOnClickListener(v -> showSeekerLocation(seekerLocationVisible));
        showSeekerPictureButton.setOnClickListener(v -> showSeekerPicture(seekerPictureVisible));
    }

    // 윈도우 펼치고 닫기
    private void showCommonInfo(int commonInfoVisible) {
        switch (commonInfoVisible) {
            case 0:
                commonInfoLayout.setVisibility(View.VISIBLE);
                showCommonInfoButton.setText("-");
                this.commonInfoVisible = 1;
                break;
            case 1:
                commonInfoLayout.setVisibility(View.GONE);
                showCommonInfoButton.setText("+");
                this.commonInfoVisible = 0;
                break;
        }

    }

    private void showSeekerInfo(int seekerInfoVisible) {
        switch (seekerInfoVisible) {
            case 0:
                seekerInfoLayout.setVisibility(View.VISIBLE);
                showSeekerInfoButton.setText("-");
                this.seekerInfoVisible = 1;
                break;
            case 1:
                seekerInfoLayout.setVisibility(View.GONE);
                this.seekerInfoVisible = 0;
                showSeekerInfoButton.setText("+");
                break;
        }

    }

    private void showSeekerRecord(int seekerRecordVisible) {
        switch (seekerRecordVisible) {
            case 0:
                seekerRecordLayout.setVisibility(View.VISIBLE);
                this.seekerRecordVisible = 1;
                showSeekerRecordButton.setText("-");
                break;
            case 1:
                seekerRecordLayout.setVisibility(View.GONE);
                this.seekerRecordVisible = 0;
                showSeekerRecordButton.setText("+");
                break;
        }

    }

    private void showSeekerQualification(int seekerQualificationVisible) {
        switch (seekerQualificationVisible) {
            case 0:
                seekerQualificationLayout.setVisibility(View.VISIBLE);
                this.seekerQualificationVisible = 1;
                showSeekerQualificationButton.setText("-");
                break;
            case 1:
                seekerQualificationLayout.setVisibility(View.GONE);
                this.seekerQualificationVisible = 0;
                showSeekerQualificationButton.setText("+");
                break;
        }

    }

    private void showSeekerLocation(int seekerLocationVisible) {
        switch (seekerLocationVisible) {
            case 0:
                seekerLocationLayout.setVisibility(View.VISIBLE);
                this.seekerLocationVisible = 1;
                showSeekerLocationButton.setText("-");
                showSeekerLocationMap(seekerVO.getSeekerLatitude(),seekerVO.getSeekerLongitude());
                break;
            case 1:
                seekerLocationLayout.setVisibility(View.GONE);
                this.seekerLocationVisible = 0;
                showSeekerLocationButton.setText("+");
                mapView.removeAllViews();
                break;
        }

    }

    private void showSeekerPicture(int seekerPictureVisible) {
        switch (seekerPictureVisible) {
            case 0:
                seekerPictureLayout.setVisibility(View.VISIBLE);
                this.seekerPictureVisible = 1;
                showSeekerPictureButton.setText("-");
                break;
            case 1:
                seekerPictureLayout.setVisibility(View.GONE);
                this.seekerPictureVisible = 0;
                showSeekerPictureButton.setText("+");
                break;
        }


    }
    //end 윈도우 펼치고 닫기

}
