package com.edu.lx.onedayworkfinal.seeker.info;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.request.ImageRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.seeker.SeekerMainActivity;
import com.edu.lx.onedayworkfinal.seeker.recycler_view.SeekerCertificateRecyclerAdapter;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.CertificationVO;
import com.edu.lx.onedayworkfinal.vo.SeekerVO;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MyInfoFragment extends Fragment implements  MapView.POIItemEventListener{

    SeekerMainActivity activity;

    public SeekerVO item;

    TextView seekerId;
    TextView seekerName;
    TextView seekerSex;
    TextView seekerBirth;
    TextView seekerEmail;
    TextView seekerIsPicture;
    TextView seekerCash;
    ImageView seekerPicture;
    Spinner bankSpinner;

    FrameLayout mapContainer;

    RecyclerView certificateRecyclerView;

    EditText seekerAccount;
    EditText seekerInfo;

    SelectPointButtonFragment selectPointButtonFragment;

    public Bitmap picture;
    public File image;
    public Uri imageUri;

    public ArrayList<CertificationVO> items;

    public SeekerCertificateRecyclerAdapter adapter;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SeekerMainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.seeker_my_info_fragment,container,false);
        seekerId = rootView.findViewById(R.id.seekerId);
        seekerName = rootView.findViewById(R.id.seekerName);
        seekerSex = rootView.findViewById(R.id.seekerSex);
        seekerBirth = rootView.findViewById(R.id.seekerBirth);
        seekerEmail = rootView.findViewById(R.id.seekerEmail);
        seekerIsPicture = rootView.findViewById(R.id.seekerIsPicture);
        seekerCash = rootView.findViewById(R.id.seekerCash);
        seekerAccount = rootView.findViewById(R.id.seekerAccount);
        seekerInfo = rootView.findViewById(R.id.seekerInfo);
        seekerPicture = rootView.findViewById(R.id.seekerPicture);
        //Button
        Button takePictureButton = rootView.findViewById(R.id.takePictureButton);
        takePictureButton.setOnClickListener(v -> takePicture());
        Button addCertificateButton = rootView.findViewById(R.id.addCertificateButton);
        addCertificateButton.setOnClickListener(v -> addCertificate());
        Button updateMyInfo = rootView.findViewById(R.id.updateMyInfo);
        updateMyInfo.setOnClickListener(v -> {
            updateMyInfo(item);
        });
        //Spinner 설정
        bankSpinner = rootView.findViewById(R.id.bankSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity.getApplicationContext(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.account));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bankSpinner.setAdapter(adapter);

        //SeekerVO 정보를 검색하고 난 후에 정보가 있다면 다음지도를 띄워주고 없으면 위치선택 버튼을 띄워준다
        mapContainer = rootView.findViewById(R.id.mapContainer);
        certificateRecyclerView = rootView.findViewById(R.id.certificateRecyclerView);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //SeekerVO 의 상세정보 조회하기
        requestSeekerDetail(Base.sessionManager.getUserDetails().get("id"));

        //SeekerVO 의 자격증 목록 조회하기
        requestSeekerCertificationDetail(Base.sessionManager.getUserDetails().get("id"));
    }

    /**
     * updateMyInfo
     * @param item seekerVO 를 update 함
     */
    private void updateMyInfo(SeekerVO item) {
        String url = getResources().getString(R.string.url) + "updateSeeker.do";
        SimpleMultiPartRequest request = new SimpleMultiPartRequest(
                Request.Method.POST, url,
                this::processUpdateResult,
                error -> {

                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                item.setBank(bankSpinner.getSelectedItem().toString());
                params.put("seekerVO",item.toString());
                return params;
            }
        };
        if (image != null) {
            Log.d("imageUri",image.getPath());
            Log.d("imageUri",image.getAbsolutePath());
            request.addFile("seekerPhoto",image.getAbsolutePath());
        }
        request.addMultipartParam("seekerVO","text/plain",item.toString());
        request.setShouldCache(false);
        Base.requestQueue.add(request);

    }

    /**
     * processUpdateResult
     * @param response update 결과에 따라 ToastMessage 를 띄움
     */
    private void processUpdateResult(String response) {
        int updateReulst = Integer.parseInt(response);
        if (updateReulst == 1) {
            Toast.makeText(activity.getApplicationContext(),"정보가 성공적으로 업데이트 되었습니다",Toast.LENGTH_LONG).show();
        }

    }

    /**
     * requestSeekerCertificateDetail
     * @param id Base.sessionManager 로 부터 로그인된 id 를 가져온다.
     *           해당 아이디로 사용자에 대한 자격증 목록을 가져온다
     */
    private void requestSeekerCertificationDetail(String id) {
        String url = getResources().getString(R.string.url) + "requestSeekerCertificationDetail.do";
        StringRequest request = new StringRequest(Request.Method.POST,url,
                this::processSeekerCertificationList,
                error -> {}){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("seekerId",id);
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    /**
     * processSeekerCertificationList
     * @param response 서버로 부터 응답받은 SeekerVO 의 CertificationList
     *                 해당 정보를 리사이클러 뷰로 뿌려준다
     */
    private void processSeekerCertificationList(String response) {
        CertificationVO[] results = Base.gson.fromJson(response,CertificationVO[].class);
        items = new ArrayList<>(Arrays.asList(results));
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity.getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        certificateRecyclerView.setLayoutManager(layoutManager);

        adapter = new SeekerCertificateRecyclerAdapter(activity);
        adapter.setItems(items);
        certificateRecyclerView.setAdapter(adapter);
    }

    /**
     * requestSeekerDetail
     * @param id Base.sessionManager 로 부터 로그인된 id 를 가져온다.
     *           해당 아이디로 사용자에 대한 상세정보를 가져온다
     */
    private void requestSeekerDetail(String id) {
        String url = getResources().getString(R.string.url) + "requestSeekerDetail.do";
        StringRequest request = new StringRequest(Request.Method.POST,url,
                this::processSeekerDetailResponse,
                error -> {}){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("seekerId",id);
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    /**
     * processSeekerDetailResponse
     * @param response 서버로 부터 응답받은 SeekerVO 의 상세정보
     *                 SeekerVO 정보를 view 에 뿌려준다
     */
    private void processSeekerDetailResponse(String response) {
        item = Base.gson.fromJson(response,SeekerVO.class);
        seekerId.setText(item.getSeekerId());
        seekerName.setText(item.getSeekerName());
        seekerSex.setText(item.getSeekerSex());
        seekerBirth.setText(item.getSeekerBirth());
        seekerEmail.setText(item.getSeekerEmail());
        seekerCash.setText(Base.decimalFormat(item.getSeekerCash()));
        seekerAccount.setText(item.getSeekerAccount());
        seekerInfo.setText(item.getSeekerInfo());

        if (item.getSeekerPicture() != null) {
            showPicture(item);
        }
        //사진 존재 유무
        if (item.getSeekerPicture().length() > 0) {
            seekerIsPicture.setText("사진 있음");
        }else {
            seekerIsPicture.setText("없음");
        }

        //SeekerVO 에 위치정보가 없다면 mapContainer 에 위치를 선택할 수 있는 버튼을 보여준다
        if (item.getSeekerLatitude() == 0 ) {
            selectPointButtonFragment = new SelectPointButtonFragment();
            activity.getSupportFragmentManager().beginTransaction().add(R.id.mapContainer,selectPointButtonFragment).commit();

            //SeekerVO 에 위치정보가 있다면 다음맵 객체를 만들어서 mapContainer 에 addView 시켜준다
        } else {
            showMapView(item);

        }
    }

    /**
     * showMapView
     * @param item 에 있는 좌표정보를 이용하여 지도를 띄워준다
     */
    public void showMapView(SeekerVO item) {
        MapView mapView = new MapView(activity);
        mapContainer.addView(mapView);
        MapPOIItem myLocation = new MapPOIItem();
        myLocation.setItemName("나의 활동위치");
        myLocation.setTag(0);
        myLocation.setMapPoint(MapPoint.mapPointWithGeoCoord(item.getSeekerLatitude(),item.getSeekerLongitude()));
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(item.getSeekerLatitude(),item.getSeekerLongitude()),3,true);
        mapView.setPOIItemEventListener(this);
        mapView.addPOIItem(myLocation);
    }

    /**
     * 자격증 추가하는 팝업창을 띄운다
     */
    private void addCertificate() {
        Intent intent = new Intent(activity,AddCertificateActivity.class);
        intent.putExtra("seekerId",item.getSeekerId());
        activity.startActivityForResult(intent,402);
    }

    /**
     * takePicture
     * 사진찍기 화면으로 보내주고 결과값으로 사진을 가져온다
     */
    private void takePicture() {
        //촬영 후 이미지 가져옴
        String state = Environment.getExternalStorageState();

        //외장 메모리검사
        if (Environment.MEDIA_MOUNTED.equals(state)){
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (cameraIntent.resolveActivity(activity.getPackageManager())!=null){
                File photoFile = null;
                try{
                    photoFile = createImageFile();
                }catch (Exception e) {
                    e.printStackTrace();
                }
                if (photoFile != null) {
                    //getUriFroFile 의 두 번째 인자는 Manifest provider 의 authorites 와 일치해야함
                    Uri providerURI = FileProvider.getUriForFile(activity,activity.getPackageName(),photoFile);
                    imageUri = providerURI;
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,providerURI);
                    activity.startActivityForResult(cameraIntent,403);

                }
            } else {
                Snackbar.make(activity.getWindow().getDecorView().getRootView(),"패키지 실패",Snackbar.LENGTH_LONG).show();
            }
        }
    }

    /**
     * createImageFile
     * 카메라로 촬영한 이미지 생성하기
     * @return 카메라로 촬영된 이미지 파일
     */
    private File createImageFile() {
        String imgFileName = System.currentTimeMillis()+".jpg";
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures","gyeom");

        if (!storageDir.exists()){
            Log.d("FileInfo","storageDir 존재x" + storageDir.toString());
            storageDir.mkdirs();
        }
        Log.d("FileInfo","storageDis 존재함" + storageDir.toString());
        imageFile = new File(storageDir,imgFileName);
        image = imageFile;
        return imageFile;
    }

    /**
     * showPicture
     * 찍은 사진이 있다면 사진을 보여준다. 사진 유무는 onViewCreated 에서 결정된다
     * @param item seekerVO 의 이미지 정보를 가져온다
     */
    public void showPicture(SeekerVO item) {

        if (item == null){
            Bitmap bitmap;
            BitmapFactory.Options options = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),options);

            //이미지를 상황에 맞게 회전시킨다
            try {
                ExifInterface exif = new ExifInterface(image.getAbsolutePath());
                int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
                int exifDegree = exifOrientationToDegrees(exifOrientation);
                //bitmap = rotate(bitmap,exifDegree);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bitmap != null) {
                //비트맵을 문자열로 변환
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

                picture = bitmap;
                seekerPicture.setImageBitmap(bitmap);
            }

        }else {
            String url = getResources().getString(R.string.url) + item.getSeekerPicture();
            ImageRequest request = new ImageRequest(url, activity.getResources(), activity.getContentResolver(),
                    response -> setServerImage(response),
                    0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.ARGB_8888,
                    error -> {

                    });
            Base.requestQueue.add(request);
        }
    }

    private void setServerImage(Bitmap response) {
        seekerPicture.setRotation(90);
        seekerPicture.setImageBitmap(response);
    }

    /**
     * 이미지를 회전시킵니다.
     *
     * @param bitmap 비트맵 이미지
     * @param degrees 회전 각도
     * @return 회전된 이미지
     */
    private Bitmap rotate(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2);

            try {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != converted) {
                    bitmap.recycle();
                    bitmap = converted;
                }
            } catch (OutOfMemoryError ex) {
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
                return bitmap;
            }
        }
        return bitmap;
    }
    /**
     * EXIF정보를 회전각도로 변환하는 메서드
     *
     * @param exifOrientation EXIF 회전각
     * @return 실제 각도
     */
    private int exifOrientationToDegrees(int exifOrientation) {
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        } return 0;
    }

    /**
     * showSelectLocationActivity
     * SelectLocationActivity 좌표 선택 화면을 보여준다
     */
    public void showSelectLocationActivity() {
        Intent intent = new Intent(activity,SelectLocationActivity.class);
        intent.putExtra("lat",item.getSeekerLatitude());
        intent.putExtra("lng",item.getSeekerLongitude());
        activity.startActivityForResult(intent,401);
    }

    //다음 맵 아이템 클릭 리스너
    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        mapContainer.removeAllViews();
        showSelectLocationActivity();
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
    //end 다음 맵 아이템 클릭 리스너
}
