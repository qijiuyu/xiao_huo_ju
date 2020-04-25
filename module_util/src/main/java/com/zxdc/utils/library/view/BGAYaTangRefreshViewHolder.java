package com.zxdc.utils.library.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ProgressBar;

import com.zxdc.utils.library.R;

import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 13:05
 * 描述:类似新浪微博下拉刷新风格
 */
public class BGAYaTangRefreshViewHolder extends BGARefreshViewHolder {
    private android.widget.ProgressBar ProgressBar;
    private RotateAnimation mUpAnim;
    private RotateAnimation mDownAnim;
    private Context context;

    /**
     * @param context
     * @param isLoadingMoreEnabled 上拉加载更多是否可用
     */
    public BGAYaTangRefreshViewHolder(Context context, boolean isLoadingMoreEnabled) {
        super(context, isLoadingMoreEnabled);
        this.context=context;
        initAnimation();
    }

    private void initAnimation() {
        mUpAnim = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mUpAnim.setDuration(150);
        mUpAnim.setFillAfter(true);

        mDownAnim = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mDownAnim.setFillAfter(true);
    }

    /**
     * 设置未满足刷新条件，提示继续往下拉的文本
     *
     * @param pullDownRefreshText
     */
    public void setPullDownRefreshText(String pullDownRefreshText) {
    }

    /**
     * 设置满足刷新条件时的文本
     *
     * @param releaseRefreshText
     */
    public void setReleaseRefreshText(String releaseRefreshText) {
    }

    /**
     * 设置正在刷新时的文本
     *
     * @param refreshingText
     */
    public void setRefreshingText(String refreshingText) {
    }

    @Override
    public View getRefreshHeaderView() {
        if (mRefreshHeaderView == null) {
            mRefreshHeaderView = View.inflate(mContext, R.layout.my_refresh_head, null);
            mRefreshHeaderView.setBackgroundColor(Color.TRANSPARENT);
            if (mRefreshViewBackgroundColorRes != -1) {
                mRefreshHeaderView.setBackgroundResource(mRefreshViewBackgroundColorRes);
            }
            if (mRefreshViewBackgroundDrawableRes != -1) {
                mRefreshHeaderView.setBackgroundResource(mRefreshViewBackgroundDrawableRes);
            }
            ProgressBar=(ProgressBar)mRefreshHeaderView.findViewById(R.id.progress);
        }
        return mRefreshHeaderView;
    }

    @Override
    public void handleScale(float scale, int moveYDistance) {
    }

    @Override
    public void changeToIdle() {
    }

    @Override
    public void changeToPullDown() {
        ProgressBar.setIndeterminateDrawable(context.getResources().getDrawable(
                R.mipmap.ic_drop_down_dermas_refreshing));
        ProgressBar.setProgressDrawable(context.getResources().getDrawable(
                R.mipmap.ic_drop_down_dermas_refreshing));
        mDownAnim.setDuration(150);
    }

    @Override
    public void changeToReleaseRefresh() {
        ProgressBar.setVisibility(View.VISIBLE);
        ProgressBar.setIndeterminateDrawable(context.getResources().getDrawable(
                R.drawable.bg_progress));
        ProgressBar.setProgressDrawable(context.getResources().getDrawable(
                R.drawable.bg_progress));
    }

    @Override
    public void changeToRefreshing() {

    }

    @Override
    public void onEndRefreshing() {
        mDownAnim.setDuration(0);
    }


}