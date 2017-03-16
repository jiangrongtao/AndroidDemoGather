package com.jrt.dome.drawabledome.base;

import android.app.Application;

/**
 * Created by 0bug-yun on 2016/8/12.
 */
public class MyApplication extends Application {
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static MyApplication getInstance() {
         return mInstance;
    }
}
