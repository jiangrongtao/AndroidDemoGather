package com.jrt.textselectmodetest;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 描述：文本的选中模式的使用
 * 开发者：开发者的乐趣JRT
 * 创建时间：2017-3-12 12:29
 * CSDN地址：http://blog.csdn.net/Jiang_Rong_Tao/article
 * E-mail：jrtxb520@163.com
 **/
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private EditText mTvSelect;
    private MainActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext =this;
        init();
    }
    private void init() {
        mTvSelect = (EditText) findViewById(R.id.tv_select);
        mTvSelect.setCustomSelectionActionModeCallback(new MyActionModeCallback());
    }
    private class MyActionModeCallback implements ActionMode.Callback {
        private Menu mMenu;
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater menuInflater = actionMode.getMenuInflater();
            menuInflater.inflate(R.menu.menu,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            this.mMenu=menu;
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.it_all:
                    //全选
                    mTvSelect.selectAll();
                    Toast.makeText(mContext, "完成全选", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.it_copy:
                    String selectText = getSelectText(SelectMode.COPY);
                    mTvSelect.setText(selectText);
                    Toast.makeText(mContext, "选中的内容已复制到剪切板", Toast.LENGTH_SHORT).show();
                    this.mMenu.close();
                    break;
                case R.id.it_cut:
                    //剪切
                    String txt = getSelectText(SelectMode.CUT);
                    mTvSelect.setText(txt);
                    Toast.makeText(mContext, "选中的内容已剪切到剪切板", Toast.LENGTH_SHORT).show();
                    this.mMenu.close();
                    break;

                case R.id.it_paste:
                    //获取剪切班管理者
                    ClipboardManager cbs = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                    if (cbs.hasPrimaryClip()){
                        mTvSelect.setText(cbs.getPrimaryClip().getItemAt(0).getText());
                    }
                    this.mMenu.close();
                    break;
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {

        }
    }

    /**
     *  统一处理复制和剪切的操作
     * @param mode 用来区别是复制还是剪切
     * @return
     */
    private String getSelectText(SelectMode mode) {
        //获取剪切班管理者
        ClipboardManager cbs = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
        //获取选中的起始位置
        int selectionStart = mTvSelect.getSelectionStart();
        int selectionEnd = mTvSelect.getSelectionEnd();
        Log.i(TAG,"selectionStart="+selectionStart+",selectionEnd="+selectionEnd);
        //截取选中的文本
        String txt = mTvSelect.getText().toString();
        String substring = txt.substring(selectionStart, selectionEnd);
        Log.i(TAG,"substring="+substring);
        //将选中的文本放到剪切板
        cbs.setPrimaryClip(ClipData.newPlainText(null,substring));
        //如果是复制就不往下操作了
        if (mode==SelectMode.COPY)
            return txt;
        txt = txt.replace(substring, "");
        return txt;
    }

    /**
     * 用枚举来区分是复制还是剪切
     */
    public enum SelectMode{
        COPY,CUT;
    }
}
