package com.jrt.flowlayout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.Checkable;

/**
 * 描述：AndroidDemoGather
 * 开发者：开发者的乐趣JRT
 * 创建时间：2017-3-17 11:15
 * CSDN地址：http://blog.csdn.net/Jiang_Rong_Tao/article
 * E-mail：jrtxb520@163.com
 **/
public class CheckableButton extends Button implements Checkable {
    private boolean mChecked;
    private static final int[] CHECKED_STATE_LIST = new int[] { android.R.attr.state_checked };
    private boolean mBroadcasting;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private OnCheckedChangeListener mOnCheckedChangeWidgetListener;
    public CheckableButton(Context context) {
        super(context);
    }
    public CheckableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public CheckableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (mChecked) {
            mergeDrawableStates(drawableState, CHECKED_STATE_LIST);
        }
        return drawableState;
    }
    /**
     *  改变按钮的选中状态
     *
     * @param checked
     */
    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            refreshDrawableState();
            // Avoid infinite recursions if setChecked() is called from a listener
            if (mBroadcasting) {
                return;
            }
            mBroadcasting = true;
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
            }
            if (mOnCheckedChangeWidgetListener != null) {
                mOnCheckedChangeWidgetListener.onCheckedChanged(this, mChecked);
            }
        }
    }
    @Override
    public boolean isChecked() {
        return mChecked;
    }
    @Override
    public void toggle() {
        setChecked(!mChecked);
    }
    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }
    void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeWidgetListener = listener;
    }
    /**
     * Interface definition for a callback to be invoked when the checked state
     * of a checkable button changed.
     */
    public interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a checkable button has changed.
         *
         * @param buttonView The checkable button view whose state has changed.
         * @param isChecked The new checked state of buttonView.
         */
        void onCheckedChanged(CheckableButton buttonView, boolean isChecked);
    }
}
