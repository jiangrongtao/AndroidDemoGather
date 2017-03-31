package com.jrt.bottomnavigation.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * 描述：AndroidDemoGather
 * 开发者：开发者的乐趣JRT
 * 创建时间：2017-3-31 16:48
 * CSDN地址：http://blog.csdn.net/Jiang_Rong_Tao/article
 * E-mail：jrtxb520@163.com
 **/
public class MyPageAdapter extends  PagerAdapter{
    List<ImageView> mList;
    public MyPageAdapter(List<ImageView> list){
        this.mList=list;
    }
    @Override
    public int getCount() {
        return mList.size();
    }
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView(mList.get(position));
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = mList.get(position);
        container.addView(imageView);
        return imageView;
    }

}
