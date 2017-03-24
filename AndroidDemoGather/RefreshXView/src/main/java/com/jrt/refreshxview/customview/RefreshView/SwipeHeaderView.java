package com.jrt.refreshxview.customview.RefreshView;

import android.view.View;

public class SwipeHeaderView {

    public SwipeHeaderView(){}
    public SwipeHeaderView(View view, Object data, boolean isSelectable){
        this.view = view;
        this.data = data;
        this.isSelectable = isSelectable;
    }

    public View view;
    public Object data;
    public boolean isSelectable;

    public boolean isShow = true;

}
