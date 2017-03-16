package com.jrt.dome.drawabledome.utils;

import android.content.Context;
import android.view.WindowManager;

import com.jrt.dome.drawabledome.base.MyApplication;


/**
 * Created by 0bug-yun on 2016/8/24.
 */
public class WindowsUtil {
    static WindowManager wm = (WindowManager) MyApplication.getInstance()
            .getSystemService(Context.WINDOW_SERVICE);

    public static int getWindowsWidth() {

        return wm.getDefaultDisplay().getWidth();
    }

    public static int getWindowsHeight() {
        return wm.getDefaultDisplay().getHeight();
    }


}
