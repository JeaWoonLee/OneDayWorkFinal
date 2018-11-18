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
        seekerFindButton.setOnClickListener(v -> SeekerIDFind());

        return rootView;
    }

    private void SeekerIDFind(){

    }

}
