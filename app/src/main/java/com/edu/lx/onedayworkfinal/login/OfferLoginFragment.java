package com.edu.lx.onedayworkfinal.login;

import android.content.Context;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.util.volley.BaseApplication;

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
        seekerLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOffer();
            }
        });
        return rootView;
    }

    private void loginOffer() {
        final String offerId = offerIdInput.getText().toString();
        final String offerPw = offerPwInput.getText().toString();

        String url = getResources().getString(R.string.url) + "loginOffer.do";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("onResponse",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userId",offerId);
                params.put("userPw",offerPw);

                return params;
            }
        };

        request.setShouldCache(false);
        BaseApplication.requestQueue.add(request);
    }
}
