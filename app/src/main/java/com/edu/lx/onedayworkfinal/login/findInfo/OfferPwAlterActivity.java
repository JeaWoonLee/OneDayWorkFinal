package com.edu.lx.onedayworkfinal.login.findInfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.login.LoginActivity;

public class OfferPwAlterActivity extends AppCompatActivity {

    EditText offerPwNewInput;
    EditText offerPwReInput;
    Button offerPwCheckButton;
    Button gotoLoginPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_pw_alter);

        offerPwNewInput = findViewById(R.id.offerPwNewInput);
        offerPwReInput = findViewById(R.id.offerPwReInput);
        offerPwCheckButton = findViewById(R.id.offerPwCheckButton);
        gotoLoginPage = findViewById(R.id.gotoLoginPage);

        offerPwCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        gotoLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OfferPwAlterActivity.this,LoginActivity.class);
                startActivityForResult(intent,410);
            }
        });
    }
}
