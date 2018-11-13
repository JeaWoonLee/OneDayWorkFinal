package com.edu.lx.onedayworkfinal.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.offer.OfferMainActivity;
import com.edu.lx.onedayworkfinal.util.session.SessionManager;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.OfferVO;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class OfferLoginFragment extends Fragment {

    LoginActivity activity;
    EditText offerIdInput;
    EditText offerPwInput;
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        activity = (LoginActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.login_offer_fragment,container,false);
        offerIdInput = rootView.findViewById(R.id.offerIdInput);
        offerPwInput = rootView.findViewById(R.id.offerPwInput);

        Button seekerLoginButton = rootView.findViewById(R.id.offerLoginButton);
        seekerLoginButton.setOnClickListener(v -> loginOffer());
        return rootView;
    }

    private void loginOffer() {
        final String offerId = offerIdInput.getText().toString();
        final String offerPw = offerPwInput.getText().toString();

        String url = getResources().getString(R.string.url) + "offerMobileLogin.do";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    OfferVO offerVO = Base.gson.fromJson(response,OfferVO.class);
                    if (offerVO != null) {
                        processOfferLogin(offerVO);
                    }else {
                        Toast.makeText(activity,"로그인에 실패하였습니다",Toast.LENGTH_LONG).show();
                    }
                },
                error -> Log.i("error", Arrays.toString(error.getStackTrace()))
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userId",offerId);
                params.put("userPw",offerPw);

                return params;
            }
        };

        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    private void processOfferLogin(OfferVO offerVO) {
        Toast.makeText(activity,"로그인에 성공하였습니다",Toast.LENGTH_LONG).show();
        //TODO 사업자 로그인 구현하기(윤정민 - 종료)
        // 사업자 로그인 정보를 세션에 담기
        Base.sessionManager.createLoginSession(offerVO.getOfferName(),offerVO.getOfferId(), SessionManager.IS_OFFER);

        Intent intent = new Intent(activity, OfferMainActivity.class);
        activity.startActivityForResult(intent,101);

    }
}
