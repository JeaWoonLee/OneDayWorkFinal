package com.edu.lx.onedayworkfinal.join;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


import com.android.volley.Request;

import com.android.volley.error.AuthFailureError;
import com.android.volley.request.StringRequest;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.OfferVO;

import java.util.HashMap;
import java.util.Map;

public class OfferJoinFragment extends Fragment {

    JoinActivity activity;

    public OfferVO item;

    //아이디입력
    AppCompatEditText offerIdInput;
    //패스워드 체크 버트
    AppCompatEditText offerPwInput;
    AppCompatEditText offerPwReInput;
    //이름 입력
    AppCompatEditText offerNameInput;
    //이메일 입력
    AppCompatEditText offerEmailInput;
    //계좌 입력
    AppCompatEditText offerAccountInput;
    Spinner accountSpinner;
    //회사명 입력
    AppCompatEditText companyNameInput;

    AppCompatEditText offerAddressInput;

    //중복 확인
    Button checkIdOverlapButton1;
    final int OVERLAP_CHECK_FAIL = 0;
    final int OVERLAP_CHECK_SUCCESS = 1;
    int overlapIndex = OVERLAP_CHECK_FAIL;

    Button offerJoinButton;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (JoinActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.join_offer_fragment,container,false);

        //사업자 회원가입 구현하기(김동가 - 회원가입 기능구현 종료)
        offerIdInput = rootView.findViewById(R.id.offerIdInput);
        offerPwInput = rootView.findViewById(R.id.offerPwInput);
        offerPwReInput = rootView.findViewById(R.id.offerPwReInput);
        offerEmailInput = rootView.findViewById(R.id.offerEmailInput);
        offerNameInput = rootView.findViewById(R.id.offerNameInput);
        offerJoinButton = rootView.findViewById(R.id.offerJoinButton);
        offerAccountInput = rootView.findViewById(R.id.offerAccountInput);
        companyNameInput = rootView.findViewById(R.id.companyNameInput);
        offerAddressInput = rootView.findViewById(R.id.offerAddressInput);
        checkIdOverlapButton1 = rootView.findViewById(R.id.checkIdOverlapButton1);

        accountSpinner = rootView.findViewById(R.id.accountSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity.getApplicationContext(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.account));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountSpinner.setAdapter(adapter);

        checkIdOverlapButton1.setOnClickListener(v -> requestCheckOverlap());


        offerJoinButton.setOnClickListener(v -> requestJoinOffer());

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void requestCheckOverlap(){
        if (TextUtils.isEmpty(offerIdInput.getText().toString())){
            Toast.makeText(activity,"아이디를 확인하여 주십시오",Toast.LENGTH_LONG).show();
            return;
        }

        final String userId = offerIdInput.getText().toString();
        String url = getResources().getString(R.string.url) +"checkOfferOverlap.do";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> processOverlapResult(response),
                error -> {

                }
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("userId",userId);
                return params;
            }
        };

        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    public void requestJoinOffer(){

        if (requiredCheck()) return;

        final String offerId = offerIdInput.getText().toString();
        final String offerPw = offerPwInput.getText().toString();
        final String offerName = offerNameInput.getText().toString();
        final String offerEmail = offerEmailInput.getText().toString();
        final String offerAccount = offerAccountInput.getText().toString();
        final String companyName = companyNameInput.getText().toString();
        final String bank = accountSpinner.getSelectedItem().toString();
        final String offerAddress = offerAddressInput.getText().toString();

        String url = getResources().getString(R.string.url) + "joinOffer.do";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> processJoinResponse(response),
                error -> {

                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("offerId",offerId);
                params.put("offerPw",offerPw);
                params.put("offerName",offerName);
                params.put("offerEmail",offerEmail);
                params.put("offerAccount",offerAccount);
                params.put("companyName",companyName);
                params.put("bank",bank);
                params.put("offerAddress",offerAddress);
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);

    }

    private void processJoinResponse (String response) {
        ResponseVO joinResponse = Base.gson.fromJson(response, ResponseVO.class);

        if (joinResponse.response == ResponseVO.JOIN_RESPONSE_SUCCESS) {
            Toast.makeText(activity,"회원가입이 완료되었습니다. \n 해당 아이디로 로그인 해주세요",Toast.LENGTH_LONG).show();
            activity.setResult(Activity.RESULT_OK);
            activity.finish();
        } else if (joinResponse.response == ResponseVO.JOIN_RESPONSE_FAIL) {
            Toast.makeText(activity,"회원가입에 실패하였습니다!",Toast.LENGTH_LONG).show();
        }
    }

    public void processOverlapResult(String response){

        ResponseVO overlapResponse = Base.gson.fromJson(response, ResponseVO.class);

        if (overlapResponse.response == ResponseVO.OVERLAP_CHECK_SUCCESS) {

            overlapIndex = OVERLAP_CHECK_SUCCESS;

            checkIdOverlapButton1.setClickable(false);
            checkIdOverlapButton1.setBackgroundColor(getResources().getColor(R.color.gray,activity.getTheme()));

            offerIdInput.setClickable(false);
            offerIdInput.setCursorVisible(false);
            offerIdInput.setFocusable(false);
            offerIdInput.setFocusableInTouchMode(false);
            offerIdInput.setBackgroundColor(getResources().getColor(R.color.gray,activity.getTheme()));

        } else if (overlapResponse.response == ResponseVO.OVERLAP_CHECK_FAIL) {

            overlapIndex = OVERLAP_CHECK_FAIL;
            Toast.makeText(activity,"해당 아이디가 이미 존재합니다!",Toast.LENGTH_LONG).show();
        }
    }

    private boolean requiredCheck() {
        if (TextUtils.isEmpty(offerIdInput.getText())) {
            offerIdInput.setError("아이디를 입력해야 합니다!");
            return true;

        } else if (overlapIndex == OVERLAP_CHECK_FAIL) {
            checkIdOverlapButton1.setError("중복확인을 해야 합니다!");
            return true;

        }else if (TextUtils.isEmpty(offerPwInput.getText())) {
            offerPwInput.setError("패스워드를 입력해야 합니다!");
            return true;

        } else if (!TextUtils.equals(offerPwInput.getText(),offerPwReInput.getText())) {
            offerPwReInput.setError("패스워드를 다르게 입력하였습니다!");
            return true;

        } else if (TextUtils.isEmpty(offerNameInput.getText())) {
            offerNameInput.setError("이름을 입력해야 합니다!");
            return true;

        } else if (TextUtils.isEmpty(offerEmailInput.getText())) {
            offerEmailInput.setError("이메일을 입력해야 합니다!");
            return true;

        } else if (TextUtils.isEmpty(offerAccountInput.getText())){
            offerAccountInput.setError("계좌번호를 입력하셔야 합니다");
            return true;

        } else if (TextUtils.isEmpty(companyNameInput.getText())){
            companyNameInput.setError("사명을 적어주십시오");
            return true;
        }else if (TextUtils.isEmpty(offerAddressInput.getText())){
            offerAddressInput.setError("주소를 입력해주세요");
        }
        return false;
    }

}
