package com.itoutiao.news.navigationviewtest;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
/**
 * 描述：NavigationView的使用
 * 开发者：开发者的乐趣JRT
 * 创建时间：2017-3-11 23:29
 * CSDN地址：http://blog.csdn.net/Jiang_Rong_Tao/article
 * E-mail：jrtxb520@163.com
 **/
public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.nv_view)
    NavigationView mNvView;
    @InjectView(R.id.drawerlayout)
    DrawerLayout mDrawerlayout;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    private MainActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext =this;
        ButterKnife.inject(mContext);
        init();
    }

    private void init() {
        mToolbar.setTitle("开发者的乐趣JRT");
        initItemStates();
        //给NavigationView设置条目点击事件
        setNavigationItemSelected();
        //控制侧滑菜单和toolbar的状态
        changeDrawerLayoutAndToolbarStates();
        //修改头布局的文字或者图片
        editHeaderTitle();
    }

    private void setNavigationItemSelected() {
        //给NavigationView设置条目点击事件
        mNvView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //设置条目选中状态
                item.setChecked(true);
                //关闭侧滑菜单
                mDrawerlayout.closeDrawers();
                return false;
            }
        });
    }

    private void changeDrawerLayoutAndToolbarStates() {
        //将DrawerLayout和Toolbar关联使用 显示出三条横线的icon
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(mContext,mDrawerlayout,mToolbar,0,0);
        //开启异步
        toggle.syncState();
        //监听toggle的状态，控制菜单的显示于隐藏
        mDrawerlayout.addDrawerListener(toggle);
    }

    /**
     * 修改头布局
     */
    private void editHeaderTitle() {
        View headerView = mNvView.getHeaderView(0);
        TextView mTvHead = (TextView) headerView.findViewById(R.id.tv_head);
        mTvHead.setText("王者荣耀");
    }

    /**
     * 初始化菜单的Item的选中状态
     */
    private void initItemStates() {
        //一个存放颜色的数组
        int[] colors = new int[] { ContextCompat.getColor(this, R.color.icon_pre),
                        ContextCompat.getColor(this,R.color.icon_nor)};
        //选择状态的二维数组
        int[][] states = new int[2][];
        states[0] = new int[] { android.R.attr.state_checked};
        states[1] = new int[] {};//表示正常状态
        //设置item的icon选中的状态
        ColorStateList tintList = new ColorStateList(states, colors);
        mNvView.setItemIconTintList(tintList);
        //设置item的文本选中的颜色状态
        mNvView.setItemTextColor(tintList);
    }
}
