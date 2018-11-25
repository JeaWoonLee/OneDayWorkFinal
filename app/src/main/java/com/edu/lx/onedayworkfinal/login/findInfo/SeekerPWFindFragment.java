package com.edu.lx.onedayworkfinal.login.findInfo;

import android.content.Context;
import android.content.Intent;
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
import com.android.volley.error.AuthFailureError;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.SeekerVO;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SeekerPWFindFragment extends Fragment {


    FindPWActivity activity;

    EditText seekerID;
    EditText seekerEMail;
    TextView seekerPW;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (FindPWActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container2, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_seeker_pwfind,container2,false);

        seekerID = rootView.findViewById(R.id.seekerID);
        seekerEMail = rootView.findViewById(R.id.seekerEMail);
        seekerPW = rootView.findViewById(R.id.seekerPW);

        Button seekerFindButton = rootView.findViewById(R.id.seekerFindButton);
        seekerFindButton.setOnClickListener(v -> SeekerPWFind());

        return rootView;
    }

    private void SeekerPWFind(){
        final String seekerId = seekerID.getText().toString();
        final String seekerEmail = seekerEMail.getText().toString();

        String url = getResources().getString(R.string.url)+"seekerPwFind.do";
        StringRequest request = new StringRequest(Request.Method.POST,url,
                response -> {
                    SeekerVO seekerVO = Base.gson.fromJson(response,SeekerVO.class);
                    if (seekerVO != null){
                        processSeekerPwFind(seekerVO);
                    }else {
                        Toast.makeText(activity,"비밀번호를 정확히 입력해주시길 바랍니다.",Toast.LENGTH_LONG).show();
                    }
                },error -> Log.d("error", Arrays.toString(error.getStackTrace()))
                ){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String > params = new HashMap<>();
                params.put("seekerId",String.valueOf(seekerId));
                params.put("seekerEmail",String.valueOf(seekerEmail));
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    private void processSeekerPwFind(SeekerVO seekerVO){
        Toast.makeText(activity,"비밀번호를 확인하였습니다",Toast.LENGTH_LONG).show();
        seekerPW.setText(seekerVO.getSeekerPw());

        Intent intent = new Intent(activity,SeekerPwAlterActivity.class);
        startActivityForResult(intent,408);
    }

}
