package com.edu.lx.onedayworkfinal.login.findInfo;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.edu.lx.onedayworkfinal.R;

public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner accountspinner = (Spinner) findViewById(R.id.accountSpinner);
//        accountspinner.setOnItemClickListener();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
