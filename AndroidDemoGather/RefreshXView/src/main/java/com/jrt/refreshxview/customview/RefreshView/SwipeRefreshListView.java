package com.jrt.refreshxview.customview.RefreshView;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.jrt.refreshxview.R;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 SwipeRefreshLayout下拉刷新，上拉加载更多
 **/
public class SwipeRefreshListView extends SwipeRefreshLayout implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener{

    private static final int AUTO_REFRESH = 1, STOP_REFRESH = 2;

    public static final int[] REFRESH_COLORS = {android.R.color.holo_blue_bright, android.R.color.holo_green_light,
            android.R.color.holo_orange_light, android.R.color.holo_red_light};

    private Context context;

    /**
     * 底部自动加载状态：停止
     */
    private static final int AUTO_LOAD_STATUS_NONE = 0;
    /**
     * 底部自动加载状态：运行
     */
    private static final int AUTO_LOAD_STATUS_RUNNING = 1;

    /**
     * 底部自动加载当前状态
     */
    private int mCurrentAutoLoadStatus = AUTO_LOAD_STATUS_NONE;

    private boolean mBottomAutoRefresh = false;

    private boolean mAddedFooterView = false;

    private ListView mListView;
    private View mFooterView;

    private List<SwipeHeaderView> mListHeaderView = new LinkedList<>();
    private List<SwipeHeaderView> mListFooterView = new LinkedList<>();
    private ListAdapter mListAdapter;

    private OnPullRefreshListener mOnPullRefreshListener;
    private OnAutoLoadListener mOnAutoLoadListener;

    private OnScrollListener onScrollListener;

    private boolean inAdvanceLoading = false;

    public SwipeRefreshListView(Context context) {
        super(context);
        this.context = context;
        initLayout(null);
    }

    public SwipeRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initLayout(attrs);
    }

    private void initLayout(AttributeSet attrs) {
        mListView = new ListView(context, attrs);
        mListView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);
        mListView.setOnScrollListener(this);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFooterView = inflater.inflate(R.layout.view_auto_load, null);
        mFooterView.setVisibility(View.GONE);
        addView(mListView);
    }

    private Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case AUTO_REFRESH:
                    setRefreshing(true);
                    onRefresh();
                    break;
                case STOP_REFRESH:
                    setRefreshing(false);
                    break;
                default:
                    break;
            }
            return true;
        }
    });

    /**
     * 获取一个ListView
     *
     * @return
     */
    public ListView getRefreshableView() {
        return mListView;
    }


    public void setOnScrollCallBack(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    /**
     * 添加一个HeaderView
     *
     * @param view
     */
    public void addHeaderView(View view) {
        addHeaderView(view, null, false);
    }

    /**
     * 添加一个HeaderView
     *
     * @param view
     * @param data
     * @param isSelectable
     */
    public void addHeaderView(View view, Object data, boolean isSelectable) {
        SwipeHeaderView header = new SwipeHeaderView(view, data, isSelectable);
        mListHeaderView.add(header);
        initListViews();
    }

    /**
     * 删除一个头部View
     *
     * @param view
     */
    public void removeHeaderView(View view) {
        int count = mListHeaderView.size();
        for (int i = 0; i < count; i++) {
            SwipeHeaderView header = mListHeaderView.get(i);
            if (header.view == view) {
                header.isShow = false;
                break;
            }
        }
        initListViews();
    }

    public void addFooterView(View view) {
        addFooterView(view, null, false);
    }

    public void addFooterView(View view, Object data, boolean isSelectable) {
        SwipeHeaderView footer = new SwipeHeaderView(view, data, isSelectable);
        mListFooterView.add(footer);
    }

    public void removeFooterView(View view) {
        int count = mListFooterView.size();
        int removeIndex = -1;
        for (int i = 0; i < count; i++) {
            SwipeHeaderView footer = mListFooterView.get(i);
            if (footer.view == view) {
                footer.isShow = false;
                break;
            }
        }
        initListViews();
    }

    /**
     * 设置adapter
     *
     * @param adapter
     */
    public void setAdapter(ListAdapter adapter) {
        mListAdapter = adapter;
        initListViews();
    }

    /**
     * 初始化所有view
     */
    private void initListViews() {
        if (mListView == null) return;
        mListView.setAdapter(null);
        Iterator<SwipeHeaderView> iteratorHeader = mListHeaderView.iterator();
        while (iteratorHeader.hasNext()) {
            SwipeHeaderView view = iteratorHeader.next();
            mListView.removeHeaderView(view.view);
            if (!view.isShow) {
                iteratorHeader.remove();
            }
        }
        Iterator<SwipeHeaderView> iteratorFooter = mListFooterView.iterator();
        while (iteratorFooter.hasNext()) {
            SwipeHeaderView view = iteratorFooter.next();
            mListView.removeFooterView(view.view);
            if (!view.isShow) {
                iteratorFooter.remove();
            }
        }
        for (SwipeHeaderView view : mListHeaderView) {
            mListView.addHeaderView(view.view, view.data, view.isSelectable);
        }
        for (SwipeHeaderView view : mListFooterView) {
            mListView.addFooterView(view.view, view.data, view.isSelectable);
        }
        mListView.setAdapter(mListAdapter);
    }

    /**
     * 设置下拉刷新接口
     *
     * @param l
     */
    public void setOnPullRefreshListener(OnPullRefreshListener l) {
        mOnPullRefreshListener = l;
        if (mOnPullRefreshListener != null) {
            setColorSchemeResources(REFRESH_COLORS);
            setEnabled(true);
            setOnRefreshListener(this);
        } else {
            setEnabled(false);
            setOnRefreshListener(null);
        }
    }

    /**
     * 设置底部自动刷新接口
     *
     * @param l
     */
    public void setOnAutoLoadingListener(OnAutoLoadListener l) {
        mOnAutoLoadListener = l;
        if (mOnAutoLoadListener != null) {
            if (!mAddedFooterView) {
                addFooterView(mFooterView);
                initListViews();
                mAddedFooterView = true;
            }
            if (!mBottomAutoRefresh) {
                mBottomAutoRefresh = true;
                mFooterView.setPadding(0, 0, 0, 0);
            }
        } else {
            if (mBottomAutoRefresh) {
                mBottomAutoRefresh = false;
                mFooterView.setVisibility(View.GONE);
                mFooterView.setPadding(0, -mFooterView.getHeight(), 0, 0);
            }
        }
        invalidate();
    }

    /**
     * 设置是否预加载
     *
     * @param b
     */
    public void setInAdvanceLoading(boolean b) {
        this.inAdvanceLoading = b;
    }

    /**
     * 自动刷新
     */
    public void doAutoRefresh() {
        handler.sendEmptyMessageDelayed(AUTO_REFRESH, 500);
    }

    /**
     * 完成刷新
     */
    public void doComplete() {
        mCurrentAutoLoadStatus = AUTO_LOAD_STATUS_NONE;
        handler.sendEmptyMessageDelayed(STOP_REFRESH, 500);
    }

    @Override
    public void onRefresh() {
        if (mOnPullRefreshListener != null) mOnPullRefreshListener.onRefresh();
    }

    private void doLastItemVisible() {
        if (mCurrentAutoLoadStatus == AUTO_LOAD_STATUS_NONE) {
            mCurrentAutoLoadStatus = AUTO_LOAD_STATUS_RUNNING;
            mOnAutoLoadListener.onAutoLoad();
        }
    }

    private boolean mLastVisibleItem;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mOnAutoLoadListener == null) return;
        if (mBottomAutoRefresh && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
            mFooterView.setVisibility(View.VISIBLE);
        }
        if (inAdvanceLoading) {
            if (mBottomAutoRefresh && mLastVisibleItem) {
                doLastItemVisible();
            }
        } else {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE &&
                    mBottomAutoRefresh &&
                    mLastVisibleItem) {
                doLastItemVisible();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mOnAutoLoadListener != null) {
            if (inAdvanceLoading) {
                mLastVisibleItem = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount - 7);
            } else {
                mLastVisibleItem = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount - 1);
            }
        }
        if (onScrollListener != null) {
            onScrollListener.onScrollListenerCallBack(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    /**
     * 下拉刷新接口
     */
    public interface OnPullRefreshListener {
        /**
         * 下拉刷新回调方法
         */
        void onRefresh();
    }

    /**
     * 底部自动加载接口
     */
    public interface OnAutoLoadListener {
        /**
         * 底部自动加载回调方法
         */
        void onAutoLoad();
    }

    /**
     * 自定义监控滑动和加载更多需要实现此接口
     */
    public interface OnScrollListener {

        /**
         * 滑动回调接口
         *
         * @param view
         * @param firstVisibleItem
         * @param visibleItemCount
         * @param totalItemCount
         */
        void onScrollListenerCallBack(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
    }

}
