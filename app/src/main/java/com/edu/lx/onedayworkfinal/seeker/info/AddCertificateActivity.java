package com.edu.lx.onedayworkfinal.seeker.info;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.request.StringRequest;
import com.edu.lx.onedayworkfinal.R;
import com.edu.lx.onedayworkfinal.util.volley.Base;
import com.edu.lx.onedayworkfinal.vo.CertificationVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddCertificateActivity extends AppCompatActivity {

    Spinner certificateSpinner;
    EditText seekerCertificateNumber;

    String seekerId;
    String certificateNumber;
    String certificate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_certificate);
        Intent intent = getIntent();
        seekerId = intent.getStringExtra("seekerId");

        certificateSpinner = findViewById(R.id.certificateSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.certificate));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        certificateSpinner.setAdapter(adapter);
        seekerCertificateNumber = findViewById(R.id.seekerCertificateNumber);

        Button addCertificateButton = findViewById(R.id.addCertificateButton);
        addCertificateButton.setOnClickListener(v -> addCertificate());

        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> cancel());
    }


    private void addCertificate() {
        certificateNumber = seekerCertificateNumber.getText().toString();
        certificate = certificateSpinner.getSelectedItem().toString();

        updateCertificate(certificateNumber,certificate,seekerId);
    }

    private void updateCertificate(String certificateNumber, String certificate, String seekerId) {
        String url = getResources().getString(R.string.url) + "updateCertificate.do";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                this::processUpdateCertificateResponse,
                error -> {

                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("seekerCertificateNumber",certificateNumber);
                params.put("certificateName",certificate);
                params.put("seekerId",seekerId);
                return params;
            }
        };
        request.setShouldCache(false);
        Base.requestQueue.add(request);
    }

    /**
     * processUpdateCertificateResponse
     * @param response 업데이트 결과 처리
     */
    private void processUpdateCertificateResponse(String response) {
        int updateReulst = Integer.parseInt(response);
        if (updateReulst == 1) {
            Intent intent = new Intent();
            intent.putExtra("certificateNumber",certificateNumber);
            intent.putExtra("certificate",certificate);
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
    }

    private void cancel() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

}
