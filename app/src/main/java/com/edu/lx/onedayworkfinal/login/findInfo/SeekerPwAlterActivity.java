package com.edu.lx.onedayworkfinal.login.findInfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.login.LoginActivity;

public class SeekerPwAlterActivity extends AppCompatActivity {

    EditText seekerPwNewInput;
    EditText seekerPwReInput;
    Button seekerPwCheckButton;
    Button gotoLoginPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker_pw_alter);

        seekerPwNewInput = findViewById(R.id.seekerPwNewInput);
        seekerPwReInput = findViewById(R.id.seekerPwReInput);
        seekerPwCheckButton = findViewById(R.id.seekerPwCheckButton);
        gotoLoginPage = findViewById(R.id.gotoLoginPage);

        seekerPwCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        gotoLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeekerPwAlterActivity.this,LoginActivity.class);
                startActivityForResult(intent,411);
            }
        });
    }
}
