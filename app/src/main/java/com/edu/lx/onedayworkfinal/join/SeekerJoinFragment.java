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
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.util.volley.Base;

import java.util.HashMap;
import java.util.Map;

public class SeekerJoinFragment extends Fragment {

    JoinActivity activity;

    //아이디 입력
    AppCompatEditText seekerIdInput;

    //패스워드 체크 버튼
    AppCompatEditText seekerPwInput;
    AppCompatEditText seekerPwReInput;

    //이메일 입력
    AppCompatEditText seekerEmailInput;

    //이름 입력
    AppCompatEditText seekerNameInput;

    //성별 체크 라디오 버튼
    RadioGroup sexGroup;
    RadioButton selectedRadio;

    //중복확인
    Button checkIdOverlapButton;
    final int OVERLAP_CHECK_FAIL = 0;
    final int OVERLAP_CHECK_SUCCESS = 1;
    int overlapIndex = OVERLAP_CHECK_FAIL;


    //회원가입 버튼
    Button seekerJoinButton;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (JoinActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.join_seeker_fragment,container,false);

        seekerIdInput = rootView.findViewById(R.id.seekerIdInput);
        seekerPwInput = rootView.findViewById(R.id.seekerPwInput);
        seekerPwReInput = rootView.findViewById(R.id.seekerPwReInput);
        sexGroup = rootView.findViewById(R.id.sexGroup);
        seekerEmailInput = rootView.findViewById(R.id.seekerEmailInput);
        seekerNameInput = rootView.findViewById(R.id.seekerNameInput);
        seekerJoinButton = rootView.findViewById(R.id.seekerJoinButton);
        checkIdOverlapButton = rootView.findViewById(R.id.checkIdOverlapButton);

        //성별 선택 라디오 버튼
        sexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioId = group.getCheckedRadioButtonId();
                selectedRadio = rootView.findViewById(radioId);
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //중복확인 체크 버튼
        checkIdOverlapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCheckOverlap();
            }
        });

        //구직자 회원가입 버튼
        seekerJoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestJoinSeeker();
            }
        });

    }

    //중복확인 체크
    private void requestCheckOverlap() {
        final String userId = seekerIdInput.getText().toString();
        String url = getResources().getString(R.string.url) + "checkSeekerOverlap.do";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processOverlapResult(response);

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("userId",userId);
                return params;
            }
        };

        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    //회원가입
    private void requestJoinSeeker() {

        //입력값 체크
        if (requiredCheck()) return;

        final String seekerId = seekerIdInput.getText().toString();
        final String seekerPw = seekerPwInput.getText().toString();
        final String seekerName = seekerNameInput.getText().toString();
        final String seekerEmail = seekerEmailInput.getText().toString();
        final String seekerSex = selectedRadio.getText().toString();

        String url = getResources().getString(R.string.url) + "joinSeeker.do";

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processJoinResponse(response);
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("seekerId",seekerId);
                params.put("seekerPw",seekerPw);
                params.put("seekerName",seekerName);
                params.put("seekerEmail",seekerEmail);
                params.put("seekerSex",seekerSex);
                return params;
            }
        };

        request.setShouldCache(false);
        Base.requestQueue.add(request);

    }


    /*
        회원가입 결과처리
        서버에서 insert 결과가 1 이라면 회원가입을 성공처리하고 finish() 한다
     */
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

    /*
        아이디 중복체크
        DB 에서 select count(*) 쿼리 결과가 1 이라면 해당 아이디가 존재하는 것 이므로 fail 처리한다
        쿼리 결과가 0 이라면 중복확인이 성공 한 것으로 간주하고 중복확인 버튼을 회색으로 만들면서 비활성화 한다
     */
    private void processOverlapResult(String response) {
        ResponseVO overlapResponse = Base.gson.fromJson(response, ResponseVO.class);

        //중복확인 결과가 0 (OVERLAP_CHECK_SUCCESS)
        if (overlapResponse.response == ResponseVO.OVERLAP_CHECK_SUCCESS) {

            //중복확인결과 성공처리
            overlapIndex = OVERLAP_CHECK_SUCCESS;

            //중복확인 버튼 비활성화
            checkIdOverlapButton.setClickable(false);
            checkIdOverlapButton.setBackgroundColor(getResources().getColor(R.color.gray,activity.getTheme()));

            //아이디 입력창 비활성화
            seekerIdInput.setClickable(false);
            seekerIdInput.setCursorVisible(false);
            seekerIdInput.setFocusable(false);
            seekerIdInput.setFocusableInTouchMode(false);
            seekerIdInput.setBackgroundColor(getResources().getColor(R.color.gray,activity.getTheme()));

        } else if (overlapResponse.response == ResponseVO.OVERLAP_CHECK_FAIL) {

            overlapIndex = OVERLAP_CHECK_FAIL;
            Toast.makeText(activity,"해당 아이디가 이미 존재합니다!",Toast.LENGTH_LONG).show();
        }
    }

    /*
        회원가입 버튼을 눌렀을 때, 서버에 요청을 보내기 전에 어플리케이션에서 데이터 무결성을 확인한다
        1. 각 항목(아이디, 패스워드, 이름, 이메일, 성별) 입력여부
        2. 아이디 중복확인 체크 여부
        3. 패스워드 확인 일치 여부
     */
    private boolean requiredCheck() {
        //아이디 체크
        if (TextUtils.isEmpty(seekerIdInput.getText())) {
            seekerIdInput.setError("아이디를 입력해야 합니다!");
            return true;

            //아이디 중복확인 체크
        } else if (overlapIndex == OVERLAP_CHECK_FAIL) {
            checkIdOverlapButton.setError("중복확인을 해야 합니다!");
            return true;

            //패스워드 체크
        }else if (TextUtils.isEmpty(seekerPwInput.getText())) {
            seekerPwInput.setError("패스워드를 입력해야 합니다!");
            return true;

            //패스워드 확인 체크
        } else if (!TextUtils.equals(seekerPwInput.getText(),seekerPwReInput.getText())) {
            seekerPwReInput.setError("패스워드를 다르게 입력하였습니다!");
            return true;

            //이름 체크
        } else if (TextUtils.isEmpty(seekerNameInput.getText())) {
            seekerNameInput.setError("이름을 입력해야 합니다!");
            return true;

            //이메일 체크
        } else if (TextUtils.isEmpty(seekerEmailInput.getText())) {
            seekerEmailInput.setError("이메일을 입력해야 합니다!");
            return true;

            //성별 체크
        } else if (sexGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(activity, "성별을 체크해야 합니다!", Toast.LENGTH_LONG).show();
            return true;
        }

        return false;

    }
}
