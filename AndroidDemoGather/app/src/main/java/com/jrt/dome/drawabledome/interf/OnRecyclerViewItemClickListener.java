package com.jrt.dome.drawabledome.interf;

import android.view.View;

/**
 * Created by lingbug on 2016-12-29.
 * RecyclerView 条目点击事件回调接口
 */

public interface OnRecyclerViewItemClickListener {
    /**
     * 条目点击事件
     * @param view
     * @param position
     */
    void onClickItem(View view, int position);
//    void onLongClickItem(View view, int position);
}
