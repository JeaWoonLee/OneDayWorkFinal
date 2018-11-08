package com.edu.lx.onedayworkfinal.util.volley;

import android.content.Context;
import android.location.LocationManager;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.edu.lx.onedayworkfinal.util.session.SessionManager;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Base extends MultiDexApplication {

    //Volley
    public static RequestQueue requestQueue;
    //Gson
    public static Gson gson;
    //LocationManager
    public static LocationManager locationManager;
    //1000 단위 콤마 찍어주기
    public static String decimalFormat(int num) {
        DecimalFormat dc = new DecimalFormat("###,###,###,###");
        return dc.format(num);
    }

    //SimpleDateFormat
    public static SimpleDateFormat simpleDateFormat;
    //Session Manager
    public static SessionManager sessionManager;
    //init Volley
    @Override
    public void onCreate () {
        super.onCreate();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        sessionManager = new SessionManager(getApplicationContext());
        gson = new Gson();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd", Locale.KOREA);
    }

    //MultiDex
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onTerminate () {
        super.onTerminate();
    }
}
