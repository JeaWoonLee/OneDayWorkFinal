package com.edu.lx.onedayworkfinal.offer.my_info;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.ImageRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.offer.OfferMainActivity;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.OfferVO;

import java.util.HashMap;
import java.util.Map;

public class OfferMyInfoFragment extends Fragment {
    OfferMainActivity activity;

    public OfferVO item;

    TextView offerId;
    TextView offerName;
    TextView offerEmail;
    TextView companyNo;
    TextView companyName;
    TextView offerCash;
    TextView offerIsSign;
    LinearLayout offerSignLayout;
    ImageView offerSign;
    EditText offerAccount;

    Spinner accountSpinner;

    Button showSignButton;
    int offerSignVisible = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (OfferMainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.offer_my_info_fragment,container,false);

        offerId = rootView.findViewById(R.id.offerId);
        offerName = rootView.findViewById(R.id.offerName);
        offerEmail = rootView.findViewById(R.id.offerEmail);
        companyNo = rootView.findViewById(R.id.companyNo);
        companyName = rootView.findViewById(R.id.companyName);
        offerCash = rootView.findViewById(R.id.offerCash);
        offerIsSign = rootView.findViewById(R.id.offerIsSign);
        accountSpinner = rootView.findViewById(R.id.accountSpinner);
        offerAccount = rootView.findViewById(R.id.offerAccount);

        offerSignLayout= rootView.findViewById(R.id.offerSignLayout);
        offerSign = rootView.findViewById(R.id.offerSign);

        showSignButton = rootView.findViewById(R.id.showSignButton);
        Button signRegistButton = rootView.findViewById(R.id.signRegistButton);
        signRegistButton.setOnClickListener(v -> registSign() );
        Button updateMyInfo = rootView.findViewById(R.id.updateMyInfo);
        updateMyInfo.setOnClickListener(v -> updateMyInfo(item));

        accountSpinner = rootView.findViewById(R.id.accountSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity.getApplicationContext(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.account));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountSpinner.setAdapter(adapter);

        return rootView;
    }

    public void registSign(){
        Intent intent = new Intent(activity,OfferDrawSignActivity.class);
        intent.putExtra("offerVO",item.toString());
        startActivityForResult(intent,411);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requestOfferDetail(Base.sessionManager.getUserDetails().get("id"));
    }

    public void updateMyInfo(OfferVO item){
        String url = getResources().getString(R.string.url)+"updateOffer.do";
        SimpleMultiPartRequest request = new SimpleMultiPartRequest(Request.Method.POST,url,
                this::processUpdateOfferResult,
                error -> {

                })
        {
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<>();
                item.setBank(accountSpinner.getSelectedItem().toString());
                params.put("offerVO",item.toString());
                return params;
            }
        };
        request.addMultipartParam("offerVO","text/plain",item.toString());
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    private void processUpdateOfferResult(String response){
        int updateOfferResult = Integer.parseInt(response);
        if (updateOfferResult == 1){
            Toast.makeText(activity.getApplicationContext(),"정보가 성공적으로 변경 되었습니다.",Toast.LENGTH_LONG).show();

        }
    }

    public void requestOfferDetail(String id){
        String url = getResources().getString(R.string.url)+"requestOfferDetail.do";
        StringRequest request = new StringRequest(Request.Method.POST,url,
                this::processOfferDetailResponse,
                error -> {}){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("offerId",id);
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    private void processOfferDetailResponse(String response){
        item = Base.gson.fromJson(response,OfferVO.class);
        offerId.setText(item.getOfferId());
        offerName.setText(item.getOfferName());
        offerEmail.setText(item.getOfferEmail());
        companyName.setText(item.getCompanyName());
        companyNo.setText(item.getCompanyNo());
        offerCash.setText(String.valueOf(item.getOfferCash()));
        offerAccount.setText(item.getOfferAccount());
        if (item.getOfferSign() != null) {
            showSignButton.setOnClickListener(v -> {
                switch (offerSignVisible) {
                    case 0:
                        offerSignVisible = 1;
                        offerSignLayout.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        offerSignVisible = 0;
                        offerSignLayout.setVisibility(View.GONE);
                        break;
                }
            });

            offerIsSign.setText("등록됨");
            String url = getResources().getString(R.string.url) + item.getOfferSign();
            ImageRequest request = new ImageRequest(url, activity.getResources(), activity.getContentResolver(),
                    response1 -> offerSign.setImageBitmap(response1), 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888,
                    error -> {

                    });
            Base.requestQueue.add(request);
        }else {
            showSignButton.setVisibility(View.GONE);
        }
    }


}
