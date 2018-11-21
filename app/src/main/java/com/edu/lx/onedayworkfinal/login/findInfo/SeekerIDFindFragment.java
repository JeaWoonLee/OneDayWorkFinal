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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.request.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.SeekerVO;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SeekerIDFindFragment extends Fragment {

    FindIDActivity activity;
    EditText seekerNmInput;
    EditText seekerEmInput;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (FindIDActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_seeker_idfind,container,false);

        seekerNmInput = rootView.findViewById(R.id.seekerNmInput);
        seekerEmInput = rootView.findViewById(R.id.seekerEmInput);

        Button seekerFindButton = rootView.findViewById(R.id.seekerFindButton);
        seekerFindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeekerIDFind();
            }
        });

        return rootView;
    }

    private void SeekerIDFind(){

        final String seekerEm = seekerEmInput.getText().toString();
        final String seekerNm = seekerNmInput.getText().toString();

        String url = getResources().getString(R.string.url)+"seekerIdFind.do";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    SeekerVO seekerVO = Base.gson.fromJson(response,SeekerVO.class);
                    if (seekerVO != null){
                        ProcessSeekerIdFind();
                    }else {
                        Toast.makeText(activity,"고객님의 아이디를 찾을 수 없습니다",Toast.LENGTH_LONG).show();
                    }
                },
                error -> Log.i("error", Arrays.toString(error.getStackTrace()))
        ){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params= new HashMap<>();
                params.put("userEm",seekerEm);
                params.put("userNm",seekerNm);
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    private void ProcessSeekerIdFind(){
        Toast.makeText(activity,"고객님의 아이디를 찾았습니다.",Toast.LENGTH_LONG).show();
    }
}
