package com.jrt.flowlayout.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jrt.flowlayout.R;
import com.jrt.flowlayout.view.CheckableButton;
import com.jrt.flowlayout.view.FlowLayout;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 描述：从下面平滑弹出的Dialog
 * 开发者：开发者的乐趣JRT
 * 创建时间：2017-3-17 12:21
 * CSDN地址：http://blog.csdn.net/Jiang_Rong_Tao/article
 * E-mail：jrtxb520@163.com
 **/
public class JRTDialog extends AlertDialog {
    private final Window mWindow;
    private String[] citys=new String[]{"北京","上海","深圳","广州","重庆",
            "郑州","合肥","青岛","西安","武汉",
            "兰州","保定","沈阳","石家庄","天津",
            "无锡","杭州","西宁","苏州","南京"};

    public JRTDialog(Context context, int themeResId) {
        super(context, themeResId);
        mWindow = getWindow();
        mWindow.setGravity(Gravity.BOTTOM);
        //R.style.Animation_custom_popup 自定义动画
        mWindow.setWindowAnimations(themeResId);
        show();//必须放在setContentView之前show
        setContentView(R.layout.jrt_dialog_loyout);
        addChildTo(context,((FlowLayout) findViewById(R.id.flow_layout)));
        initCallBack();
    }

    private void initCallBack() {
        ((Button)findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JRTDialog.this.dismiss();
            }
        });
    }

    private void addChildTo(final Context context, FlowLayout flowLayout) {
        final ArrayList<String> list = new ArrayList<String>();
        list.addAll(Arrays.asList(citys));
        for (int i = 0; i < list.size(); i++) {
            final int j = i;
            Button btn = new CheckableButton(context);
            btn.setHeight(TransUtils.dip2px(context,32));
            btn.setTextSize(16);
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
