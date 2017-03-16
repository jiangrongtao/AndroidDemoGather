package com.jrt.dome.drawabledome;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.jrt.dome.drawabledome.adapter.MyDrawableListAdapter;
import com.jrt.dome.drawabledome.base.BaseActivity;
import com.jrt.dome.drawabledome.interf.OnRecyclerViewItemClickListener;
import com.jrt.dome.drawabledome.view.DividerItemDecoration;

public class MainActivity extends BaseActivity {
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private DividerItemDecoration mItemDecoration;
    private MyDrawableListAdapter myDrawableListAdapter;
    private CollapsingToolbarLayout collapsLayout;

    @Override
    protected int getLayoutId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
         mToolbar = (Toolbar) findViewById(R.id.toolbar);
         mRecyclerView = (RecyclerView) findViewById(R.id.rv_recyclerView);
         collapsLayout = (CollapsingToolbarLayout) findViewById(R.id.ctl_Collaps);
        showLoadingDialog();
    }

    @Override
    protected void initData() {
        //初始化Toolbar
        setSupportActionBar(mToolbar);
        //初始化收缩布局
        collapsLayout.setTitle("Drawable的不同用法");
        collapsLayout.setExpandedTitleColor(Color.WHITE);
        collapsLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this,R.color.colorAccent));
        collapsLayout.setCollapsedTitleGravity(Gravity.CENTER_HORIZONTAL);
        collapsLayout.setExpandedTitleGravity(Gravity.BOTTOM|Gravity.CENTER);
        //初始化RecyclerView
        String[] datas=getResources().getStringArray(R.array.drawable_name);
        myDrawableListAdapter = new MyDrawableListAdapter(mContext, datas);
        mRecyclerView.setAdapter(myDrawableListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        //设置RecyclerView的分割线
        mItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(mItemDecoration);
        SystemClock.sleep(1000);
        dismissDialog();
    }

    @Override
    protected void initListener() {
       if (myDrawableListAdapter!=null){
           myDrawableListAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
               @Override
               public void onClickItem(View view, int position) {
                   Intent intent=new Intent(mContext,DrawableDetailActivity.class);
                   intent.putExtra("position",position);
                   startActivity(intent);
               }
           });
       }
    }
}
