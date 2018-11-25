package com.edu.lx.onedayworkfinal.login.findInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.login.LoginActivity;

public class OfferPwAlterActivity extends AppCompatActivity {

    EditText offerPwNewInput;
    EditText offerPwReInput;
    Button offerPwAlterCheckButton;
    Button gotoLoginPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_pw_alter);


        offerPwNewInput = findViewById(R.id.offerPwNewInput);
        offerPwReInput = findViewById(R.id.offerPwReInput);
        offerPwAlterCheckButton = findViewById(R.id.offerPwAlterCheckButton);
        gotoLoginPage = findViewById(R.id.gotoLoginPage);


        offerPwAlterCheckButton.setOnClickListener(v -> {

        });

        gotoLoginPage.setOnClickListener(v -> {
            Intent intent = new Intent(OfferPwAlterActivity.this,LoginActivity.class);
            startActivityForResult(intent,410);
        });
    }
}
