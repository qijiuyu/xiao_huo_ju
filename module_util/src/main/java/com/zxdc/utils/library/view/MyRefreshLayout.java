package com.zxdc.utils.library.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

import com.zxdc.utils.library.util.LogUtils;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;


public class MyRefreshLayout extends BGARefreshLayout {

    /**
     * 接口解耦
     */
    MyRefreshLayoutListener mListener;

    boolean isLoadingMore = true;


    public MyRefreshLayout(Context context) {
        super(context);
    }

    public MyRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        setRefreshViewHolder(new BGAYaTangRefreshViewHolder(getContext(), true));
        setDelegate(new BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                if (mListener != null) {
                    setIsLoadingMoreEnabled(true);
                    mListener.onRefresh(refreshLayout);
                }
            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                if(!isLoadingMore){
                    return false;
                }else{
                    if (mListener != null) {
                        mListener.onLoadMore(refreshLayout);
                    }
                    return true;
                }
            }

        });
    }

    public void setMyRefreshLayoutListener(MyRefreshLayoutListener listener) {
        this.mListener = listener;
    }


    /**
     * 开始刷新
     */
    public void startRefresh(){
        new Handler().postDelayed(new Runnable() {
            public void run() {
                beginRefreshing();
            }
        },400);
    }

    /**
     * 刷新完成
     */
    public void refreshComplete() {
        endRefreshing();
    }

    /**
     * 加载完成
     */
    public void loadMoreComplete() {
        endLoadingMore();
    }

    /**
     * 设置是否可以使用上拉加载
     * @param isLoadingMoreEnabled false 为不能上拉加载
     */
    public void setIsLoadingMoreEnabled(boolean isLoadingMoreEnabled) {
        isLoadingMore = isLoadingMoreEnabled;
    }

}
