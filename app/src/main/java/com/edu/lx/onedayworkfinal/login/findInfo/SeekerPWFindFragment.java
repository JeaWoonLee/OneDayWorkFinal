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

public class SeekerPWFindFragment extends Fragment {

    FindPWActivity activity;
    EditText seekerIDInput;
    EditText seekerEmInput;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (FindPWActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_seeker_pwfind,container,false);

        seekerIDInput = rootView.findViewById(R.id.seekerIDInput);
        seekerEmInput = rootView.findViewById(R.id.seekerEmInput);

        Button seekerFindButton = rootView.findViewById(R.id.seekerFindButton);
        seekerFindButton.setOnClickListener(v -> SeekerPWFind());

        return rootView;
    }

    private void SeekerPWFind(){

    }

}
