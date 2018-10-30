package com.edu.lx.onedayworkfinal.util.volley;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class Base extends MultiDexApplication {

    //Volley
    public static RequestQueue requestQueue;
    //Gson
    public static Gson gson;
    //init Volley
    @Override
    public void onCreate () {
        super.onCreate();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        gson = new Gson();
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
