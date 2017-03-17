package com.jrt.flowlayout.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.jrt.flowlayout.R;
import com.jrt.flowlayout.view.CheckableButton;
import com.jrt.flowlayout.view.FlowLayout;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 描述：AndroidDemoGather
 * 开发者：开发者的乐趣JRT
 * 创建时间：2017-3-17 13:36
 * CSDN地址：http://blog.csdn.net/Jiang_Rong_Tao/article
 * E-mail：jrtxb520@163.com
 **/
public class JRTPopupWindow extends PopupWindow {
    private String[] citys=new String[]{"我的美丽大庆阳","帝都北京","深圳","花都广州","雾都重庆",
            "郑州","合肥","青岛","八百里秦川西安","武汉","魔都上海",
            "金城兰州","保定","沈阳","石家庄","冰城哈尔滨",
            "无锡","杭州","西宁","苏州"};

    public JRTPopupWindow(final Activity activity,View view) {
        View dialogContent = View.inflate(activity, R.layout.jrt_dialog_loyout, null);
        addChildTo(activity,((FlowLayout) dialogContent.findViewById(R.id.flow_layout)));
        setAnimationStyle(R.style.Animation_custom_popup);
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.6f;
        activity.getWindow().setAttributes(lp);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(dialogContent);
        showAtLocation(view, Gravity.BOTTOM, 0, 0);
        update();
        ((Button)dialogContent.findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dimissPop();
            }
        });
        setOnDismissListener(new OnDismissListener() {
            public void onDismiss() {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.alpha = 1f;
                activity.getWindow().setAttributes(lp);
            }
        });
    }

    public void dimissPop() {
        dismiss();
    }
    private void addChildTo(final Context context, FlowLayout flowLayout) {
        final ArrayList<String> list = new ArrayList<String>();
        list.addAll(Arrays.asList(citys));
        for (int i = 0; i < list.size(); i++) {
            final int j = i;
            Button btn = new CheckableButton(context);
            btn.setHeight(TransUtils.dip2px(context,28));
            btn.setTextSize(14);
            btn.setTextColor(context.getResources().getColorStateList(R.color.checkable_text_color));
            btn.setBackgroundResource(R.drawable.checkable_background);
            btn.setText(list.get(i));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "您点击了" + list.get(j), Toast.LENGTH_SHORT).show();
                }
            });
            flowLayout.addView(btn);
        }
    }
}