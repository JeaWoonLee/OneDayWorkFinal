package com.edu.lx.onedayworkfinal.login.findInfo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.edu.lx.onedayworkfinal.R;

public class OfferPWFindFragment extends Fragment {

    OfferPwAlterFragment offerPwAlterFragment;

    FindPWActivity activity;
    EditText offerIDInput;
    EditText offerEmInput;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (FindPWActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container2, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_offer_pwfind,container2,false);

        offerIDInput = rootView.findViewById(R.id.offerIDInput);
        offerEmInput = rootView.findViewById(R.id.offerEmInput);

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


    }

    private void offerPwAlter(){

        activity.getSupportFragmentManager().beginTransaction().replace(R.id.container2,offerPwAlterFragment).commit();
    }

}
