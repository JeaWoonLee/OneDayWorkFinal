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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.seeker.SeekerMainActivity;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.SeekerVO;

import java.util.HashMap;
import java.util.Map;

public class SeekerLoginFragment extends Fragment {

    LoginActivity activity;
    EditText seekerIdInput;
    EditText seekerPwInput;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        activity = (LoginActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.login_seeker_fragment,container,false);

        seekerIdInput = rootView.findViewById(R.id.seekerIdInput);
        seekerPwInput = rootView.findViewById(R.id.seekerPwInput);

        //로그인 버튼
        Button seekerLoginButton = rootView.findViewById(R.id.seekerLoginButton);
        seekerLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSeeker();
            }
        });
        return rootView;
    }

    //구직자 로그인
    private void loginSeeker() {
        final String seekerId = seekerIdInput.getText().toString();
        final String seekerPw = seekerPwInput.getText().toString();

        String url = getResources().getString(R.string.url) + "seekerLogin.do";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SeekerVO seekerVO = Base.gson.fromJson(response,SeekerVO.class);
                        if (seekerVO != null) {
                            processSeekerLogin(seekerVO);
                        }else {
                            Toast.makeText(activity,"로그인에 실패했습니다",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error",error.getStackTrace().toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("userId",seekerId);
                params.put("userPw",seekerPw);
                return params;
            }
        };

        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    //로그인 결과 처리
    private void processSeekerLogin(SeekerVO seekerVO) {

        //노동자 로그인 결과를 세션에 담음
        Base.sessionSeeker = seekerVO;

        //노동자 메인화면으로 이동
        Intent intent = new Intent(activity, SeekerMainActivity.class);
        activity.startActivityForResult(intent,101);
    }


}
