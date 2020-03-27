package com.ylean.soft.lfd.utils;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.zxdc.utils.library.util.LogUtils;


/**
 * Created by HeTingwei on 2018/4/30.
 */

public class MyOnTouchListener implements View.OnTouchListener {

    private static int timeout=400;//双击间四百毫秒延时
    private int clickCount = 0;//记录连续点击次数
    private Handler handler;
    private MyClickCallBack myClickCallBack;
    public interface MyClickCallBack{
        void oneClick(MotionEvent event,float x);//点击一次的回调
        void doubleClick(MotionEvent event);//连续点击两次的回调

    }


    public MyOnTouchListener(MyClickCallBack myClickCallBack) {
        this.myClickCallBack = myClickCallBack;
        handler = new Handler();
    }

    float x = 0;
    public boolean onTouch(View v, final MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
             x=event.getRawX();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            clickCount++;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (clickCount == 1) {
                        myClickCallBack.oneClick(event,x);
                    }else if(clickCount==2){
                        myClickCallBack.doubleClick(event);
                    }
                    handler.removeCallbacksAndMessages(null);
                    //清空handler延时，并防内存泄漏
                    clickCount = 0;//计数清零
                }
            },timeout);//延时timeout后执行run方法中的代码
        }
        return true;//让点击事件继续传播，方便再给View添加其他事件监听
    }
}

