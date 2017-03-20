package com.daimajia.slider.library.Transformers;

import android.view.View;

import com.nineoldandroids.view.ViewHelper;
/**
 * 描述：放大旋转切换
 * 开发者：开发者的乐趣JRT
 * 创建时间：2017-3-20 13:02
 * CSDN地址：http://blog.csdn.net/Jiang_Rong_Tao/article
 * E-mail：jrtxb520@163.com
 **/
public class ZoomRotateOutTransformer extends BaseTransformer {

    @Override
    protected void onTransform(View view, float position) {
        final float rotation = -180f * position;
        ViewHelper.setPivotY(view,view.getHeight()*0.5f);
        ViewHelper.setPivotX(view,view.getWidth() * 0.5f);
        final float scale = 1f + Math.abs(position);
        ViewHelper.setScaleX(view,scale);
        ViewHelper.setScaleY(view,scale);

        ViewHelper.setAlpha(view,position < -1f || position > 1f ? 0f : 1f - (scale - 1f));
        if(position < -0.9){
            //-0.9 to prevent a small bug
            ViewHelper.setTranslationX(view,view.getWidth() * position);
            //TODO
            ViewHelper.setRotation(view,rotation);
        }
    }

}