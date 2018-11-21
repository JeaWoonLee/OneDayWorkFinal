package com.edu.lx.onedayworkfinal.login.findInfo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.request.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.OfferVO;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class OfferIDFindFragment extends Fragment {

    FindIDActivity activity;
    EditText offerNmInput;
    EditText offerEmInput;
    TextView offerIdCheck;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (FindIDActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_offer_idfind,container,false);

       offerNmInput = rootView.findViewById(R.id.offerNmInput);
       offerEmInput =  rootView.findViewById(R.id.offerEmInput);
       offerIdCheck = rootView.findViewById(R.id.offerIdCheck);

        Button offerFindButton = rootView.findViewById(R.id.offerFindButton);
        offerFindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OfferIDFind();
            }
        });

        return rootView;
    }

    private void OfferIDFind(){
        final String offerEm = offerNmInput.getText().toString();
        final String offerNm = offerEmInput.getText().toString();

        String url = getResources().getString(R.string.url)+"offerIdFind.do";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    OfferVO offerVO = Base.gson.fromJson(response,OfferVO.class);
                    if (offerVO != null){
                        processOfferIdFind(offerVO);
                    }else{
                        Toast.makeText(activity,"정보를 정확히 입력해주십시요",Toast.LENGTH_LONG).show();
                    }
                },
                error -> Log.i("error", Arrays.toString(error.getStackTrace()))
        ){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("userEm",offerEm);
                params.put("userNm",offerNm);
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    private void processOfferIdFind(OfferVO offerVO){
        Toast.makeText(activity,"아이디를 찾았습니다.",Toast.LENGTH_LONG).show();

        offerIdCheck.setText(offerVO.getOfferId());
    }


}
