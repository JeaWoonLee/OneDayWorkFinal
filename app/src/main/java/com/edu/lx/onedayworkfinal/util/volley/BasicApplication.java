package com.edu.lx.onedayworkfinal.util.volley;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

public class BasicApplication extends MultiDexApplication {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    //TODO VolleyBaseSetting - RequestQueue
    //TODO Manifests 설정
}
