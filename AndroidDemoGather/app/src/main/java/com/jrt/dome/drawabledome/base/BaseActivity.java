package com.jrt.dome.drawabledome.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ant.liao.GifView;
import com.jrt.dome.drawabledome.R;
import com.jrt.dome.drawabledome.utils.WindowsUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by lingbug on 2016-12-29.
 */

public abstract class  BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected Dialog mLoadingDialog;
    protected BaseActivity mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        processLollipopAbove();
        setContentView(getLayoutId());
        mContext=this;
        initView();
        initData();
        initListener();
    }

    protected abstract int getLayoutId();
    protected abstract void initView();
    protected abstract void initData();
    protected abstract void initListener();
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 显示加载等待Dialog
     */
    public void showLoadingDialog() {
        showLoadingDialog("拼命加载中...");
    }

    /**
     * 显示加载等待Dialog
     *
     * @param loadingStr
     */
    public void showLoadingDialog(String loadingStr) {
        dismissDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        mLoadingDialog = builder.create();
        mLoadingDialog.show();
        LayoutInflater inflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.loading_dialog_view, null);
        TextView tipTextview = (TextView) view.findViewById(R.id.tipTextView);
        GifView gifView = (GifView) view.findViewById(R.id.gifView);
        gifView.setGifImage(R.drawable.aa);
        if (TextUtils.isEmpty(loadingStr)) {
            tipTextview.setVisibility(View.GONE);
        } else {
            tipTextview.setVisibility(View.VISIBLE);
            tipTextview.setText(loadingStr);
        }
        mLoadingDialog.setContentView(view);
        Window dialogWindow = mLoadingDialog.getWindow();
        mLoadingDialog.setCanceledOnTouchOutside(false);
        dialogWindow.setGravity(Gravity.CENTER);
        android.view.WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.height = getWindowManager().getDefaultDisplay().getWidth() * 1/4;
        layoutParams.width = getWindowManager().getDefaultDisplay().getWidth() * 2/ 3;
        dialogWindow.setAttributes(layoutParams);
        mLoadingDialog.show();
    }
    /**
     * 显示加载等待Dialog
     *
     *
     */
    public void showWiatDialog() {
        dismissDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        mLoadingDialog = builder.create();
        mLoadingDialog.show();
        LayoutInflater inflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.loading_dialog_view, null);
        TextView tipTextview = (TextView) view.findViewById(R.id.tipTextView);
        tipTextview.setVisibility(View.GONE);
        GifView gifView = (GifView) view.findViewById(R.id.gifView);
        gifView.setGifImage(R.drawable.bb);
        mLoadingDialog.setContentView(view);
        Window dialogWindow = mLoadingDialog.getWindow();
        mLoadingDialog.setCanceledOnTouchOutside(false);
        dialogWindow.setGravity(Gravity.CENTER);
        android.view.WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        int i = WindowsUtil.getWindowsWidth() * 1 / 3;
        layoutParams.height =i;
        layoutParams.width = i;
        dialogWindow.setAttributes(layoutParams);
        mLoadingDialog.show();
    }
    /**
     * 隐藏加载等待Dialog
     */
    public void dismissDialog() {
        if (mLoadingDialog != null) {
            try {
                mLoadingDialog.dismiss();
                mLoadingDialog = null;
            } catch (Exception e) {
            }
        }
    }


    @Override
    public void onClick(View view) {

    }

    /**

     * 处理Lollipop以上

     * Lollipop可以设置为沉浸，不能设置字体颜色(所以白色背景会很丑)

     * M(API23)可以设定

     */

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void processLollipopAbove() {
        processMIUI(true);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            return;
        }

        int flag = window.getDecorView().getSystemUiVisibility();

        if (true) {
            flag |= (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS

                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }

        if (true) {

            //改变字体颜色

            flag |=  View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

        }

        window.getDecorView().setSystemUiVisibility(flag);

        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    /**
     * 改变小米的状态栏字体颜色为黑色, 要求MIUI6以上
     *lightStatusBar为真时表示黑色字体
     * @param lightStatusBar
     */
    private void processMIUI(boolean lightStatusBar) {
        Window window = getWindow();
        Class<? extends Window> clazz = window.getClass();

        try {

            int darkModeFlag;

            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");

            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");

            darkModeFlag = field.getInt(layoutParams);

            Method extraFlagField = clazz.getMethod("setExtraFlags",int.class,int.class);

            extraFlagField.invoke(window, lightStatusBar? darkModeFlag : 0, darkModeFlag);

        } catch (Exception ignored) {

        }

    }
}
