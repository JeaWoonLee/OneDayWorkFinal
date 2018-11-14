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

import com.edu.lx.onedayworkfinal.R;

public class AddCertificateActivity extends AppCompatActivity {

    Spinner certificateSpinner;
    EditText seekerCertificateNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_certificate);

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
        String certificateNumber = seekerCertificateNumber.getText().toString();
        String certificate = certificateSpinner.getSelectedItem().toString();

        Intent intent = new Intent();
        intent.putExtra("certificateNumber",certificateNumber);
        intent.putExtra("certificate",certificate);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    private void cancel() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
