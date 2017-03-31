package com.jrt.bottomnavigation;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.jrt.bottomnavigation.adapter.MyPageAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomNavigationBar;
    @InjectView(R.id.viewPager)
    ViewPager mViewPager;
    List<ImageView> mList;
    private MainActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        initView();
        initData();
    }

    private void initView() {
        mContext = this;
        ButterKnife.inject(mContext);
    }

    private void initData() {
        BadgeItem badgeItem = new BadgeItem();
        badgeItem.setText("99");
//        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_home_nor, "首页").setActiveColorResource(R.color.red).setBadgeItem(badgeItem))
                .addItem(new BottomNavigationItem(R.drawable.ic_video_nor, "视频").setActiveColorResource(R.color.green))
                .addItem(new BottomNavigationItem(R.drawable.ic_mycenter_nor, "我的").setActiveColorResource(R.color.blue))
                .initialise();
        initViewPager();
    }

    private void initViewPager() {
        mList=new ArrayList<>();
        int[] images=new int[]{R.drawable.banner1,R.drawable.banner2,R.drawable.banner3};
        for (int i = 0; i <images.length ; i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(images[i]);
            mList.add(imageView);
        }
        mViewPager.setAdapter(new MyPageAdapter(mList));
        mBottomNavigationBar.bindViewPager(mViewPager);
    }
}
