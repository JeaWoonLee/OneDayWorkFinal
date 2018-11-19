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
import android.widget.TextView;

import com.edu.lx.onedayworkfinal.R;

public class SeekerPWFindFragment extends Fragment {

    SeekerPwAlterFragment seekerPwAlterFragment;

    FindPWActivity activity;

    EditText seekerIDInput;
    EditText seekerEmInput;
    TextView seekerPwCheck;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (FindPWActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container2, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_seeker_pwfind,container2,false);

        seekerIDInput = rootView.findViewById(R.id.seekerIDInput);
        seekerEmInput = rootView.findViewById(R.id.seekerEmInput);
        seekerPwCheck = rootView.findViewById(R.id.seekerPwCheck);

        Button seekerFindButton = rootView.findViewById(R.id.seekerFindButton);
        seekerFindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeekerPWFind();
            }
        });

        Button seekerPwAlterButton = rootView.findViewById(R.id.seekerPwAlterButton);
        seekerPwAlterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekerPwAlter();
            }
        });

        return rootView;
    }

    private void SeekerPWFind(){

    }

    private void seekerPwAlter(){

        activity.getSupportFragmentManager().beginTransaction().replace(R.id.container2,seekerPwAlterFragment).commit();
    }
}
