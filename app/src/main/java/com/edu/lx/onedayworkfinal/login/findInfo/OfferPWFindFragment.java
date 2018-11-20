package com.edu.lx.onedayworkfinal.login.findInfo;

import android.content.Context;
import android.content.Intent;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.OfferVO;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class OfferPWFindFragment extends Fragment {


    FindPWActivity activity;
    EditText offerID;
    EditText offerEMail;
    TextView offerPW;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (FindPWActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container2, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_offer_pwfind,container2,false);

        offerID = rootView.findViewById(R.id.offerID);
        offerEMail = rootView.findViewById(R.id.offerEMail);
        offerPW = rootView.findViewById(R.id.offerPW);

        Button offerFindButton = rootView.findViewById(R.id.offerFindButton);
        offerFindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OfferPWFind();
            }
        });

        Button offerPwAlterButton = rootView.findViewById(R.id.offerPwAlterButton);
        offerPwAlterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offerPwAlter();
            }
        });
        return rootView;
    }

    private void OfferPWFind(){
        final String offerId = offerID.getText().toString();
        final String offerEmail = offerEMail.getText().toString();

        String url = getResources().getString(R.string.url)+"offerPwFind.do";
        StringRequest request = new StringRequest(Request.Method.POST,url,
                response -> {
                    OfferVO offerVO = Base.gson.fromJson(response,OfferVO.class);
                    if (offerVO != null){
                        processOfferPwFind(offerVO);
                    }else {
                        Toast.makeText(activity,"비밀번호를 정확히 입력하세요",Toast.LENGTH_LONG).show();
                    }
                },error -> Log.d("erroer", Arrays.toString(error.getStackTrace()))



        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("offerId",String.valueOf(offerId));
                params.put("offerEmail",String.valueOf(offerEmail));
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    private void processOfferPwFind(OfferVO offerVO){
        Toast.makeText(activity,"비밀번호를 찾았습니다.",Toast.LENGTH_LONG).show();
        offerPW.setText(offerVO.getOfferPw());

    }

    private void offerPwAlter(){
        Intent intent = new Intent(activity,OfferPwAlterActivity.class);
        startActivityForResult(intent,409);
    }

}
