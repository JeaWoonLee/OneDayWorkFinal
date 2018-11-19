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

public class OfferIDFindFragment extends Fragment {

    FindIDActivity activity;
    EditText offerNmInput;
    EditText offerEmInput;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (FindIDActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_offer_idfind,container,false);

       offerNmInput = rootView.findViewById(R.id.offerNmInput);
       offerEmInput =  rootView.findViewById(R.id.offerEmInput);

        Button offerFindButton = rootView.findViewById(R.id.offerFindButton);
        offerFindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OfferIDFind();
            }
        });

        return rootView;
    }

    private void OfferIDFind(){

    }


}
