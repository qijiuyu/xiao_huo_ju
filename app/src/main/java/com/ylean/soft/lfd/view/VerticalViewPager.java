package com.ylean.soft.lfd.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 可以上下滑动的viewpageer
 */
public class VerticalViewPager extends ViewPager {

    private boolean isVertical = false;
    private long mRecentTouchTime;

    public VerticalViewPager(@NonNull Context context) {
        super(context);
    }

    public VerticalViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private void init() {
        setPageTransformer(true, new HorizontalVerticalPageTransformer());
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public boolean isVertical() {
        return isVertical;
    }

    public void setVertical(boolean vertical) {
        isVertical = vertical;
        init();
    }


    private class HorizontalVerticalPageTransformer implements PageTransformer {

        private static final float MIN_SCALE = 0.25f;

        @Override
        public void transformPage(@NonNull View page, float position) {
            if (isVertical) {
                if (position < -1) {
                    page.setAlpha(0);
                } else if (position <= 1) {
                    page.setAlpha(1);
                    // Counteract the default slide transition
                    float xPosition = page.getWidth() * -position;
                    page.setTranslationX(xPosition);
                    //set Y position to swipe in from top
                    float yPosition = position * page.getHeight();
                    page.setTranslationY(yPosition);
                } else {
                    page.setAlpha(0);
                }
            } else {
                int pageWidth = page.getWidth();
                if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    page.setAlpha(0);
                } else if (position <= 0) { // [-1,0]
                    // Use the default slide transition when moving to the left page
                    page.setAlpha(1);
                    page.setTranslationX(0);
                    page.setScaleX(1);
                    page.setScaleY(1);
                } else if (position <= 1) { // (0,1]
                    // Fade the page out.
                    page.setAlpha(1 - position);
                    // Counteract the default slide transition
                    page.setTranslationX(pageWidth * -position);
                    page.setTranslationY(0);
                    // Scale the page down (between MIN_SCALE and 1)
                    float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
                    page.setScaleX(scaleFactor);
                    page.setScaleY(scaleFactor);
                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    page.setAlpha(0);
                }
            }
        }
    }

    /**
     * 交换x轴和y轴的移动距离
     * @param event 获取事件类型的封装类MotionEvent
     */
    private MotionEvent swapXY(MotionEvent event) {
        //获取宽高
        float width = getWidth();
        float height = getHeight();
        //将Y轴的移动距离转变成X轴的移动距离
        float swappedX = (event.getY() / height) * width;
        //将X轴的移动距离转变成Y轴的移动距离
        float swappedY = (event.getX() / width) * height;
        //重设event的位置
        event.setLocation(swappedX, swappedY);
        return event;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mRecentTouchTime = System.currentTimeMillis();
        if (getCurrentItem() == 0 && getChildCount() == 0) {
            return false;
        }
        if (isVertical) {
            boolean intercepted = super.onInterceptTouchEvent(swapXY(ev));
            swapXY(ev);
            // return touch coordinates to original reference frame for any child views
            return intercepted;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (getCurrentItem() == 0 && getChildCount() == 0) {
            return false;
        }
        if (isVertical) {
            return super.onTouchEvent(swapXY(ev));
        } else {
            return super.onTouchEvent(ev);
        }
    }
}