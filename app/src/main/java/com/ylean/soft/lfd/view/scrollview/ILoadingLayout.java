package com.ylean.soft.lfd.view.scrollview;

public interface ILoadingLayout {
    public void pullToRefresh();

    public void releaseToRefresh();

    public void refreshing();

    public void normal();
}

