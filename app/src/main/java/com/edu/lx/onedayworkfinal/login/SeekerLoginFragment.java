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

public class SeekerLoginFragment extends Fragment {

    LoginActivity activity;
    EditText seekerIdInput;
    EditText seekerPwInput;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        activity = (LoginActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.login_seeker_fragment,container,false);

        seekerIdInput = rootView.findViewById(R.id.seekerIdInput);
        seekerPwInput = rootView.findViewById(R.id.seekerPwInput);

        Button seekerLoginButton = rootView.findViewById(R.id.seekerLoginButton);
        seekerLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSeeker();
            }
        });
        return rootView;
    }

    private void loginSeeker() {
        String seekerId = seekerIdInput.getText().toString();
        String seekerPw = seekerPwInput.getText().toString();

        Toast.makeText(activity,"loginSeeker Id : " + seekerId + ", Pw : " + seekerPw,Toast.LENGTH_LONG).show();
    }
}
