package com.daimajia.slider.library.Transformers;

import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * 描述：垂直旋转切换
 * 开发者：开发者的乐趣JRT
 * 创建时间：2017-3-20 13:02
 * CSDN地址：http://blog.csdn.net/Jiang_Rong_Tao/article
 * E-mail：jrtxb520@163.com
 **/
public class FlipVerticalTransformer extends BaseTransformer {

    @Override
    protected void onTransform(View view, float position) {
        final float rotation = 180f * position;
        ViewHelper.setAlpha(view,rotation > 90f || rotation < -90f ? 0 : 1);
        ViewHelper.setPivotY(view,view.getHeight()*0.5f);
        ViewHelper.setPivotX(view,view.getWidth() * 0.5f);
        ViewHelper.setRotationX(view,rotation);
    }
}
