package com.edu.lx.onedayworkfinal.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.edu.lx.onedayworkfinal.R;

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
        String offerId = offerIdInput.getText().toString();
        String offerPw = offerPwInput.getText().toString();

        Toast.makeText(activity,"loginOffer Id : " + offerId + ", Pw : " + offerPw,Toast.LENGTH_LONG).show();
    }
}
