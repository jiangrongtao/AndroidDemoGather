package com.jrt.flowlayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jrt.flowlayout.util.JRTDialog;
import com.jrt.flowlayout.util.JRTPopupWindow;
import com.jrt.flowlayout.view.CheckableButton;
import com.jrt.flowlayout.view.FlowLayout;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private MainActivity mContext;
    private Button mBtnShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mBtnShow = (Button) findViewById(R.id.bt_show);
    }

    public void showDialog(View view){
//         new JRTDialog(mContext,R.style.Animation_custom_popup);
        new JRTPopupWindow(mContext,mBtnShow);
    }
}
