package com.jrt.dome.drawabledome;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;


import com.jrt.dome.drawabledome.base.BaseActivity;

public class DrawableDetailActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_drawable_detail;
    }

    @Override
    protected void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void initData() {
          showLoadingDialog();
    }

    @Override
    protected void initListener() {

    }
}
