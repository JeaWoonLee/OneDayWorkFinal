package com.edu.lx.onedayworkfinal.offer.my_info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.offer.OfferMainActivity;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.OfferVO;

public class OfferMyInfoFragment extends Fragment {
    OfferMainActivity activity;

    public OfferVO item;

    TextView offerId;
    TextView offerName;
    TextView offerEmail;
    TextView companyNo;
    TextView companyName;
    TextView offerCash;
    TextView offerIsSign;
    Spinner accountSpinner;

    EditText offerAccount;
    EditText offerInfo;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (OfferMainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.offer_my_info_fragment,container,false);

        offerId = rootView.findViewById(R.id.offerId);
        offerName = rootView.findViewById(R.id.offerName);
        offerEmail = rootView.findViewById(R.id.offerEmail);
        companyNo = rootView.findViewById(R.id.companyNo);
        companyName = rootView.findViewById(R.id.companyName);
        offerCash = rootView.findViewById(R.id.offerCash);
        offerIsSign = rootView.findViewById(R.id.offerIsSign);
        accountSpinner = rootView.findViewById(R.id.accountSpinner);
        offerAccount = rootView.findViewById(R.id.offerAccount);
        offerInfo = rootView.findViewById(R.id.offerInfo);

        Button showSignButton = rootView.findViewById(R.id.showSignButton);
        showSignButton.setOnClickListener(v -> { });
        Button signRegistButton = rootView.findViewById(R.id.signRegistButton);
        signRegistButton.setOnClickListener(v -> registSign() );
        Button updateMyInfo = rootView.findViewById(R.id.updateMyInfo);
        updateMyInfo.setOnClickListener(v -> updateMyInfo(item));

        accountSpinner = rootView.findViewById(R.id.accountSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity.getApplicationContext(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.account));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountSpinner.setAdapter(adapter);

        return rootView;
    }

    public void registSign(){
        Intent intent = new Intent(activity,OfferDrawSignActivity.class);
        startActivityForResult(intent,411);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requestOfferDetail(Base.sessionManager.getUserDetails().get("id"));
    }

    public void updateMyInfo(OfferVO item){


    }

    public void requestOfferDetail(String id){
        Intent intent = new Intent(activity,OfferDrawSignActivity.class);
        startActivityForResult(intent,412);

    }


}
